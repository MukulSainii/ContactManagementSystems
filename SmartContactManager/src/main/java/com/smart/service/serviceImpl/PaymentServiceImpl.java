package com.smart.service.serviceImpl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.smart.Exception.BaseException;
import com.smart.Exception.NotFoundException;
import com.smart.dao.MyOrderRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.MyOrder;
import com.smart.service.serviceInterface.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final MyOrderRepository myOrderRepository;
    private final UserRepository userRepository;
    @Override
    public void updatePayment(Map<String, Object> data) {
        String orderId = data.get("order_id").toString();
        MyOrder myOrder = myOrderRepository.findByOrderId(orderId)
              .orElseThrow(()-> new NotFoundException("order not found, orderId: "+orderId,"payment failed, please try again"));
        myOrder.setPaymentId(data.get("payment_id").toString());
        myOrder.setStatus(data.get("status").toString());
        myOrderRepository.save(myOrder);
    }

    @Override
    public String createOrder(Map<String, Object> data, String username) {
        try {
            int amt = Integer.parseInt(data.get("amount").toString());
            //create client(key,secret) ==from rozarpay site which we generate
            var client = new RazorpayClient("rzp_test_aetInMhOh05zJ3", "3XoB97MoSvRzLHjcmf7zTTFN");
            //creating order
            JSONObject object = new JSONObject();
            object.put("amount", amt * 100);//our amount in rupees ,but we have converted it into paisa
            object.put("currency", "INR");
            object.put("receipt", "txn_23456");

            Order order = client.orders.create(object);

            // save order in your database
            MyOrder myOrder = new MyOrder();
            myOrder.setAmount(order.get("amount") + "");
            myOrder.setOrderId(order.get("id"));
            myOrder.setPaymentId(null);
            myOrder.setStatus("created");
            myOrder.setReceipt(order.get("receipt"));
            myOrder.setUser(userRepository.getUserByUserName(username)
                    .orElseThrow(() -> new RuntimeException("User Not Found"))
            );
            myOrderRepository.save(myOrder);
            return order.toString();
        }catch(Exception ex){
            throw new BaseException("create order failed","payment failed, please try again");
        }
    }
}
