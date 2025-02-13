package org.example.compiler.lexer;

/**
 * Define os tipos de tokens que podem ser reconhecidos pelo analisador léxico.
 * Cada token representa um elemento sintático da linguagem processada pelo compilador.
 */
public enum TokenType {
    /** Barra "/" utilizada para caminhos. */
    SLASH,

    /** Ponto e vírgula ";" utilizado para separação de comandos. */
    SEMICOLON,

    /** Ponto "." utilizado para extensões de arquivos ou referências ao diretório atual. */
    DOT,

    /** Dois pontos ":" utilizados em contextos específicos como separação de elementos. */
    TWODOTS,

    /** Representa um caminho de arquivo ou diretório. */
    PATH,

    /** Identificador genérico (nomes de arquivos, diretórios, comandos). */
    INDENTIFIER,

    /** Caracteres especiais não categorizados. */
    SPECIAL,

    /** Espaços em branco (espaço, tabulação). */
    WHITESPACE,

    /** Argumentos passados para comandos. */
    ARGUMENTS,

    /** Comando para criar diretórios (mkdir). */
    MKDIR,

    /** Comando para remover arquivos ou diretórios (rm). */
    RM,

    /** Comando para criar arquivos vazios (touch). */
    TOUCH,

    /** Comando para mudar de diretório (cd). */
    CD,

    /** Comando para listar arquivos e diretórios (ls). */
    LS,

    /** Comando para exibir o diretório atual (pwd). */
    PWD,

    /** Representa um valor numérico. */
    NUMBER,

    /** Identificador (possível duplicação de INDENTIFIER). */
    IDENTIFIER,

    /** Comando para abrir o editor de texto (nano). */
    NANO,

    /** Indica o fim da entrada (End of File). */
    EOF,
}
