package me.ccampo.uuid62.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import me.ccampo.uuid62.core.toBase62String
import me.ccampo.uuid62.core.uuidFromBase62String
import java.io.IOException
import java.util.*


/**
 * JSON Serializer which converts a UUID to a base62 encoded string.
 */
class UUIDSerializer : StdSerializer<UUID>(UUID::class.java) {
    @Throws(IOException::class)
    override fun serialize(value: UUID, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.toBase62String())
    }
}

/**
 * Generalized JSON UUID deserializer which supports deserializing both base62 encoded UUIDs as well as the canonical
 * textual representations in hexadecimal.
 */
class UUIDDeserializer : UUIDDeserializer() {
    @Throws(IOException::class)
    override fun deserialize(parser: JsonParser, context: DeserializationContext): UUID {
        val token = parser.currentToken
        if (token == JsonToken.VALUE_STRING) {
            val value = parser.valueAsString.trim()
            // Looks like a UUID? Must be a UUID.
            return if (value.contains("-")) super.deserialize(parser, context) else uuidFromBase62String(value)
        }
        throw IllegalStateException("Unable to deserialize base62 string into UUID; value = $token")
    }
}
