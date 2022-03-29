package by.rom.socialnetwork.service;


import by.rom.socialnetwork.model.Message;
import by.rom.socialnetwork.model.User;
import by.rom.socialnetwork.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Page<Message> messageList(Pageable pageable, String tag, User user) {

        if (tag != null && !tag.isEmpty()) {
            return messageRepository.findByTag(tag, pageable);
        }
        if (user != null){
            return messageRepository.findByUser(pageable, user);
        }
        else {
            return messageRepository.findAll(pageable);
        }
    }


    public void modifyLike(User user, long messageId){
        Message message = messageRepository.getById(messageId);
        Set<User> likes = message.getLikes();
        Set<User> dislikes = message.getDislikes();

        checkLikeAndDislike(user, message, likes, dislikes);
    }

    public void modifyDislike(User user, long messageId){
        Message message = messageRepository.getById(messageId);

        Set<User> dislikes = message.getDislikes();
        Set<User> likes = message.getLikes();

        checkLikeAndDislike(user, message, dislikes, likes);
    }

    private void checkLikeAndDislike(User user, Message message, Set<User> dislikes, Set<User> likes) {

        if (likes.contains(user)){
            dislikes.add(user);
            likes.remove(user);
        }
        else {
            if (dislikes.contains(user)){
                dislikes.remove(user);
            }
            else{
                dislikes.add(user);
            }
        }

        messageRepository.save(message);
    }

    public void deleteById(long id){
        Message byId = messageRepository.getById(id);
        messageRepository.deleteById(id);
    }

    public List<Message> findAll(){
        return messageRepository.findAll();
    }

}
