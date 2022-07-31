package ir.sobhan.lms.security;

import ir.sobhan.lms.service.UserPrincipalDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

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
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringAntMatchers("/**")
                .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/instructors/getStudentsOfCourseSection/**").hasAnyRole(Role.INSTRUCTOR.name(),Role.ADMIN.name())
                    .antMatchers("/instructors/updateCourseSection").hasAnyRole(Role.INSTRUCTOR.name(),Role.ADMIN.name())
                    .antMatchers(HttpMethod.DELETE, "/instructors/**").hasAnyRole(Role.INSTRUCTOR.name(),Role.ADMIN.name())
                    .antMatchers("/students/summery").hasRole(Role.STUDENT.name())
                    .antMatchers("/students/semesterGrades/**").hasRole(Role.STUDENT.name())
                    .antMatchers(HttpMethod.POST, "/students/**").hasRole(Role.STUDENT.name())
                    .antMatchers(HttpMethod.POST, "/instructors/**").hasRole(Role.INSTRUCTOR.name())
                    .antMatchers(HttpMethod.PUT, "/instructors/**").hasRole(Role.INSTRUCTOR.name())
                    .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
                    .antMatchers(HttpMethod.POST, "/users").anonymous()
                .anyRequest()
                    .authenticated()
                .and()
                .formLogin();
    }
}
