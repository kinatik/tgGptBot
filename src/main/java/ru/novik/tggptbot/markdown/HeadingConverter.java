package ru.novik.tggptbot.markdown;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.List;

public class HeadingConverter extends AbstractMarkdownConverter {

    @Override
    public SendMessage convert(SendMessage sendMessage) {
        String text = sendMessage.getText();
        String[] split = text.split("\n");
        int start;
        List<MessageEntity> entities = sendMessage.getEntities();
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : split) {
            int prefixLength = 0;
            if (s.startsWith("### ")) {
                prefixLength = 4;
            } else if (s.startsWith("## ")) {
                prefixLength = 3;
            } else if (s.startsWith("# ")) {
                prefixLength = 2;
            }
            if (prefixLength > 0) {
                start = stringBuilder.length();
                stringBuilder.append(s, prefixLength, s.length()).append("\n");
                MessageEntity messageEntity = new MessageEntity("bold", start, stringBuilder.length());
                entities.add(messageEntity);
            } else {
                stringBuilder.append(s).append("\n");
            }
        }
        sendMessage.setText(stringBuilder.toString());
        sendMessage.setParseMode("MarkdownV2");
        return sendMessage;
    }
}
