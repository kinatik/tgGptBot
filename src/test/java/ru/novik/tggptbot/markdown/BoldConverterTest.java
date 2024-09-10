package ru.novik.tggptbot.markdown;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BoldConverterTest {

    @Test
    void convert_singleBoldText() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("**bold**");
        sendMessage.setEntities(new LinkedList<>());

        BoldConverter converter = new BoldConverter();
        SendMessage result = converter.convert(sendMessage);

        assertEquals("bold", result.getText());
        assertEquals(1, result.getEntities().size());
        assertEquals("bold", result.getEntities().get(0).getType());
        assertEquals(0, result.getEntities().get(0).getOffset());
        assertEquals(4, result.getEntities().get(0).getLength());
    }

    @Test
    void convert_multipleBoldTexts() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("**bold1** and **bold2**");
        sendMessage.setEntities(new LinkedList<>());

        BoldConverter converter = new BoldConverter();
        SendMessage result = converter.convert(sendMessage);

        assertEquals("bold1 and bold2", result.getText());
        assertEquals(2, result.getEntities().size());
        assertEquals("bold", result.getEntities().get(0).getType());
        assertEquals(0, result.getEntities().get(0).getOffset());
        assertEquals(5, result.getEntities().get(0).getLength());
        assertEquals(10, result.getEntities().get(1).getOffset());
        assertEquals(5, result.getEntities().get(1).getLength());
    }

    @Test
    void convert_multipleBoldTextsWithItalic() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("***bold1*** and ***bold2***");
        sendMessage.setEntities(new LinkedList<>());

        BoldConverter converter = new BoldConverter();
        SendMessage result = converter.convert(sendMessage);

        assertEquals("*bold1* and *bold2*", result.getText());
        assertEquals(2, result.getEntities().size());
        assertEquals("bold", result.getEntities().get(0).getType());
        assertEquals(0, result.getEntities().get(0).getOffset());
        assertEquals(7, result.getEntities().get(0).getLength());
        assertEquals(12, result.getEntities().get(1).getOffset());
        assertEquals(7, result.getEntities().get(1).getLength());
    }

    @Test
    void convert_singleBoldTextWithAsteriskInside() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("**bold1* and *bold2**");
        sendMessage.setEntities(new LinkedList<>());

        BoldConverter converter = new BoldConverter();
        SendMessage result = converter.convert(sendMessage);

        assertEquals("bold1* and *bold2", result.getText());
        assertEquals(1, result.getEntities().size());
        assertEquals("bold", result.getEntities().get(0).getType());
        assertEquals(0, result.getEntities().get(0).getOffset());
        assertEquals(17, result.getEntities().get(0).getLength());
    }

    @Test
    void convert_noBoldText() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("no bold text");
        sendMessage.setEntities(new LinkedList<>());

        BoldConverter converter = new BoldConverter();
        SendMessage result = converter.convert(sendMessage);

        assertEquals("no bold text", result.getText());
        assertTrue(result.getEntities().isEmpty());
    }

    @Test
    void convert_emptyText() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("");
        sendMessage.setEntities(new LinkedList<>());

        BoldConverter converter = new BoldConverter();
        SendMessage result = converter.convert(sendMessage);

        assertEquals("", result.getText());
        assertTrue(result.getEntities().isEmpty());
    }

    @Test
    void convert_unclosedBoldText() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("**bold");
        sendMessage.setEntities(new LinkedList<>());

        BoldConverter converter = new BoldConverter();
        SendMessage result = converter.convert(sendMessage);

        assertEquals("**bold", result.getText());
        assertTrue(result.getEntities().isEmpty());
    }
}