package io.craigmiller160.oauth2.util

import org.junit.jupiter.api.Test
import java.lang.RuntimeException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
        assertFailsWith<RuntimeException> {
            runCatching {
                "Hello World"
            }
                    .chain { Result.failure(RuntimeException("Dying")) }
                    .getOrThrow()
        }
    }

    @Test
    fun `chain a successful transform onto a failed Result`() {
        TODO("Finish this")
    }

    @Test
    fun `chain a failed transform onto a failed Result`() {
        TODO("Finish this")
    }

}