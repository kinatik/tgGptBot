package ru.novik.tggptbot.validation;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Getter
@Setter
@AllArgsConstructor
public class CheckResult {
    private final boolean valid;
    private final SendMessage message;

    public CheckResult(boolean valid) {
        this.valid = valid;
        this.message = null;
    }
}
