package ru.novik.tggptbot.validation;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.novik.tggptbot.properties.BotProperty;

public class MessageValidator {

    private final AbstractMessageValidator validator;

    public MessageValidator(BotProperty botProperty) {
        validator = new HasMessageValidator();
        validator.linkWith(new HasTextValidator())
                .linkWith(new AllowedChatIdValidator(botProperty));
    }

    public CheckResult check(Update update) {
        return validator.check(update);
    }


    private abstract static class AbstractMessageValidator {
        public AbstractMessageValidator next;

        public AbstractMessageValidator linkWith(AbstractMessageValidator next) {
            this.next = next;
            return next;
        }

        public CheckResult check(Update update) {
            if (next == null) {
                return new CheckResult(true);
            }
            return next.check(update);
        }
    }


    private static class HasMessageValidator extends AbstractMessageValidator {
        @Override
        public CheckResult check(Update update) {
            if (update.hasMessage()) {
                return super.check(update);
            }
            return new CheckResult(false);
        }
    }


    private static class HasTextValidator extends AbstractMessageValidator {
        @Override
        public CheckResult check(Update update) {
            if (update.getMessage() != null && update.getMessage().hasText()) {
                return super.check(update);
            }
            return new CheckResult(false);
        }
    }


    private static class AllowedChatIdValidator extends AbstractMessageValidator {
        private final BotProperty botProperty;
        public AllowedChatIdValidator(BotProperty botProperty) {
            this.botProperty = botProperty;
        }

        @Override
        public CheckResult check(Update update) {
            Long chatId = update.getMessage().getChatId();
            if (!botProperty.getAllowedChatIds().contains(chatId)) {
                SendMessage message = new SendMessage();
                message.setChatId(update.getMessage().getChatId());
                message.setText(String.format(botProperty.getMessageNotAllowedChatIdReply(), chatId));
                return new CheckResult(false, message);
            }
            return new CheckResult(true);
        }
    }

}
