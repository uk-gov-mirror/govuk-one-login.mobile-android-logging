package uk.gov.logging.api.v3

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.everyItem
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.Test
import uk.gov.logging.api.v3.LoggingTestData.LOG_MESSAGE
import uk.gov.logging.api.v3.LoggingTestData.LOG_TAG
import uk.gov.logging.api.v3.LoggingTestData.debug
import uk.gov.logging.api.v3.LoggingTestData.error
import uk.gov.logging.api.v3.LoggingTestData.errorEntries
import uk.gov.logging.api.v3.LoggingTestData.errorWithCustomKey
import uk.gov.logging.api.v3.LoggingTestData.info
import uk.gov.logging.api.v3.LoggingTestData.messageEntries
import uk.gov.logging.api.v3.LoggingTestData.warning
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
            info,
        )

        assertThat(
            logger,
            contains(isBasicEntry()),
        )

        assertThat(
            logger,
            contains(
                allOf(
                    isLogLevel(LogLevel.Info),
                    hasMessage(LOG_MESSAGE),
                    hasTag(LOG_TAG),
                ),
            ),
        )
    }

    @Test
    fun `Verify debug messages are stored within the logger instance`() {
        logger.log(
            debug,
        )

        assertThat(
            logger,
            contains(isBasicEntry()),
        )

        assertThat(
            logger,
            contains(
                allOf(
                    isLogLevel(LogLevel.Debug),
                    hasMessage(LOG_MESSAGE),
                    hasTag(LOG_TAG),
                ),
            ),
        )
    }

    @Test
    fun `Verify warn messages are stored within the logger instance`() {
        logger.log(
            warning,
        )

        assertThat(
            logger,
            contains(isBasicEntry()),
        )

        assertThat(
            logger,
            contains(
                allOf(
                    isLogLevel(LogLevel.Warn),
                    hasMessage(LOG_MESSAGE),
                    hasTag(LOG_TAG),
                ),
            ),
        )
    }

    @Test
    fun `Verify error basic messages are stored within the logger instance`() {
        logger.log(
            error,
        )

        assertThat(
            logger,
            contains(isErrorEntry()),
        )

        assertThat(
            logger,
            contains(
                allOf(
                    isLogLevel(LogLevel.Error),
                    hasMessage(LOG_MESSAGE),
                    hasTag(LOG_TAG),
                ),
            ),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with basic entries has expected size`() {
        logger.log(messageEntries)
        assertThat(
            logger,
            hasSize(messageEntries.size),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with list of basic entries `() {
        logger.log(messageEntries)

        assertThat(
            logger,
            hasLogEntry(
                everyItem(
                    allOf(
                        hasMessage(LOG_MESSAGE),
                        hasTag(LOG_TAG),
                    ),
                ),
            ),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with error entries has expected size`() {
        logger.log(errorEntries)
        assertThat(
            logger,
            hasSize(errorEntries.size),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with list of error entries `() {
        logger.log(errorEntries)

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
        val expectedEntry: LogEntry =
            LogEntry.Info(
                tag = LOG_TAG,
                message = LOG_MESSAGE,
            )

        logger.log(messageEntries)

        assertThat(
            logger,
            hasLogEntry(hasItem<LogEntry>(expectedEntry)),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with error entries`() {
        logger.log(error)
        assertThat(
            logger,
            contains(isErrorEntry()),
        )

        assertThat(
            logger,
            contains(
                allOf(
                    isLogLevel(LogLevel.Error),
                    hasMessage(LOG_MESSAGE),
                    hasTag(LOG_TAG),
                ),
            ),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with error entry with exception`() {
        logger.log(error)
        assertThat(
            logger,
            contains(
                hasException(equalTo(error.throwable)),
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
        logger.log(error)
        assertThat(
            logger,
            hasLogEntry(
                hasItem(
                    hasException(error.throwable::class),
                ),
            ),
        )
    }

    @Test
    fun `verify in-memory logging behaviour with error entry with custom keys`() {
        logger.log(errorWithCustomKey)
        assertThat(
            logger,
            contains(
                hasCustomKeys(equalTo(errorWithCustomKey.customKeys)),
            ),
        )
    }
}
