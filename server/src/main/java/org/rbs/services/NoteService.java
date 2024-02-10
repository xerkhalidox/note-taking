package org.rbs.services;

import org.rbs.data.dto.NoteDto;
import org.rbs.data.entities.Note;
import org.rbs.data.mappers.NoteMapper;
import org.rbs.data.repositories.NoteRepository;
import org.rbs.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.rbs.data.constants.Errors.EMPTY_NOTE;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteMapper noteMapper;

    public List<NoteDto> getNotes(Pageable pageable) {
        try {
            return this.noteRepository.findAll(pageable)
                    .stream()
                    .map(this.noteMapper::mapToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public NoteDto getNoteById(final Long id) {
        try {
            final Note note = this.noteRepository.findById(id).orElse(null);
            return this.noteMapper.mapToDto(note);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void saveNote(NoteDto noteDto) {
        try {
            if (noteDto != null && noteDto.getText() != null && !noteDto.getText().isEmpty()) {
                final Note note = this.noteMapper.mapToEntity(noteDto);
                this.noteRepository.save(note);
            } else {
                throw new ApiRequestException(EMPTY_NOTE, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
