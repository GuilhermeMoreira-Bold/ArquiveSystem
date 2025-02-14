package org.example.system.disk;

/**
 * A classe DiskUtils contém constantes e variáveis utilizadas para configurar o sistema de arquivos.
 * As constantes são usadas para definir parâmetros como o nome do disco, tamanho, clusters, e outros detalhes de estrutura do disco.
 */
public class DiskUtils {
    // Nome do arquivo do disco virtual
    public static String DISK_NAME = "test.bin";

    // Tamanho total do disco (10MB)
    public static final int DISK_SIZE = 10 * 1024 * 1024;

    // Tamanho de um cluster (4KB)
    public static final int CLUSTER_SIZE = 4 * 1024;

    // Total de blocos no disco
    public static final int TOTAL_BLOCKS = DISK_SIZE / CLUSTER_SIZE;

    // Tamanho da tabela de alocação de arquivos (FAT)
    public static final int FAT_SIZE = TOTAL_BLOCKS;

    // Deslocamento inicial da FAT
    public static final int FAT_INITIAL_OFFSET = 12;

    // Deslocamento inicial da área de dados
    public static final int DATA_AREA_OFFSET = (FAT_INITIAL_OFFSET + FAT_SIZE) * 4;

    // Deslocamento da tabela do diretório raiz
    public static final int ROOT_DIRECTORY_TABLE_OFFSET = DATA_AREA_OFFSET;

    // Tamanho de uma entrada no diretório
    public static final int ENTRY_SIZE = 269;

    // Valor indicando uma área livre
    public static final byte FREE_AREA = (byte) 0XFF;

    // Tamanho disponível para dados de arquivos em um cluster (excluindo o espaço da entrada)
    public static final int CLUSTER_DATA_AREA_SIZE = CLUSTER_SIZE - ENTRY_SIZE;

    // Flag para arquivos
    public static final byte BIT_ARQUIVE = 1;

    // Flag para diretórios
    public static final byte BIT_DIRECTORY = 0;

    // Flag para indicar que a entrada está preenchida
    public static final byte BIT_FILLED = 1;

    // Flag para indicar que a entrada está livre
    public static final byte BIT_FREE = 0;
}
