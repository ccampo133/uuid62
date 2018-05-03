package me.ccampo.uuid62.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import me.ccampo.uuid62.core.util.toBase62String
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

@JsonSerialize
class SerializationTests {

    @Test
    fun `uuid is serialized to base62`() {
        val mapper = jacksonObjectMapper().registerUUID62Module()
        val id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e")
        val result = mapper.writeValueAsString(id).filter { it != '"' }
        assertThat(result).isEqualTo(id.toBase62String())
    }

    @Test
    fun `base62 is deserialized to uuid`() {
        val mapper = jacksonObjectMapper().registerUUID62Module()
        val id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e")
        val result = mapper.readValue("\"${id.toBase62String()}\"", UUID::class.java)
        assertThat(result).isEqualTo(id)
    }

    @Test
    fun `raw uuid is deserialized to uuid`() {
        val mapper = jacksonObjectMapper().registerUUID62Module()
        val id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e")
        val result = mapper.readValue("\"$id\"", UUID::class.java)
        assertThat(result).isEqualTo(id)
    }

    data class RawIdStub(@JsonSerialize(using = UUIDSerializer::class) val id: UUID)
    data class Base62IdStub(val id: UUID)

    @Test
    fun `serialization outputs raw uuid when annotation is present`() {
        val mapper = jacksonObjectMapper().registerUUID62Module()
        val id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e")
        val stub = RawIdStub(id)
        val result = mapper.writeValueAsString(stub)
        val expected = "{\"id\":\"$id\"}"
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `deserialization outputs uuid when raw uuid is present`() {
        val mapper = ObjectMapper()
                .registerUUID62Module()
                .registerKotlinModule()
        val id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e")
        val json = "{\"id\":\"$id\"}"
        val result = mapper.readValue(json, RawIdStub::class.java)
        val expected = RawIdStub(id)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `serialization outputs base62 uuid when annotation is absent`() {
        val mapper = ObjectMapper()
        mapper.registerUUID62Module()
        val id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e")
        val stub = Base62IdStub(id)
        val result = mapper.writeValueAsString(stub)
        val expected = "{\"id\":\"${id.toBase62String()}\"}"
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `deserialization outputs uuid when base62 uuid is present`() {
        val mapper = ObjectMapper()
                .registerUUID62Module()
                .registerKotlinModule()
        val id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e")
        val json = "{\"id\":\"${id.toBase62String()}\"}"
        val result = mapper.readValue(json, RawIdStub::class.java)
        val expected = RawIdStub(id)
        assertThat(result).isEqualTo(expected)
    }
}
