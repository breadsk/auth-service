package cl.nicolas.auth.service;

import cl.nicolas.auth.model.User;
import cl.nicolas.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(String username, String rawPassword, Set<String> roles) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword)); // hashear
        user.setRoles(roles);
        userRepository.save(user);
    }
}
