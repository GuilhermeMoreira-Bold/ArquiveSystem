package org.example.console.pipeline;

import org.example.console.pipeline.component.IOComponent;
import org.example.console.pipeline.execptions.UnexpectInputType;
import org.example.console.pipeline.pass.CompilationPass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A classe `CompilationPipeline` representa uma sequência de estágios (passes) do processo de compilação.
 * Ela organiza e executa uma série de passes de compilação em uma ordem definida.
 * Cada estágio do pipeline recebe um tipo de entrada e gera um tipo de saída específico.
 */
@SuppressWarnings("rawtypes")
public class CompilationPipeline {

    private List<CompilationPass<? extends IOComponent, ? extends IOComponent>> passes;

    /**
     * Constrói um novo pipeline de compilação vazio.
     */
    public CompilationPipeline() {
        passes = new ArrayList<>();
    }

    /**
     * Adiciona um estágio ao pipeline de compilação.
     *
     * @param pass O estágio a ser inserido no pipeline.
     * @return O próprio pipeline para permitir encadeamento de métodos.
     */
    public CompilationPipeline insertStage(CompilationPass<? extends IOComponent, ? extends IOComponent> pass) {
        passes.add(pass);
        return this;
    }

    /**
     * Executa o pipeline de compilação com uma entrada fornecida.
     * Para cada estágio, ele verifica se a entrada é do tipo esperado, executa o pass e passa a saída para o próximo estágio.
     *
     * @param input O componente de entrada que será processado pelo pipeline.
     * @throws UnexpectInputType Se o tipo de entrada não for compatível com o esperado pelo pass.
     * @throws IOException Se ocorrer um erro durante a execução de qualquer pass.
     */
    public void execute(IOComponent input) throws UnexpectInputType, IOException {
        IOComponent currentInput = input;

        for (CompilationPass<? extends IOComponent, ? extends IOComponent> pass : passes) {
            checkInputType(pass, currentInput);  // Verifica se a entrada tem o tipo esperado
            System.out.println("Executing " + pass.getDebugName());
            currentInput = runPass(pass, currentInput);  // Executa o pass e passa a saída para o próximo pass
        }
    }

    /**
     * Executa um pass específico do pipeline.
     *
     * @param pass O pass a ser executado.
     * @param input O componente de entrada para o pass.
     * @param <I> O tipo de entrada do pass.
     * @param <O> O tipo de saída do pass.
     * @return O resultado da execução do pass.
     * @throws IOException Se ocorrer um erro durante a execução do pass.
     */
    @SuppressWarnings("unchecked")
    private <I extends IOComponent<I>, O extends IOComponent<O>> IOComponent runPass(CompilationPass<I, O> pass, IOComponent input) throws IOException {
        if (!pass.getInputType().isInstance(input)) {
            throw new IllegalArgumentException("Input type mismatch. Expected: " + pass.getInputType() + ", but got: " + input.getClass());
        }
        return pass.pass((I) input);  // Executa o pass e retorna a saída
    }

    /**
     * Verifica se o tipo de entrada é compatível com o tipo esperado pelo pass.
     *
     * @param pass O pass a ser verificado.
     * @param input O componente de entrada para o pass.
     * @throws UnexpectInputType Se o tipo de entrada não for compatível com o tipo esperado pelo pass.
     */
    private void checkInputType(CompilationPass<? extends IOComponent, ? extends IOComponent> pass, IOComponent input) throws UnexpectInputType {
        if (!pass.getInputType().equals(input.getClass())) {
            throw new UnexpectInputType();  // Lança exceção caso o tipo de entrada não seja o esperado
        }
    }
}
