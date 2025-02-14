package org.example.console.pipeline.component;

/**
 * A classe `IOComponent` é uma classe genérica que serve como base para os componentes do pipeline de compilação.
 * Ela usa a técnica de auto-referência (tipo genérico `T extends IOComponent<T>`) para garantir que os tipos
 * utilizados nas subclasses sigam o mesmo padrão.
 *
 * @param <T> O tipo da subclasse que estende esta classe.
 */
public class IOComponent<T extends IOComponent<T>> {


}
