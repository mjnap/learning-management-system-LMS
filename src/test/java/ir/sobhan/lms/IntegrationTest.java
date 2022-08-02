package ir.sobhan.lms;

import ir.sobhan.lms.security.Role;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LmsApplication.class)
@TestPropertySource("/application-test.properties")
@WebAppConfiguration
public class IntegrationTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .build();
    }

    @SneakyThrows
    @Test
    public void test(){
        this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                "  \"userName\": \"ali\"," +
                "  \"password\": \"ali123\"," +
                "  \"name\": \"Ali\"," +
                "  \"phone\": \"09133573148\"," +
                "  \"nationalId\": \"123456789\"" +
                "}"))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post("/admin/newInstructor").contentType(MediaType.APPLICATION_JSON)
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
                        "  \"phone\": \"09217679934\"," +
                        "  \"nationalId\": \"125898965\"" +
                        "}"))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post("/admin/newStudent").contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"userName\": \"kia\"," +
                        "  \"studentId\": \"1234\"," +
                        "  \"degree\": \"BS\"" +
                        "}"))
                .andExpect(status().isCreated());

        //termId == 5
        this.mockMvc.perform(post("/admin/newTerm").contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"title\": \"first semester\"," +
                        "  \"open\": true" +
                        "}"))
                .andExpect(status().isCreated());

        //courseId == 6
        this.mockMvc.perform(post("/admin/newCourse").contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "  \"title\": \"math\"," +
                        "  \"units\": 5" +
                        "}"))
                .andExpect(status().isCreated());

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
        //courseSectionId == 7
        this.mockMvc.perform(post("/instructors/newCourseSection").contentType(MediaType.APPLICATION_JSON)
                .with(user("ali").password("ali123").roles(Role.INSTRUCTOR.name()))
                .content("{" +
                        "  \"courseTitle\": \"math\"," +
                        "  \"termId\": 5" +
                        "}"))
                .andExpect(status().isCreated());

        try{
            this.mockMvc.perform(post("/students/registerCourse/{courseSectionId}" , 7)
                    .with(user("kia").password("kia123").roles(Role.STUDENT.name())))
                    .andExpect(status().isCreated());
        }catch (Exception e){
            e.getStackTrace();
        }

        this.mockMvc.perform(put("/instructors/grading").contentType(MediaType.APPLICATION_JSON)
                .with(user("ali").password("ali123").roles(Role.INSTRUCTOR.name()))
                .content("{" +
                        "  \"courseSectionId\": 7," +
                        "  \"studentsScore\": [" +
                        "    {" +
                        "      \"studentId\": 4," +
                        "      \"score\": 18.5" +
                        "    }" +
                        "  ]" +
                        "}"))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/students/semesterGrades/{termId}" , 5)
                .with(user("kia").password("kia123").roles(Role.STUDENT.name())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.average").value(18.5));
    }
}
