package org.example.system.disk;

import org.example.system.disk.handlers.DataAreaHandler;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.example.system.disk.DiskUtils.*;

/**
 * A classe DataArea é responsável por gerenciar a área de dados do disco.
 * Ela oferece métodos para ler, escrever, e manipular as entradas e dados na área do disco.
 */
public class DataArea {
    // Manipulador para operações de leitura e gravação na área de dados
    private final DataAreaHandler IOHandler;

    /**
     * Construtor para inicializar a área de dados do disco.
     * @param raf O RandomAccessFile do disco.
     * @param exists Flag que indica se o disco já existe.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    public DataArea(RandomAccessFile raf, boolean exists) throws IOException {
        this.IOHandler = new DataAreaHandler(raf);
        IOHandler.initialize(exists);
        initialize(exists);
    }

    /**
     * Inicializa a área de dados.
     * Se o disco não existir, cria a tabela de diretório raiz.
     * @param exists Flag que indica se o disco já existe.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    public void initialize(boolean exists) throws IOException {
        IOHandler.initialize(exists);
        if (!exists) {
            createRootDirTable();
        }
    }

    /**
     * Cria a tabela de diretórios raiz se o disco for novo.
     * @throws FileNotFoundException Se o arquivo não for encontrado.
     */
    public void createRootDirTable() throws FileNotFoundException {
        try (RandomAccessFile raf = new RandomAccessFile(DISK_NAME, "r")) {
            // Cria uma entrada para o diretório raiz "/"
            Entry root = new Entry("/", (byte) 1, 1, (byte) 1, 0);
            IOHandler.writeAt(root.toBytes(), ROOT_DIRECTORY_TABLE_OFFSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Escreve uma entrada no disco, no cluster especificado.
     * @param cluster O número do cluster onde a entrada será gravada.
     * @param entry A entrada a ser gravada.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    public void writeEntry(int cluster, Entry entry) throws IOException {
        byte[] clusterContent = IOHandler.readAt(cluster);

        // Procura por espaço livre no cluster para a entrada
        for (int i = 0; i <= clusterContent.length; i += ENTRY_SIZE) {
            byte c = clusterContent[i];
            if (c == FREE_AREA) {
                boolean hasSpace = true;

                // Verifica se o cluster está totalmente livre
                for (int j = 0; j < 269; j++) {
                    if (clusterContent[i + j] != FREE_AREA) {
                        hasSpace = false;
                        break;
                    }
                }
                if (hasSpace) {
                    IOHandler.writeAt(entry.toBytes(), (int) (DATA_AREA_OFFSET + ((long) cluster * CLUSTER_SIZE) + i));
                    return;
                }
            } else {
                byte[] buffer = new byte[ENTRY_SIZE];
                System.arraycopy(clusterContent, i, buffer, 0, ENTRY_SIZE);
                Entry e = Entry.toEntry(buffer);
                if (e.getStatus() == 0) {
                    IOHandler.writeAt(entry.toBytes(), (int) (DATA_AREA_OFFSET + ((long) cluster * CLUSTER_SIZE) + i));
                    return;
                }
            }
        }
    }

    /**
     * Escreve uma entrada no índice especificado.
     * @param index O índice onde a entrada será gravada.
     * @param entry A entrada a ser gravada.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    public void writeEntryAt(int index, Entry entry) throws IOException {
        IOHandler.writeAt(entry.toBytes(), index);
    }

    /**
     * Escreve o conteúdo no índice especificado.
     * @param index O índice onde o conteúdo será gravado.
     * @param content O conteúdo a ser gravado.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    public void writeContentAt(int index, byte[] content) throws IOException {
        IOHandler.writeAt(content, index);
    }

    /**
     * Lê todos os dados do disco.
     * @return Os dados do disco.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    public byte[] readAllDisk() throws IOException {
        return IOHandler.read();
    }

    /**
     * Lê uma entrada de um cluster.
     * @param cluster O número do cluster a ser lido.
     * @return Os dados da entrada.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    public byte[] readEntry(int cluster) throws IOException {
        return IOHandler.readAt(cluster);
    }

    /**
     * Lê uma entrada no índice especificado.
     * @param clusterId O índice onde a entrada será lida.
     * @return Os dados da entrada.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    public byte[] readEntryAt(int clusterId) throws IOException {
        return IOHandler.readAt(clusterId);
    }

    /**
     * Lê o conteúdo de um cluster.
     * @param clusterId O número do cluster a ser lido.
     * @return O conteúdo do cluster.
     * @throws IOException Se ocorrer algum erro de I/O.
     */
    public byte[] readContentAt(int clusterId) throws IOException {
        return IOHandler.readDataAt(clusterId);
    }
}
