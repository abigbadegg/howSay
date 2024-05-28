package com.howsay.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookResponse {

    Integer code;

    String message;

    public static WebhookResponse ok() {
        return new WebhookResponse(HttpStatus.OK.value(), "ok");
    }

}
