package org.example.console.pipeline;

import org.example.console.pipeline.component.IOComponent;
import org.example.console.pipeline.execptions.UnexpectInputType;
import org.example.console.pipeline.pass.CompilationPass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class CompilationPipeline {

    private List<CompilationPass<? extends IOComponent, ? extends IOComponent>> passes;

    public CompilationPipeline() {
        passes = new ArrayList<>();
    }

    public CompilationPipeline insertStage(CompilationPass<? extends IOComponent, ? extends IOComponent> pass) {
        passes.add(pass);
        return this;
    }

    public void execute(IOComponent input) throws UnexpectInputType, IOException {
        IOComponent currentInput = input;

        for (CompilationPass<? extends IOComponent, ? extends IOComponent> pass : passes) {
            checkInputType(pass, currentInput);
            System.out.println("Executing " + pass.getDebugName());
            currentInput = runPass(pass, currentInput);
        }
    }

    @SuppressWarnings("unchecked")
    private <I extends IOComponent<I>, O extends IOComponent<O>> IOComponent runPass(CompilationPass<I, O> pass, IOComponent input) throws IOException {
        if (!pass.getInputType().isInstance(input)) {
            throw new IllegalArgumentException("Input type mismatch. Expected: " + pass.getInputType() + ", but got: " + input.getClass());
        }
        return pass.pass((I) input);
    }

    private void checkInputType(CompilationPass<? extends IOComponent, ? extends IOComponent> pass, IOComponent input) throws UnexpectInputType {
        if (!pass.getInputType().equals(input.getClass())) {
            throw new UnexpectInputType();
        }
    }
}