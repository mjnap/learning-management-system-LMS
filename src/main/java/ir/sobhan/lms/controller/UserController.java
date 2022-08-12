package ir.sobhan.lms.controller;

import ir.sobhan.lms.model.dto.inputdto.UserInputDTO;
import ir.sobhan.lms.model.dto.outputdto.UserOutputDTO;
import ir.sobhan.lms.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<UserOutputDTO> all(@RequestParam int size, @RequestParam int page) {
        return userService.getAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public UserOutputDTO one(@PathVariable Long id) {
        return userService.getById(id).toDTO();
    }

    @PostMapping()
    public ResponseEntity<?> newUser(@RequestBody UserInputDTO userInputDTO) {

        if (userService.existUserName(userInputDTO.getUserName()))
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("This username already exists");
        log.info("User added : " + userInputDTO.getUserName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.create(userInputDTO).toDTO());
    }
}
