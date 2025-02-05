package org.example.system.disk;

import java.util.*;

public class FileAllocationTable {
    List<Integer> fileClusters;

    private final int FREE = -1;
    private final int EOF = -2;
    public FileAllocationTable(int size) {
        fileClusters = new ArrayList<>(Collections.nCopies(size, FREE));
    }

    public List<Integer> findFileClusters(Entry entryFile) {
        List<Integer> clusters = new ArrayList<>();
        int nextCluster = entryFile.getStartBlock();
        while (nextCluster != EOF && nextCluster < fileClusters.size()) {
            clusters.add(nextCluster);
            nextCluster = fileClusters.get(nextCluster);
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
           fileClusters.set(clusterIndex,clusterIndex); //temporary


            if(i > 0){
                fileClusters.set(clusters.get(i -1), clusterIndex);
            }
       }
       fileClusters.set(clusters.get(clusters.size() -1) , EOF);
       return clusters.get(0);
    }
    public int findFreeBlock() {
        for (int i = 0; i < fileClusters.size(); i++) {
            if (fileClusters.get(i) == FREE) {
                return i;
            }
        }
        throw new IllegalStateException("No free block found");
    }

    public void removeFileCluster(Entry entryFile) {
        int current = entryFile.getStartBlock();
        while ( current != EOF) {
            int next = fileClusters.get(current);
            fileClusters.set(current, FREE);
            current = next;
        }
    }

    public void printAllBlocks(){
        System.out.println("File clusters: " + fileClusters.toString());
    }
}
