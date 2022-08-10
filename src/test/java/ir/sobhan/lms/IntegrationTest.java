package ir.sobhan.lms;

import ir.sobhan.lms.security.Role;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@WebAppConfiguration
public class IntegrationTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @SneakyThrows
    @Test
    public void test() {
        this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"userName\": \"ali\"," +
                        "  \"password\": \"ali123\"," +
                        "  \"name\": \"Ali\"," +
                        "  \"phone\": \"09133573148\"," +
                        "  \"nationalId\": \"123456789\"" +
                        "}"))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post("/instructors/new-instructor").contentType(MediaType.APPLICATION_JSON)
                .with(user("javad").password("mjnap").roles(Role.ADMIN.name()))
                .content("{" +
                        "  \"userName\": \"ali\"," +
                        "  \"rank\": \"FULL\"" +
                        "}"))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"userName\": \"kia\"," +
                        "  \"password\": \"kia123\"," +
                        "  \"name\": \"Kia\"," +
                        "  \"phone\": \"092176799456\"," +
                        "  \"nationalId\": \"125898965\"" +
                        "}"))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post("/students/new-student").contentType(MediaType.APPLICATION_JSON)
                .with(user("javad").password("mjnap").roles(Role.ADMIN.name()))
                .content("{" +
                        "  \"userName\": \"kia\"," +
                        "  \"studentId\": \"1234\"," +
                        "  \"degree\": \"BS\"" +
                        "}"))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post("/terms/new-term").contentType(MediaType.APPLICATION_JSON)
                .with(user("javad").password("mjnap").roles(Role.ADMIN.name()))
                .content("{" +
                        "  \"title\": \"first semester\"," +
                        "  \"open\": true" +
                        "}"))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post("/courses/new-course").contentType(MediaType.APPLICATION_JSON)
                .with(user("javad").password("mjnap").roles(Role.ADMIN.name()))
                .content("{" +
                        "  \"title\": \"math\"," +
                        "  \"units\": 5" +
                        "}"))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post("/course-sections/create").contentType(MediaType.APPLICATION_JSON)
                .with(user("ali").password("ali123").roles(Role.INSTRUCTOR.name()))
                .content("{" +
                        "  \"courseTitle\": \"math\"," +
                        "  \"termId\": 6" +
                        "}"))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post("/course-section-registrations/register-course").param("courseSectionId", "8")
                .with(user("kia").password("kia123").roles(Role.STUDENT.name())))
                .andExpect(status().isCreated());

        this.mockMvc.perform(put("/course-section-registrations/grading").contentType(MediaType.APPLICATION_JSON)
                .with(user("ali").password("ali123").roles(Role.INSTRUCTOR.name()))
                .content("{" +
                        "  \"courseSectionId\": 8," +
                        "  \"studentsScore\": [" +
                        "    {" +
                        "      \"studentId\": 5," +
                        "      \"score\": 18.5" +
                        "    }" +
                        "  ]" +
                        "}"))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/students/semester-grades/{termId}", 6)
                .with(user("kia").password("kia123").roles(Role.STUDENT.name())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.average").value(18.5));
    }
}
