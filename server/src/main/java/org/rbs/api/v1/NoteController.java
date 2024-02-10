package org.rbs.api.v1;

import org.rbs.data.dto.NoteDto;
import org.rbs.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.rbs.data.constants.Apis.*;

@RestController
public class NoteController extends AbstractController {
    @Autowired
    private NoteService noteService;

    @GetMapping(NOTE)
    public ResponseEntity<List<NoteDto>> getAllNotes(@RequestParam(defaultValue = "0") int page) {
        System.out.println("Test");
        Pageable pageable = PageRequest.of(page, DEFAULT_SIZE);
        final List<NoteDto> notesDto = this.noteService.getNotes(pageable);
        return new ResponseEntity<>(notesDto, HttpStatus.OK);
    }

    @GetMapping(NOTE_ID)
    public ResponseEntity<NoteDto> getNoteById(@PathVariable final Long id) {
        final NoteDto noteDto = this.noteService.getNoteById(id);
        return new ResponseEntity<>(noteDto, HttpStatus.OK);
    }

    @PostMapping(NOTE)
    public ResponseEntity<Void> saveNote(@RequestBody NoteDto noteDto)  {
        this.noteService.saveNote(noteDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
