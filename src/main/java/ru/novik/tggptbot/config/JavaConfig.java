package ru.novik.tggptbot.config;

import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.novik.openai.ApiClient;
import ru.novik.openai.services.DefaultApi;
import ru.novik.tggptbot.properties.BotProperty;
import ru.novik.tggptbot.properties.OpenaiProperty;
import ru.novik.tggptbot.validation.MessageValidator;

import java.util.concurrent.TimeUnit;

@EnableConfigurationProperties
@org.springframework.context.annotation.Configuration
public class JavaConfig {

    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient.Builder builder = okHttpClient.newBuilder();
        builder.callTimeout(120, TimeUnit.SECONDS);
        builder.connectTimeout(120, TimeUnit.SECONDS);
        builder.readTimeout(120, TimeUnit.SECONDS);
        builder.writeTimeout(120, TimeUnit.SECONDS);
        return builder.build();
    }

    @Bean
    public DefaultApi defaultApi(OkHttpClient okHttpClient, OpenaiProperty openaiProperty) {
        ApiClient apiClient = new ApiClient(okHttpClient);
        apiClient.addDefaultHeader("Authorization", "Bearer " + openaiProperty.getApiKey());
        return new DefaultApi(apiClient);
    }

    @Bean
    public MessageValidator messageValidator(BotProperty botProperty) {
        return new MessageValidator(botProperty);
    }

}
