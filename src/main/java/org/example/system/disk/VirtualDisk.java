package org.example.system.disk;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.example.system.disk.DiskUtils.*;

public class VirtualDisk {
    private FileAllocationTable fat;
    private DataArea dataArea;

    public VirtualDisk(boolean exists, String path) throws IOException {
        DISK_NAME = path;
        if (exists) {
            initializeDisk(true);
        } else {
            mountNewDisk();
            initializeDisk(false);
        }
    }

    private void initializeDisk(boolean exists) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(DISK_NAME, "rw");
        fat = new FileAllocationTable(raf, exists);
        dataArea = new DataArea(raf, exists);
    }


    private void mountNewDisk() throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(DISK_NAME))) {
            dos.writeInt(DISK_SIZE);
            dos.writeInt(CLUSTER_SIZE);
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
    public int addEntry(Entry entry) throws IOException {
       entry.setStartBlock(fat.addFileCluster(entry));
       dataArea.writeEntry(entry.getParent(),entry);
       dataArea.writeEntry(entry.getStartBlock(),entry);
       return entry.getStartBlock();
   }

   public void editEntry(Entry entry,String content) throws IOException {

        writeEntryWithContent(entry,content);
   }

   public void writeEntryWithContent(Entry entry,String content) throws IOException {
        byte[] stringBytes = content.getBytes();
        int stringIndex = 0;
        int remainingToCopyBytes = stringBytes.length;

       fat.reallocateFileSize(entry);
       List<Integer> clustersIndex = fat.findFileClusters(entry);
          for(int i : clustersIndex){
              if(i == -2) {
                  break;
              }

              byte[] dest = new byte[CLUSTER_DATA_AREA_SIZE];
              System.arraycopy(stringBytes, stringIndex, dest, 0, Math.min(CLUSTER_DATA_AREA_SIZE, remainingToCopyBytes));

              if(remainingToCopyBytes < CLUSTER_DATA_AREA_SIZE) {
                  Arrays.fill(dest, remainingToCopyBytes, dest.length - 1, FREE_AREA);
              }

              remainingToCopyBytes -= CLUSTER_DATA_AREA_SIZE;
              stringIndex += Math.min(stringIndex, stringBytes.length);
              dataArea.writeContentAt(i * CLUSTER_SIZE + ENTRY_SIZE + DATA_AREA_OFFSET, dest);
          }
   }

   public String readEntryData(Entry entry) throws IOException {
       List<Integer> clusters = fat.findFileClusters(entry);
       StringBuilder sb = new StringBuilder();
       for(int i : clusters){
           if(i == -2) break;
          byte[] b = dataArea.readContentAt(  i * CLUSTER_SIZE + DATA_AREA_OFFSET + ENTRY_SIZE);

          int index = 0;

          for(; index < b.length; index++) {
              if(b[index] == FREE_AREA){
                  break;
              }
          }

          sb.append(new String(Arrays.copyOfRange(b, 0, index)));
       }
       return sb.toString();
   }


   public void removeEntry(Entry entry) throws IOException {
        fat.removeFileCluster(entry);

        byte[] buffer = dataArea.readEntry(entry.getParent());
        int offset = 0;

       for (int i = 0; i < buffer.length; i+= ENTRY_SIZE) {
           if(offset == CLUSTER_SIZE) break;
           byte actualByte = buffer[offset];
           if(actualByte == FREE_AREA){
               offset += ENTRY_SIZE;
           }else{
               byte[] e = new byte[ENTRY_SIZE];
               System.arraycopy(buffer, offset, e, 0, ENTRY_SIZE);
               Entry ent = Entry.toEntry(e);
               if(!ent.getName().equals(entry.getName())) {
                   offset += ENTRY_SIZE;
               }else{
                   ent.setStatus((byte)0);

                   int pointer = DATA_AREA_OFFSET + (CLUSTER_SIZE * entry.getParent()) + offset;
                   dataArea.writeEntryAt(pointer,ent);
                   break;
               }
           }
       }

      buffer = dataArea.readEntry(entry.getStartBlock());
       offset = 0;

       for (int j = 0; j < buffer.length; j += ENTRY_SIZE) {
           if(offset == CLUSTER_SIZE) break;
           byte actualByte = buffer[offset];
           if(actualByte == FREE_AREA){
               offset += ENTRY_SIZE;
           }else{
               byte[] e = new byte[ENTRY_SIZE];
               System.arraycopy(buffer, offset, e, 0, ENTRY_SIZE);
               Entry ent = Entry.toEntry(e);
               if(!ent.getName().equals(entry.getName())) {
                   offset += ENTRY_SIZE;
               }else{
                   ent.setStatus((byte)0);
                   int pointer = DATA_AREA_OFFSET + (CLUSTER_SIZE * entry.getStartBlock()) + offset;

                   dataArea.writeEntryAt(pointer,ent);
                   break;
               }
           }
       }
   }

   public byte[] readArquive(int starterBlock) throws IOException {
        //TODO for read arquives
       return dataArea.readEntry(starterBlock);
   }

   public byte[] readDir(int clusterId) throws IOException {
        return dataArea.readEntry(clusterId);
   }

    public long getClusterSize() {
        return DiskUtils.CLUSTER_SIZE;
    }

   public int getUsedClusters() {
        return fat.getUsedClusterCount();
   }

   public int getTotalClusters() {
        return DiskUtils.TOTAL_BLOCKS;
   }

   public int[] getFileClustersArray() {
       return Arrays.copyOf(fat.fileClusters, fat.fileClusters.length);
   }

}
