package org.example.system.disk.handlers;

import java.io.IOException;

/**
 * Interface que define as operações de leitura e escrita em um dispositivo de armazenamento.
 * A interface é genérica, permitindo o uso de diferentes tipos de dados para manipulação do disco.
 * @param <T> Tipo de dados que será manipulado pelo handler (ex: int[], byte[], etc.).
 */
public interface DiskIOHandler<T> {

    /**
     * Inicializa o dispositivo de armazenamento (ex: disco). Se o dispositivo for novo,
     * deve ser criado e inicializado adequadamente.
     * @param isNew Flag que indica se o dispositivo é novo ou já existente.
     * @return O valor de tipo T após a inicialização (ex: FAT, dados, etc.).
     * @throws IOException Se ocorrer algum erro de I/O durante a operação.
     */
    T initialize(boolean isNew) throws IOException;

    /**
     * Lê os dados do dispositivo de armazenamento.
     * @return O valor de tipo T lido do dispositivo.
     * @throws IOException Se ocorrer algum erro de I/O durante a operação.
     */
    T read() throws IOException;

    /**
     * Escreve um valor no dispositivo de armazenamento.
     * @param value O valor de tipo T a ser escrito.
     * @throws IOException Se ocorrer algum erro de I/O durante a operação.
     */
    void write(T value) throws IOException;

    /**
     * Escreve um valor no dispositivo de armazenamento em um índice específico.
     * @param value O valor de tipo T a ser escrito.
     * @param index O índice onde o valor será escrito.
     * @throws IOException Se ocorrer algum erro de I/O durante a operação.
     */
    void writeAt(T value, int index) throws IOException;

    /**
     * Lê um valor específico de um índice no dispositivo de armazenamento.
     * @param index O índice de onde o valor será lido.
     * @return O valor de tipo T lido no índice especificado.
     * @throws IOException Se ocorrer algum erro de I/O durante a operação.
     */
    T readAt(int index) throws IOException;
}
