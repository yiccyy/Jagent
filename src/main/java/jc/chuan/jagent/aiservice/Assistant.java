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
    chatMemoryProvider = "chatMemoryProvider"
)
public interface Assistant {
    //阻塞式
//    @SystemMessage("You are a helpful assistant")
    String chat(String message);
    //流式
//    @SystemMessage(fromResource = "system.txt")
    @SystemMessage("You are a helpful assistant")
    Flux<String> chatStreaming(@MemoryId Integer memoryId,  @UserMessage String message);
}
