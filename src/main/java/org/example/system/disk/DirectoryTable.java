package org.example.system.disk;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DirectoryTable {
    private final int ENTRY_SIZE = 265; // tamanho dos arquivos em bytes
    private final int TABLE_SIZE = 1024; // tamanho da tabela
    Entry parentDir;

    public int getTABLE_SIZE() {

        return TABLE_SIZE;
    }

    List<Entry> entries;

    public DirectoryTable(Entry parentDir) {
        entries = new ArrayList<>();
        this.parentDir = parentDir;
    }

    public void add(Entry entryFile) {
        entries.add(entryFile);
    }
    public void remove(Entry entryFile) {
        if(entryFile.getStatus() == 1){
            System.out.println("Directory not empty");
            return;
        }
        entries.remove(entryFile);
    }
    public Entry getByName(String name) {
        for (Entry entry : entries) {
            if(entry.name.equals(name)) {
                return entry;
            }
        }
        return null;
    }
    public void print() {
        if (entries.isEmpty()) {
            System.out.println("Tabela de diretórios vazia.");
        } else {
            for (Entry entry : entries) {
                System.out.println(entry);
            }
        }
    }

    public void deserialize(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        entries.clear();
        while (buffer.remaining() >= ENTRY_SIZE) {
            byte[] entryBytes = new byte[ENTRY_SIZE];
            buffer.get(entryBytes);
            Entry entry = Entry.fromBytes(entryBytes);
            if(!entry.name.trim().isEmpty()) {
                entries.add(entry);
            }
        }
    }
}
