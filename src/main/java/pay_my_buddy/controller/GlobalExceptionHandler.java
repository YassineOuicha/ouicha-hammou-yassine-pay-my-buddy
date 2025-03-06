package pay_my_buddy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pay_my_buddy.exception.InsufficientBalanceException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInsufficientBalanceException(InsufficientBalanceException ex) {
        return ex.getMessage(); // "Solde insuffisant pour effectuer la transaction"
    }
}