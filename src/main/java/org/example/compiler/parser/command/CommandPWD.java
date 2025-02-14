package org.example.compiler.parser.command;

import org.example.system.FileSystem;
import org.example.system.directories.Directory;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o comando `pwd`, que exibe o caminho absoluto do diretório atual no sistema de arquivos.
 *
 * Esse comando percorre os diretórios pais até chegar à raiz e constrói o caminho completo do diretório
 * onde o sistema de arquivos está atualmente.
 */
public class CommandPWD extends CommandNode {

    /**
     * Executa o comando `pwd` no contexto do sistema de arquivos.
     *
     * O comando percorre os diretórios pais do diretório atual até chegar à raiz e monta a representação
     * do caminho absoluto do diretório atual.
     *
     * @param context O sistema de arquivos onde o comando será executado.
     * @return O caminho absoluto do diretório atual.
     */
    @Override
    public String execute(FileSystem context) {
        StringBuilder path = new StringBuilder();
        Directory d = context.getCurrent();

        // Percorre os diretórios pais até chegar à raiz
        while(true) {
            if (d == null) break;  // Se não houver mais pais, interrompe o loop
            path.insert(0, d.getName().equals("/") ? "" : "/" + d.getName());
            d = d.getParent();  // Move para o diretório pai
        }

        return path.toString();  // Retorna o caminho absoluto
    }

    /**
     * Retorna uma representação em string do comando `pwd`.
     *
     * @return A string "CommandPWD{}" representando o comando.
     */
    @Override
    public String toString() {
        return "CommandPWD{}";
    }
}
