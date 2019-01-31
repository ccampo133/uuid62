package me.ccampo.uuid62.core.util.example;

import me.ccampo.uuid62.core.util.UUIDUtilsKt;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * These tests are duplicates of the Kotlin tests, but exist mainly to show how to use this library in Java.
 *
 * @author ccampo on 2019-01-31
 */
public class UUIDUtilsTestsJava {

    @Test
    public void uuidToByteArray() {
        final UUID uuid = UUID.fromString("86559453-e224-4921-baee-6fb5c0252e85");
        final byte[] expected = new byte[]{-122, 85, -108, 83, -30, 36, 73, 33, -70, -18, 111, -75, -64, 37, 46, -123};
        final byte[] result = UUIDUtilsKt.toByteArray(uuid);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void uuidFromByteArray() {
        final UUID expected = UUID.fromString("86559453-e224-4921-baee-6fb5c0252e85");
        final byte[] bytes = new byte[]{-122, 85, -108, 83, -30, 36, 73, 33, -70, -18, 111, -75, -64, 37, 46, -123};
        final UUID result = UUIDUtilsKt.uuidFromByteArray(bytes);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void uuidToBase62EncodedString() {
        final UUID uuid = UUID.fromString("86559453-e224-4921-baee-6fb5c0252e85");
        final String result = UUIDUtilsKt.toBase62String(uuid);
        final String expected = "GWFlTJOJJFiuuftaBuEXKE";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void uuidFromBase62EncodedString() {
        final UUID result = UUIDUtilsKt.uuidFromBase62String("GWFlTJOJJFiuuftaBuEXKE");
        final UUID expected = UUID.fromString("86559453-e224-4921-baee-6fb5c0252e85");
        assertThat(result).isEqualTo(expected);
    }
}
