package jc.chuan.jagent.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
//import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author cy
 * @date 2026/5/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final EmbeddingStoreIngestor embeddingStoreIngestor;
    private final TextCleaner textCleaner;

    public void ingest(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        log.info("Ingesting document: {}", filename);

        DocumentParser parser = getParser(filename);
        try (InputStream inputStream = file.getInputStream()) {
            Document document = parser.parse(inputStream);
            String cleanedText = textCleaner.clean(document.text());
            document = Document.from(cleanedText, document.metadata());
            embeddingStoreIngestor.ingest(document);
        }

        log.info("Document ingested successfully: {}", filename);
    }

    private DocumentParser getParser(String filename) {
        if (filename != null && filename.toLowerCase().endsWith(".pdf")) {
//            return new ApachePdfBoxDocumentParser();
            return new ApacheTikaDocumentParser();
        }
        return new TextDocumentParser();
    }
}
