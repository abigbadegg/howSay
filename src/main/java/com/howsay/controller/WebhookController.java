package com.howsay.controller;

import org.apache.shenyu.common.utils.JsonUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.howsay.dto.Alert;
import com.howsay.dto.AlertManagerMessageDto;
import com.howsay.dto.WebhookResponse;
import com.howsay.dto.WxMessageDto;
import com.howsay.service.WxWebhookApiService;
import com.howsay.util.MessageBuilder;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true)
public class WebhookController {

    WxWebhookApiService wxWebhookApiService;

    @PostMapping("webhook")
    public WebhookResponse webhook(@RequestBody AlertManagerMessageDto messageDto) {
        String json = JsonUtils.toJson(messageDto);
        log.info("webhook: {}", json);
        return doSendWebhook(messageDto);
    }

    @Timed
    @PostMapping("rateLimitAlert")
    public WebhookResponse webhookAlertSdk(@RequestBody AlertManagerMessageDto messageDto) {
        String json = JsonUtils.toJson(messageDto);
        log.info("rateLimitAlert: {}", json);
        return doSendWebhook(messageDto);
    }

    @PostMapping("logWarnTotalAlert")
    public WebhookResponse webhookHypnos(@RequestBody AlertManagerMessageDto messageDto) {
        String json = JsonUtils.toJson(messageDto);
        log.info("logWarnTotalAlert: {}", json);
        return doSendWebhook(messageDto);
    }

    private WebhookResponse doSendWebhook(AlertManagerMessageDto messageDto) {
        if (messageDto.resolved()) {
            return WebhookResponse.ok();
        }
        // messageDto.getAlerts().forEach(this::send);
        return WebhookResponse.ok();
    }

    public void send(Alert alert) {

        String summary = alert.getAnnotations().get("summary");
        String description = alert.getAnnotations().get("description");
        String application = alert.getLabels().get("application");

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.addLine("**%s** 告警  ", application);
        messageBuilder.addLine("%s  ", summary);
        if (description != null) {
            messageBuilder.addLine("%s  ", description);
        }
        alert.getLabels().forEach((key, value) -> {
            messageBuilder.addLine("> %s: <font color=\"comment\">%s</font>", key, value);
        });
        WxMessageDto wxMessageDto = new WxMessageDto("markdown", messageBuilder.build());
        wxWebhookApiService.send(wxMessageDto);
    }

}
