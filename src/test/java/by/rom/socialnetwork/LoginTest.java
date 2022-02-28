package by.rom.socialnetwork;

import by.rom.socialnetwork.controller.MainController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user.sql", "/create-message.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LoginTest {

    @Autowired
    private MainController mainController;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    public MockMvc mockMvc;

    @Test
    public void contextLoads() throws Exception {
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please, Login")));
    }

    @Test
    public void loginAvailableForAll() throws Exception{

        mockMvc.perform(get("/main/0"))
                .andExpect(status().is3xxRedirection())
                .andDo(print())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void correctLoginTest() throws Exception {
        mockMvc.perform(formLogin("/login").user("max").password("1"))
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void invalidLoginForm() throws Exception {
        String loginErrorUrl = "/login?error";

        mockMvc.perform(formLogin())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(loginErrorUrl))
                .andExpect(unauthenticated());

        mockMvc.perform(get(loginErrorUrl))
                .andExpect(content().string(containsString("Invalid username or password.")));
    }

    @Test
    public void invalidUserNameOrPasswordLoginPage() throws Exception{
        mockMvc.perform(post("/login").param("user", "Invalid user"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void invalidUserNameOrPasswordRegistrationPage() throws Exception{
        mockMvc.perform(post("/registration").param("rpassword", "RepeatPassword"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
