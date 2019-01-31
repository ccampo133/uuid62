package me.ccampo.uuid62.spring

import me.ccampo.uuid62.core.util.uuidFromBase62String
import org.springframework.core.convert.converter.Converter
import java.util.UUID

/**
 * Can be used with Spring MVC to convert the string representation of a UUID to an instance of [UUID].
 * Useful for controller parameter binding, such as URL paths. Must be registered as a converter in Spring
 * most likely using a WebMvcConfigurer or WebMvcConfigurerAdapter.
 */
class StringToUUIDConverter : Converter<String, UUID> {
    override fun convert(source: String): UUID {
        return if (source.contains('-')) UUID.fromString(source) else uuidFromBase62String(source)
    }
}
