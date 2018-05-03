package me.ccampo.uuid62.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * @author ccampo on 2018-05-03
 */
class Base62Tests {

    @Test
    fun `test encode`() {
        val bytes = byteArrayOf(64, -34, 123)
        val result = Base62.encode(bytes)
        val expected = "A59eA"
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test decode`() {
        val input = "A59eA"
        val result = Base62.decode(input)
        val expected = byteArrayOf(64, -34, 123)
        assertThat(result).isEqualTo(expected)
    }
}
