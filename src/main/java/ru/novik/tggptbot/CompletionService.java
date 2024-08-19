package ru.novik.tggptbot;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.novik.openai.ApiException;
import ru.novik.openai.model.CompletionsRequest;
import ru.novik.openai.model.CompletionsResponse;
import ru.novik.openai.model.CompletionsResponseUsage;
import ru.novik.openai.model.Message;
import ru.novik.openai.services.DefaultApi;
import ru.novik.tggptbot.properties.BotProperty;
import ru.novik.tggptbot.properties.OpenaiProperty;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class CompletionService {

    private final DefaultApi defaultApi;
    private OpenaiProperty openaiProperty;
    private BotProperty botProperty;
    private final Map<Long, List<Message>> chatHistory = new HashMap<>();

    public String call(Long userId, String text) {
        if (!chatHistory.containsKey(userId)) {
            chatHistory.put(userId, new LinkedList<>());

            Message systemMessage = new Message();
            systemMessage.setContent(openaiProperty.getSystemMessage());
            systemMessage.setRole(Message.RoleEnum.SYSTEM);

            List<Message> messages = chatHistory.get(userId);
            messages.add(systemMessage);
        }

        if (chatHistory.get(userId).size() > botProperty.getMaxHistorySize()) {
            chatHistory.get(userId).remove(0);
        }

        Message userMessage = new Message();
        userMessage.setContent(text);
        userMessage.setRole(Message.RoleEnum.USER);

        List<Message> messages = chatHistory.get(userId);
        messages.add(userMessage);

        CompletionsRequest completionsRequest = new CompletionsRequest();
        completionsRequest.setModel(CompletionsRequest.ModelEnum.fromValue(openaiProperty.getModel()));
        completionsRequest.messages(messages);
        completionsRequest.setUser(userId.toString());


        try {
            CompletionsResponse completionsResponse = defaultApi.chatCompletion(completionsRequest);
            CompletionsResponseUsage usage = completionsResponse.getUsage();
            if (usage != null) {
                log.info("CompletionTokens: {}", usage.getCompletionTokens());
                log.info("PromptTokens: {}", usage.getPromptTokens());
                log.info("TotalTokens: {}", usage.getTotalTokens());
            }
            if (!completionsResponse.getChoices().isEmpty()) {
                Message message = completionsResponse.getChoices().get(0).getMessage();
                messages.add(message);
                return message.getContent();
            }
            return "No response from AI";
        } catch (ApiException e) {
            log.error("Error calling OpenAI", e);
            return "Error calling OpenAI";
        }

    }

    public void clearChatHistory(Long userId) {
        chatHistory.remove(userId);
    }

}
