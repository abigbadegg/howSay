package com.howsay.util;

import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {

    final List<String> messages;

    public MessageBuilder() {
        this.messages = new ArrayList<>();
    }

    public MessageBuilder addLine(String format, Object ...args) {
        messages.add(String.format(format, args));
        return this;
    }

    public String build() {
        return String.join("\n", messages);
    }

}
