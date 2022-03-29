package by.rom.socialnetwork.service;

import by.rom.socialnetwork.exception.NotFoundUser;
import by.rom.socialnetwork.model.secutiry.SecureToken;
import by.rom.socialnetwork.model.Role;
import by.rom.socialnetwork.model.User;
import by.rom.socialnetwork.repository.ConfirmationTokenRepository;
import by.rom.socialnetwork.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final MailSender mailSender;

    private final UserRepository userRepository;

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final PasswordEncoder encoder;

    public void saveUser(User user){
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundUser("User with id:" + id + ", not found!"));
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByName(name);
        if (userDetails == null){
            throw new UsernameNotFoundException("Not found");
        }
        else return userDetails;
    }

    public boolean addUser(User user){
        User userByEmail = userRepository.findByEmail(user.getEmail());

        if (userByEmail != null){
            return false;
        }

//        user.setActive(true);
        user.setRole(Role.USER);
        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);

        sendMessage(user);

        return true;
    }

    public boolean verify(String token) {
        SecureToken tokenFromDB = confirmationTokenRepository.findByToken(token);

        if (tokenFromDB != null){

            User user = userRepository.findByEmail(tokenFromDB.getUser().getEmail());
            user.setEnabled(true);

            userRepository.save(user);
        }
        else {
            return false;
        }
        return true;
    }

    public void updateProfileUser(User user, Role role, boolean isEnabled) {

        if (role != null){
            user.setRole(role);
        }
        if (user.getAvatarName().isEmpty()){
            user.setAvatarName(null);
        }
        user.setEnabled(isEnabled);

        saveUser(user);
    }


    private void sendMessage(User user) {
        SecureToken secureToken = new SecureToken(user);

        confirmationTokenRepository.save(secureToken);

        if (!user.getEmail().isEmpty()){
            String message = String.format("Hello, %s.\nWelcome! Please, visit link: http://localhost:8081/activate/%s, for registration",
                    user.getName(),
                    secureToken.getToken());

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean resetPasswordMessage(String email, String code){
        User userByEmail = userRepository.findByEmail(email);
        if (userByEmail == null){
            return false;
        }
        SecureToken secureToken = new SecureToken(userByEmail);
        confirmationTokenRepository.save(secureToken);

        String message = String.format("Hello, your code: %s", secureToken.getToken());
        mailSender.send(email, "Reset Password", message);
        return true;
    }

    public User checkCode(String code){
        SecureToken secureToken = confirmationTokenRepository.findByToken(code);
        if (secureToken != null){
            return secureToken.getUser();
        }
        return null;
    }

    public void addFollower(Long id, User user) {
        User userFromDB = getById(id);

        userFromDB.getSubscribers().add(user);
        saveUser(user);
    }

    public void removeFollower(Long id, User user) {
        User userFromDB = getById(id);

        userFromDB.getSubscribers().remove(user);
        saveUser(user);
    }
}
