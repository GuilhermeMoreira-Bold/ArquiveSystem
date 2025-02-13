package org.example.system;
import org.example.system.arquives.Arquive;
import org.example.system.directories.Directory;
import org.example.system.disk.Entry;
import org.example.system.disk.VirtualDisk;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.Map;

import static org.example.system.disk.DiskUtils.*;

public class FileSystem {
    Directory root;
    Directory current;
    VirtualDisk disk;
    private final boolean MAINTAIN_DATA = true;
    public Directory getCurrent() {
        return current;
    }

    public void setCurrent(Directory current) {
        this.current = current;
    }

    public FileSystem() throws IOException {
        disk = new VirtualDisk(MAINTAIN_DATA, DISK_NAME);
        initialize();
    }

    private void initialize() throws IOException {
      byte[] buffer =  disk.readDir(0);
      Entry rootEntry = Entry.toEntry(buffer);
      root = new Directory(rootEntry.getName(), null, rootEntry.getStatus(),rootEntry.getStartBlock());
      current = root;
      if (MAINTAIN_DATA) {
          addDataToDir(0,root);
          loadRootDirs();
      }
    }

    public VirtualDisk getDisk() {
        return disk;
    }


    private void loadRootDirs() throws IOException {
        for (Map.Entry<String, Directory> dir :
                root.getChildrens().entrySet()){
            addDataToDir(dir.getValue().getStaterBlock(),dir.getValue());
        }
    }
    private void addDataToDir(int index, Directory dir) throws IOException {
        // Lê os bytes do disco relativos a este diretório
        byte[] buffer = disk.readDir(index);

        // Percorre o buffer, lendo as entradas
        int offset = ENTRY_SIZE; // supondo que haja algum cabeçalho de ENTRY_SIZE no início
        while (offset < buffer.length) {
            if (offset == 4096) break;  // limite do bloco, por exemplo

            byte actualByte = buffer[offset];
            if (actualByte == FREE_AREA) {
                offset++;
                continue;
            }

            // Copia os bytes que compõem a entry
            byte[] e = new byte[ENTRY_SIZE];
            System.arraycopy(buffer, offset, e, 0, ENTRY_SIZE);
            Entry entry = Entry.toEntry(e);

            // Verifica o status para ignorar entradas inválidas
            if (entry.getStatus() == 0) {
                offset += ENTRY_SIZE;
                continue;
            }

            // Verifica se é diretório ou arquivo
            if (entry.getType() == 0) {
                // É um diretório
                Directory subDir = new Directory(
                        entry.getName(),
                        dir,
                        entry.getStatus(),
                        entry.getStartBlock()
                );

                // Adiciona o subdiretório ao diretório atual
                dir.addSubdirectory(entry.getName(), subDir);

                // Chama recursivamente para carregar o conteúdo do subdiretório
                addDataToDir(subDir.getStaterBlock(), subDir);

            } else {
                // É um arquivo
                String data = new String(disk.readEntryData(entry).getBytes());
                int freeAreaPos = data.indexOf(FREE_AREA);
                if (freeAreaPos != -1) {
                    // Se encontrar FREE_AREA no meio, trunca a string
                    data = data.substring(0, freeAreaPos);
                }

                Arquive arquivo = new Arquive(entry.getName(),
                        data,
                        entry.getSize(),
                        entry.getStartBlock());

                // Adiciona o arquivo ao diretório atual
                dir.addData(arquivo);
            }

            offset += ENTRY_SIZE;
        }
    }

    public DefaultMutableTreeNode getFileSystemTree() {
        return buildTree(root);
    }

    private FileSystemTreeNode buildTree(Directory directory) {
        FileSystemTreeNode node = new FileSystemTreeNode(directory.getName(), true);

        for (Directory subDir : directory.getChildrens().values()) {
            node.add(buildTree(subDir));
        }

        for (Arquive file : directory.getData()) {
            node.add(new FileSystemTreeNode(file.getName(), false));
        }

        return node;
    }

    public static class FileSystemTreeNode extends DefaultMutableTreeNode {
        private final boolean isDirectory;

        public FileSystemTreeNode(String name, boolean isDirectory) {
            super(name);
            this.isDirectory = isDirectory;
        }

        public boolean isDirectory() {
            return isDirectory;
        }
    }

    public String debugDataArea(int index) throws IOException {
         return new String(disk.readDir(index));
//
//        Entry en = Entry.toEntry(disk.readDir(index));
//        System.out.println(en);
    }

    @Override
    public String toString() {
        return "FileSystem{" +
                "root=" + root +
//                ", current=" + current +
                '}';
    }

    public Directory getRoot() {
        return root;
    }

    public boolean isInRoot() {
        return current == root;
    }
}
