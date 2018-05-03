package me.ccampo.uuid62.core

import java.util.*
import kotlin.experimental.or

/**
 * Provides Base62 encoding and decoding.
 *
 * The Base62 alphabet used by this algorithm in common is equivalent to the Base64 alphabet as defined by RFC 2045.
 * The only exception is a representations for 62 and 63 6-bit values. For that values special encoding is used.
 *
 * NOTICE: This is a Kotlin implementation of Pavel Myasnov's original implementation
 * (see: https://github.com/glowfall/base62). Some parts of the code have been completely re-written however the
 * structure and algorithms overall remain the same.
 *
 * See NOTICE in the main repository directory for a copy of the original license.
 */
object Base62 {
    /*
     * This array is a lookup table that translates 6-bit positive integer index values into their "Base62 Alphabet"
     * equivalents as specified in Table 1 of RFC 2045 excepting special characters for 62 and 63 values.
     */
    private val ALPHABET = charArrayOf(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    )

    /*
     * This array is a lookup table that translates Unicode characters drawn from the "Base64 Alphabet" (as specified in
     * Table 1 of RFC 2045) into their 6-bit positive integer equivalents. Characters that are not in the Base62
     * alphabet but fall within the bounds of the array are translated to -1.
     *
     * Note that there are no special characters in Base62 alphabet that could represent 62 and 63 values, so they both
     * are absent in this decode table.
     */
    private val DECODE_TABLE = byteArrayOf(
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
            -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
            -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51
    )

    // Mask for the data that should be written in compact 5-bits form (00011110)
    private const val COMPACT_MASK = 0x1E

    // Mask for extracting 5 bits of the data (00011111)
    private const val MASK_5BITS = 0x1F

    /**
     * Encodes binary data using a Base62 algorithm.
     *
     * @param data binary data to encode
     * @return String containing Base62 characters
     */
    fun encode(data: ByteArray): String {
        // Reserving capacity for the worst case when each output character represents compacted 5-bits data
        val sb = StringBuilder(data.size * 8 / 5 + 1)
        val input = BitInputStream(data)
        while (input.hasMore()) {
            // Read no more than 6 bits from the stream
            val rawBits = input.readBits(6)

            // For some cases special processing is needed, so _bits_ will contain final data representation needed to
            // form next output character
            val bits = when (COMPACT_MASK) {
                rawBits and COMPACT_MASK -> {
                    // We can't represent all 6 bits of the data, so extract only least significant 5 bits
                    // and step one bit back in the stream.
                    input.seekBit(-1)
                    rawBits and MASK_5BITS
                }
                else -> rawBits // In most cases, all 6 bits are used to form output character
            }

            // Look up next character in the encoding table and append it to the output StringBuilder
            sb.append(ALPHABET[bits])
        }
        return sb.toString()
    }

    /**
     * Decodes a Base62 String into byte array.
     *
     * @param input String containing Base62 data
     * @return Array containing decoded data.
     */
    fun decode(input: String): ByteArray {
        val length = input.length
        // Create stream with capacity enough to fit
        val out = BitOutputStream(length * 6)

        val lastCharPos = length - 1
        for (i in 0 until length) {
            // Obtain data bits from decoding table for the next character
            val bits = decodedBitsForCharacter(input[i])

            // Determine bits count needed to write to the stream
            val bitsCount: Int
            bitsCount = when {
            // Compact form detected, write down only 5 bits
                bits and COMPACT_MASK == COMPACT_MASK -> 5
            // For the last character write down all bits that needed for the completion of the stream
                i >= lastCharPos -> out.bitsCountUpToByte
            // In most cases the full 6-bits form will be used
                else -> 6
            }

            out.writeBits(bitsCount, bits)
        }

        return out.toArray()
    }

    private fun decodedBitsForCharacter(character: Char): Int {
        if (character.toInt() >= DECODE_TABLE.size || DECODE_TABLE[character.toInt()].toInt() < 0) {
            throw IllegalArgumentException("Invalid Base62 symbol: $character")
        }
        return DECODE_TABLE[character.toInt()].toInt()
    }

    private class BitInputStream(private val buffer: ByteArray) {
        private var offset = 0

        fun seekBit(pos: Int) {
            offset += pos
            if (offset < 0 || offset > buffer.size * 8) throw IndexOutOfBoundsException()
        }

        fun readBits(bitsCount: Int): Int {
            if (bitsCount < 0 || bitsCount > 7) throw IndexOutOfBoundsException()

            val bitNum = offset % 8
            val byteNum = offset / 8
            val firstRead = Math.min(8 - bitNum, bitsCount)
            val secondRead = bitsCount - firstRead
            var result = (buffer[byteNum].toInt() and ((1 shl firstRead) - 1 shl bitNum)).ushr(bitNum)
            if (secondRead > 0 && byteNum + 1 < buffer.size) {
                result = result or (buffer[byteNum + 1].toInt() and (1 shl secondRead) - 1 shl firstRead)
            }
            offset += bitsCount
            return result
        }

        fun hasMore(): Boolean = offset < buffer.size * 8
    }

    private class BitOutputStream(capacity: Int) {
        private val buffer: ByteArray = ByteArray(capacity / 8)
        private var offset = 0

        val bitsCountUpToByte: Int
            get() {
                val currentBit = offset % 8
                return if (currentBit == 0) 0 else 8 - currentBit
            }

        fun writeBits(bitsCount: Int, bits: Int) {
            val bitNum = offset % 8
            val byteNum = offset / 8
            val firstWrite = Math.min(8 - bitNum, bitsCount)
            val secondWrite = bitsCount - firstWrite
            buffer[byteNum] = buffer[byteNum] or (bits and (1 shl firstWrite) - 1 shl bitNum).toByte()
            if (secondWrite > 0) {
                buffer[byteNum + 1] = buffer[byteNum + 1] or (bits.ushr(firstWrite) and (1 shl secondWrite) - 1).toByte()
            }
            offset += bitsCount
        }

        fun toArray(): ByteArray {
            val newLength = offset / 8
            return if (newLength == buffer.size) buffer else Arrays.copyOf(buffer, newLength)
        }
    }
}
