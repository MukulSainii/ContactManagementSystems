package com.smart.service.serviceInterface;

import java.util.Map;

public interface PaymentService {
    public void updatePayment(Map<String, Object> data);
    public String createOrder(Map<String, Object> data, String username);
}
