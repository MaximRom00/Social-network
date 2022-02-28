package by.rom.socialnetwork.repository;

import by.rom.socialnetwork.model.Message;
import by.rom.socialnetwork.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);

    User findByEmail(String email);

    Page<User> findAllById(Pageable pageable, long id);

}
