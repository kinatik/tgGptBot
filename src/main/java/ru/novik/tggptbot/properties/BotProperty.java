package ru.novik.tggptbot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Getter
@Setter
@ConfigurationProperties(prefix = "bot")
@Configuration
public class BotProperty {
    private String token;
    private String name;
    private String superUserId;
    private Set<Long> allowedChatIds;
    private Integer maxHistorySize;
    private String messageNotAllowedChatIdReply;
    private String messageStartCommandReply;
    private String messagePleaseWait;
}



