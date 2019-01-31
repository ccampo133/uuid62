package me.ccampo.uuid62.springboot.autoconfigure;

import me.ccampo.uuid62.jackson.UUID62Module;
import me.ccampo.uuid62.spring.StringToUUIDConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ccampo on 2018-05-03
 */
@Configuration
@ConditionalOnProperty(value = "uuid62.autoconfigure.enabled", havingValue = "true", matchIfMissing = true)
public class UUID62AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UUID62Module uuid62Module() {
        return new UUID62Module();
    }

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnProperty(value = "uuid62.autoconfigure.enabled", havingValue = "true", matchIfMissing = true)
    public static class UUID62WebMvcConfiguration implements WebMvcConfigurer {

        @Override
        public void addFormatters(final FormatterRegistry registry) {
            registry.addConverter(new StringToUUIDConverter());
        }
    }
}
