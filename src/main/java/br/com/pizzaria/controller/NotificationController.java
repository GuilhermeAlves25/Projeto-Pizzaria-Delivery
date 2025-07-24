package br.com.pizzaria.controller;

import br.com.pizzaria.security.CustomUserDetails;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/notifications")
public class NotificationController {


    private static final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();


    @GetMapping(path = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        String userEmail = userDetails.getUsername();


        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);


        emitter.onCompletion(() -> emitters.remove(userEmail, emitter));
        emitter.onTimeout(() -> emitters.remove(userEmail, emitter));
        emitter.onError(e -> emitters.remove(userEmail, emitter));


        emitters.put(userEmail, emitter);

        return emitter;
    }


    public static void sendEventToUser(String userEmail, String data) {
        SseEmitter emitter = emitters.get(userEmail);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("status-update").data(data));
            } catch (IOException e) {
                emitters.remove(userEmail);
            }
        }
    }
}