package ru.novik.tggptbot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "openai")
@Configuration
public class OpenaiProperty {
    private String apiKey;
    private String systemMessage;
    private String model;
}



