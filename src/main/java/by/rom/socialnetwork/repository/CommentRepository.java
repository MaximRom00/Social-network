package by.rom.socialnetwork.repository;

import by.rom.socialnetwork.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
