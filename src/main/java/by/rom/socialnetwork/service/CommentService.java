package by.rom.socialnetwork.service;

import by.rom.socialnetwork.model.Comment;
import by.rom.socialnetwork.model.Message;
import by.rom.socialnetwork.repository.CommentRepository;
import by.rom.socialnetwork.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MessageRepository messageRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public void saveComment(String text, long id){
        Message message = messageRepository.getById(id);

        String timeNow = LocalDateTime.now().format(formatter);

        LocalDateTime parse = LocalDateTime.parse(timeNow,formatter);

        Comment comment = Comment.builder()
                .text(text)
                .message(message)
                .timestamp(parse).build();

        commentRepository.save(comment);
    }

    public void deleteComment(long id){
        commentRepository.deleteById(id);
    }
}
