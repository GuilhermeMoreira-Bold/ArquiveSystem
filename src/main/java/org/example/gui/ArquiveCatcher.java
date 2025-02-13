package org.example.gui;

import org.example.compiler.pipeline.component.IOComponent;
import org.example.system.arquives.Arquive;

public class ArquiveCatcher extends IOComponent<ArquiveCatcher> {
      private static ArquiveCatcher instance;
      private static Arquive arquive;


    private ArquiveCatcher() {

    }
    public ArquiveCatcher(Arquive a){
        instance = this;
        this.arquive = a;
    }

    public static ArquiveCatcher getInstance() {
        if (instance == null) {
            instance = new ArquiveCatcher();
        }
        return instance;
    }

    public Arquive getArquive() {
        return arquive;
    }
}
