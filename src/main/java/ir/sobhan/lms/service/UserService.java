package ir.sobhan.lms.service;

import ir.sobhan.lms.config.AdminConfig;
import ir.sobhan.lms.dao.UserRepository;
import ir.sobhan.lms.model.entity.User;
import ir.sobhan.lms.security.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final AdminConfig adminConfig;
    private final PasswordEncoder passwordEncoder;

//    @Bean
//    CommandLineRunner addAdmin(UserRepository userRepository){
//        User user = new User(
//                adminConfig.getUserName(),
//                passwordEncoder.encode(adminConfig.getPassword()),
//                "Mohammadjavad",
//                "09217679934",
//                "12456123156",
//                true,
//                true);
//        user.setRoles(Role.ADMIN.name());
//        return args -> {
//            log.info("Define an admin " + userRepository.save(user));
//        };
//    }
}
