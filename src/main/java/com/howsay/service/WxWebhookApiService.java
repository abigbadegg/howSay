package com.howsay.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.howsay.dto.WxMessageDto;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Service
public class WxWebhookApiService {

    WebClient webClient;

    static String WEBHOOK_KEY = "e6d8e29c-87a5-415a-a193-9d5e15d7d7e7";

    public void send(WxMessageDto messageDto) {
        System.out.println(messageDto);
        webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/send")
                        .queryParam("key", WEBHOOK_KEY)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(messageDto)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}
