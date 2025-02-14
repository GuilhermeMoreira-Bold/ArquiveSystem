package org.example.console.util;

import org.example.console.pipeline.component.IOComponent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CMD extends IOComponent<CMD> {
    private String source;

    /**
     * Construtor que recebe um caminho de arquivo e um booleano indicando se é um arquivo.
     * Se for um arquivo, lê seu conteúdo, caso contrário, trata o caminho como o comando.
     *
     * @param path O caminho do arquivo ou comando.
     * @param isFile Indicador se o caminho é um arquivo ou um comando direto.
     */
    public CMD(String path, boolean isFile) {
        if (isFile) {
            source = getFileContent(path); // Lê o conteúdo do arquivo se for um arquivo
        } else {
            source = path; // Se não for arquivo, trata como comando
        }
    }

    /**
     * Construtor que recebe diretamente o comando como uma string.
     *
     * @param command O comando.
     */
    public CMD(String command) {
        this.source = command;
    }

    /**
     * Lê o conteúdo de um arquivo e retorna como uma string.
     *
     * @param path O caminho do arquivo.
     * @return O conteúdo do arquivo como string.
     */
    private String getFileContent(String path) {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n"); // Adiciona linha por linha
            }
        } catch (IOException e) {
            throw new RuntimeException(e); // Lança exceção em caso de erro
        }

        return builder.toString(); // Retorna o conteúdo do arquivo
    }

    /**
     * Retorna o conteúdo (seja de arquivo ou comando direto).
     *
     * @return O conteúdo do comando.
     */
    public String getSource() {
        return source;
    }
}
