package com.shop.ecommerce.service;

import com.shop.ecommerce.payload.request.ReplyRequest;

public interface ReplyService {
    void processReply(ReplyRequest replyRequest);
    void updateReply(ReplyRequest replyRequest);
    void deleteReply(Long id, String email);
}
