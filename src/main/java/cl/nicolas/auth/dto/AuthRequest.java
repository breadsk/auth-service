package cl.nicolas.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Proposito: Recibir las credenciales del cliente en el endpoint /auth/login
 * Validaciones: @NotBlank asegura que los campos no sean nulos ni vacios
 * Uso : El cliente envia un JSON con username y passworde
 */

@Data
public class AuthRequest {
    @NotBlank(message = "El usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
