package esprit.tn.foyer_bi10.exception;

/**
 * Exception métier levée quand une règle fonctionnelle est violée.
 * Traduite en HTTP 400 par GlobalExceptionHandler.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
