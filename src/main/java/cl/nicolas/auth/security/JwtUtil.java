package cl.nicolas.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generación: Crea un JWT con sub (username), roles (concatenados), fechas de
 * emisión
 * y expiración, firmado con HS256.
 * Validación: Verifica firma y expiración, devuelve los claims.
 */

/**
 * Proposito: Marca la clase como un bean de Spring, para que el contenedor la
 * gestione ( la cree e inyecte
 * automaticamente donde se necesite, por ejemplo en JwtAuthenticationFilter).
 * Spring escaneará esta clase y la registrará en su contexto
 */

@Component
public class JwtUtil {

    // Clave fija: Duoc.1983
    private static final String SECRET = "Duoc.1983Duoc.1983Duoc.1983Duoc.1983"; // Clave de al menos 256 bits
    /**
     * Constante: 3.600.000 milisegundos = 1 hora.
     * El token será válido durante ese tiempo desde su emisión.
     */
    private static final long EXPIRATION_TIME = 3600000; // 1 hora

    /**
     * Proposito: Convertir la clave secreta (String) en un objeto Key utilizable
     * por JWT para firmar/verficar
     * con el algoritmo HMAC-SHA256
     * 
     * @return
     */
    private Key getSigningKey() {
        byte[] keyBytes = SECRET.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Agregar roles al payload
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return validateToken(token).getSubject();
    }

    public String extractRoles(String token) {
        Claims claims = validateToken(token);
        return claims.get("roles", String.class);
    }
}
