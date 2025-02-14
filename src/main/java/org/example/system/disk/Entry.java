package org.example.system.disk;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * A classe Entry representa uma entrada no sistema de arquivos, seja um arquivo ou diretório.
 * Armazena informações como nome, bloco de início, tamanho, tipo e status.
 */
public class Entry {
    String name; // Nome do arquivo ou diretório
    private int startBlock; // Bloco de início (4 bytes)
    private int parent; // Bloco do diretório pai (4 bytes)
    private int size; // Tamanho do arquivo ou diretório (4 bytes)
    private static final int NAME_SIZE = 255; // Máximo de 255 bytes para o nome (regra POSIX)
    private final byte type; // Tipo de entrada: 0 = diretório, 1 = arquivo
    private byte status; // Status da entrada: 0 = livre, 1 = ocupado
    private final int ENTRY_SIZE = 269; // Tamanho total da entrada (269 bytes)

    // Métodos de acesso (getters)
    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public int getStartBlock() {
        return startBlock;
    }

    public int getParent() {
        return parent;
    }

    public byte getStatus() {
        return status;
    }

    public byte getType() {
        return type;
    }

    // Métodos de modificação (setters)
    public void setStartBlock(int startBlock) {
        this.startBlock = startBlock;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Construtor para criar uma entrada de arquivo ou diretório com nome, tipo, tamanho, status e diretório pai.
     *
     * @param name Nome da entrada.
     * @param type Tipo da entrada: 0 para diretório, 1 para arquivo.
     * @param size Tamanho da entrada.
     * @param status Status da entrada: 0 = livre, 1 = ocupado.
     * @param parent Diretório pai da entrada.
     * @throws IllegalArgumentException Se o nome exceder o tamanho máximo permitido (255 bytes).
     */
    public Entry(String name, byte type, int size, byte status, int parent) {
        if (name.getBytes().length > NAME_SIZE) {
            throw new IllegalArgumentException("too long");
        }
        this.name = name;
        this.size = size;
        this.type = type;
        this.status = status;
        this.parent = parent;
    }

    /**
     * Construtor para criar uma entrada de arquivo ou diretório a partir de dados fornecidos.
     *
     * @param name Nome da entrada.
     * @param startBlock Bloco de início.
     * @param parent Bloco do diretório pai.
     * @param size Tamanho da entrada.
     * @param type Tipo da entrada: 0 para diretório, 1 para arquivo.
     * @param status Status da entrada: 0 = livre, 1 = ocupado.
     */
    public Entry(String name, int startBlock, int parent, int size, byte type, byte status) {
        if (name.getBytes().length > NAME_SIZE) {
            throw new IllegalArgumentException("too long");
        }
        this.name = name;
        this.startBlock = startBlock;
        this.parent = parent;
        this.size = size;
        this.type = type;
        this.status = status;
    }

    /**
     * Converte um array de bytes para uma instância de Entry.
     *
     * @param bytes O array de bytes representando os dados da entrada.
     * @return A instância de Entry gerada a partir dos bytes.
     */
    public static Entry toEntry(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        byte[] nameBytes = new byte[255];
        buffer.get(nameBytes);

        String name = new String(nameBytes).trim();

        int starterBlock = buffer.getInt();
        int parent = buffer.getInt();
        byte type = buffer.get();
        byte status = buffer.get();
        int size = buffer.getInt();

        return new Entry(name, starterBlock, parent, size, type, status);
    }

    /**
     * Converte a instância de Entry para um array de bytes.
     *
     * @return O array de bytes representando a entrada.
     */
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(ENTRY_SIZE);
        StringBuilder str = new StringBuilder();

        // Preenche o nome com espaços se for menor que o tamanho máximo permitido
        if (name.getBytes().length < NAME_SIZE) {
            str.append(name);
            for (int i = name.getBytes().length; i < NAME_SIZE; i++) {
                str.append(' ');  // Preenchimento com espaço (padrão FAT)
            }
        }
        buffer.put(str.toString().getBytes(StandardCharsets.UTF_8));  // Adiciona o nome à buffer
        buffer.putInt(startBlock);  // Adiciona o bloco de início
        buffer.putInt(parent);  // Adiciona o diretório pai
        buffer.put(type);  // Adiciona o tipo (diretório ou arquivo)
        buffer.put(status);  // Adiciona o status
        buffer.putInt(size);  // Adiciona o tamanho

        return buffer.array();  // Retorna o array de bytes resultante
    }

    /**
     * Retorna uma representação em string da instância de Entry.
     *
     * @return A string com os dados da entrada.
     */
    @Override
    public String toString() {
        return "Entry{" +
                "name='" + name + '\'' +
                ", startBlock=" + startBlock +
                ", parent=" + parent +
                ", size=" + size +
                ", type=" + type +
                ", status=" + status +
                ", ENTRY_SIZE=" + ENTRY_SIZE +
                '}';
    }
}
