package edu.du.campusflow.exception;

public class NotLoggedInException extends Exception {
    public NotLoggedInException(String message) {
        super(message);
    }

    public NotLoggedInException() {
        super("User is not logged in");
    }
}
