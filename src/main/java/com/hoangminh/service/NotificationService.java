package com.hoangminh.service;

import com.hoangminh.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getNotificationsByUserId(Long userId);

    NotificationDTO markAsRead(Long notificationId);
}