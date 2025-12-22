import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null) {
            return new AuthResponse("Invalid credentials", false);
        }

        // 🔥 IMPORTANT FIX (matches instead of equals)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse("Invalid credentials", false);
        }

        return new AuthResponse("Login successful", true);
    }
}
