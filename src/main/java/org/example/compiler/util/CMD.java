package org.example.compiler.util;

import org.example.compiler.pipeline.component.IOComponent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CMD  extends IOComponent<CMD> {
    private  String source;

    public CMD(String path, boolean isFile) {
        if(isFile) {
            source = getFileContent(path);
        } else {
            source = path;
        }
    }

    public CMD(String command) {
        this.source = command;
    }

    private String getFileContent(String path) {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return builder.toString();
    }

    public String getSource() {
        return source;
    }
}