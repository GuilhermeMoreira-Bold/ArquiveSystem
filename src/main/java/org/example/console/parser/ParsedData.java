package org.example.console.parser;

import org.example.console.parser.command.CommandNode;
import org.example.console.pipeline.component.IOComponent;

import java.util.List;

/**
 * A classe ParsedData representa o resultado da análise sintática, contendo
 * uma lista de comandos interpretados a partir dos tokens processados.
 *
 * Esta classe é usada como um componente de entrada/saída dentro do pipeline
 * de compilação, armazenando a estrutura de comandos extraída pelo Parser.
 */
public class ParsedData extends IOComponent<ParsedData> {

    /** Lista de comandos interpretados. */
    public final List<CommandNode> commands;

    /**
     * Construtor da classe ParsedData.
     *
     * @param commands Lista de comandos extraídos da análise sintática.
     */
    public ParsedData(List<CommandNode> commands) {
        this.commands = commands;
    }
}
