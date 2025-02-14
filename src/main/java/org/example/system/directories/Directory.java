package org.example.system.directories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.system.arquives.Arquive;

/**
 * Representa um diretório dentro de um sistema de arquivos.
 * Um diretório pode conter arquivos e subdiretórios,
 * e possui informações sobre seu estado e bloco inicial.
 */
public class Directory {
    String name;
    List<Arquive> data;
    Map<String, Directory> childrens;
    Directory parent;
    byte status;
    int staterBlock;
    int size = 0;

    /**
     * Define o bloco inicial do diretório.
     *
     * @param staterBlock o bloco inicial do diretório.
     */
    public void setStaterBlock(int staterBlock) {
        this.staterBlock = staterBlock;
    }

    /**
     * Retorna o mapa de subdiretórios (filhos) do diretório.
     *
     * @return mapa contendo os subdiretórios do diretório.
     */
    public Map<String, Directory> getChildrens() {
        return childrens;
    }

    /**
     * Construtor que cria um diretório com o nome, diretório pai, status e bloco inicial.
     *
     * @param name o nome do diretório.
     * @param parent o diretório pai.
     * @param status o status do diretório.
     * @param staterBlock o bloco inicial do diretório.
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
     * Construtor que cria um diretório com o nome, diretório pai e status.
     * O bloco inicial será atribuído automaticamente.
     *
     * @param name o nome do diretório.
     * @param parent o diretório pai.
     * @param status o status do diretório.
     */
    public Directory(String name, Directory parent, byte status) {
        this.name = name;
        this.parent = parent;
        this.status = status;
        this.childrens = new HashMap<>();
        this.data = new ArrayList<>();
    }

    /**
     * Retorna o número total de arquivos e subdiretórios contidos no diretório.
     *
     * @return o tamanho do diretório.
     */
    public int getSize() {
        return size;
    }

    /**
     * Retorna o bloco inicial do diretório.
     *
     * @return o bloco inicial do diretório.
     */
    public int getStaterBlock() {
        return staterBlock;
    }

    /**
     * Retorna o status do diretório.
     *
     * @return o status do diretório.
     */
    public byte getStatus() {
        return status;
    }

    /**
     * Adiciona um arquivo ao diretório e aumenta o tamanho do diretório.
     *
     * @param arquive o arquivo a ser adicionado.
     */
    public void addData(Arquive arquive) {
        size++;
        data.add(arquive);
    }

    /**
     * Remove um arquivo do diretório e diminui o tamanho do diretório.
     *
     * @param arquive o arquivo a ser removido.
     */
    public void removeData(Arquive arquive) {
        size--;
        data.remove(arquive);
    }

    /**
     * Adiciona um subdiretório ao diretório e aumenta o tamanho do diretório.
     *
     * @param name o nome do subdiretório.
     * @param subdirectory o subdiretório a ser adicionado.
     */
    public void addSubdirectory(String name, Directory subdirectory) {
        size++;
        childrens.put(name, subdirectory);
    }

    /**
     * Remove um subdiretório do diretório e diminui o tamanho do diretório.
     *
     * @param subdirectory o subdiretório a ser removido.
     */
    public void removeSubdirectory(Directory subdirectory) {
        size--;
        childrens.remove(subdirectory.name);
    }

    /**
     * Retorna o nome do diretório.
     *
     * @return o nome do diretório.
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna a lista de arquivos contidos no diretório.
     *
     * @return a lista de arquivos do diretório.
     */
    public List<Arquive> getData() {
        return data;
    }

    /**
     * Retorna o diretório pai do diretório atual.
     *
     * @return o diretório pai.
     */
    public Directory getParent() {
        return parent;
    }

    /**
     * Retorna uma representação em string do diretório, incluindo seus dados, filhos e outras propriedades.
     *
     * @return a representação em string do diretório.
     */
    @Override
    public String toString() {
        if (parent != null) {
            return "Directory{" +
                    "name='" + name + '\'' +
                    ", data=" + data +
                    ", childrens=" + childrens +
                    ", parent=" + parent.getName() +
                    ", status=" + status +
                    ", starter_block=" + staterBlock +
                    '}';
        }
        return "Directory{" +
                "name='" + name + '\'' +
                ", data=" + data +
                ", childrens=" + childrens +
                ", status=" + status +
                ", starter_block=" + staterBlock +
                '}';
    }

    /**
     * Retorna o caminho completo do diretório, começando da raiz.
     *
     * @return o caminho completo do diretório.
     */
    public String getPath() {
        StringBuilder path = new StringBuilder();
        Directory d = this;

        while(true){
            if(d == null) break;
            path.insert(0, d.getName().equals("/") ? "" : "/" + d.getName());
            d = d.getParent();
        }

        return path.toString();
    }
}
