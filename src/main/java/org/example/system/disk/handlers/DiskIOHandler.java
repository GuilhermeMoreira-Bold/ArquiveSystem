package org.example.system.disk.handlers;

import java.io.IOException;

public interface DiskIOHandler<T> {
    T initialize(boolean isNew) throws IOException;
    T read() throws IOException;
    void write(T value) throws IOException;
    void writeAt(T value, int index) throws IOException;
    T readAt(int index) throws IOException;
}
