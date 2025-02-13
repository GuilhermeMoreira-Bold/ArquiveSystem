package org.example.compiler.parser.command;

import org.example.system.FileSystem;
import org.example.system.directories.Directory;

import java.util.Map;

/**
 * Representa o comando `cd` (change directory) no sistema de arquivos simulado.
 *
 * Este comando permite a navegação entre diretórios dentro do sistema de arquivos,
 * incluindo a capacidade de retornar ao diretório pai (`..`).
 */
public class CommandCD extends CommandNode {

    /** Nome do diretório para o qual o usuário deseja navegar. */
    private String directoryName;

    /**
     * Construtor da classe CommandCD.
     *
     * @param directoryName Nome do diretório de destino.
     */
    public CommandCD(String directoryName) {
        this.directoryName = directoryName;
    }

    /**
     * Executa o comando `cd` no contexto do sistema de arquivos.
     *
     * @param context O sistema de arquivos onde o comando será executado.
     * @return Uma mensagem indicando o resultado da operação.
     */
    @Override
    public String execute(FileSystem context) {
        if (directoryName.equals("..")) {
            if (context.isInRoot()) {
                return "Already in root directory";
            } else {
                context.setCurrent(context.getCurrent().getParent());
                return toString();
            }
        }

        for (Map.Entry<String, Directory> dirs : context.getCurrent().getChildrens().entrySet()) {
            if (directoryName.equals(dirs.getKey())) {
                context.setCurrent(dirs.getValue());
                return toString();
            }
        }
        return "No such directory: " + directoryName;
    }

    /**
     * Retorna a representação em string do comando.
     *
     * @return Comando formatado como `cd <diretório>`.
     */
    @Override
    public String toString() {
        return String.format("cd %s", directoryName);
    }
}
