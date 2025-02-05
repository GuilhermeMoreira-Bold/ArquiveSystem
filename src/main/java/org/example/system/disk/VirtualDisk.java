package org.example.system.disk;

import java.io.*;
import java.nio.ByteBuffer;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class VirtualDisk {
    private  final String DISK_NAME = "virtual_disk.img";
    private  final int DISK_SIZE = 10 * 1024 * 1024;
    private  final int CLUSTER = 4 * 1024;

    private  final int TOTAL_BLOCKS = DISK_SIZE / CLUSTER;
    private final int FAT_SIZE = TOTAL_BLOCKS * 4;
    private final int FAT_INITIAL_OFFSET = 12;
    private final int ROOT_DIRECTORY_TABLE_OFFSET = 62;
    private final int DATA_AREA_OFFSET = 62;
    

    public void createDisk() throws FileNotFoundException {
        try(FileOutputStream fos = new FileOutputStream("test.bin");
            DataOutputStream dos = new DataOutputStream(fos);
        ){
            dos.writeInt(DISK_SIZE);
            dos.writeInt(CLUSTER);
            dos.writeInt(TOTAL_BLOCKS);
            dos.writeInt(FAT_SIZE);
            createDataArea();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void createDataArea() throws FileNotFoundException {
        try(RandomAccessFile raf = new RandomAccessFile("test.bin", "rw");
        ){
            raf.seek(DATA_AREA_OFFSET);
            for(int i = 0; i < TOTAL_BLOCKS; i++) {
                raf.writeByte(0XFF); // representa vazio
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public void readDisk() throws FileNotFoundException {
        try(FileInputStream fis = new FileInputStream("test.bin");
            DataInputStream dis = new DataInputStream(fis);
        ){
            System.out.println("Disk Size: " + dis.readInt());
            System.out.println("Cluster Size: " + dis.readInt());
            System.out.println("Total Block: " + dis.readInt());
            System.out.println("Fat Size: " + dis.readInt());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
   public void writeToFAT(int indice, int value) throws FileNotFoundException {
        try(RandomAccessFile raf = new RandomAccessFile("test.bin", "rw")){
            raf.seek( (indice * 8L) + FAT_INITIAL_OFFSET );
            raf.writeInt(value);
        }catch (Exception e){
            throw new RuntimeException(e);
        };
   }
   public int readFromFat(int indice) throws FileNotFoundException {
        try(RandomAccessFile raf = new RandomAccessFile("test.bin", "r")){
            raf.seek((indice * 8L) + FAT_INITIAL_OFFSET);
           return raf.readInt();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
   }

   public void readRootDirTable() throws FileNotFoundException {
        try(RandomAccessFile raf = new RandomAccessFile("test.bin", "r")){
            raf.seek(ROOT_DIRECTORY_TABLE_OFFSET);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
   }

   public byte[] readDataAreaWithClusterSize(int index) throws FileNotFoundException {
       try(RandomAccessFile raf = new RandomAccessFile("test.bin", "r")){
           raf.seek(index + DATA_AREA_OFFSET);
           byte[] content =  new byte[CLUSTER];
           raf.read(content);
           return content;
//           return new String(content, StandardCharsets.UTF_8);
       }catch (Exception e){
           throw new RuntimeException(e);
       }
   }
   public void writeDataAreaWithClusterSize(int index, String content){
       try(RandomAccessFile raf = new RandomAccessFile("test.bin", "rw")){
           raf.seek(index + DATA_AREA_OFFSET);
           raf.writeBytes(content);
       }catch (Exception e){
           throw new RuntimeException(e);
       }
   }

   public void writeEntry(int cluster, Entry entry) throws FileNotFoundException {
      try(RandomAccessFile raf = new RandomAccessFile("test.bin", "rw")){
          byte[] clusterContent = readDataAreaWithClusterSize(cluster);
          boolean hasSpace = true;


          for (int i = 0; i < clusterContent.length; i++) {
              byte c = clusterContent[i];
              if (c == (byte) 0XFF) {
                for(int j = 0; j < 265; j++){
                    if(clusterContent[i + j] != (byte) 0XFF){
                        hasSpace = false;
                    }
                }
                if(hasSpace){
                    System.out.println(i);
                    System.out.println(DATA_AREA_OFFSET +  cluster  + i);

                    raf.seek(DATA_AREA_OFFSET +  cluster  + i);
                    raf.writeBytes(entry.toString());
                    return;
                }

              }
          }
      }catch (Exception e){
          throw new RuntimeException(e);
      }
   }

}