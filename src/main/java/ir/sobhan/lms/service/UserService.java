package ir.sobhan.lms.service;

import ir.sobhan.lms.config.AdminConfig;
import ir.sobhan.lms.dao.UserRepository;
import ir.sobhan.lms.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    final AdminConfig adminConfig;

    @Bean
    CommandLineRunner addAdmin(UserRepository userRepository){
        return args -> {
            log.info("Define an admin " + userRepository.save(new User(adminConfig.getUserName()
                    ,adminConfig.getPassword(),
                    "Mohammadjavad",
                    "09217679934",
                    "12456123156",
                    true,
                    true)));
        };
    }
}
