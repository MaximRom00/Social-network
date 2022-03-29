package by.rom.socialnetwork.repository;

import by.rom.socialnetwork.model.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
}
