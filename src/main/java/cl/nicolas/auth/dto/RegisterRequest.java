package cl.nicolas.auth.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

/**
 * Proposito: Recibir los datos para crear un nuevo usuario en el endpoint
 * /auth/register
 * Roles: Permite asignar roles al usuario (Por defecto ROLE_USER si no se envia
 * 
 */

@Data
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private Set<String> roles; // ej: ["ROLE_USER"]
}
