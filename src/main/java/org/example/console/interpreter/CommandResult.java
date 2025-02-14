package org.example.console.interpreter;

import org.example.console.pipeline.component.IOComponent;

import java.util.List;

/**
 * A classe `CommandResult` é um tipo de componente de I/O que armazena os resultados dos comandos executados.
 * Ela herda de `IOComponent` para ser compatível com o pipeline de compilação e permite a passagem de dados
 * entre diferentes estágios do pipeline.
 *
 *
 */
public class CommandResult extends IOComponent<CommandResult> {
    // Lista de resultados dos comandos executados
    private List<String> results;

    /**
     * Construtor que recebe uma lista de resultados dos comandos.
     *
     * @param results A lista de resultados dos comandos.
     */
    public CommandResult(List<String> results) {
        this.results = results;
    }

    /**
     * Método para obter os resultados dos comandos.
     *
     * @return A lista de resultados.
     */
    public List<String> getResults() {
        return results;
    }
}
