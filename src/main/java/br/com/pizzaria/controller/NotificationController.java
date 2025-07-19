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

    // Mapa para guardar a conexão de cada usuário (email -> conexão)
    private static final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     * Endpoint que o cliente "assina" para receber notificações em tempo real.
     */
    @GetMapping(path = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        String userEmail = userDetails.getUsername();

        // Cria um SseEmitter (a conexão) com um timeout longo
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        // Define o que fazer quando a conexão fechar, der erro ou timeout
        emitter.onCompletion(() -> emitters.remove(userEmail, emitter));
        emitter.onTimeout(() -> emitters.remove(userEmail, emitter));
        emitter.onError(e -> emitters.remove(userEmail, emitter));

        // Armazena a conexão do usuário no mapa
        emitters.put(userEmail, emitter);

        return emitter;
    }

    /**
     * Método estático para que outras partes do sistema possam enviar notificações.
     */
    public static void sendEventToUser(String userEmail, String data) {
        SseEmitter emitter = emitters.get(userEmail);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("status-update").data(data));
            } catch (IOException e) {
                emitters.remove(userEmail); // Remove em caso de erro
            }
        }
    }
}