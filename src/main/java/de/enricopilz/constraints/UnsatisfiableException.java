package de.enricopilz.constraints;

/** indicates an unsatisfiable assignment */
public class UnsatisfiableException extends Exception {

    public UnsatisfiableException(String message) {
        super(message);
    }
}
