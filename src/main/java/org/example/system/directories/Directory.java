package org.example.system.directories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.system.arquives.Arquive;

/**
 * Representa um diretório no sistema de arquivos.
 * Um diretório pode conter arquivos e subdiretórios.
 */
public class Directory {

    /** Nome do diretório. */
    String name;

    /** Lista de arquivos armazenados no diretório. */
    List<Arquive> data;

    /** Map de subdiretórios (filhos) do diretório. */
    Map<String, Directory> childrens;

    /** Diretório pai do diretório atual. */
    Directory parent;

    /** Status do diretório. Pode indicar se o diretório está ocupado ou livre, por exemplo. */
    byte status;

    /** Bloco inicial onde o diretório está localizado. */
    int staterBlock;

    /** Tamanho do diretório, representado pela quantidade de arquivos e subdiretórios. */
    int size = 0;

    /**
     * Constrói um diretório com nome, diretório pai, status e bloco inicial.
     *
     * @param name Nome do diretório.
     * @param parent Diretório pai do diretório atual.
     * @param status Status do diretório (ocupado, livre, etc.).
     * @param staterBlock Bloco inicial do diretório.
     */
    public Directory(String name, Directory parent, byte status, int staterBlock) {
        this.name = name;
        this.childrens = new HashMap<>();
        this.data = new ArrayList<>();
        this.status = status;
        this.parent = parent;
        this.staterBlock = staterBlock;
    }

    /**
     * Constrói um diretório com nome, diretório pai e status.
     * O bloco inicial será determinado mais tarde.
     *
     * @param name Nome do diretório.
     * @param parent Diretório pai do diretório atual.
     * @param status Status do diretório (ocupado, livre, etc.).
     */
    public Directory(String name, Directory parent, byte status) {
        this.name = name;
        this.parent = parent;
        this.status = status;
        this.childrens = new HashMap<>();
        this.data = new ArrayList<>();
    }

    /**
     * Retorna o número de arquivos e subdiretórios dentro do diretório.
     *
     * @return O tamanho do diretório, que é a quantidade de arquivos e subdiretórios.
     */
    public int getSize() {
        return size;
    }

    /**
     * Retorna o bloco inicial onde o diretório está localizado.
     *
     * @return O bloco inicial do diretório.
     */
    public int getStaterBlock() {
        return staterBlock;
    }

    /**
     * Retorna o status do diretório (por exemplo, se está ocupado ou livre).
     *
     * @return O status do diretório.
     */
    public byte getStatus() {
        return status;
    }

    /**
     * Adiciona um arquivo ao diretório.
     *
     * @param arquive O arquivo a ser adicionado.
     */
    public void addData(Arquive arquive) {
        size++; // Incrementa o tamanho do diretório
        data.add(arquive); // Adiciona o arquivo à lista de dados
    }

    /**
     * Remove um arquivo do diretório.
     *
     * @param arquive O arquivo a ser removido.
     */
    public void removeData(Arquive arquive) {
        size--; // Decrementa o tamanho do diretório
        data.remove(arquive); // Remove o arquivo da lista de dados
    }

    /**
     * Adiciona um subdiretório ao diretório.
     *
     * @param name O nome do subdiretório.
     * @param subdirectory O subdiretório a ser adicionado.
     */
    public void addSubdirectory(String name, Directory subdirectory) {
        size++; // Incrementa o tamanho do diretório
        childrens.put(name, subdirectory); // Adiciona o subdiretório ao mapa de subdiretórios
    }

    /**
     * Remove um subdiretório do diretório.
     *
     * @param subdirectory O subdiretório a ser removido.
     */
    public void removeSubdirectory(Directory subdirectory) {
        size--; // Decrementa o tamanho do diretório
        childrens.remove(subdirectory.name); // Remove o subdiretório do mapa de subdiretórios
    }

    /**
     * Retorna o nome do diretório.
     *
     * @return O nome do diretório.
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna a lista de arquivos armazenados no diretório.
     *
     * @return A lista de arquivos do diretório.
     */
    public List<Arquive> getData() {
        return data;
    }

    /**
     * Retorna o diretório pai do diretório atual.
     *
     * @return O diretório pai.
     */
    public Directory getParent() {
        return parent;
    }

    /**
     * Representa o diretório como uma string.
     * Inclui informações sobre o nome, arquivos, subdiretórios e status.
     *
     * @return A representação em string do diretório.
     */
    @Override
    public String toString() {
        if (parent != null) { // Se o diretório tem um diretório pai, exibe o nome do pai
            return "Directory{" +
                    "name='" + name + '\'' +
                    ", data=" + data +
                    ", childrens=" + childrens +
                    ", parent=" + parent.getName() +
                    ", status=" + status +
                    ", starter_block=" + staterBlock +
                    '}';
        }
        // Caso contrário, exibe o diretório sem o diretório pai
        return "Directory{" +
                "name='" + name + '\'' +
                ", data=" + data +
                ", childrens=" + childrens +
                ", status=" + status +
                ", starter_block=" + staterBlock +
                '}';
    }

    /**
     * Retorna o caminho completo do diretório a partir da raiz.
     * O caminho é construído adicionando os diretórios pais.
     *
     * @return O caminho completo do diretório.
     */
    public String getPath() {
        StringBuilder path = new StringBuilder();
        Directory d = this;

        // Subindo na árvore de diretórios até a raiz
        while (true) {
            if (d == null) break; // Se não houver mais diretórios pais, para de subir
            path.insert(0, d.getName().equals("/") ? "" : "/" + d.getName()); // Adiciona o nome do diretório ao caminho
            d = d.getParent(); // Vai para o diretório pai
        }

        return path.toString();
    }
}
