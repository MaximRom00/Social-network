package by.rom.socialnetwork.repository;

import by.rom.socialnetwork.model.secutiry.SecureToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<SecureToken, Long> {

    SecureToken findByToken(String confirmationToken);
}
