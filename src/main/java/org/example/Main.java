package org.example;

//import org.example.parser.Parser;
import org.example.compiler.parser.Parser;
import org.example.compiler.pipeline.CompilationPipeline;
import org.example.compiler.pipeline.execptions.UnexpectInputType;
import org.example.compiler.scanner.Scanner;
import org.example.compiler.util.CMD;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws UnexpectInputType {
        CompilationPipeline pipilene = new CompilationPipeline();
        CMD cmd = new CMD("/home/guilherme/IdeaProjects/ARQUIVES_SO/src/main/resources/cmd.test");

        pipilene.insertStage(new Scanner()).insertStage(new Parser());

        pipilene.execute(cmd);
    }
}