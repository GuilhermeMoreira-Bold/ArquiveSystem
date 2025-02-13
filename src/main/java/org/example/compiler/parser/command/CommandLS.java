package org.example.compiler.parser.command;

import org.example.system.arquives.Arquive;
import org.example.system.FileSystem;
import org.example.system.directories.Directory;

import java.util.List;
import java.util.Map;

/**
 * Representa o comando `ls` (list) que exibe o conteúdo do diretório atual.
 *
 * Este comando lista os subdiretórios e arquivos presentes no diretório
 * de trabalho atual dentro do sistema de arquivos simulado.
 */
public class CommandLS extends CommandNode {

    /**
     * Executa o comando `ls` no contexto do sistema de arquivos.
     *
     * Este método percorre os subdiretórios e arquivos do diretório atual
     * e retorna uma string formatada com seus nomes.
     *
     * @param context O sistema de arquivos onde o comando será executado.
     * @return Uma string contendo os nomes dos subdiretórios e arquivos do diretório atual.
     */
    @Override
    public String execute(FileSystem context) {
        StringBuilder content = new StringBuilder();

        // Lista os subdiretórios
        for (Map.Entry<String, Directory> dirs : context.getCurrent().getChildrens().entrySet()) {
            if (dirs.getValue().getChildrens() != null) {
                content.append(dirs.getKey() + "\n");
            }
        }

        // Lista os arquivos
        List<Arquive> arquives = context.getCurrent().getData();
        for (Arquive arquive : arquives) {
            content.append(arquive.getName() + "\n");
        }

        return "\n" + content;
    }

    /**
     * Retorna a representação em string do comando.
     *
     * @return O comando formatado como `ls`.
     */
    @Override
    public String toString() {
        return "ls";
    }
}
