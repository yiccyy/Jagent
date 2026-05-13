package jc.chuan.jagent.rag;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author cy
 * @date 2026/5/13
 */
@Component
public class TextCleaner {

    public String clean(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        text = Normalizer.normalize(text, Normalizer.Form.NFKC);

        text = text.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");

        text = text.replaceAll("[ \\t]+", " ");
        text = text.replaceAll("\\n{3,}", "\n\n");

        text = Arrays.stream(text.split("\n"))
                .map(String::trim)
                .collect(Collectors.joining("\n"));

        return text.trim();
    }
}
