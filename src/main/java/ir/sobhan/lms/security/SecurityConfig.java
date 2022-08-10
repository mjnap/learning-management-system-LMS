package ir.sobhan.lms.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserPrincipalDetailService userPrincipalDetailService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userPrincipalDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/course-sections/update/**", "/course-sections/delete/**",
                        "/course-sections/get-students/**").hasAnyRole(Role.INSTRUCTOR.name(), Role.ADMIN.name())
                .antMatchers("/course-section-registrations/register-course", "/students/semester-grades/**",
                        "/students/summary").hasRole(Role.STUDENT.name())
                .antMatchers("/course-sections/create", "/course-section-registrations/grading").hasRole(Role.INSTRUCTOR.name())
                .antMatchers("/courses/new-course", "/courses/update-course/**", "/courses/delete-course/**",
                        "/instructors/new-instructor", "/instructors/update-instructor/**", "/instructors/delete-instructor/**",
                        "/students/new-student", "/students/update-student/**", "/students/delete-student/**",
                        "/terms/new-term", "/terms/update-term/**", "/terms/delete-term/**").hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.POST, "/users").anonymous()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();
    }
}
