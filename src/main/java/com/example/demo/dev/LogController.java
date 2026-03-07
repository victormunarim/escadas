package com.example.demo.dev;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class LogController {

    private static final int MAX_LINES = 200;

    @GetMapping("/dev/logs")
    public List<String> logs() throws Exception {

        Path path = Path.of("logs/app.log");

        if (!Files.exists(path)) {
            return List.of("Log file not created yet...");
        }

        List<String> lines = new ArrayList<>();

        try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")) {

            long pointer = file.length() - 1;
            int lineCount = 0;
            StringBuilder sb = new StringBuilder();

            while (pointer >= 0 && lineCount < MAX_LINES) {

                file.seek(pointer);
                char c = (char) file.read();

                if (c == '\n') {
                    lines.add(sb.reverse().toString());
                    sb.setLength(0);
                    lineCount++;
                } else {
                    sb.append(c);
                }

                pointer--;
            }

            if (sb.length() > 0) {
                lines.add(sb.reverse().toString());
            }
        }

        Collections.reverse(lines);
        return lines;
    }
}