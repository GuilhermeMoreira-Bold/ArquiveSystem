package org.example.system.disk;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Entry {
    String name;
    private int startBlock; //4 bytes
    private int parent; // 4 bytes
    private int size; // 4 bytes
    private static final int NAME_SIZE = 255; // MAX 255 bytes POSIX rule
    private final byte type; // 0 = diretório, 1 = arquivo
    private byte status; // 0 = livre, 1 = ocupado
    private final int ENTRY_SIZE = 269;

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
    public void setStartBlock(int startBlock) {
        this.startBlock = startBlock;
    }

    public Entry(String name, byte type, int size, byte status, int parent) {
        if(name.getBytes().length > NAME_SIZE) {
            throw new IllegalArgumentException("too long");
        }
        this.name = name;
        this.size = size;
        this.type = type;
        this.status = status;
        this.startBlock = 200; // atualizar depois somente para teste
        this.parent = parent;
    }
    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public static Entry toEntry(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        byte[] nameBytes = new byte[255];
        buffer.get(nameBytes);

        String name = new String(nameBytes).trim();

        int startBlock  = buffer.getInt();
        int parent = buffer.getInt();
        int size = buffer.getInt();
        byte type = buffer.get();
        byte status = buffer.get();
        return new Entry(name, type, size, status, parent);
    }
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(ENTRY_SIZE);
        StringBuilder str = new StringBuilder();
        if(name.getBytes().length < NAME_SIZE) {
            str.append(name);
            for(int i = name.getBytes().length; i < NAME_SIZE; i++) {
                str.append(' '); // padrão FAT para preencher nomes
            }
        }
        buffer.put(str.toString().getBytes(StandardCharsets.UTF_8));
        buffer.putInt(startBlock);
        buffer.putInt(parent);
        buffer.put(type);
        buffer.put(status);
        buffer.putInt(size);

        return buffer.array();
    }

    public byte getType() {
        return type;
    }

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
