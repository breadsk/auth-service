package cl.nicolas.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Proposito: Devolver el JWT Generado al cliente tras una autenticacion
 * exitosa.
 * Contenido : El token que el cliente debe guardar y usar en peticiones
 * posteriores
 * (en el header Authorization: Bearer <token>)
 */

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
}
