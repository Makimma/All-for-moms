package ru.hse.moms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RewardNotFoundException extends RuntimeException{
    public RewardNotFoundException(String message) {
        super(message);
    }
}
