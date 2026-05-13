package jc.chuan.jagent.controller;

import jakarta.annotation.Resource;
import java.io.IOException;
import jc.chuan.jagent.aiservice.Assistant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class ChatController {

    @Resource
    private Assistant assistant;

    @GetMapping("/chat")
    public String chat(@RequestParam String sessionId, @RequestParam String message) {
        if (message == null || message.isBlank()) {
            return "";
        }
        return assistant.chat(sessionId, message);
    }

    @GetMapping("/chat/stream")
    public SseEmitter chatStream(@RequestParam String sessionId, @RequestParam String message) {
        SseEmitter emitter = new SseEmitter();
        if (message == null || message.isBlank()) {
            emitter.complete();
            return emitter;
        }
        Flux<String> flux = assistant.chatStreaming(sessionId, message);
        flux.subscribe(
            token -> {
                try {
                    emitter.send(SseEmitter.event().data(token));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            },
            emitter::completeWithError,
            () -> {
                try {
                    emitter.send(SseEmitter.event().name("done").data(""));
                } catch (IOException e) {
                    // ignore
                }
                emitter.complete();
            }
        );
        return emitter;
    }

}
