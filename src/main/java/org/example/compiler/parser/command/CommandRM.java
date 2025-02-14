package org.example.compiler.parser.command;

import org.example.system.arquives.Arquive;
import org.example.system.FileSystem;
import org.example.system.directories.Directory;
import org.example.system.disk.Entry;

import java.io.IOException;
import java.util.Map;

/**
 * Representa o comando `rm`, que é utilizado para remover diretórios ou arquivos do sistema de arquivos.
 *
 * Este comando verifica se o nome fornecido corresponde a um diretório ou arquivo existente no diretório atual.
 * Se for um diretório, ele deve estar vazio para ser removido. Se for um arquivo, ele é removido diretamente.
 */
public class CommandRM extends CommandNode {
    private String name;

    /**
     * Constrói um comando `rm` com o nome do diretório ou arquivo a ser removido.
     *
     * @param name O nome do diretório ou arquivo a ser removido.
     */
    public CommandRM(String name) {
        this.name = name;
    }

    /**
     * Executa o comando `rm` no contexto do sistema de arquivos.
     *
     * O comando tenta remover o diretório ou arquivo especificado. Se for um diretório, ele deve estar vazio.
     * Se for um arquivo, ele é removido diretamente.
     *
     * @param context O sistema de arquivos onde o comando será executado.
     * @return Uma mensagem indicando o sucesso ou erro da operação.
     */
    @Override
    public String execute(FileSystem context) {
        // Tenta remover um diretório
        for (Map.Entry<String, Directory> dir : context.getCurrent().getChildrens().entrySet()) {
            if (dir.getKey().equals(name)) {
                // Verifica se o diretório está vazio
                if (dir.getValue().getSize() > 0) {
                    return "ERROR DIR NOT EMPTY";  // Erro caso o diretório não esteja vazio
                }
                Directory d = dir.getValue();
                Entry entry = new Entry(d.getName(), d.getStaterBlock(), context.getCurrent().getStaterBlock(), 0, (byte) 0, d.getStatus());

                try {
                    context.getDisk().removeEntry(entry);  // Remove a entrada do disco
                } catch (IOException e) {
                    throw new RuntimeException(e);  // Exceção em caso de erro no disco
                }

                context.getCurrent().removeSubdirectory(d);  // Remove o diretório da lista de subdiretórios
                return "RM success";  // Sucesso na remoção do diretório
            }
        }

        // Tenta remover um arquivo
        for (Arquive arquive : context.getCurrent().getData()) {
            if (arquive.getName().equals(name)) {
                context.getCurrent().removeData(arquive);  // Remove o arquivo do diretório
                try {
                    context.getDisk().removeEntry(new Entry(arquive.getName(), arquive.getStaterBlock(), context.getCurrent().getStaterBlock(), arquive.getSize(), (byte) 1, (byte) 1));  // Remove a entrada do disco
                } catch (IOException e) {
                    throw new RuntimeException(e);  // Exceção em caso de erro no disco
                }

                return "RM success";  // Sucesso na remoção do arquivo
            }
        }

        return "RM error";  // Erro caso o diretório ou arquivo não seja encontrado
    }

    /**
     * Retorna uma representação em string do comando `rm`.
     *
     * @return A string "CommandRMDIR{}" representando o comando.
     */
    @Override
    public String toString() {
        return "CommandRMDIR{}";
    }
}
