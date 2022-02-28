package by.rom.socialnetwork;

import by.rom.socialnetwork.controller.MainController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@WithUserDetails("max")
@Sql(value = {"/create-user.sql", "/create-message.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MainControllerTest {
    @Autowired
    private MainController mainController;

    @Autowired
    public MockMvc mockMvc;

//    look at the main page on messages and user's author...
    @Test
    public void mainPageTest() throws Exception {
        mockMvc.perform(get("/main/0"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(view().name("main"))
                .andExpect(model().attributeExists("message"))
                .andExpect(xpath("//*[@id=\"navbar-list-4\"]/ul/div/h3/span").string("Max"));
    }

//    Check count of messages.
    @Test
    public void checkAllMessages() throws Exception {
        mockMvc.perform(get("/main/0"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id=\"message-list\"]/div").nodeCount(5));
    }

    @Test
    public void checkMessagesByTag() throws Exception{
        mockMvc.perform(get("/main/0").param("tag","day of week"))
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id=\"message-list\"]/div").nodeCount(2));
    }

    @Test
    public void addMessageWithFile() throws Exception{
        MockHttpServletRequestBuilder builderMultiPart = multipart("/main")
                .file("file", "newFile".getBytes())
                .param("tag", "new Tag")
                .param("text", "new Message")
                .with(SecurityMockMvcRequestPostProcessors.csrf());

        mockMvc.perform(builderMultiPart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/message/1"));


        mockMvc.perform(get("/main/0"))
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id=\"message-list\"]/div").nodeCount(6));
    }
}
