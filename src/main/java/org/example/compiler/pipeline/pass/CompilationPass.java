package org.example.compiler.pipeline.pass;

import org.example.compiler.pipeline.component.IOComponent;

import java.io.IOException;

public abstract class CompilationPass<I extends IOComponent<I>, O extends IOComponent<O>> {

    public abstract Class<I> getInputType();

    public abstract Class<O> getOutputType();

    public abstract String getDebugName();

    public abstract O pass(I input) throws IOException;
}