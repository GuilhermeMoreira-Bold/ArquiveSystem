package org.example.compiler.parser.command;

import org.example.system.FileSystem;
import org.example.system.directories.Directory;
import org.example.system.disk.Entry;

import java.io.IOException;
import java.util.Map;

/**
 * Representa o comando `mkdir` (make directory), utilizado para criar um novo diretório
 * dentro do diretório atual no sistema de arquivos simulado.
 *
 * Este comando verifica se o diretório já existe e, caso não exista, cria um novo diretório
 * e o adiciona ao diretório atual.
 */
public class CommandMKDIR extends CommandNode {

    /** Nome do diretório a ser criado. */
    private String dirName;

    /**
     * Construtor da classe CommandMKDIR.
     *
     * @param directory Nome do diretório a ser criado.
     */
    public CommandMKDIR(String directory) {
        this.dirName = directory;
    }

    /**
     * Retorna a representação em string do comando `mkdir`.
     *
     * @return A representação do comando como "CommandMKDIR{dir='nome_do_diretorio'}".
     */
    @Override
    public String toString() {
        return "CommandMKDIR{" +
                "dir='" + dirName + '\'' +
                '}';
    }

    /**
     * Executa o comando `mkdir` no contexto do sistema de arquivos.
     *
     * Este método cria um novo diretório se ele não existir ainda, e o adiciona ao diretório atual.
     * Se o diretório já existir, retorna uma mensagem de erro.
     *
     * @param context O sistema de arquivos onde o comando será executado.
     * @return Uma mensagem indicando o resultado da operação ("success" ou "error").
     * @throws IOException Caso haja um erro ao adicionar o diretório no sistema de arquivos.
     */
    @Override
    public String execute(FileSystem context) throws IOException {
        // Cria o novo diretório
        Directory dir = new Directory(dirName, context.getCurrent(), (byte) 1);

        // Verifica se o diretório já existe
        if (context.getCurrent().getChildrens() != null) {
            for (Map.Entry<String, Directory> dirs : context.getCurrent().getChildrens().entrySet()) {
                if (dir.getName().equals(dirs.getKey())) {
                    System.out.println(" already exists");
                    return "error"; // Se já existir, retorna erro
                }
            }
        }

        // Adiciona o novo diretório ao sistema de arquivos
        int starterBlock = context.getDisk().addEntry(new Entry(dir.getName(), (byte) 0, 1, (byte) 1, context.getCurrent().getStaterBlock()));
        dir.setStaterBlock(starterBlock);
        context.getCurrent().addSubdirectory(dirName, dir);

        return "success"; // Retorna sucesso após criação
    }
}
