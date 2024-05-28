package com.howsay.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;

@Data
public class Alert {

    String status;

    Map<String, String> labels;

    Map<String, String> annotations;

    LocalDateTime startsAt;

    LocalDateTime endsAt;
}
