package me.ccampo.uuid62.core

/**
 * @author ccampo on 2018-04-29
 */
import java.nio.ByteBuffer
import java.util.*

/**
 * Converts this UUID to a byte array
 */
fun UUID.toByteArray(): ByteArray {
    return ByteBuffer.wrap(ByteArray(16))
            .putLong(this.mostSignificantBits)
            .putLong(this.leastSignificantBits)
            .array()
}

/**
 * Converts this UUID to a Base62 encoded string
 */
fun UUID.toBase62String(): String {
    return Base62.encode(this.toByteArray())
}

/**
 * Converts a Base62 encoded string to a UUID
 */
fun uuidFromBase62String(id: String): UUID {
    return uuidFromByteArray(Base62.decode(id))
}

/**
 * Converts a byte array to a UUID
 */
fun uuidFromByteArray(bytes: ByteArray): UUID {
    val buffer = ByteBuffer.wrap(bytes)
    val high = buffer.long
    val low = buffer.long
    return UUID(high, low)
}
