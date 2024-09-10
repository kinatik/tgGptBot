package ru.novik.tggptbot.markdown;

public class Converter {

    private AbstractMarkdownConverter converter;

    public Converter() {
        converter = new HeadingConverter();
    }


}
