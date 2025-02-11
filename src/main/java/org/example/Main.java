package org.example;
import org.example.compiler.interpreter.CommandExecutor;
import org.example.compiler.parser.Parser;
import org.example.compiler.pipeline.CompilationPipeline;
import org.example.compiler.pipeline.execptions.UnexpectInputType;
import org.example.compiler.scanner.Scanner;
import org.example.compiler.util.CMD;
import org.example.system.FileSystem;
import org.example.system.directories.Directory;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws UnexpectInputType, IOException {
        CompilationPipeline pipilene = new CompilationPipeline();
        CMD cmd = new CMD("src/main/resources/cmd.test");
        FileSystem fl = new FileSystem();
        pipilene.insertStage(new Scanner()).insertStage(new Parser()).insertStage(new CommandExecutor(fl));

        pipilene.execute(cmd);

//        FileSystem fileSystem = new FileSystem();
//
////        fileSystem.createDirectory(new Directory("Athos_teste", fileSystem.getCurrent(),(byte) 1));
//        fileSystem.debugDataArea(0);
//        fileSystem.debugDataArea(1);
////        fileSystem.removeDirectory("Athos_teste");
//
////        fileSystem.createDirectory(new Directory("Athos_teste_2", fileSystem.getCurrent(),(byte) 1));
//
////        fileSystem.debugDataArea(0);
////        fileSystem.debugDataArea(1);
//
//        //        fileSystem.moveDir("Athos_teste");
//
////        fileSystem.createDirectory(new Directory("Athos_teste", fileSystem.getCurrent(),(byte) 1));
//
////        fileSystem.createDirectory(new Directory("Athos_teste_2", fileSystem.getCurrent(),(byte) 0));
////        fileSystem.createDirectory(new Directory("Athos_teste_3", fileSystem.getCurrent(),(byte) 0));
////        fileSystem.createDirectory(new Directory("Athos_teste_4", fileSystem.getCurrent(),(byte) 0));
//
////        fileSystem.debugDataArea(1);
////        fileSystem.debugDataArea(2);
//
////        fileSystem.createDirectory(new Directory("Athos_teste_2", fileSystem.getCurrent(),(byte) 0));
////        fileSystem.debugDataArea(1);
////        fileSystem.debugDataArea(0);
//
////        fileSystem.removeDirectory("Athos_teste_2");
////        fileSystem.removeDirectory("Athos_teste_3");
////        fileSystem.removeDirectory("Athos_teste_4");
//
//        System.out.println(fileSystem.getCurrent());
    }
}