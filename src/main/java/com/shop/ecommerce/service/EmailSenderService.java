package com.shop.ecommerce.service;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String message);
}
