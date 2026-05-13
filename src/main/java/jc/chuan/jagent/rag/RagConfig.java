package jc.chuan.jagent.rag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cy
 * @date 2026/5/13
 */
@Configuration
public class RagConfig {

    @Bean
    public EmbeddingStoreIngestor embeddingStoreIngestor(
            @Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel,
            EmbeddingStore<TextSegment> redisEmbeddingStore) {
        return EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(redisEmbeddingStore)
                .documentSplitter(DocumentSplitters.recursive(500, 100))
                .build();
    }
}
