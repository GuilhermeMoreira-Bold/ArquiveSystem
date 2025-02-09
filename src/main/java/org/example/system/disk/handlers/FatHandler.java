package org.example.system.disk.handlers;

import java.io.IOException;
import java.io.RandomAccessFile;
import static org.example.system.disk.DiskUtils.*;
import static org.example.system.disk.DiskUtils.FREE_AREA;

public class FatHandler implements DiskIOHandler<int[]>,AutoCloseable {
   private final RandomAccessFile raf;

    public FatHandler(RandomAccessFile raf) {
        this.raf = raf;
    }

    @Override
    public int[] initialize(boolean isNew) throws IOException {
        if (isNew) {
            createFatArea();
        }
        return getFat();
    }

    @Override
    public int[] read() throws IOException {
        return getFat();
    }

    @Override
    public int[] readAt(int index) throws IOException {
        if (index < 0 || index >= FAT_SIZE) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for FAT size " + FAT_SIZE);
        }
        raf.seek((index * 4L) + FAT_INITIAL_OFFSET);
        return new int[]{raf.readInt()};
    }

    @Override
    public void writeAt(int[] value, int index) throws IOException {
            if (index < 0 || index >= FAT_SIZE) {
                throw new IndexOutOfBoundsException("Index " + index + " out of bounds for FAT size " + FAT_SIZE);
            }
            raf.seek( (index * 4L) + FAT_INITIAL_OFFSET );
            raf.writeInt(value[0]);


    }

    @Override
    public void write(int[] value) throws IOException {
            raf.seek(FAT_INITIAL_OFFSET);
            for (int i : value) {
                raf.writeInt(i);
            }
    }


    private void createFatArea() throws IOException {

            raf.seek(FAT_INITIAL_OFFSET);
            for(int i = 0; i < FAT_SIZE; i ++){
                raf.writeInt(FREE_AREA);
            }


    }
    private int[] getFat() throws IOException {

            raf.seek(FAT_INITIAL_OFFSET);
            int[] list = new int[FAT_SIZE];
            for(int i = 0;i < FAT_SIZE; i ++){
                list[i] = raf.readInt();
            }
            return list;
    }

    @Override
    public void close() throws IOException {
        if(raf != null){
            raf.close();
        }
    }
}
