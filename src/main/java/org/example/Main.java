package org.example;
import org.example.compiler.interpreter.CommandExecutor;
import org.example.compiler.parser.Parser;
import org.example.compiler.pipeline.CompilationPipeline;
import org.example.compiler.pipeline.execptions.UnexpectInputType;
import org.example.compiler.scanner.Scanner;
import org.example.compiler.util.CMD;
import org.example.system.FileSystem;
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
    }
}