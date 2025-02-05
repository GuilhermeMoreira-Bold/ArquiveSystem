package org.example.system.execptions;

public class NotExistingDirectoryExecption extends RuntimeException {
    public NotExistingDirectoryExecption(String message) {
        super(message);
    }
}
