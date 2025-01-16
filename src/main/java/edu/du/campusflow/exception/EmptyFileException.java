package edu.du.campusflow.exception;

public class EmptyFileException extends RuntimeException {
    public EmptyFileException(String message) {
        super(message);
    }
    public EmptyFileException() {
        super("File is empty");
    }
}
