package org.example.compiler.parser.command;

import org.example.gui.ArquiveCatcher;
import org.example.system.FileSystem;
import org.example.system.arquives.Arquive;

import java.io.IOException;

/**
 * Representa o comando `nano`, que é utilizado para abrir um arquivo dentro do diretório atual
 * e possibilitar a edição desse arquivo.
 *
 * Este comando verifica se o arquivo existe no diretório atual e, se encontrado,
 * abre o arquivo para edição. Caso contrário, retorna uma mensagem informando que o arquivo não foi encontrado.
 */
public class CommandNANO extends CommandNode {

    /** Nome do arquivo a ser editado. */
    private String arquiveName;

    /**
     * Construtor da classe CommandNANO.
     *
     * @param arquiveName Nome do arquivo a ser aberto e editado.
     */
    public CommandNANO(String arquiveName) {
        this.arquiveName = arquiveName;
    }

    /**
     * Executa o comando `nano` no contexto do sistema de arquivos.
     *
     * Este método verifica se o arquivo especificado existe no diretório atual e, se encontrado,
     * o abre para edição utilizando a classe `ArquiveCatcher`. Se o arquivo não for encontrado,
     * retorna uma mensagem informando o erro.
     *
     * @param context O sistema de arquivos onde o comando será executado.
     * @return Uma mensagem indicando o resultado da operação ("success" ou "don't found").
     * @throws IOException Caso ocorra um erro ao tentar acessar ou editar o arquivo.
     */
    @Override
    public String execute(FileSystem context) throws IOException {
        if (arquiveName == null) {
            return "No arquive name specified"; // Retorna erro se não houver nome de arquivo
        }

        // Procura o arquivo no diretório atual
        for (Arquive a : context.getCurrent().getData()) {
            if (a.getName().equals(arquiveName)) {
                ArquiveCatcher arquive = new ArquiveCatcher(a); // Abre o arquivo para edição
                return "success"; // Retorna sucesso se o arquivo for encontrado
            }
        }
        return "don't found"; // Retorna erro se o arquivo não for encontrado
    }
}
