package io.craigmiller160.oauth2.util

import org.junit.jupiter.api.Test
import java.io.IOException
import java.lang.RuntimeException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

// TODO move into library
class ResultUtilsTest {

    @Test
    fun `chain a successful transform onto a successful Result`() {
        val result = runCatching {
            "Hello World"
        }
                .chain { Result.success(123) }
                .getOrThrow()
        assertEquals(123, result)
    }

    @Test
    fun `chain a failed transform onto a successful Result`() {
        assertFailsWith<IOException> {
            runCatching {
                "Hello World"
            }
                    .chain { Result.failure<Int>(IOException("IO Dying")) }
                    .getOrThrow()
        }
    }

    @Test
    fun `chain a successful transform onto a failed Result`() {
        assertFailsWith<RuntimeException> {
            runCatching {
                throw RuntimeException("Dying")
            }
                    .chain { Result.success(123) }
                    .getOrThrow()
        }
    }

    @Test
    fun `chain a failed transform onto a failed Result`() {
        assertFailsWith<RuntimeException> {
            runCatching {
                throw RuntimeException("Dying")
            }
                    .chain { Result.failure<Int>(IOException("IO Dying")) }
                    .getOrThrow()
        }
    }

}