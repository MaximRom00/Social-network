package by.rom.socialnetwork;

import by.rom.socialnetwork.model.User;
import by.rom.socialnetwork.repository.UserRepository;
import by.rom.socialnetwork.service.MailSender;
import by.rom.socialnetwork.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;


    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;


    @Test
    public void shouldAddUser(){
        User user = User.builder()
                .email("email@email.com").build();

        boolean isUserCreated = userService.addUser(user);

        Assertions.assertTrue(isUserCreated);
        Assertions.assertTrue(user.isEnabled());
        Assertions.assertNotNull(user.getRole());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1)).send(
                ArgumentMatchers.eq(user.getEmail()),
                ArgumentMatchers.eq("Activation code"),
                ArgumentMatchers.contains("Hello, " + user.getName()));
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(user.getPassword());
    }


    @Test
    public void addUserFail(){
        User user = User.builder()
                .name("New user").build();

        Mockito.doReturn(new User())
                .when(userRepository)
                .findByName("New user");

        boolean isSavedUser = userService.addUser(user);
        Assertions.assertFalse(isSavedUser);
    }
}
