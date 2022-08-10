package ir.sobhan.lms.controller;

import ir.sobhan.lms.model.dto.inputdto.TermInputDTO;
import ir.sobhan.lms.model.dto.outputdto.TermOutputDTO;
import ir.sobhan.lms.service.TermService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
public class TermController {

    private final TermService termService;

    @GetMapping()
    public List<TermOutputDTO> all(@RequestParam int size, @RequestParam int page) {
        return termService.getAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public TermOutputDTO one(@PathVariable Long id) {
        return termService.getOne(id).toDTO();
    }

    @PostMapping("/new-term")
    public ResponseEntity<?> newTerm(@RequestBody TermInputDTO termInputDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(termService.save(termInputDTO.toEntity()));
    }

    @PutMapping("/update-term/{termId}")
    public ResponseEntity<?> updateTerm(@PathVariable Long termId,
                                        @RequestBody TermInputDTO termInputDTO) {
        return ResponseEntity
                .ok(termService.update(termId, termInputDTO));
    }

    @DeleteMapping("/delete-term/{id}")
    @Transactional
    public ResponseEntity<?> deleteTerm(@PathVariable Long id) {
        termService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
