package by.rom.socialnetwork.service;

import by.rom.socialnetwork.model.PrivateMessage;
import by.rom.socialnetwork.repository.PrivateMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class PrivateMessageService {

    private final PrivateMessageRepository privateMessageRepository;

    private final UserService userService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<PrivateMessage> getAllMessages() {
        return privateMessageRepository.findAll();
    }

    public PrivateMessage getByIdSender(long id){
        return privateMessageRepository.getById(id);
    }

    public void save(String text, long idSender, long idReceiver) {
        String timeNow = LocalDateTime.now().format(formatter);

        LocalDateTime parse = LocalDateTime.parse(timeNow,formatter);

        PrivateMessage privateMessage = PrivateMessage.builder()
                .text(text)
                .timestamp(parse)
                .receiver(userService.getById(idReceiver))
                .sender(userService.getById(idSender))
                .build();

        privateMessageRepository.save(privateMessage);
    }

    public void deleteById(long id) {
        privateMessageRepository.deleteById(id);
    }
}
