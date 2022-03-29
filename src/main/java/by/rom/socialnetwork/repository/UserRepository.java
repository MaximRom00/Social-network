package by.rom.socialnetwork.repository;

import by.rom.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);

    User findByEmail(String email);

    Optional<User> findById(Long id);
}
