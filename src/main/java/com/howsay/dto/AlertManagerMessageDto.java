package com.howsay.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AlertManagerMessageDto {

    String receiver;

    String status;

    List<Alert> alerts;

    Map<String, String> commonLabels;

    Map<String, String> commonAnnotations;

    public boolean resolved() {
        return "resolved".equals(status);
    }

}
