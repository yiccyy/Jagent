package jc.chuan.jagent.aiservice;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

/**
 * @author cy
 * @date 2026/5/12
 */
@AiService(
    wiringMode = AiServiceWiringMode.EXPLICIT,
    chatModel = "openAiChatModel",
    streamingChatModel = "openAiStreamingChatModel",
//    chatMemory = "chatMemory"
    chatMemoryProvider = "chatMemoryProvider"   //会话记忆提供者

)
public interface Assistant {
    //阻塞式
    String chat(@MemoryId String sessionId, @UserMessage String message);
    //流式
    @SystemMessage(fromResource = "system.txt")
    Flux<String> chatStreaming(@MemoryId String memoryId,  @UserMessage String message);
}
