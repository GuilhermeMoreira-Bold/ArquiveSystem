package org.example.system.disk.handlers;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import static org.example.system.disk.DiskUtils.*;
import static org.example.system.disk.DiskUtils.FREE_AREA;

/**
 * A classe FatHandler gerencia as operações de leitura e escrita na Tabela de Alocação de Arquivos (FAT) do disco.
 * Implementa a interface DiskIOHandler para abstrair o acesso ao disco.
 */
public class FatHandler implements DiskIOHandler<int[]>, AutoCloseable {
    private final RandomAccessFile raf;

    /**
     * Construtor para inicializar o manipulador da FAT.
     * @param raf O RandomAccessFile do disco.
     */
    public FatHandler(RandomAccessFile raf) {
        this.raf = raf;
    }

    /**
     * Inicializa a FAT. Se o disco não existir, cria uma área FAT vazia.
     * @param exists Flag que indica se o disco já existe.
     * @return A FAT lida do disco.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    @Override
    public int[] initialize(boolean exists) throws IOException {
        if (!exists) {
            createFatArea(); // Cria a área FAT se o disco for novo
        }
        return getFat(); // Retorna a FAT existente ou recém-criada
    }

    /**
     * Lê a FAT do disco.
     * @return A FAT lida do disco.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    @Override
    public int[] read() throws IOException {
        return getFat(); // Retorna a FAT lida do disco
    }

    /**
     * Lê um único valor da FAT no índice especificado.
     * @param index O índice da FAT a ser lido.
     * @return O valor lido da FAT no índice especificado.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    @Override
    public int[] readAt(int index) throws IOException {
        if (index < 0 || index >= FAT_SIZE) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for FAT size " + FAT_SIZE);
        }
        raf.seek((index * 4L) + FAT_INITIAL_OFFSET); // Posiciona o ponteiro no índice especificado
        return new int[]{raf.readInt()}; // Lê o valor e o retorna em um array
    }

    /**
     * Escreve um valor na FAT no índice especificado.
     * @param value O valor a ser escrito na FAT.
     * @param index O índice onde o valor será escrito.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    @Override
    public void writeAt(int[] value, int index) throws IOException {
        if (index < 0 || index >= FAT_SIZE) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for FAT size " + FAT_SIZE);
        }
        raf.seek((index * 4L) + FAT_INITIAL_OFFSET); // Posiciona o ponteiro no índice especificado
        raf.writeInt(value[0]); // Escreve o valor na FAT
    }

    /**
     * Escreve a FAT inteira no disco.
     * @param value A FAT a ser gravada.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    @Override
    public void write(int[] value) throws IOException {
        raf.seek(FAT_INITIAL_OFFSET); // Posiciona o ponteiro no início da FAT
        for (int i : value) {
            raf.writeInt(i); // Escreve cada valor da FAT
        }
    }

    /**
     * Cria a área FAT inicial. Inicializa com um valor EOF para o diretório raiz e marca os outros blocos como livres.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    private void createFatArea() throws IOException {
        raf.seek(FAT_INITIAL_OFFSET); // Posiciona o ponteiro no início da FAT
        raf.writeInt(-2); // Marca o EOF do diretório raiz
        for (int i = 0; i < FAT_SIZE - 1; i++) {
            raf.writeInt(-1); // Marca os outros blocos como livres (-1)
        }
    }

    /**
     * Obtém a FAT do disco.
     * @return A FAT lida do disco.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    private int[] getFat() throws IOException {
        raf.seek(FAT_INITIAL_OFFSET); // Posiciona o ponteiro no início da FAT
        int[] list = new int[FAT_SIZE];
        for (int i = 0; i < FAT_SIZE; i++) {
            list[i] = raf.readInt(); // Lê cada valor da FAT e armazena no array
        }
        return list;
    }

    /**
     * Fecha o RandomAccessFile ao finalizar o uso.
     * @throws IOException Se ocorrer algum erro de I/O ao fechar o arquivo.
     */
    @Override
    public void close() throws IOException {
        if (raf != null) {
            raf.close(); // Fecha o arquivo
        }
    }
}
