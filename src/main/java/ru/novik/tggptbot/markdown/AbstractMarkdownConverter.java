package ru.novik.tggptbot.markdown;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public abstract class AbstractMarkdownConverter {

    private AbstractMarkdownConverter next;

    public AbstractMarkdownConverter next(AbstractMarkdownConverter next) {
        this.next = next;
        return next;
    }

    public SendMessage convert(SendMessage sendMessage) {
        return sendMessage;
    }
}
