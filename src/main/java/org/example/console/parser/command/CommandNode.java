package org.example.console.parser.command;

import org.example.system.FileSystem;

import java.io.IOException;

/**
 * Classe abstrata que define a estrutura base para todos os comandos do sistema de arquivos.
 * Cada comando específico (como `CommandCD`, `CommandLS`, etc.) deve estender esta classe e
 * implementar o método `execute`, que descreve o comportamento do comando no contexto do sistema de arquivos.
 */
public abstract class CommandNode {

    /**
     * Método abstrato responsável pela execução do comando no contexto do sistema de arquivos.
     *
     * Cada subclasse deve fornecer uma implementação específica para o comando correspondente.
     *
     * @param context O sistema de arquivos onde o comando será executado.
     * @return Uma mensagem indicando o resultado da execução do comando.
     * @throws IOException Caso ocorra um erro ao executar o comando, como problemas de leitura ou escrita.
     */
    public abstract String execute(FileSystem context) throws IOException;
}
