package jc.chuan.jagent.controller;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import jc.chuan.jagent.rag.DocumentService;
import jc.chuan.jagent.rag.RetrievalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author cy
 * @date 2026/5/13
 *
 * 面向上传文档等后台管理
 */
@Slf4j
@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final DocumentService documentService;
    private final RetrievalService retrievalService;

    /**
     * 上传并摄入文档到向量库
     */
    @PostMapping("/document/upload")
    public ResponseEntity<Map<String, String>> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            documentService.ingest(file);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "filename", file.getOriginalFilename()
            ));
        } catch (Exception e) {
            log.error("Failed to ingest document", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 语义检索
     */
    @PostMapping("/search")
    public ResponseEntity<List<String>> search(@RequestParam("query") String query,
                                               @RequestParam(value = "maxResults", defaultValue = "5") int maxResults) {
        List<EmbeddingMatch<TextSegment>> results = retrievalService.search(query, maxResults);
        List<String> texts = results.stream()
                .map(match -> match.embedded().text())
                .toList();
        return ResponseEntity.ok(texts);
    }

    /**
     * 带相似度分数的语义检索
     */
    @PostMapping("/search/detail")
    public ResponseEntity<List<Map<String, Object>>> searchDetail(
            @RequestParam("query") String query,
            @RequestParam(value = "maxResults", defaultValue = "5") int maxResults) {
        List<EmbeddingMatch<TextSegment>> results = retrievalService.search(query, maxResults);
        List<Map<String, Object>> details = results.stream()
                .map(match -> Map.<String, Object>of(
                        "text", match.embedded().text(),
                        "score", match.score()
                ))
                .toList();
        return ResponseEntity.ok(details);
    }
}
