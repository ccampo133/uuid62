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

    /**
     * Converts a base 62 string representation of a UUID into a [UUID] instance.
     *
     * If for some reason this fails, Spring will fall back on [org.springframework.beans.propertyeditors.UUIDEditor]
     * to covert the string representation of the UUID to an actual [UUID] instance.
     */
    override fun convert(source: String): UUID = uuidFromBase62String(source)
}
