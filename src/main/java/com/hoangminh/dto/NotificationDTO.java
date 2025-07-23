package com.hoangminh.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationDTO {
    private Long id;
    private String message;
    private boolean isRead;
    private Date createdAt;
}