package org.example;

//import org.example.parser.Parser;
import org.example.compiler.pipeline.execptions.UnexpectInputType;
import org.example.system.directories.Directory;
import org.example.system.disk.Entry;
import org.example.system.disk.FileAllocationTable;
import org.example.system.disk.VirtualDisk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws UnexpectInputType, IOException {
//        CompilationPipeline pipilene = new CompilationPipeline();
//        CMD cmd = new CMD("/home/guilherme/IdeaProjects/ARQUIVES_SO/src/main/resources/cmd.test");
//        FileSystem fl = new FileSystem(new Directory("root",null));
//        pipilene.insertStage(new Scanner()).insertStage(new Parser()).insertStage(new CommandExecutor(fl));
//
//        pipilene.execute(cmd);
        VirtualDisk disk = new VirtualDisk();


        Entry test = new Entry("oi_athos", (byte) 0,1, (byte) 0,0);
        Entry test2 = new Entry("HI ATHOS", (byte) 0,1, (byte) 0,0);

        disk.writeEntry(0, test);
        disk.writeEntry(0, test2);
        Directory root =  disk.getRootDir();

        System.out.println(root.toString());
//        System.out.println("Cluster 0: " + new String (disk.readDataAreaFromCluster(0)));
//        System.out.println("Cluster 1: " + new String (disk.readDataAreaFromCluster(1), StandardCharsets.UTF_8));

    }
}