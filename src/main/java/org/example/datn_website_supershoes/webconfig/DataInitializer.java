package org.example.datn_website_supershoes.webconfig;

import org.example.datn_website_supershoes.model.PaymentMethod;
import org.example.datn_website_supershoes.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initializePaymentMethods();
    }

    private void initializePaymentMethods() {
        addPaymentMethodIfNotExist("QR payment", "Thanh toán QR", 1);
        addPaymentMethodIfNotExist("Cash payment", "Thanh toán tiền mặt", 2);
    }

    private void addPaymentMethodIfNotExist(String methodName, String note, Integer type) {
        if (paymentMethodRepository.findByMethodNameAndType(methodName,type).isEmpty()) {
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setMethodName(methodName);
            paymentMethod.setNote(note);
            paymentMethod.setStatus("ACTIVE");
            paymentMethod.setType(type);
            paymentMethodRepository.save(paymentMethod);
        }
    }
}
