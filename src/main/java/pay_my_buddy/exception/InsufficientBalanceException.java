package pay_my_buddy.exception;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
