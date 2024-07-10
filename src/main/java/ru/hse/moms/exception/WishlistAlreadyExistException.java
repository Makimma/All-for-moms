package ru.hse.moms.exception;

public class WishlistAlreadyExistException extends RuntimeException {
    public WishlistAlreadyExistException(String message) {
        super(message);
    }
}
