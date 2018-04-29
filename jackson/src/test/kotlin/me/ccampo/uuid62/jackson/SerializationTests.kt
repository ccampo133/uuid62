package me.ccampo.uuid62.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import me.ccampo.uuid62.core.toBase62String
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

/**
 * @author ccampo on 2018-04-29
 */
class SerializationTests {

    @Test
    fun testSerialization() {
        val mapper = ObjectMapper()
        mapper.registerModule(UUIDModule())
        val id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e")
        val result = mapper.writeValueAsString(id).filter { it != '"' }
        assertThat(result).isEqualTo(id.toBase62String())
    }

    @Test
    fun testDeserialization() {
        val mapper = ObjectMapper()
        mapper.registerModule(UUIDModule())
        val id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e")
        val result = mapper.readValue("\"${id.toBase62String()}\"", UUID::class.java)
        assertThat(result).isEqualTo(id)
    }
}
