package org.example.datn_website_supershoes.webconfig;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class NotificationController {

    private final EmitterProcessor<String> emitterProcessor = EmitterProcessor.create();

    // API này để client có thể nhận thông báo khi có thay đổi
    @GetMapping(value = "/sse/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamNotifications() {
        return emitterProcessor; // Trả về một Flux để phát đi thông báo
    }

    // Phương thức này sẽ được gọi khi có thay đổi trong database (cập nhật size chẳng hạn)
    public void sendNotification(String message) {
        emitterProcessor.onNext(message);  // Phát đi sự kiện
    }
}
