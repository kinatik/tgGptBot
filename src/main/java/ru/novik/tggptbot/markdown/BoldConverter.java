package ru.novik.tggptbot.markdown;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.LinkedList;
import java.util.List;

public class BoldConverter extends AbstractMarkdownConverter {

    @Override
    public SendMessage convert(SendMessage sendMessage) {
        StringBuilder stringBuilder = new StringBuilder();
        List<MessageEntity> entities = new LinkedList<>();

        String text = sendMessage.getText();
        char[] ch = text.toCharArray();
        MessageEntity messageEntity = null;
        for (int i = 0; i < ch.length; i++) {
            boolean doubleAsterisk = isDoubleAsterisk(ch, i);
            if (doubleAsterisk && areAsterisksFollowedByText(ch, i) && messageEntity == null) {
                messageEntity = new MessageEntity("bold", i - 4 * entities.size(), 0);
                i++;
                continue;
            }

            if (doubleAsterisk && isTextFollowedByAsterisks(ch, i) && messageEntity != null) {
                messageEntity.setLength(stringBuilder.length() - messageEntity.getOffset());
                entities.add(messageEntity);
                messageEntity = null;
                i++;
                continue;
            }

            stringBuilder.append(ch[i]);

        }
        if (messageEntity != null) {
            stringBuilder.insert(messageEntity.getOffset(), "**");
        }
        sendMessage.getEntities().addAll(entities);
        sendMessage.setText(stringBuilder.toString());
        return sendMessage;
    }

    public boolean isDoubleAsterisk(char[] ch, int i) {
        return currCharIsAsterisk(ch, i) && nextCharIsAsterisk(ch, i);
    }

    public boolean currCharIsAsterisk(char[] ch, int i) {
        return ch[i] == '*';
    }

    public boolean nextCharIsAsterisk(char[] ch, int i) {
        return i + 1 < ch.length && ch[i + 1] == '*';
    }

    public boolean areAsterisksFollowedByText(char[] ch, int i) {
        return i + 2 < ch.length && isText(ch, i + 2) || i + 2 == ch.length;
    }

    public boolean isItemPrecidedByAsterisks(char[] ch, int i) {
        return i == 0 || currCharIsAsterisk(ch, i - 1);
    }

    public boolean isTextFollowedByAsterisks(char[] ch, int i) {
        return i == 0 || isText(ch, i - 1);
    }

    public boolean isText(char[] ch, int i) {
        return ch[i] != ' ';
    }

}
