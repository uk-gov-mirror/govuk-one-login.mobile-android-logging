package uk.gov.logging.impl.analytics.v3

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import uk.gov.logging.api.LogTagProvider
import uk.gov.logging.api.analytics.AnalyticsEvent
import uk.gov.logging.api.analytics.logging.v3.AnalyticsLogger
import uk.gov.logging.impl.analytics.extensions.setCollectionEnabled

class FirebaseAnalyticsLogger(
    private val analytics: FirebaseAnalytics,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : AnalyticsLogger,
    LogTagProvider {
    override suspend fun logEvent(
        shouldLogEvent: Boolean,
        vararg events: AnalyticsEvent,
    ) {
        debugLog(
            tag = tag,
            msg = "Should log event: $shouldLogEvent",
        )
        if (shouldLogEvent) {
            events.forEach { event ->
                internalLogEvent(event)
            }
        }
    }

    override fun setEnabled(isEnabled: Boolean) {
        Firebase.setCollectionEnabled(isEnabled)
    }

    /**
     * Ensures sequential delivery of analytics events using a [Mutex] to prevent
     * concurrent events sharing the same timestamp, which would cause Firebase
     * to group them under a single event.
     */

    private val mutex = Mutex()

    private suspend fun internalLogEvent(event: AnalyticsEvent) {
        mutex.withLock {
            withContext(dispatcher) {
                val bundledParameters = event.toBundle()
                analytics.logEvent(event.eventType, bundledParameters)
                debugLog(
                    tag = tag,
                    msg = "Firebase event sent with: $bundledParameters",
                )
            }
        }
    }
}
