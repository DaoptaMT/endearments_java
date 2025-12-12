package mt.endearments.repository.user;

import mt.endearments.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByName(String name);
    Boolean existsByName(String name);
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
