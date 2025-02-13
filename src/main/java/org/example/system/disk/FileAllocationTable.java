package org.example.system.disk;

import org.example.system.disk.handlers.FatHandler;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * A classe `FileAllocationTable` (FAT) gerencia a alocação e desalocação de clusters para arquivos no disco virtual.
 * Ela mantém uma tabela que aponta para os clusters de cada arquivo e oferece métodos para manipular essa tabela.
 */
public class FileAllocationTable {

    int[] fileClusters;  // Tabela de clusters alocados aos arquivos

    private final int FREE = -1;  // Valor que representa um bloco livre
    private final int EOF = -2;  // Valor que representa o final de um arquivo (End of File)

    private final FatHandler IOHandler;  // Manipulador de entrada/saída para a FAT

    /**
     * Constrói uma instância da tabela de alocação de arquivos (FAT).
     * Inicializa a tabela a partir de um arquivo e determina o estado do disco.
     *
     * @param raf o arquivo de acesso aleatório usado para ler e gravar dados
     * @param exists indica se o disco já existe
     * @throws IOException se ocorrer um erro ao acessar o arquivo
     */
    public FileAllocationTable(RandomAccessFile raf, boolean exists) throws IOException {
        this.IOHandler = new FatHandler(raf);  // Cria o manipulador de FAT
        fileClusters = IOHandler.initialize(exists);  // Inicializa a tabela de clusters
    }

    /**
     * Encontra os clusters alocados para um arquivo específico.
     *
     * @param entryFile a entrada do arquivo
     * @return uma lista de índices de clusters alocados ao arquivo
     */
    public List<Integer> findFileClusters(Entry entryFile) {
        List<Integer> clusters = new ArrayList<>();
        int nextCluster = entryFile.getStartBlock();
        while (nextCluster != EOF && nextCluster < fileClusters.length) {
            clusters.add(nextCluster);
            nextCluster = fileClusters[nextCluster];  // Vai para o próximo cluster
        }
        if (nextCluster == EOF) {
            clusters.add(nextCluster);  // Adiciona o EOF à lista de clusters
        }
        return clusters;
    }

    /**
     * Realoca o tamanho de um arquivo, adicionando ou removendo clusters conforme necessário.
     *
     * @param entryFile a entrada do arquivo a ser realocada
     * @throws IOException se ocorrer um erro ao realocar os clusters
     */
    public void reallocateFileSize(Entry entryFile) throws IOException {
        List<Integer> clusters = findFileClusters(entryFile);
        if (entryFile.getSize() == 0) return;  // Se o tamanho for zero, não faz nada

        // Se o arquivo precisa de mais clusters
        if (entryFile.getSize() + 1 > clusters.size()) {
            int clusterIndex = clusters.get(clusters.size() - 2);
            for (int i = 0; i < entryFile.getSize() - clusters.size() + 1; i++) {
                fileClusters[clusterIndex] = findFreeBlock();  // Aloca um novo bloco
                clusterIndex = fileClusters[clusterIndex];
                clusters.add(clusters.size() - 1, clusterIndex);
                System.out.println("Free block: " + clusterIndex);
            }
            fileClusters[clusterIndex] = EOF;  // Marca o final do arquivo
            writeAllDataToDisk();  // Salva a FAT no disco

            // Se o arquivo precisa de menos clusters
        } else if (entryFile.getSize() + 1 < clusters.size()) {
            int clusterIndex = clusters.get(entryFile.getSize());
            fileClusters[clusters.get(entryFile.getSize() - 1)] = EOF;  // Marca o novo final do arquivo

            for (int i = entryFile.getSize(); i < clusters.size() - entryFile.getSize() + 1; i++) {
                int tempClusterIndex = clusterIndex;
                clusterIndex = fileClusters[tempClusterIndex];

                if (clusterIndex == EOF) {
                    fileClusters[tempClusterIndex] = FREE;  // Libera os clusters não utilizados
                    break;
                }

                fileClusters[tempClusterIndex] = FREE;  // Libera o cluster
            }
            writeAllDataToDisk();  // Salva a FAT no disco
        }

        System.out.println("File clusters: " + Arrays.toString(fileClusters));
    }

    /**
     * Adiciona clusters a um arquivo, alocando os blocos necessários para armazenar seus dados.
     *
     * @param entryFile a entrada do arquivo a ser alocado
     * @return o índice do primeiro cluster alocado ao arquivo
     * @throws IOException se ocorrer um erro ao adicionar os clusters
     */
    public int addFileCluster(Entry entryFile) throws IOException {
        ArrayList<Integer> clusters = new ArrayList<>();

        // Aloca os clusters necessários
        for (int i = 0; i < entryFile.getSize(); i++) {
            int clusterIndex = findFreeBlock();  // Encontra um bloco livre
            clusters.add(clusterIndex);
            fileClusters[clusterIndex] = clusterIndex;  // Marca o bloco como usado temporariamente

            if (i > 0) {
                fileClusters[clusters.get(i - 1)] = clusterIndex;  // Aloca o cluster anterior
            }
        }

        fileClusters[clusters.get(clusters.size() - 1)] = EOF;  // Marca o final do arquivo
        writeAllDataToDisk();  // Salva a FAT no disco

        return clusters.get(0);  // Retorna o primeiro cluster alocado
    }

    /**
     * Encontra o próximo bloco livre na tabela de alocação de arquivos.
     *
     * @return o índice do bloco livre
     * @throws IllegalStateException se não houver blocos livres disponíveis
     */
    public int findFreeBlock() {
        for (int i = 0; i < fileClusters.length; i++) {
            if (fileClusters[i] == FREE) {
                return i;  // Retorna o índice do bloco livre
            }
        }
        throw new IllegalStateException("No free block found");  // Se não encontrar nenhum bloco livre
    }

    /**
     * Remove os clusters de um arquivo da tabela de alocação, liberando os blocos usados.
     *
     * @param entryFile a entrada do arquivo a ser removido
     * @throws IOException se ocorrer um erro ao remover os clusters
     */
    public void removeFileCluster(Entry entryFile) throws IOException {
        int current = entryFile.getStartBlock();
        while (current != EOF) {
            int next = fileClusters[current];
            fileClusters[current] = FREE;  // Libera o bloco atual
            current = next;  // Move para o próximo bloco
        }
        writeAllDataToDisk();  // Salva as alterações no disco
    }

    /**
     * Escreve todos os dados da tabela de alocação de arquivos no disco.
     *
     * @throws IOException se ocorrer um erro ao escrever os dados no disco
     */
    public void writeAllDataToDisk() throws IOException {
        IOHandler.write(fileClusters);  // Escreve os dados da FAT no disco
    }

    /**
     * Exibe todos os blocos da tabela de alocação de arquivos.
     */
    public void printAllBlocks() {
        System.out.println("File clusters: " + Arrays.toString(fileClusters));  // Exibe a tabela de clusters
    }
}
