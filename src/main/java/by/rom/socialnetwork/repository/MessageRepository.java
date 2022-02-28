package by.rom.socialnetwork.repository;

import by.rom.socialnetwork.model.Message;
import by.rom.socialnetwork.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MessageRepository extends JpaRepository<Message, Long> {

     Page<Message> findByTag(String tag, Pageable pageable);

     Page<Message> findAll(Pageable pageable);

     @Query("from Message m where m.author = :user")
     Page<Message> findByUser(Pageable pageable, @Param("user") User user);
}
