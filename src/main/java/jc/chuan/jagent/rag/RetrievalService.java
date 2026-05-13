package jc.chuan.jagent.rag;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cy
 * @date 2026/5/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RetrievalService {

    @Resource
    private final EmbeddingStore<TextSegment> embeddingStore;
    @Resource
    private final EmbeddingModel embeddingModel;

    public List<EmbeddingMatch<TextSegment>> search(String query, int maxResults) {
        log.debug("Searching for: {}", query);
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        List<EmbeddingMatch<TextSegment>> results = embeddingStore.search(
            EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(maxResults)
                .build()
        ).matches();
        log.debug("Found {} results", results.size());
        return results;
    }
}
