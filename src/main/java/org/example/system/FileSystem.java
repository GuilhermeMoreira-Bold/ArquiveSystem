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

                for(Arquive a : dir.getData()){
                    if(a.getName().equals(arquivo.getName())){

                    }else{
                        dir.addData(arquivo);
                        return;
                    }
                }
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
