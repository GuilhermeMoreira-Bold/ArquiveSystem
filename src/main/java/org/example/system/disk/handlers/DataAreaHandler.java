package org.example.system.disk.handlers;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import static org.example.system.disk.DiskUtils.*;

public class DataAreaHandler implements DiskIOHandler<byte[]>, AutoCloseable {
    private final RandomAccessFile raf;

    public DataAreaHandler(RandomAccessFile raf) {
        this.raf = raf;
    }

    @Override
    public byte[] initialize(boolean isNew) throws IOException {
        if (!isNew) {
            createDataArea();
        }
        return read();
    }

    @Override
    public byte[] read() throws IOException {
        raf.seek(DATA_AREA_OFFSET);
        byte[] data = new byte[TOTAL_BLOCKS];
        raf.readFully(data);
        return data;
    }

    @Override
    public byte[] readAt(int index) throws IOException {
        if (index < 0 || index >= TOTAL_BLOCKS) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for data area size " + TOTAL_BLOCKS);
        }
        raf.seek((long) index * CLUSTER + DATA_AREA_OFFSET);
        byte[] content = new byte[CLUSTER];
        raf.readFully(content);
        return content;
    }

    @Override
    public void write(byte[] value) throws IOException {
        if (value.length > TOTAL_BLOCKS) {
            throw new IllegalArgumentException("Data size exceeds total block size.");
        }
        raf.seek(DATA_AREA_OFFSET);
        raf.write(value);
    }

    @Override
    public void writeAt(byte[] value, int index) throws IOException {
        if (index < 0 || index >= DISK_SIZE) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for data area size " + DISK_SIZE);
        }
        raf.seek(index);
        raf.write(value);
    }

    @Override
    public void close() throws IOException {
        if (raf != null) {
            raf.close();
        }
    }

    private void createDataArea() throws IOException {
        raf.seek(DATA_AREA_OFFSET);
        byte[] emptyData = new byte[TOTAL_BLOCKS];
        Arrays.fill(emptyData, FREE_AREA);
        raf.write(emptyData);
    }
}
