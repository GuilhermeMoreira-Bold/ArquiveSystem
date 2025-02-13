package org.example.system.disk.handlers;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import static org.example.system.disk.DiskUtils.*;

/**
 * Classe responsável por gerenciar operações de leitura e escrita na área de dados do disco.
 * Implementa a interface DiskIOHandler para leitura e escrita de dados de forma genérica.
 */
public class DataAreaHandler implements DiskIOHandler<byte[]>, AutoCloseable {
    private final RandomAccessFile raf;

    /**
     * Construtor que inicializa a classe com um arquivo de acesso aleatório.
     * @param raf O RandomAccessFile que será utilizado para ler e escrever no disco.
     */
    public DataAreaHandler(RandomAccessFile raf) {
        this.raf = raf;
    }

    /**
     * Inicializa a área de dados do disco. Se o disco já existe, lê a área de dados,
     * caso contrário, cria a área de dados.
     * @param isNew Flag que indica se o disco é novo ou já existe.
     * @return Dados lidos da área de dados.
     * @throws IOException Se ocorrer algum erro de I/O durante a operação.
     */
    @Override
    public byte[] initialize(boolean isNew) throws IOException {
        if (!isNew) {
            createDataArea();
        }
        return read();
    }

    /**
     * Lê todos os dados da área de dados do disco.
     * @return Os dados da área de dados do disco.
     * @throws IOException Se ocorrer algum erro de I/O durante a operação.
     */
    @Override
    public byte[] read() throws IOException {
        raf.seek(DATA_AREA_OFFSET);
        byte[] data = new byte[TOTAL_BLOCKS];
        raf.readFully(data);
        return data;
    }

    /**
     * Lê os dados de um índice específico na área de dados do disco.
     * @param index O índice do cluster a ser lido.
     * @return Os dados do cluster especificado.
     * @throws IOException Se ocorrer algum erro de I/O durante a operação.
     */
    @Override
    public byte[] readAt(int index) throws IOException {
        if (index < 0 || index >= TOTAL_BLOCKS) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for data area size " + TOTAL_BLOCKS);
        }
        raf.seek((long) index * CLUSTER_SIZE + DATA_AREA_OFFSET);
        byte[] content = new byte[CLUSTER_SIZE];
        raf.readFully(content);
        return content;
    }

    /**
     * Escreve os dados na área de dados do disco.
     * @param value O valor a ser escrito.
     * @throws IOException Se ocorrer algum erro de I/O durante a operação.
     */
    @Override
    public void write(byte[] value) throws IOException {
        if (value.length > TOTAL_BLOCKS) {
            throw new IllegalArgumentException("Data size exceeds total block size.");
        }
        raf.seek(DATA_AREA_OFFSET);
        raf.write(value);
    }

    /**
     * Escreve os dados em um índice específico na área de dados do disco.
     * @param value O valor a ser escrito.
     * @param index O índice onde o valor será escrito.
     * @throws IOException Se ocorrer algum erro de I/O durante a operação.
     */
    @Override
    public void writeAt(byte[] value, int index) throws IOException {
        if (index < 0 || index >= DISK_SIZE) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for data area size " + DISK_SIZE);
        }
        raf.seek(index);
        raf.write(value);
    }

    /**
     * Fecha o arquivo de acesso aleatório utilizado para operações de leitura e escrita.
     * @throws IOException Se ocorrer algum erro ao fechar o arquivo.
     */
    @Override
    public void close() throws IOException {
        if (raf != null) {
            raf.close();
        }
    }

    /**
     * Cria a área de dados preenchendo-a com a constante FREE_AREA.
     * @throws IOException Se ocorrer algum erro de I/O durante a operação.
     */
    private void createDataArea() throws IOException {
        raf.seek(DATA_AREA_OFFSET);
        byte[] emptyData = new byte[DISK_SIZE];
        Arrays.fill(emptyData, FREE_AREA);
        raf.write(emptyData);
    }

    /**
     * Lê os dados de um índice específico na área de dados.
     * @param index O índice do cluster a ser lido.
     * @return O conteúdo do cluster.
     * @throws IOException Se ocorrer algum erro de I/O durante a operação.
     */
    public byte[] readDataAt(int index) throws IOException {
        if (index < 0 || index >= DISK_SIZE) {
            throw new IndexOutOfBoundsException("Index out of bounds for data area size");
        }
        raf.seek((long) index);
        byte[] content = new byte[CLUSTER_DATA_AREA_SIZE];
        raf.readFully(content);
        return content;
    }
}
