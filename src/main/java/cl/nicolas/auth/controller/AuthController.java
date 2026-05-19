package cl.nicolas.auth.controller;

import cl.nicolas.auth.dto.AuthRequest;
import cl.nicolas.auth.dto.AuthResponse;
import cl.nicolas.auth.dto.RegisterRequest;
import cl.nicolas.auth.security.JwtUtil;
import cl.nicolas.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        String username = request.getUsername();
        MDC.put("user", username);
        log.info("Intento de login iniciado");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            // Log exitoso con información del token ( solo primeros caracteres por
            // seguridad)
            String tokenPreview = token.substring(0, Math.min(token.length(), 10)) + "...";
            MDC.put("tokenPreview", tokenPreview);
            log.info("Login exitoso para usuario: {}", username);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException e) {
            log.warn("Intento de login fallido para usuario {} - Credenciales invalida", username);
            throw e;
        } finally {
            MDC.clear();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        String username = request.getUsername();
        MDC.put("user", username);
        log.info("Intento de registro para usuario: {}", username);

        try {
            userService.registerUser(username, request.getPassword(), request.getRoles());
            log.info("Usuario registrado exitosamente: {}", username);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
        } catch (RuntimeException e) {
            log.error("Fallo en registro para usuario: {} - {}", username, e.getMessage());
            throw e; // El manejador global devolverá 400 o 409 según corresponda
        } finally {
            MDC.clear();
        }
    }

}
