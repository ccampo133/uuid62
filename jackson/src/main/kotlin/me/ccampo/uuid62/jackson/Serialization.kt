package me.ccampo.uuid62.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import me.ccampo.uuid62.core.util.toBase62String
import me.ccampo.uuid62.core.util.uuidFromBase62String
import java.io.IOException
import java.util.UUID

/**
 * JSON Serializer which converts a UUID to a base62 encoded string.
 */
class UUID62Serializer : StdSerializer<UUID>(UUID::class.java) {

    /**
     * Writes a UUID to a base 62 string in JSON.
     */
    @Throws(IOException::class)
    override fun serialize(value: UUID, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.toBase62String())
    }
}

/**
 * Generalized JSON UUID deserializer which supports deserializing both base62 encoded
 * UUIDs as well as the canonical textual representations in hexadecimal.
 */
class UUID62Deserializer : UUIDDeserializer() {

    /**
     * Convert's a JSON representation of a UUID (either base62 or standard) to a Java UUID
     */
    @Throws(IOException::class)
    override fun deserialize(parser: JsonParser, context: DeserializationContext): UUID {
        val token = parser.currentToken
        if (JsonToken.VALUE_STRING == token) {
            val value = parser.valueAsString.trim()
            // Looks like a UUID? Must be a UUID.
            return if (value.contains("-")) super.deserialize(parser, context) else uuidFromBase62String(value)
        }
        throw IllegalStateException("Unable to deserialize base62 string into UUID; value = $token")
    }
}

/**
 * A custom Jackson module used to register serialization/deserialization for base62 UUIDs.
 */
class UUID62Module : SimpleModule() {
    init {
        addDeserializer<UUID>(UUID::class.java, UUID62Deserializer())
        addSerializer<UUID>(UUID::class.java, UUID62Serializer())
    }
}

/**
 * Convenience extension function to register the uuid62 Jackson module with the given object mapper.
 */
fun ObjectMapper.registerUUID62Module(): ObjectMapper = this.registerModule(UUID62Module())
