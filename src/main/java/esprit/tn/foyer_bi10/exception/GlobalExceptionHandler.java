package esprit.tn.foyer_bi10.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /** Violation d'une règle métier → 400 Bad Request */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "erreur",       ex.getMessage(),
                        "statut",       400,
                        "horodatage",   Instant.now().toString()
                ));
    }

    /** Suppression d'un ID inexistant → 404 Not Found */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EmptyResultDataAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erreur",       "Cet enregistrement n'existe pas.",
                        "statut",       404,
                        "horodatage",   Instant.now().toString()
                ));
    }

    /** Violation de contrainte FK/UNIQUE → 409 Conflict */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "erreur",       "Cet enregistrement est lié à d'autres données et ne peut pas être supprimé.",
                        "statut",       409,
                        "horodatage",   Instant.now().toString()
                ));
    }

    /** Toute autre exception non prévue → 500 Internal Server Error */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        log.error("Erreur interne non gérée", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "erreur",       "Une erreur interne est survenue. Veuillez réessayer.",
                        "statut",       500,
                        "horodatage",   Instant.now().toString()
                ));
    }
}
