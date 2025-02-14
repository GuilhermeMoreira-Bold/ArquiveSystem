package org.example.system.arquives;

/**
 * Representa um arquivo no sistema de arquivos.
 * Um arquivo contém um nome, dados e informações sobre seu tamanho e localização no disco.
 */
public class Arquive {

    /** Nome do arquivo. */
    String name;

    /** Dados armazenados no arquivo. */
    String data;

    /** Tamanho do arquivo em blocos. */
    int blocksSize;

    /** Bloco inicial onde o arquivo está localizado. */
    int staterBlock;

    /**
     * Retorna o nome do arquivo.
     *
     * @return O nome do arquivo.
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna os dados armazenados no arquivo.
     *
     * @return Os dados do arquivo.
     */
    public String getData() {
        return data;
    }

    /**
     * Define os dados a serem armazenados no arquivo.
     *
     * @param data Os dados a serem definidos para o arquivo.
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Retorna o tamanho do arquivo em blocos.
     *
     * @return O tamanho do arquivo em blocos.
     */
    public int getSize() {
        return blocksSize;
    }

    /**
     * Retorna o bloco inicial onde o arquivo está armazenado.
     *
     * @return O bloco inicial do arquivo.
     */
    public int getStaterBlock() {
        return staterBlock;
    }

    /**
     * Define o bloco inicial onde o arquivo deve ser armazenado.
     *
     * @param staterBlock O bloco inicial do arquivo.
     */
    public void setStaterBlock(int staterBlock) {
        this.staterBlock = staterBlock;
    }

    /**
     * Constrói um arquivo com nome, dados, tamanho e bloco inicial.
     *
     * @param name O nome do arquivo.
     * @param data Os dados do arquivo.
     * @param blocksSize O tamanho do arquivo em blocos.
     * @param staterBlock O bloco inicial onde o arquivo está armazenado.
     */
    public Arquive(String name, String data, int blocksSize, int staterBlock) {
        this.staterBlock = staterBlock;
        this.name = name;
        this.data = data;
        this.blocksSize = blocksSize;
    }
}
