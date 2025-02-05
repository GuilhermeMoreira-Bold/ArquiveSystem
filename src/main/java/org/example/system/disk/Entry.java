package org.example.system.disk;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Entry {
    String name;
    private int startBlock; //4 bytes
    private int size; // 4 bytes
    private static final int NAME_SIZE = 255; // MAX 255 bytes POSIX rule
    private final byte type; // 0 = diretório, 1 = arquivo
    private byte status; // 0 = livre, 1 = ocupado

    public int getSize() {
        return size;
    }

    public int getStartBlock() {
        return startBlock;
    }

    public void setStartBlock(int startBlock) {
        this.startBlock = startBlock;
    }

    public Entry(String name, byte type, int size, byte status) {
        if(name.getBytes().length > NAME_SIZE) {
            throw new IllegalArgumentException("name too long");
        }
        this.name = name;
        this.size = size;
        this.type = type;
        this.status = status;
        this.startBlock = 1; // atualizar depois somente para teste
    }


    public static Entry fromBytes(byte[] data){
        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte[] nameBytes = new byte[NAME_SIZE];
        buffer.get(nameBytes);
        String name = new String(nameBytes, StandardCharsets.UTF_8);
        byte type = buffer.get();
        int startBlock = buffer.getInt();
        int size = buffer.getInt();
        byte status = buffer.get();
        return new Entry(name,type,size,status);
    }
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        if(name.getBytes().length < NAME_SIZE) {
            str.append(name);
            for(int i = name.getBytes().length; i < NAME_SIZE; i++) {
                str.append("X");
            }
        }
        str.append(startBlock);
        str.append(size);
        str.append(type);
        str.append(status);

        return str.toString();
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }
}
