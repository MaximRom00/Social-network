package by.rom.socialnetwork.controller;

import by.rom.socialnetwork.model.PrivateMessage;
import by.rom.socialnetwork.model.User;
import by.rom.socialnetwork.service.PrivateMessageService;
import by.rom.socialnetwork.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/userMessage")
@AllArgsConstructor
public class PrivateMessageController {

    private final PrivateMessageService privateMessageService;

    private final UserService userService;

    @GetMapping
    public String allReceivers(Model model){
        List<User> users = userService.findAll();

        model.addAttribute("receivers", users);
         return "userPrivateMessages";
    }

    @GetMapping("/{user}")
    public String messageById(@PathVariable User user, Model model){
        List<PrivateMessage> privateMessages = privateMessageService.getAllMessages()
                                                .stream()
                .sorted(Comparator.comparing(PrivateMessage::getTimestamp))
                .collect(Collectors.toList());

        model.addAttribute("message", privateMessages);

        List<User> users = userService.findAll();

        model.addAttribute("user", user);
        model.addAttribute("receivers", users);

        return "userPrivateMessagesById";
    }

    @PostMapping
    public String savePrivateMessage(@RequestParam String text,
                                     @RequestParam long idSender,
                                     @RequestParam long idReceiver,
                                     @RequestHeader(value = "referer", required = false) String referer){

        privateMessageService.save(text, idSender, idReceiver);

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        return "redirect:" + components.getPath();
    }

    @GetMapping("/delete/{id}")
    public String deleteMessage(@PathVariable long id, @RequestHeader(value = "referer", required = false) String referer){

        privateMessageService.deleteById(id);

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        return "redirect:" + components.getPath();
    }
}
