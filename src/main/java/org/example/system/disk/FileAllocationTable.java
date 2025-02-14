package org.example.system.disk;

import org.example.system.disk.handlers.FatHandler;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class FileAllocationTable {

    int[] fileClusters;

    private final int FREE = -1;
    private final int EOF = -2;

    private final FatHandler IOHandler;

    public FileAllocationTable(RandomAccessFile raf, boolean exists) throws IOException {
      this.IOHandler = new FatHandler(raf);
      fileClusters = IOHandler.initialize(exists);
    }

    public List<Integer> findFileClusters(Entry entryFile) {
        List<Integer> clusters = new ArrayList<>();
        int nextCluster = entryFile.getStartBlock();
        while (nextCluster != EOF && nextCluster < fileClusters.length) {
            clusters.add(nextCluster);
            nextCluster = fileClusters[nextCluster];
        }
        if(nextCluster == EOF) {
            clusters.add(nextCluster);
        }
        return clusters;
    }

    public void reallocateFileSize(Entry entryFile) throws IOException {
        List<Integer> clusters = findFileClusters(entryFile);
        if(entryFile.getSize() == 0) return;
        if(entryFile.getSize() + 1 > clusters.size()) {
            int clusterIndex = clusters.get(clusters.size() - 2);
            for(int i = 0; i < entryFile.getSize() - clusters.size() + 1; i ++) {
                fileClusters[clusterIndex] = findFreeBlock();
                clusterIndex = fileClusters[clusterIndex];
                clusters.add(clusters.size() -1, clusterIndex);
                System.out.println("Free block: " + clusterIndex);
            }

            fileClusters[clusterIndex] = EOF;
            writeAllDataToDisk();
        } else if(entryFile.getSize() + 1 < clusters.size()) {
            int clusterIndex = clusters.get(entryFile.getSize());
            fileClusters[clusters.get(entryFile.getSize() - 1)] = EOF;

            for(int i = entryFile.getSize(); i < clusters.size() - entryFile.getSize() + 1; i ++) {
                int tempClusterIndex = clusterIndex;
                clusterIndex = fileClusters[tempClusterIndex];

                if(clusterIndex == EOF) {
                    fileClusters[tempClusterIndex] = FREE;
                    break;
                }

                fileClusters[tempClusterIndex] = FREE;
            }

            writeAllDataToDisk();
        }

        System.out.println("File clusters: " + Arrays.toString(fileClusters));
    }

    public int addFileCluster(Entry entryFile) throws IOException {
        ArrayList<Integer> clusters = new ArrayList<>();

       for(int i = 0; i < entryFile.getSize(); i ++) {
           int clusterIndex = findFreeBlock();
           clusters.add(clusterIndex);
           fileClusters[clusterIndex] = clusterIndex; //temporary don't remove this break's the logic

            if(i > 0){
                fileClusters[clusters.get(i -1)] = clusterIndex;
            }
       }
       fileClusters[clusters.get(clusters.size() -1)] =  EOF;

       writeAllDataToDisk();

       return clusters.get(0);
    }

    public int findFreeBlock() {
        for (int i = 0; i < fileClusters.length; i++) {
            if (fileClusters[i] == FREE) {
                return i;
            }
        }
        throw new IllegalStateException("No free block found");
    }

    public void removeFileCluster(Entry entryFile) throws IOException {
        int current = entryFile.getStartBlock();
        while (current != EOF) {
            int next = fileClusters[current];
            fileClusters[current] =  FREE;
            current = next;
        }
        writeAllDataToDisk();
    }

    public void writeAllDataToDisk() throws IOException {
        IOHandler.write(fileClusters);
    }

    public void printAllBlocks(){
        System.out.println("File clusters: " + Arrays.toString(fileClusters));
    }

    public int getUsedClusterCount() {
        int count = 0;
        for (int clusterValue : fileClusters) {
            if (clusterValue != FREE) {
                count++;
            }
        }
        return count;
    }

    public int getFreeClusterCount() {
        int count = 0;
        for (int clusterValue : fileClusters) {
            if (clusterValue == FREE) {
                count++;
            }
        }
        return count;
    }
}
