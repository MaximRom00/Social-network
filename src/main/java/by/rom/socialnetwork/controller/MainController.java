package by.rom.socialnetwork.controller;

import by.rom.socialnetwork.model.Message;
import by.rom.socialnetwork.model.User;
import by.rom.socialnetwork.repository.MessageRepository;
import by.rom.socialnetwork.service.CommentService;
import by.rom.socialnetwork.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class MainController {

    private final MessageRepository messageRepository;

    private final MessageService messageService;

    private final CommentService commentService;

    public MainController(MessageRepository messageRepository, MessageService messageService, CommentService commentService) {
        this.messageRepository = messageRepository;
        this.messageService = messageService;
        this.commentService = commentService;
    }

    @Value("${upload.path}")
    private String path;

    @GetMapping("/")
    private String login(){
        return "first";
    }

    @GetMapping(value = "/main/{page}")
    private String main(@AuthenticationPrincipal User user,
                        @PathVariable("page") Integer page,
                        @RequestParam(name = "tag", required = false) String tag,
                        Model model){


        Pageable pageable = PageRequest.of(page, 6);

        Page<Message> messages = messageService.messageList(pageable, tag, null);
        model.addAttribute("tag", tag);

        model.addAttribute("message", messages);
        model.addAttribute("mess", new Message());

        model.addAttribute("page", page);
        System.out.println("Page - " + page);
        model.addAttribute("totalPages", messages.getTotalPages());

        return "main";
    }

    @PostMapping(value = "/main")
    private String saveMessage(@AuthenticationPrincipal User user,
                              @Valid @ModelAttribute(name = "mess") Message message,
                              BindingResult bindingResult, Model model,
                              @RequestParam("file") MultipartFile file) throws IOException {


        if (bindingResult.hasErrors()){
            if (message.getId() != null){
                return "redirect:/message/" + user.getId() + "/0";
            }
            return "main";
        }

        else{
            fileSaving(message, file);

             message.setAuthor(user);

            messageRepository.save(message);
        }
        return "redirect:/message/" + user.getId() + "/0";
    }

    @PostMapping(value = "/main/comment")
    public String saveComment( @RequestParam(name = "text") String text,
                              @RequestParam(name = "id") int id,
                              @RequestHeader(value = "referer", required = false) String referer){

        commentService.saveComment(text, id);

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        return "redirect:" + components.getPath();

    }

    private void fileSaving(Message message, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()){
            File directory = new File(path);
            if (!directory.exists()){
                directory.mkdir();
            }

            String uuidFileName = UUID.randomUUID().toString();
            String fileName = uuidFileName + "." + file.getOriginalFilename();

            message.setFileName(fileName);

            file.transferTo(new File(path + "/" + fileName));
        }
    }
}
