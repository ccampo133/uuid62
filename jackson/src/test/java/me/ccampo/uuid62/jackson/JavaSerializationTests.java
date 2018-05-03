package me.ccampo.uuid62.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import me.ccampo.uuid62.core.util.UUIDUtilsKt;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaSerializationTests {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        this.mapper = new ObjectMapper().registerModule(new UUID62Module());
    }

    @Test
    public void testPojoWithGetterAndSetterSerializesToBase62UUID() throws JsonProcessingException {
        final UUID id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e");
        final PojoWithGetterAndSetter pojo = new PojoWithGetterAndSetter().setId(id);
        final String result = mapper.writeValueAsString(pojo);
        final String expected = "{\"id\":\"" + UUIDUtilsKt.toBase62String(id) + "\"}";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testAnnotatedPojoWithGetterAndSetterSerializesToRawUUID() throws JsonProcessingException {
        final UUID id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e");
        final AnnotatedPojoWithGetterAndSetter pojo = new AnnotatedPojoWithGetterAndSetter().setId(id);
        final String result = mapper.writeValueAsString(pojo);
        final String expected = "{\"id\":\"" + id.toString() + "\"}";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testPojoWithPublicFieldSerializesToBase62UUID() throws JsonProcessingException {
        final UUID id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e");
        final PojoWithPublicField pojo = new PojoWithPublicField(id);
        final String result = mapper.writeValueAsString(pojo);
        final String expected = "{\"id\":\"" + UUIDUtilsKt.toBase62String(id) + "\"}";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testAnnotatedPojoWithPublicFieldSerializesToRawUUID() throws JsonProcessingException {
        final UUID id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e");
        final AnnotatedPojoWithPublicField pojo = new AnnotatedPojoWithPublicField(id);
        final String result = mapper.writeValueAsString(pojo);
        final String expected = "{\"id\":\"" + id.toString() + "\"}";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testBase62UUIDSerializesToPojoWithGetterAndSetter() throws IOException {
        final UUID id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e");
        final String json = "{\"id\":\"" + UUIDUtilsKt.toBase62String(id) + "\"}";
        final PojoWithGetterAndSetter result = mapper.readValue(json, PojoWithGetterAndSetter.class);
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    public void testRawUUIDSerializesToPojoWithGetterAndSetter() throws IOException {
        final UUID id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e");
        final String json = "{\"id\":\"" + id.toString() + "\"}";
        final PojoWithGetterAndSetter result = mapper.readValue(json, PojoWithGetterAndSetter.class);
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    public void testBase62UUIDDeserializesToPojoWithPublicField() throws IOException {
        final UUID id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e");
        final String json = "{\"id\":\"" + UUIDUtilsKt.toBase62String(id) + "\"}";
        final PojoWithPublicField result = mapper.readValue(json, PojoWithPublicField.class);
        assertThat(result.id).isEqualTo(id);
    }

    @Test
    public void testRawUUIDDeserializesToPojoWithPublicField() throws IOException {
        final UUID id = UUID.fromString("6c92fb91-d151-47d9-82ee-029373cf013e");
        final String json = "{\"id\":\"" + id.toString() + "\"}";
        final PojoWithPublicField result = mapper.readValue(json, PojoWithPublicField.class);
        assertThat(result.id).isEqualTo(id);
    }

    public static class PojoWithGetterAndSetter {

        private UUID id;

        public UUID getId() {
            return id;
        }

        public PojoWithGetterAndSetter setId(final UUID id) {
            this.id = id;
            return this;
        }
    }

    public static class PojoWithPublicField {

        public final UUID id;

        @JsonCreator
        PojoWithPublicField(@JsonProperty("id") final UUID id) {
            this.id = id;
        }
    }

    public static class AnnotatedPojoWithGetterAndSetter {

        @JsonSerialize(using = UUIDSerializer.class)
        private UUID id;

        public UUID getId() {
            return id;
        }

        public AnnotatedPojoWithGetterAndSetter setId(final UUID id) {
            this.id = id;
            return this;
        }
    }

    public static class AnnotatedPojoWithPublicField {

        @JsonSerialize(using = UUIDSerializer.class)
        public final UUID id;

        AnnotatedPojoWithPublicField(final UUID id) {
            this.id = id;
        }
    }
}
