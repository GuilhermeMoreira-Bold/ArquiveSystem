package org.example.system.disk;

import org.example.system.arquives.Arquive;
import org.example.system.directories.Directory;

import java.io.*;

import static org.example.system.disk.DiskUtils.*;

public class VirtualDisk {
    private FileAllocationTable fat;
    private DataArea dataArea;

    public VirtualDisk(boolean exists, String path) throws IOException {
        DISK_NAME = path;
        if (exists) {
            initializeDisk(false);
        } else {
            mountNewDisk();
            initializeDisk(true);
        }
    }

    private void initializeDisk(boolean isNew) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(DISK_NAME, "rw");
        fat = new FileAllocationTable(raf, isNew);
        dataArea = new DataArea(raf, isNew);
    }


    private void mountNewDisk() throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(DISK_NAME))) {
            dos.writeInt(DISK_SIZE);
            dos.writeInt(CLUSTER);
            dos.writeInt(TOTAL_BLOCKS);
            dos.writeInt(FAT_SIZE);
        }
    }

    public void readDiskMetaData() throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(DISK_NAME))) {
            System.out.println("Disk Size: " + dis.readInt());
            System.out.println("Cluster Size: " + dis.readInt());
            System.out.println("Total Block: " + dis.readInt());
            System.out.println("Fat Size: " + dis.readInt());
            System.out.println("Data area starter point: " + DATA_AREA_OFFSET);
        }
    }

   public void addArquive(Arquive arquive) throws IOException {
    //TODO to add a arquive
   }

   public void addSubDir(int parentStaterBlock,Entry subDir) throws IOException {
        dataArea.writeEntry(parentStaterBlock,subDir);
   }

   public byte[] readArquive(int starterBlock) throws IOException {
        //TODO for read arquives
       return dataArea.readEntry(starterBlock);
   }

   public byte[] readDir(int clusterId) throws IOException {
        return dataArea.readEntry(clusterId);
   }

}
