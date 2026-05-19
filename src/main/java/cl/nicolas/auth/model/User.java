package cl.nicolas.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // almacenar siempre el hash BCrypt

    /**
     * anotaciones que definen cómo JPA (Hibernate) mapea
     * la colección de roles a la base de datos.
     */

    /**
     * Al usar fetch = FetchType.EAGER, cada vez que busques un
     * usuario con userRepository.findById(...), JPA hará un
     * JOIN (o una segunda consulta) para traer los roles
     * automáticamente. Esto evita el famoso error
     * LazyInitializationException cuando intentas
     * acceder a roles fuera de una transacción
     * abierta. Para datos pequeños como roles, es aceptable.
     */

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles; // ej: "ROLE_ADMIN", "ROLE_USER"
}
