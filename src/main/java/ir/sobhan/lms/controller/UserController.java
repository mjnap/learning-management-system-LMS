package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.UserModelAssembler;
import ir.sobhan.lms.business.exceptions.UserNotFoundException;
import ir.sobhan.lms.dao.UserRepository;
import ir.sobhan.lms.model.entity.User;
import ir.sobhan.lms.model.dto.inputdto.UserInputDTO;
import ir.sobhan.lms.model.dto.outputdto.UserOutputDTO;
import ir.sobhan.lms.security.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final UserModelAssembler userAssembler;
    private final PasswordEncoder passwordEncoder;

    @GetMapping()
    public CollectionModel<EntityModel<UserOutputDTO>> all(){

        List<EntityModel<UserOutputDTO>> userList = userRepository.findAll().stream()
                .map(userAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(userList,
                linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<UserOutputDTO> one(@PathVariable Long id){

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        return userAssembler.toModel(user);
    }

    @PostMapping()
    public ResponseEntity<?> newUser(@RequestBody UserInputDTO userInputDTO){

        User user = userInputDTO.toEntity();
        user.setPassword(passwordEncoder.encode(userInputDTO.getPassword()));
        user.setRoles(Role.USER.name());

        User newUser = userRepository.save(user);
        EntityModel<UserOutputDTO> newUserModel = userAssembler.toModel(newUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newUserModel);
    }
}
