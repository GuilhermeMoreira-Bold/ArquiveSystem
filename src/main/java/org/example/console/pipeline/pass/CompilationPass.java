package org.example.console.pipeline.pass;

import org.example.console.pipeline.component.IOComponent;

import java.io.IOException;

/**
 * A classe abstrata `CompilationPass` representa um estágio genérico no pipeline de compilação.
 * Cada pass recebe um tipo de entrada (I) e produz um tipo de saída (O).
 * Subclasses devem implementar a lógica específica de processamento no método `pass`.
 *
 * @param <I> O tipo de entrada do pass.
 * @param <O> O tipo de saída do pass.
 */
public abstract class CompilationPass<I extends IOComponent<I>, O extends IOComponent<O>> {

    /**
     * Retorna a classe do tipo de entrada esperado para este pass.
     *
     * @return A classe que representa o tipo de entrada.
     */
    public abstract Class<I> getInputType();

    /**
     * Retorna a classe do tipo de saída gerado por este pass.
     *
     * @return A classe que representa o tipo de saída.
     */
    public abstract Class<O> getOutputType();

    /**
     * Retorna o nome de depuração do pass.
     * Esse nome pode ser utilizado para logs ou mensagens de debug.
     *
     * @return O nome de depuração do pass.
     */
    public abstract String getDebugName();

    /**
     * Executa o processamento do pass.
     * Este método é chamado pelo pipeline para processar a entrada e gerar a saída.
     *
     * @param input O componente de entrada a ser processado.
     * @return O componente de saída gerado pelo pass.
     * @throws IOException Se ocorrer um erro durante o processamento.
     */
    public abstract O pass(I input) throws IOException;
}
