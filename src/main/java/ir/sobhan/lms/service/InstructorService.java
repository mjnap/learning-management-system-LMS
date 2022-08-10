package ir.sobhan.lms.service;

import ir.sobhan.lms.business.exceptions.InstructorNotFoundException;
import ir.sobhan.lms.dao.InstructorRepository;
import ir.sobhan.lms.model.dto.inputdto.InstructorUpdateInputDTO;
import ir.sobhan.lms.model.dto.outputdto.InstructorOutputDTO;
import ir.sobhan.lms.model.entity.Instructor;
import ir.sobhan.lms.model.entity.Rank;
import ir.sobhan.lms.model.entity.User;
import ir.sobhan.lms.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserService userService;

    public List<InstructorOutputDTO> getAll(Pageable pageable) {
        return instructorRepository.findAll(pageable).stream()
                .map(Instructor::toDTO)
                .collect(Collectors.toList());
    }

    public Instructor getOne(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new InstructorNotFoundException(id));
    }

    public Instructor save(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    public InstructorOutputDTO update(Long instructorId, InstructorUpdateInputDTO instructorUpdateInputDTO) {
        return instructorRepository.findById(instructorId)
                .map(instructor -> {
                    User user = instructor.getUser();
                    user.setName(instructorUpdateInputDTO.getName());
                    instructor.setUser(user);
                    instructor.setLevel(Rank.valueOf(instructorUpdateInputDTO.getRank().toUpperCase()));
                    return instructorRepository.save(instructor);
                })
                .orElseThrow(() -> new InstructorNotFoundException(instructorId))
                .toDTO();
    }

    public void delete(String userName) {
        instructorRepository.deleteByUser_UserName(userName);

        User user = userService.getOne(userName);
        userService.removeRoleFromUser(user, Role.INSTRUCTOR);
        userService.save(user);
    }
}
