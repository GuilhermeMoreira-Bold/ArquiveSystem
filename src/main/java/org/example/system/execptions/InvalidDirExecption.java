package org.example.system.execptions;

public class InvalidDirExecption extends RuntimeException {
    public InvalidDirExecption(String message) {
        super(message);
    }
}
