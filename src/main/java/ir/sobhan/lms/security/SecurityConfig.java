package ir.sobhan.lms.security;

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
                    .antMatchers(HttpMethod.GET, "/instructors/get-students-of-course-section/**").hasAnyRole(Role.INSTRUCTOR.name(),Role.ADMIN.name())
                    .antMatchers("/instructors/update-course-section").hasAnyRole(Role.INSTRUCTOR.name(),Role.ADMIN.name())
                    .antMatchers(HttpMethod.DELETE, "/instructors/**").hasAnyRole(Role.INSTRUCTOR.name(),Role.ADMIN.name())
                    .antMatchers( "/students/summary").hasRole(Role.STUDENT.name())
                    .antMatchers("/students/semester-grades/**").hasRole(Role.STUDENT.name())
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
