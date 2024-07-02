package ru.hse.moms.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.hse.moms.entity.Family;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FamilyNotFound  extends RuntimeException{
    public FamilyNotFound(String message) {
        super(message);
    }
}
