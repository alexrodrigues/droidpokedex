package com.rodriguesalex.droidpokedex.util

import com.rodriguesalex.droidpokedex.R
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test
import retrofit2.HttpException

class UserErrorMapperTest {
    @Test
    fun `from maps IOException to network error`() {
        val result = UserErrorMapper.from(IOException("offline"))

        assertEquals(R.string.error_network_title, result.titleRes)
        assertEquals(R.string.error_network_message, result.messageRes)
        assertNull(result.detailRes)
    }

    @Test
    fun `from maps HttpException to server error with status code`() {
        val httpException =
            mockk<HttpException> {
                every { code() } returns 503
            }

        val result = UserErrorMapper.from(httpException)

        assertEquals(R.string.error_server_title, result.titleRes)
        assertEquals(R.string.error_server_message, result.messageRes)
        assertEquals(R.string.error_server_detail, result.detailRes)
        assertEquals(503, result.detailArg)
    }

    @Test
    fun `from maps unknown throwable to generic error`() {
        val result = UserErrorMapper.from(IllegalStateException("boom"))

        assertEquals(R.string.error_unknown_title, result.titleRes)
        assertEquals(R.string.error_unknown_message, result.messageRes)
        assertNull(result.detailRes)
    }

    @Test
    fun `invalidPokemonId returns invalid id error`() {
        val result = UserErrorMapper.invalidPokemonId()

        assertEquals(R.string.error_invalid_id_title, result.titleRes)
        assertEquals(R.string.error_invalid_id_message, result.messageRes)
    }
}
