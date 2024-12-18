package com.example.OpendData.exceptions;

/**
 * Classe PasswordException qui est une exception qui est levée lorsqu'un mot de passe n'est pas valide.
 */
public class PasswordException extends Exception {

    /**
     * Constructeur de l'exception PasswordException.
     * @param message Message d'erreur.
     */
    public PasswordException(String message) {
        super(message);
    }
}
