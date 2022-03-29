package by.rom.socialnetwork.controller;


import by.rom.socialnetwork.model.User;
import by.rom.socialnetwork.recaptcha.ReCaptchaResponse;
import by.rom.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Objects;

@Controller
public class RegistrationController {

    @Value("${google.recaptcha.key.secret}")
    private String secret;

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    private final UserService service;

    private final RestTemplate restTemplate;

    public RegistrationController(UserService service, RestTemplate restTemplate) {
        this.service = service;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute User user, Model model){
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/registration")
    private String saveUser(@RequestParam(name = "g-recaptcha-response") String captchaResponse,
                            @Valid @ModelAttribute(name = "user") User user,
                            BindingResult bindingResult,
                            Model model){

        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        ReCaptchaResponse response = restTemplate.postForObject(url, Collections.emptyList(), ReCaptchaResponse.class);

        if (!Objects.requireNonNull(response).isSuccess()){
            model.addAttribute("captchaError", "Please verify captcha");
        }


        if (user.getPassword() != null && !user.getPassword().equals(user.getRpassword())){
            bindingResult.addError(new FieldError("rpassword", "rpassword", "Password aren't different. "));
        }

        if (bindingResult.hasErrors()){
            return "registration";
        }

        if (!service.addUser(user)){
            model.addAttribute("infoAboutSaving", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activateAccount(@PathVariable String code, Model model){
        boolean isActivated = service.verify(code);

        if (isActivated){
            model.addAttribute("message", "Login was activated.");
        }
        else model.addAttribute("message", "Code was activated");

        return "login";
    }
}
