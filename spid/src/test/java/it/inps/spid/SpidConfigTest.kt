package it.inps.spid

import it.inps.spid.model.SpidParams
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SpidConfigTest {

    @Test
    fun `SpidConfig with all properties empty`() {
        val spidConfig = SpidParams.Config(
                "",
                "",
                60,
                "",
                ""
        )
        assertFalse(spidConfig.isSpidConfigValid())
    }

    @Test
    fun `SpidConfig with all properties filled`() {
        val spidConfig = SpidParams.Config(
                "https://TEST",
                "https://TEST",
                60,
                "https://TEST",
                "https://TEST"
        )
        assertTrue(spidConfig.isSpidConfigValid())
    }

    @Test
    fun `SpidConfig with all properties filled with negative timeout`() {
        val spidConfig = SpidParams.Config(
                "https://TEST",
                "https://TEST",
                -10,
                "https://TEST",
                "https://TEST"
        )
        assertFalse(spidConfig.isSpidConfigValid())
    }

    @Test
    fun `SpidConfig without authPageUrl`() {
        val spidConfig = SpidParams.Config(
                "",
                "https://TEST",
                60,
                "https://TEST",
                "https://TEST"
        )
        assertFalse(spidConfig.isSpidConfigValid())
    }

    @Test
    fun `SpidConfig without urlCallbackPage`() {
        val spidConfig = SpidParams.Config(
                "https://TEST",
                "",
                60,
                "https://TEST",
                "https://TEST"
        )
        assertFalse(spidConfig.isSpidConfigValid())
    }

    @Test
    fun `SpidConfig without urlSpidPageInfo`() {
        val spidConfig = SpidParams.Config(
                "https://TEST",
                "https://TEST",
                60,
                "",
                "https://TEST"
        )
        assertFalse(spidConfig.isSpidConfigValid())
    }

    @Test
    fun `SpidConfig without urlRequestSpidPage`() {
        val spidConfig = SpidParams.Config(
                "https://TEST",
                "https://TEST",
                60,
                "https://TEST",
                ""
        )
        assertFalse(spidConfig.isSpidConfigValid())
    }

    @Test
    fun `SpidConfig without a valid http url`() {
        val spidConfig = SpidParams.Config(
                "TEST",
                "https://TEST",
                60,
                "https://TEST",
                "https://TEST"
        )
        assertFalse(spidConfig.isSpidConfigValid())
    }

    @Test
    fun `SpidConfig without a valid https url`() {
        val spidConfig = SpidParams.Config(
                "test://test",
                "https://TEST",
                60,
                "https://TEST",
                "https://TEST"
        )
        assertFalse(spidConfig.isSpidConfigValid())
    }

    @Test
    fun `SpidConfig with mixcase https url`() {
        val spidConfig = SpidParams.Config(
                "htTps://test",
                "httPs://TEST",
                60,
                "Https://TEST",
                "httpS://TEST"
        )
        assertTrue(spidConfig.isSpidConfigValid())
    }
}