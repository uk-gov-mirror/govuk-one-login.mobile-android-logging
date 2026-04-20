package uk.gov.logging.api.v3

import android.util.Log
import uk.gov.logging.api.v3.customkey.CustomKey

object LoggingTestData {
    private const val THROWABLE_MESSAGE = "This is a unit test!"

    const val LOG_MESSAGE = "Unit test log message"
    const val LOG_TAG = "Example log tag"

    val logThrowable = Throwable(message = THROWABLE_MESSAGE)

    val intCustomKey = CustomKey.IntKey("Key", 1)

    val basicDebugEntry =
        LogEntry.Basic(
            tag = LOG_TAG,
            message = LOG_MESSAGE,
            level = Log.DEBUG,
        )

    val basicInfoEntry =
        LogEntry.Basic(
            tag = LOG_TAG,
            message = LOG_MESSAGE,
            level = Log.INFO,
        )

    val basicWarnEntry =
        LogEntry.Basic(
            tag = LOG_TAG,
            message = LOG_MESSAGE,
            level = Log.WARN,
        )
    val basicErrorEntry =
        LogEntry.Basic(
            tag = LOG_TAG,
            message = LOG_MESSAGE,
            level = Log.ERROR,
        )

    val errorThrowableEntry =
        LogEntry.Error(
            Log.ERROR,
            LOG_MESSAGE,
            LOG_TAG,
            logThrowable,
            customKeys = listOf(),
        )

    val withExceptionEntry: LogEntry.WithException =
        LogEntry.Error(
            level = Log.ERROR,
            tag = LOG_TAG,
            message = LOG_MESSAGE,
            throwable = logThrowable,
            customKeys = listOf(),
        )

    val customKeyThrowable =
        LogEntry.Error(
            Log.ERROR,
            LOG_MESSAGE,
            LOG_TAG,
            logThrowable,
            listOf(intCustomKey),
        )

    val listOfErrorEntries =
        listOf(
            errorThrowableEntry,
            customKeyThrowable,
        )

    val listOfBasicEntries =
        listOf<LogEntry>(
            LogEntry.Basic(
                level = Log.ASSERT,
                message = LOG_MESSAGE,
                tag = LOG_TAG,
            ),
            LogEntry.Basic(
                level = Log.ERROR,
                message = LOG_MESSAGE,
                tag = LOG_TAG,
            ),
            LogEntry.Basic(
                level = Log.WARN,
                message = LOG_MESSAGE,
                tag = LOG_TAG,
            ),
            LogEntry.Basic(
                level = Log.INFO,
                message = LOG_MESSAGE,
                tag = LOG_TAG,
            ),
            LogEntry.Basic(
                level = Log.DEBUG,
                message = LOG_MESSAGE,
                tag = LOG_TAG,
            ),
            LogEntry.Basic(
                level = Log.VERBOSE,
                message = LOG_MESSAGE,
                tag = LOG_TAG,
            ),
        )
}
