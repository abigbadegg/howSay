package com.howsay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class WxMessageDto {

    private String msgtype;

    private Text text;

    private Text markdown;

    public WxMessageDto(String textMessage) {
        this("text", textMessage);
    }

    public WxMessageDto(String msgType, String message) {
        this.msgtype = msgType;
        switch (msgType) {
            case "text": this.text = new Text(message);break;
            case "markdown": this.markdown = new Text(message);break;
        }
    }

    @AllArgsConstructor
    @Data
    public static class Text {
        String content;
    }
}
