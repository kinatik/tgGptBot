package ru.novik.tggptbot.markdown;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class HeadingConverterTest {

    @Test
    void convert_singleLevelOneHeading() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("# Heading 1");
        sendMessage.setEntities(new LinkedList<>());

        HeadingConverter converter = new HeadingConverter();
        SendMessage result = converter.convert(sendMessage);

        assertEquals("Heading 1\n", result.getText());
        assertEquals(1, result.getEntities().size());
        assertEquals("bold", result.getEntities().get(0).getType());
    }

    @Test
    void convert_singleLevelTwoHeading() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("## Heading 2");
        sendMessage.setEntities(new LinkedList<>());

        HeadingConverter converter = new HeadingConverter();
        SendMessage result = converter.convert(sendMessage);

        assertEquals("Heading 2\n", result.getText());
        assertEquals(1, result.getEntities().size());
        assertEquals("bold", result.getEntities().get(0).getType());
    }

    @Test
    void convert_singleLevelThreeHeading() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("### Heading 3");
        sendMessage.setEntities(new LinkedList<>());

        HeadingConverter converter = new HeadingConverter();
        SendMessage result = converter.convert(sendMessage);

        assertEquals("Heading 3\n", result.getText());
        assertEquals(1, result.getEntities().size());
        assertEquals("bold", result.getEntities().get(0).getType());
    }

    @Test
    void convert_multipleHeadings() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("# Heading 1\n## Heading 2\n### Heading 3");
        sendMessage.setEntities(new LinkedList<>());

        HeadingConverter converter = new HeadingConverter();
        SendMessage result = converter.convert(sendMessage);

        assertEquals("Heading 1\nHeading 2\nHeading 3\n", result.getText());
        assertEquals(3, result.getEntities().size());
    }

    @Test
    void convert_noHeadings() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("No headings here");
        sendMessage.setEntities(new LinkedList<>());

        HeadingConverter converter = new HeadingConverter();
        SendMessage result = converter.convert(sendMessage);

        assertEquals("No headings here\n", result.getText());
        assertTrue(result.getEntities().isEmpty());
    }

    @Test
    void convert_emptyText() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("");
        sendMessage.setEntities(new LinkedList<>());

        HeadingConverter converter = new HeadingConverter();
        SendMessage result = converter.convert(sendMessage);

        assertEquals("\n", result.getText());
        assertTrue(result.getEntities().isEmpty());
    }
}