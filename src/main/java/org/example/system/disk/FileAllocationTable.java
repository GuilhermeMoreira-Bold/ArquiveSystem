package org.example.system.disk;

import org.example.system.disk.handlers.FatHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

import static org.example.system.disk.DiskUtils.*;

public class FileAllocationTable {

    int[] fileClusters;

    private final int FREE = 0XFF;
    private final int EOF = -2;

    private final FatHandler IOHandler;

    public FileAllocationTable(RandomAccessFile raf, boolean isNew) throws IOException {
      this.IOHandler = new FatHandler(raf);
      fileClusters = IOHandler.initialize(isNew);
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

    public int addFileCluster(Entry entryFile) {
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

    public void removeFileCluster(Entry entryFile) {
        int current = entryFile.getStartBlock();
        while ( current != EOF) {
            int next = fileClusters[current];
            fileClusters[current] =  FREE;
            current = next;
        }
    }

    public void writeAllDataToDisk() throws IOException {
        IOHandler.write(fileClusters);
    }
    public void printAllBlocks(){
        System.out.println("File clusters: " + Arrays.toString(fileClusters));
    }
}
