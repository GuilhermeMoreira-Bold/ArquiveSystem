package org.example.system.disk;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.example.system.disk.DiskUtils.*;

/**
 * A classe `VirtualDisk` representa o disco virtual no sistema de arquivos. Ela lida com a alocação de espaço no disco,
 * leitura e escrita de dados, gerenciamento de entradas no disco e manutenção de metadados relacionados aos arquivos.
 */
public class VirtualDisk {
    private FileAllocationTable fat;  // Tabela de alocação de arquivos (FAT)
    private DataArea dataArea;  // Área de dados do disco

    /**
     * Constrói uma instância do disco virtual, inicializando ou montando um disco existente ou criando um novo.
     *
     * @param exists indica se o disco já existe
     * @param path o caminho do disco
     * @throws IOException se ocorrer um erro ao acessar ou criar o disco
     */
    public VirtualDisk(boolean exists, String path) throws IOException {
        DISK_NAME = path;
        if (exists) {
            initializeDisk(true);  // Inicializa o disco existente
        } else {
            mountNewDisk();  // Cria um novo disco
            initializeDisk(false);  // Inicializa o novo disco
        }
    }

    /**
     * Inicializa o disco virtual, configurando a tabela de alocação de arquivos e a área de dados.
     *
     * @param exists indica se o disco já existe
     * @throws IOException se ocorrer um erro ao acessar o disco
     */
    private void initializeDisk(boolean exists) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(DISK_NAME, "rw");
        fat = new FileAllocationTable(raf, exists);  // Inicializa a FAT
        dataArea = new DataArea(raf, exists);  // Inicializa a área de dados
    }

    /**
     * Cria um novo disco virtual, escrevendo os metadados iniciais.
     *
     * @throws IOException se ocorrer um erro ao criar o disco
     */
    private void mountNewDisk() throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(DISK_NAME))) {
            dos.writeInt(DISK_SIZE);  // Escreve o tamanho do disco
            dos.writeInt(CLUSTER_SIZE);  // Escreve o tamanho do cluster
            dos.writeInt(TOTAL_BLOCKS);  // Escreve o número total de blocos
            dos.writeInt(FAT_SIZE);  // Escreve o tamanho da FAT
        }
    }

    /**
     * Lê os metadados do disco e os exibe.
     *
     * @throws IOException se ocorrer um erro ao ler os metadados do disco
     */
    public void readDiskMetaData() throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(DISK_NAME))) {
            System.out.println("Disk Size: " + dis.readInt());
            System.out.println("Cluster Size: " + dis.readInt());
            System.out.println("Total Block: " + dis.readInt());
            System.out.println("Fat Size: " + dis.readInt());
            System.out.println("Data area starter point: " + DATA_AREA_OFFSET);
        }
    }

    /**
     * Adiciona uma entrada de arquivo no disco, alocando clusters e escrevendo as informações necessárias na área de dados.
     *
     * @param entry a entrada do arquivo a ser adicionada
     * @return o bloco inicial da entrada no disco
     * @throws IOException se ocorrer um erro ao adicionar a entrada
     */
    public int addEntry(Entry entry) throws IOException {
        entry.setStartBlock(fat.addFileCluster(entry));  // Adiciona clusters à FAT
        dataArea.writeEntry(entry.getParent(), entry);  // Escreve a entrada na área de dados
        dataArea.writeEntry(entry.getStartBlock(), entry);  // Escreve a entrada no bloco inicial
        return entry.getStartBlock();
    }

    /**
     * Edita o conteúdo de uma entrada de arquivo no disco.
     *
     * @param entry a entrada do arquivo a ser editada
     * @param content o novo conteúdo a ser escrito
     * @throws IOException se ocorrer um erro ao editar a entrada
     */
    public void editEntry(Entry entry, String content) throws IOException {
        writeEntryWithContent(entry, content);  // Escreve o conteúdo editado
    }

    /**
     * Escreve o conteúdo de um arquivo na entrada do disco, dividindo-o em clusters.
     *
     * @param entry a entrada do arquivo
     * @param content o conteúdo a ser escrito
     * @throws IOException se ocorrer um erro ao escrever o conteúdo
     */
    public void writeEntryWithContent(Entry entry, String content) throws IOException {
        byte[] stringBytes = content.getBytes();  // Converte o conteúdo para bytes
        int stringIndex = 0;  // Índice de leitura do conteúdo
        int remainingToCopyBytes = stringBytes.length;  // Bytes restantes a serem copiados

        fat.reallocateFileSize(entry);  // Recalcula o tamanho do arquivo
        List<Integer> clustersIndex = fat.findFileClusters(entry);  // Encontra os clusters alocados ao arquivo
        for (int i : clustersIndex) {
            if (i == -2) {
                break;
            }

            byte[] dest = new byte[CLUSTER_DATA_AREA_SIZE];
            System.arraycopy(stringBytes, stringIndex, dest, 0, Math.min(CLUSTER_DATA_AREA_SIZE, remainingToCopyBytes));

            if (remainingToCopyBytes < CLUSTER_DATA_AREA_SIZE) {
                Arrays.fill(dest, remainingToCopyBytes, dest.length - 1, FREE_AREA);  // Preenche o restante com área livre
            }

            remainingToCopyBytes -= CLUSTER_DATA_AREA_SIZE;
            stringIndex += Math.min(stringIndex, stringBytes.length);
            dataArea.writeContentAt(i * CLUSTER_SIZE + ENTRY_SIZE + DATA_AREA_OFFSET, dest);  // Escreve o conteúdo no disco
        }
    }

    /**
     * Lê os dados de uma entrada de arquivo do disco.
     *
     * @param entry a entrada do arquivo a ser lida
     * @return o conteúdo do arquivo
     * @throws IOException se ocorrer um erro ao ler o arquivo
     */
    public String readEntryData(Entry entry) throws IOException {
        List<Integer> clusters = fat.findFileClusters(entry);  // Encontra os clusters alocados ao arquivo
        StringBuilder sb = new StringBuilder();
        for (int i : clusters) {
            if (i == -2) break;
            byte[] b = dataArea.readContentAt(i * CLUSTER_SIZE + DATA_AREA_OFFSET + ENTRY_SIZE);  // Lê o conteúdo de cada cluster

            int index = 0;
            for (; index < b.length; index++) {
                if (b[index] == FREE_AREA) {
                    break;
                }
            }

            sb.append(new String(Arrays.copyOfRange(b, 0, index)));  // Adiciona o conteúdo ao StringBuilder
        }
        return sb.toString();  // Retorna o conteúdo lido
    }

    /**
     * Remove uma entrada de arquivo do disco, liberando os clusters e atualizando a FAT.
     *
     * @param entry a entrada do arquivo a ser removida
     * @throws IOException se ocorrer um erro ao remover a entrada
     */
    public void removeEntry(Entry entry) throws IOException {
        fat.removeFileCluster(entry);  // Remove os clusters alocados ao arquivo

        byte[] buffer = dataArea.readEntry(entry.getParent());  // Lê a entrada do diretório pai
        int offset = 0;

        for (int i = 0; i < buffer.length; i += ENTRY_SIZE) {
            if (offset == CLUSTER_SIZE) break;
            byte actualByte = buffer[offset];
            if (actualByte == FREE_AREA) {
                offset += ENTRY_SIZE;
            } else {
                byte[] e = new byte[ENTRY_SIZE];
                System.arraycopy(buffer, offset, e, 0, ENTRY_SIZE);
                Entry ent = Entry.toEntry(e);  // Converte a entrada
                if (!ent.getName().equals(entry.getName())) {
                    offset += ENTRY_SIZE;
                } else {
                    ent.setStatus((byte) 0);  // Marca a entrada como removida
                    int pointer = DATA_AREA_OFFSET + (CLUSTER_SIZE * entry.getParent()) + offset;
                    dataArea.writeEntryAt(pointer, ent);  // Atualiza o diretório pai
                    break;
                }
            }
        }

        buffer = dataArea.readEntry(entry.getStartBlock());  // Lê a entrada do arquivo
        offset = 0;

        for (int j = 0; j < buffer.length; j += ENTRY_SIZE) {
            if (offset == CLUSTER_SIZE) break;
            byte actualByte = buffer[offset];
            if (actualByte == FREE_AREA) {
                offset += ENTRY_SIZE;
            } else {
                byte[] e = new byte[ENTRY_SIZE];
                System.arraycopy(buffer, offset, e, 0, ENTRY_SIZE);
                Entry ent = Entry.toEntry(e);  // Converte a entrada
                if (!ent.getName().equals(entry.getName())) {
                    offset += ENTRY_SIZE;
                } else {
                    ent.setStatus((byte) 0);  // Marca a entrada como removida
                    int pointer = DATA_AREA_OFFSET + (CLUSTER_SIZE * entry.getStartBlock()) + offset;
                    dataArea.writeEntryAt(pointer, ent);  // Atualiza a área de dados
                    break;
                }
            }
        }
    }

    /**
     * Lê os dados de um arquivo a partir de seu bloco inicial.
     *
     * @param starterBlock o bloco inicial do arquivo
     * @return o conteúdo do arquivo
     * @throws IOException se ocorrer um erro ao ler o arquivo
     */
    public byte[] readArquive(int starterBlock) throws IOException {
        return dataArea.readEntry(starterBlock);  // Lê a entrada de um arquivo
    }

    /**
     * Lê o diretório de arquivos a partir de um cluster específico.
     *
     * @param clusterId o ID do cluster do diretório
     * @return os dados do diretório
     * @throws IOException se ocorrer um erro ao ler o diretório
     */
    public byte[] readDir(int clusterId) throws IOException {
        return dataArea.readEntry(clusterId);  // Lê a entrada do diretório
    }
}
