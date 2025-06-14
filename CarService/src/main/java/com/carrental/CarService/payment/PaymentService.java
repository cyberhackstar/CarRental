package com.carrental.CarService.payment;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RazorpayClient razorpayClient;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    public String createOrder(double amount) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int)(amount * 100)); // amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_rcptid_" + System.currentTimeMillis());
        orderRequest.put("payment_capture", 1);

        Order order = razorpayClient.orders.create(orderRequest);
        return order.toString();
    }

    public boolean verifyWebhookSignature(String payload, String actualSignature) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(webhookSecret.getBytes(), "HmacSHA256");
            sha256Hmac.init(secretKey);

            byte[] hash = sha256Hmac.doFinal(payload.getBytes());
            String computedSignature = new String(Base64.getEncoder().encode(hash));

            return computedSignature.equals(actualSignature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
