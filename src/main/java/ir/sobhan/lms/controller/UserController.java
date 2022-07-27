package ir.sobhan.lms.controller;

import ir.sobhan.lms.business.assembler.UserModelAssembler;
import ir.sobhan.lms.business.exceptions.UserNotFoundException;
import ir.sobhan.lms.dao.UserRepository;
import ir.sobhan.lms.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
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

    @GetMapping()
    public CollectionModel<EntityModel<User>> all(){

        List<EntityModel<User>> userList = userRepository.findAll().stream()
                .map(userAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(userList,
                linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<User> one(@PathVariable Long id){

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        return userAssembler.toModel(user);
    }

    @PostMapping()
    public ResponseEntity<?> newUser(@RequestBody User user){

        User newUser = userRepository.save(user);
        EntityModel<User> newUserModel = userAssembler.toModel(newUser);

        log.info("Add new User " + newUser);

        return ResponseEntity
                .created(newUserModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(newUserModel);
    }
}
