package org.example;

//import org.example.parser.Parser;
import org.example.compiler.pipeline.execptions.UnexpectInputType;
import org.example.system.FileSystem;
import org.example.system.directories.Directory;
import org.example.system.disk.Entry;
import org.example.system.disk.FileAllocationTable;
import org.example.system.disk.VirtualDisk;

import javax.imageio.stream.FileImageInputStream;
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

        FileSystem fileSystem = new FileSystem();
        System.out.println(fileSystem);
    }
}