package uk.gov.logging.api.v3

import android.util.Log
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.everyItem
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.Test
import uk.gov.logging.api.v3.LoggingTestData.LOG_MESSAGE
import uk.gov.logging.api.v3.LoggingTestData.LOG_TAG
import uk.gov.logging.api.v3.LoggingTestData.basicDebugEntry
import uk.gov.logging.api.v3.LoggingTestData.basicErrorEntry
import uk.gov.logging.api.v3.LoggingTestData.basicInfoEntry
import uk.gov.logging.api.v3.LoggingTestData.basicWarnEntry
import uk.gov.logging.api.v3.LoggingTestData.customKeyThrowable
import uk.gov.logging.api.v3.LoggingTestData.errorThrowableEntry
import uk.gov.logging.api.v3.LoggingTestData.listOfBasicEntries
import uk.gov.logging.api.v3.LoggingTestData.listOfErrorEntries
import uk.gov.logging.api.v3.LoggingTestData.withExceptionEntry
import uk.gov.logging.api.v3.matchers.LogEntryMatchers.hasCustomKeys
import uk.gov.logging.api.v3.matchers.LogEntryMatchers.hasException
import uk.gov.logging.api.v3.matchers.LogEntryMatchers.hasLogEntry
import uk.gov.logging.api.v3.matchers.LogEntryMatchers.hasMessage
import uk.gov.logging.api.v3.matchers.LogEntryMatchers.hasTag
import uk.gov.logging.api.v3.matchers.LogEntryMatchers.isBasicEntry
import uk.gov.logging.api.v3.matchers.LogEntryMatchers.isErrorEntry
import uk.gov.logging.api.v3.matchers.LogEntryMatchers.isExceptionInstance
import uk.gov.logging.api.v3.matchers.LogEntryMatchers.isLogLevel
import uk.gov.logging.api.v3.matchers.MemorisedLoggerMatchers.hasSize

class MemorisedLoggerTest {
    private val logger by lazy {
        MemorisedLogger {}
    }

    @Test
    fun `Verify info messages are stored within the logger instance`() {
        logger.log(
            basicInfoEntry,
        )

        assertThat(
            logger,
            contains(isBasicEntry()),
        )

        assertThat(
            logger,
            contains(
                allOf(
                    isLogLevel(Log.INFO),
                    hasMessage(LOG_MESSAGE),
                    hasTag(LOG_TAG),
                ),
            ),
        )
    }

    @Test
    fun `Verify debug messages are stored within the logger instance`() {
        logger.log(
            basicDebugEntry,
        )

        assertThat(
            logger,
            contains(isBasicEntry()),
        )

        assertThat(
            logger,
            contains(
                allOf(
                    isLogLevel(Log.DEBUG),
                    hasMessage(LOG_MESSAGE),
                    hasTag(LOG_TAG),
                ),
            ),
        )
    }

    @Test
    fun `Verify warn messages are stored within the logger instance`() {
        logger.log(
            basicWarnEntry,
        )

        assertThat(
            logger,
            contains(isBasicEntry()),
        )

        assertThat(
            logger,
            contains(
                allOf(
                    isLogLevel(Log.WARN),
                    hasMessage(LOG_MESSAGE),
                    hasTag(LOG_TAG),
                ),
            ),
        )
    }

    @Test
    fun `Verify error basic messages are stored within the logger instance`() {
        logger.log(
            basicErrorEntry,
        )

        assertThat(
            logger,
            contains(isBasicEntry()),
        )

        assertThat(
            logger,
            contains(
                allOf(
                    isLogLevel(Log.ERROR),
                    hasMessage(LOG_MESSAGE),
                    hasTag(LOG_TAG),
                ),
            ),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with basic entries has expected size`() {
        logger.log(listOfBasicEntries)
        assertThat(
            logger,
            hasSize(listOfBasicEntries.size),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with list of basic entries `() {
        logger.log(listOfBasicEntries)

        assertThat(
            logger,
            hasLogEntry(
                everyItem(
                    allOf(
                        isBasicEntry(),
                        hasMessage(LOG_MESSAGE),
                        hasTag(LOG_TAG),
                    ),
                ),
            ),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with error entries has expected size`() {
        logger.log(listOfErrorEntries)
        assertThat(
            logger,
            hasSize(listOfErrorEntries.size),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with list of error entries `() {
        logger.log(listOfErrorEntries)

        assertThat(
            logger,
            hasLogEntry(
                everyItem(
                    allOf(
                        isErrorEntry(),
                        hasMessage(LOG_MESSAGE),
                        hasTag(LOG_TAG),
                    ),
                ),
            ),
        )
    }

    @Test
    fun `test memorised logger contains expected log entry`() {
        val expectedEntry =
            LogEntry.Basic(
                tag = LOG_TAG,
                message = LOG_MESSAGE,
                level = Log.INFO,
            )

        logger.log(listOfBasicEntries)

        assertThat(
            logger,
            hasLogEntry(hasItem<LogEntry>(expectedEntry)),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with error entries`() {
        logger.log(errorThrowableEntry)
        assertThat(
            logger,
            contains(isErrorEntry()),
        )

        assertThat(
            logger,
            contains(
                allOf(
                    isLogLevel(Log.ERROR),
                    hasMessage(LOG_MESSAGE),
                    hasTag(LOG_TAG),
                ),
            ),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with error entry with exception`() {
        logger.log(withExceptionEntry)
        assertThat(
            logger,
            contains(
                hasException(equalTo(withExceptionEntry.throwable)),
            ),
        )

        assertThat(
            logger,
            contains(
                isExceptionInstance(),
            ),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with error with exact throwable instance`() {
        logger.log(withExceptionEntry)
        assertThat(
            logger,
            hasLogEntry(
                hasItem(
                    hasException(withExceptionEntry.throwable::class),
                ),
            ),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with error entry with custom keys`() {
        logger.log(customKeyThrowable)
        assertThat(
            logger,
            contains(
                hasCustomKeys(equalTo(customKeyThrowable.customKeys)),
            ),
        )
    }
}
