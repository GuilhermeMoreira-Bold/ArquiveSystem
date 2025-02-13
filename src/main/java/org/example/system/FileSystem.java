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
    private Directory root;  // Diretório raiz do sistema de arquivos
    private Directory current;  // Diretório atual
    private VirtualDisk disk;  // Disco virtual associado ao sistema de arquivos
    private final boolean EXISTS = true;  // Flag para determinar se o sistema de arquivos existe

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
        disk = new VirtualDisk(EXISTS, DISK_NAME);  // Cria o disco virtual
        initialize();  // Inicializa o sistema de arquivos
    }

    /**
     * Inicializa o sistema de arquivos a partir do disco virtual, lendo o diretório raiz e configurando o estado inicial.
     *
     * @throws IOException se ocorrer um erro ao ler os dados do disco
     */
    private void initialize() throws IOException {
        byte[] buffer = disk.readDir(0);  // Lê os dados do diretório raiz
        Entry rootEntry = Entry.toEntry(buffer);  // Cria uma entrada a partir dos dados lidos
        root = new Directory(rootEntry.getName(), null, rootEntry.getStatus(), rootEntry.getStartBlock());  // Cria o diretório raiz
        current = root;  // Define o diretório atual como o raiz
        if (EXISTS) {
            addDataToDir(0, root);  // Adiciona dados ao diretório raiz
            loadRootDirs();  // Carrega os subdiretórios do diretório raiz
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
        for (Map.Entry<String, Directory> dir : root.getChildrens().entrySet()) {
            addDataToDir(dir.getValue().getStaterBlock(), dir.getValue());  // Adiciona dados para cada subdiretório
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
        byte[] buffer = disk.readDir(index);  // Lê os dados do diretório
        int offset = ENTRY_SIZE;  // Offset inicial

        for (int i = 0; i < buffer.length; i++) {
            if (offset == 4096) break;  // Limite de 4096 bytes
            byte actualByte = buffer[offset];
            if (actualByte == FREE_AREA) {
                offset++;
            } else {
                byte[] e = new byte[ENTRY_SIZE];
                System.arraycopy(buffer, offset, e, 0, ENTRY_SIZE);
                Entry entry = Entry.toEntry(e);  // Cria a entrada a partir dos dados

                if (entry.getStatus() == 0) {
                    offset += ENTRY_SIZE;
                    continue;
                }

                if (entry.getType() == 0) {
                    dir.addSubdirectory(entry.getName(), new Directory(entry.getName(), dir, entry.getStatus(), entry.getStartBlock()));  // Cria subdiretório
                } else {
                    dir.addData(new Arquive(entry.getName(), "", entry.getSize(), entry.getStartBlock()));  // Cria arquivo
                }
                offset += ENTRY_SIZE;
            }
        }
    }

    /**
     * Retorna uma representação do sistema de arquivos em formato de árvore.
     *
     * @return uma árvore representando o sistema de arquivos
     */
    public DefaultMutableTreeNode getFileSystemTree() {
        return buildTree(root);  // Constrói a árvore a partir do diretório raiz
    }

    /**
     * Constrói recursivamente a árvore de diretórios e arquivos a partir do diretório fornecido.
     *
     * @param directory o diretório a partir do qual construir a árvore
     * @return o nó raiz da árvore
     */
    private FileSystemTreeNode buildTree(Directory directory) {
        FileSystemTreeNode node = new FileSystemTreeNode(directory.getName(), true);  // Cria nó para diretório

        for (Directory subDir : directory.getChildrens().values()) {
            node.add(buildTree(subDir));  // Adiciona subdiretórios
        }

        for (Arquive file : directory.getData()) {
            node.add(new FileSystemTreeNode(file.getName(), false));  // Adiciona arquivos
        }

        return node;
    }

    /**
     * Classe interna que representa um nó na árvore de sistema de arquivos.
     * Indica se o nó é um diretório ou não.
     */
    public static class FileSystemTreeNode extends DefaultMutableTreeNode {
        private final boolean isDirectory;

        /**
         * Constrói um nó da árvore de sistema de arquivos.
         *
         * @param name o nome do nó
         * @param isDirectory indica se o nó é um diretório
         */
        public FileSystemTreeNode(String name, boolean isDirectory) {
            super(name);
            this.isDirectory = isDirectory;
        }

        /**
         * Retorna se o nó é um diretório.
         *
         * @return `true` se o nó for um diretório, caso contrário `false`
         */
        public boolean isDirectory() {
            return isDirectory;
        }
    }

    /**
     * Cria um arquivo no diretório especificado.
     *
     * @param parent o diretório onde o arquivo será criado
     * @param fileName o nome do arquivo a ser criado
     */
    public void createFile(Directory parent, String fileName) {
        if (parent.getData().stream().anyMatch(f -> f.getName().equals(fileName))) {
            System.out.println("File already exists: " + fileName);
            return;
        }

        Arquive newFile = new Arquive(fileName, "", 0, 0);
        parent.addData(newFile);  // Adiciona o novo arquivo ao diretório
        System.out.println("File created: " + fileName);
    }

    /**
     * Cria um subdiretório no diretório especificado.
     *
     * @param parent o diretório onde o subdiretório será criado
     * @param dirName o nome do subdiretório a ser criado
     */
    public void createDirectory(Directory parent, String dirName) {
        if (parent.getChildrens().containsKey(dirName)) {
            System.out.println("Directory already exists: " + dirName);
            return;
        }

        Directory newDir = new Directory(dirName, parent, (byte) 1, 0);
        parent.addSubdirectory(dirName, newDir);  // Adiciona o novo subdiretório
        System.out.println("Directory created: " + dirName);
    }

    /**
     * Exibe o conteúdo do disco virtual em um índice específico para depuração.
     *
     * @param index o índice do diretório no disco
     * @throws IOException se ocorrer um erro ao ler os dados do disco
     */
    public void debugDataArea(int index) throws IOException {
        System.out.println(new String(disk.readDir(index)));  // Exibe o conteúdo lido do disco
    }

    /**
     * Retorna uma representação em string do sistema de arquivos.
     *
     * @return uma string representando o sistema de arquivos
     */
    @Override
    public String toString() {
        return "FileSystem{" +
                "root=" + root +  // Exibe o diretório raiz
                '}';
    }

    /**
     * Retorna o diretório raiz do sistema de arquivos.
     *
     * @return o diretório raiz
     */
    public Directory getRoot() {
        return root;
    }

    /**
     * Retorna `true` se o diretório atual for o diretório raiz, caso contrário retorna `false`.
     *
     * @return `true` se o diretório atual for o raiz, caso contrário `false`
     */
    public boolean isInRoot() {
        return current == root;
    }
}
