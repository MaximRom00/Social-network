package by.rom.socialnetwork.controller;

import by.rom.socialnetwork.model.Role;
import by.rom.socialnetwork.model.User;
import by.rom.socialnetwork.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    @GetMapping
    public String allUser(Model model){
        List<User> users = userService.findAll();
        model.addAttribute("users", users);

        return "allUsers";
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getUser(@PathVariable(name = "id") int id, Model model){
        User user = userService.getById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userChange";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String changeUser(@RequestParam(name = "id") User user,
                           @ModelAttribute(name = "user") User model){


        user.setName(model.getName());
        user.setEmail(model.getEmail());

        if (model.getRole() != null){
            user.setRole(model.getRole());
        }

        userService.saveUser(user);
        return "redirect:/user";
    }

    @GetMapping("/profile/{id}")
    public String getProfile(@AuthenticationPrincipal User authUser,@PathVariable int id, Model model){

        User user = userService.getById(id);
        model.addAttribute("user", user);
        model.addAttribute("avatar", user.getAvatarName());

        model.addAttribute("roles", Role.values());

        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal User authUser,
            @Valid @ModelAttribute(name = "user") User user,
            BindingResult bindingResult, @RequestParam("file") MultipartFile file,
            @RequestParam(name = "isActive") boolean isActive,
            @RequestParam(name = "role", required = false) Role role) throws IOException {

        if (bindingResult.hasErrors()){
            return "profile";
        }

        if (file != null && !file.getOriginalFilename().isEmpty()){
            Path absolutePath = Paths.get("src/main/resources/static/icons").toAbsolutePath();

            String fileName = user.getEmail() + "_" + file.getOriginalFilename();

            user.setAvatarName(fileName);

            file.transferTo(new File(absolutePath + "/" + fileName));
        }

        userService.updateProfileUser(user, role, isActive);

        updateAuthenticatedPrincipal(authUser);

        if (user.getRole() == Role.ADMIN){
            return "redirect:/user";
        }

        return "redirect:/user/profile/" + user.getId();
    }


    @GetMapping("/follow/{id}")
    public String follow(@PathVariable Long id,
                         @AuthenticationPrincipal User user,
                         Model model){

        userService.addFollower(id, user);

        if (user.getRole().equals(Role.ADMIN)){

            return "redirect:/user/";
        }
        return "redirect:/message/" + id + "/0";
    }

    @GetMapping("/unfollow/{id}")
    public String unFollow(@PathVariable(required = false) Long id,
                           @AuthenticationPrincipal User user,
                           Model model){

        userService.removeFollower(id, user);

        if (user.getRole().equals(Role.ADMIN)){
            return "redirect:/user/";
        }

        return "redirect:/message/" + id + "/0";
    }

    @GetMapping("{method}/{id}")
    public String getUserList(@PathVariable(name = "method") String method,
                              @PathVariable(name = "id") Long id,
                              Model model){

        User user = userService.getById(id);

        model.addAttribute("user", user);
        model.addAttribute("method", method);

        if (method.equalsIgnoreCase("subscriptions")){
            model.addAttribute("list", user.getSubscriptions());
        }
        else model.addAttribute("list", user.getSubscribers());

        return "subscribers";
    }

//    Update authentication to modify profile's image in navbar.
    private void updateAuthenticatedPrincipal(User authUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getById(authUser.getId());
        ((User) authentication.getPrincipal()).setAvatarName(user.getAvatarName());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }
}
