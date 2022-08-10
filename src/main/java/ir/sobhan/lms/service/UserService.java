package ir.sobhan.lms.service;

import ir.sobhan.lms.business.exceptions.UserNotFoundException;
import ir.sobhan.lms.config.AdminConfig;
import ir.sobhan.lms.dao.UserRepository;
import ir.sobhan.lms.model.dto.inputdto.UserInputDTO;
import ir.sobhan.lms.model.dto.outputdto.UserOutputDTO;
import ir.sobhan.lms.model.entity.User;
import ir.sobhan.lms.security.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final AdminConfig adminConfig;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Bean
    CommandLineRunner addAdmin() {
        User user = new User(
                adminConfig.getUserName(),
                passwordEncoder.encode(adminConfig.getPassword()),
                "Mohammadjavad",
                "09217679934",
                "12456123156",
                true,
                true);
        user.setRoles(Role.ADMIN.name());

        if (!userRepository.existsByUserName(user.getUserName()))
            return args -> log.debug("Define an admin " + userRepository.save(user));

        return args -> log.debug("Admin active");
    }

    public List<UserOutputDTO> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).stream()
                .map(User::toDTO)
                .collect(Collectors.toList());
    }

    public User getOne(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));
    }

    public User getOne(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void addRoleToUser(User user, Role role) {
        user.setActive(true);
        user.setRoles(user.getRoles() + " " + role.name());
    }

    public void removeRoleFromUser(User user, Role roleGoal) {
        String[] roles = user.getRoles().split(" ");
        StringBuilder newRoles = new StringBuilder();
        for (String role : roles)
            if (!role.equals(roleGoal.name()))
                newRoles.append(role).append(" ");

        user.setRoles(newRoles.toString());
    }

    public boolean existUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public User create(UserInputDTO userInputDTO) {
        User user = userInputDTO.toEntity();
        user.setPassword(passwordEncoder.encode(userInputDTO.getPassword()));
        user.setRoles(Role.USER.name());

        return save(user);
    }
}
