package org.example.system;
import org.example.system.arquives.Arquive;
import org.example.system.directories.Directory;
import org.example.system.disk.Entry;
import org.example.system.disk.VirtualDisk;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.Map;

import static org.example.system.disk.DiskUtils.*;
/**
 * A classe `FileSystem` representa o sistema de arquivos virtual, manipulando a estrutura de diretórios e arquivos,
 * e interagindo com o disco virtual (`VirtualDisk`). Ela fornece métodos para navegação no sistema de arquivos, criação
 * de arquivos e diretórios, e exibição do sistema de arquivos em forma de árvore.
 */
public class FileSystem {
    Directory root;
    Directory current;
    VirtualDisk disk;
    private final boolean MAINTAIN_DATA = true;

    /**
     * Retorna o diretório atual.
     *
     * @return o diretório atual
     */
    public Directory getCurrent() {
        return current;
    }

    /**
     * Define o diretório atual.
     *
     * @param current o diretório a ser definido como o atual
     */
    public void setCurrent(Directory current) {
        this.current = current;
    }

    /**
     * Constrói uma nova instância de `FileSystem` e inicializa o disco virtual, configurando o diretório raiz e o diretório atual.
     *
     * @throws IOException se ocorrer um erro ao acessar o disco virtual
     */
    public FileSystem() throws IOException {
        disk = new VirtualDisk(MAINTAIN_DATA, DISK_NAME);
        initialize();
    }
    /**
     * Inicializa o sistema de arquivos a partir do disco virtual, lendo o diretório raiz e configurando o estado inicial.
     *
     * @throws IOException se ocorrer um erro ao ler os dados do disco
     */
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
    /**
     * Retorna o disco virtual associado ao sistema de arquivos.
     *
     * @return o disco virtual
     */
    public VirtualDisk getDisk() {
        return disk;
    }

    /**
     * Carrega os subdiretórios do diretório raiz.
     *
     * @throws IOException se ocorrer um erro ao ler os dados do disco
     */
    private void loadRootDirs() throws IOException {
        for (Map.Entry<String, Directory> dir :
                root.getChildrens().entrySet()){
            addDataToDir(dir.getValue().getStaterBlock(),dir.getValue());
        }
    }
    /**
     * Adiciona dados a um diretório, lendo as entradas do disco virtual.
     *
     * @param index o índice do diretório no disco
     * @param dir o diretório a ser atualizado
     * @throws IOException se ocorrer um erro ao ler os dados do disco
     */
    private void addDataToDir(int index, Directory dir) throws IOException {
        byte[] buffer = disk.readDir(index);

        int offset = ENTRY_SIZE;
        while (offset < buffer.length) {
            if (offset == 4096) break;

            byte actualByte = buffer[offset];
            if (actualByte == FREE_AREA) {
                offset+= ENTRY_SIZE;
                continue;
            }

            byte[] e = new byte[ENTRY_SIZE];
            System.arraycopy(buffer, offset, e, 0, ENTRY_SIZE);
            Entry entry = Entry.toEntry(e);

            if (entry.getStatus() == 0) {
                offset += ENTRY_SIZE;
                continue;
            }

            if (entry.getType() == 0) {
                Directory subDir = new Directory(
                        entry.getName(),
                        dir,
                        entry.getStatus(),
                        entry.getStartBlock()
                );

                dir.addSubdirectory(entry.getName(), subDir);

                addDataToDir(subDir.getStaterBlock(), subDir);

            } else {
                String data = new String(disk.readEntryData(entry).getBytes());
                int freeAreaPos = data.indexOf(FREE_AREA);
                if (freeAreaPos != -1) {
                    data = data.substring(0, freeAreaPos);
                }

                Arquive arquivo = new Arquive(entry.getName(),
                        data,
                        entry.getSize(),
                        entry.getStartBlock());
                if(dir.getData().isEmpty()){
                    dir.addData(arquivo);
                }

                boolean exists = false;

                for (Arquive a : dir.getData()) {
                    if (a.getName().equals(arquivo.getName())) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    dir.addData(arquivo);
                }
            }

            offset += ENTRY_SIZE;
        }
    }
    /**
     * Retorna uma representação do sistema de arquivos em formato de árvore.
     *
     * @return uma árvore representando o sistema de arquivos
     */

    public DefaultMutableTreeNode getFileSystemTree() {
        return buildTree(root);
    }
    /**
     * Constrói recursivamente a árvore de diretórios e arquivos a partir do diretório fornecido.
     *
     * @param directory o diretório a partir do qual construir a árvore
     * @return o nó raiz da árvore
     */

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

    /**
     * Classe interna que representa um nó na árvore de sistema de arquivos.
     * Indica se o nó é um diretório ou não.
     */

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
