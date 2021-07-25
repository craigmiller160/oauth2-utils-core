package io.craigmiller160.oauth2.util

import org.junit.jupiter.api.Test
import java.io.IOException
import java.lang.RuntimeException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

// TODO move into library
class ResultUtilsTest {

    @Test
    fun `flatMap a successful transform onto a successful Result`() {
        val result = runCatching {
            "Hello World"
        }
                .flatMap { Result.success(123) }
                .getOrThrow()
        assertEquals(123, result)
    }

    @Test
    fun `flatMap a failed transform onto a successful Result`() {
        assertFailsWith<IOException> {
            runCatching {
                "Hello World"
            }
                    .flatMap { Result.failure<Int>(IOException("IO Dying")) }
                    .getOrThrow()
        }
    }

    @Test
    fun `flatMap a successful transform onto a failed Result`() {
        assertFailsWith<RuntimeException> {
            runCatching {
                throw RuntimeException("Dying")
            }
                    .flatMap { Result.success(123) }
                    .getOrThrow()
        }
    }

    @Test
    fun `flatMap a failed transform onto a failed Result`() {
        assertFailsWith<RuntimeException> {
            runCatching {
                throw RuntimeException("Dying")
            }
                    .flatMap { Result.failure<Int>(IOException("IO Dying")) }
                    .getOrThrow()
        }
    }

    @Test
    fun `flatten a Result with both levels successful`() {
        val result = Result.success(Result.success("Hello World"))
                .flatten()
                .getOrThrow()
        assertEquals("Hello World", result)
    }

    @Test
    fun `flatten a Result with a successful outer level and failed inner level`() {
        assertFailsWith<IOException> {
            Result.success(Result.failure<String>(IOException("IO Dying")))
                    .flatten()
                    .getOrThrow()
        }
    }

    @Test
    fun `recoverAndFlatten with success result`() {
        TODO("Finish this")
    }

    @Test
    fun `recoverAndFlatten with failed result`() {
        TODO("Finish this")
    }

    @Test
    fun `recoverAndFlatten where Result is already success`() {
        TODO("Finish this")
    }

    @Test
    fun `recoverCatchingAndFlatten with success result`() {
        TODO("Finish this")
    }

    @Test
    fun `recoverCatchingAndFlatten with failed result`() {
        TODO("Finish this")
    }

    @Test
    fun `recoverCatchingAndFlatten where Result is already success`() {
        TODO("Finish this")
    }

}