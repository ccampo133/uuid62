package me.ccampo.uuid62.core.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.UUID

/**
 * @author ccampo on 2018-05-03
 */
class UUIDUtilsTests {

    @Test
    fun `uuid to byte array`() {
        val uuid = UUID.fromString("86559453-e224-4921-baee-6fb5c0252e85")
        val result = uuid.toByteArray()
        val expected = byteArrayOf(-122, 85, -108, 83, -30, 36, 73, 33, -70, -18, 111, -75, -64, 37, 46, -123)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `uuid from byte array`() {
        val expected = UUID.fromString("86559453-e224-4921-baee-6fb5c0252e85")
        val bytes = byteArrayOf(-122, 85, -108, 83, -30, 36, 73, 33, -70, -18, 111, -75, -64, 37, 46, -123)
        val result = uuidFromByteArray(bytes)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `uuid to base62 encoded string`() {
        val uuid = UUID.fromString("86559453-e224-4921-baee-6fb5c0252e85")
        val result = uuid.toBase62String()
        val expected = "GWFlTJOJJFiuuftaBuEXKE"
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `uuid from base62 encoded string`() {
        val result = uuidFromBase62String("GWFlTJOJJFiuuftaBuEXKE")
        val expected = UUID.fromString("86559453-e224-4921-baee-6fb5c0252e85")
        assertThat(result).isEqualTo(expected)
    }
}
