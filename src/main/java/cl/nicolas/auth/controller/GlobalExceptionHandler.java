package cl.nicolas.auth.controller;

import cl.nicolas.auth.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

/**
 * @RestControllerAdvice es una especializacion de @Component
 *                       Spring Boot hace un component-scan de todos los
 *                       paquetes bajo el paquete raiz
 *                       de tu aplicación (donde está @SpringBootApplication).
 *                       Si GlobalExceptionHandler está en un subpaquete (por
 *                       ejemplo, cl.nicolas.auth.controller)
 *                       será detectado automaticamente.
 *                       No necesitas declararlo en @import ni registrarlo
 *                       manualmente en el SecurityConfig, ni
 *                       inyectarlo en ningún lado.
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. Manejo de credenciales incorrectas (401)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Autenticación fallida: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "Credenciales inválidas",
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // 2. Manejo de excepciones de negocio (RuntimeException y subclases) (400)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        log.error("Error en la aplicación", ex);
        ErrorResponse error = new ErrorResponse(
                "Error en la solicitud",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 3. Manejo de cualquier otra excepción no capturada (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Error interno del servidor no controlado", ex);
        ErrorResponse error = new ErrorResponse(
                "Error interno en el servidor",
                "Ocurrió un error inesperado. Por favor contacte al administrador.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}