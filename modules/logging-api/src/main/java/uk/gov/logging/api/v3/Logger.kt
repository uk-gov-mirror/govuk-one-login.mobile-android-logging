package uk.gov.logging.api.v3

import uk.gov.logging.api.v3.customkey.CustomKey

fun interface Logger {
    fun log(entry: LogEntry)
}

fun Logger.log(entries: Iterable<LogEntry>) = entries.forEach(::log)

fun Logger.log(vararg entries: LogEntry): Unit = log(entries.asList())

fun Logger.info(
    tag: String,
    message: String,
) = log(
    LogEntry.Info(
        tag = tag,
        message = message,
    ),
)

fun Logger.debug(
    tag: String,
    message: String,
) = log(
    LogEntry.Debug(
        tag = tag,
        message = message,
    ),
)

fun Logger.verbose(
    tag: String,
    message: String,
) = log(
    LogEntry.Verbose(
        tag = tag,
        message = message,
    ),
)

fun Logger.error(
    tag: String,
    message: String,
    throwable: Throwable,
    vararg customKey: CustomKey,
) = log(
    LogEntry.Error(
        tag = tag,
        message = message,
        throwable = throwable,
        customKeys = customKey.toList(),
    ),
)

fun Logger.warning(
    tag: String,
    message: String,
) = log(
    LogEntry.Warn(
        tag = tag,
        message = message,
    ),
)
