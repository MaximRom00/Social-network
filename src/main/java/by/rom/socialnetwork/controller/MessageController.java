package by.rom.socialnetwork.controller;

import by.rom.socialnetwork.model.Message;
import by.rom.socialnetwork.model.User;
import by.rom.socialnetwork.repository.MessageRepository;
import by.rom.socialnetwork.repository.UserRepository;
import by.rom.socialnetwork.service.CommentService;
import by.rom.socialnetwork.service.MessageService;
import by.rom.socialnetwork.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {

    private final UserService userService;
    private final MessageService messageService;
    private final CommentService commentService;

    @GetMapping("/{id}/{page}")
    public String allMessage(@PathVariable Long id, @PathVariable("page") Integer page,
                             @AuthenticationPrincipal User user, Model model){

        Pageable pageable = PageRequest.of(page, 6);

        Page<Message> messages = null;
        User userDB = userService.getById(id);

        messages = messageService.messageList(pageable, null, userDB);

        model.addAttribute("subscriptionsCount", userDB.getSubscriptions().size());
        model.addAttribute("subscribersCount", userDB.getSubscribers().size());
        model.addAttribute("user", userDB);
        model.addAttribute("id", id);
        model.addAttribute("isSubscriber", userDB.getSubscribers().contains(user));
        model.addAttribute("mess", new Message());
        model.addAttribute("message", messages);

        model.addAttribute("page", page);
        model.addAttribute("totalPages", messages.getTotalPages());

        return "messages";
    }

    @GetMapping("/delete/{id}")
    public String deleteMessage(@AuthenticationPrincipal User user, @PathVariable Long id){
        messageService.deleteById(id);

        return "redirect:/message/" + user.getId() + "/0";
    }

    @GetMapping("/delete/comment/{id}")
    public String deleteComment(@AuthenticationPrincipal User user, @PathVariable Long id){
        commentService.deleteComment(id);

        return "redirect:/message/" + user.getId() + "/0";
    }

    @GetMapping("/byTag")
    public String messageByTag(Model model){
        List<Message> allMessages = messageService.findAll();
        List<Message> collect = allMessages.stream().sorted(Comparator.comparing(Message::getTag)).collect(Collectors.toList());

        Set<String> set = allMessages.stream().map(Message::getTag).collect(Collectors.toSet());

        model.addAttribute("messages", collect);
        model.addAttribute("set", set);
        return "messagesByTag";
    }

    @GetMapping("/{id}/like")
    public String like(@AuthenticationPrincipal User user,
                       @PathVariable(name = "id") Long messageId,
                       @RequestHeader(value = "referer", required = false) String referer){

        messageService.modifyLike(user, messageId);

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        return "redirect:" + components.getPath();
    }

    @GetMapping("/{id}/dislike")
    public String dislike(@AuthenticationPrincipal User user,
                       @PathVariable(name = "id") Long messageId,
                       @RequestHeader(value = "referer", required = false) String referer){

        messageService.modifyDislike(user, messageId);

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        return "redirect:" + components.getPath();
    }
}
