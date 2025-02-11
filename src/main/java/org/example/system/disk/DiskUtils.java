package org.example.system.disk;

public class DiskUtils {
    public  static  String DISK_NAME = "test.bin";
    public static final int DISK_SIZE = 10 * 1024 * 1024;
    public static  final int CLUSTER = 4 * 1024;

    public static final int TOTAL_BLOCKS = DISK_SIZE / CLUSTER;
    public static final int FAT_SIZE = TOTAL_BLOCKS;
    public static final int FAT_INITIAL_OFFSET = 12;
    public static final int DATA_AREA_OFFSET =  (FAT_INITIAL_OFFSET + FAT_SIZE) * 4;

    public static final int ROOT_DIRECTORY_TABLE_OFFSET = DATA_AREA_OFFSET;
    public static final int ENTRY_SIZE = 269;


    public static final byte FREE_AREA = (byte) 0XFF;
}
