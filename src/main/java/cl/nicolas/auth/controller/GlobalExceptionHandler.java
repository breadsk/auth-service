package cl.nicolas.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Autenticacion fallida: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenciales inválidas"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        log.error("Error en la aplicación", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("Error", ex.getMessage()));
    }

}
