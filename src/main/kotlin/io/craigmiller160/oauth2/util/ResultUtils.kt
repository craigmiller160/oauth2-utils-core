package io.craigmiller160.oauth2.util

// TODO move this into its own library if it works

fun <T,R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    if (isSuccess) {
        return transform(getOrThrow())
    }
    return this as Result<R>
}

// TODO tests
fun <T> Result<T>.recoverAndFlatten(transform: (Throwable) -> Result<T>): Result<T> {
    if (isSuccess) {
        return this
    }
    return transform(exceptionOrNull()!!)
}

fun <T> Result<Result<T>>.flatten(): Result<T> {
    if (isSuccess) {
        return getOrThrow()
    }
    return Result.failure(exceptionOrNull()!!)
}