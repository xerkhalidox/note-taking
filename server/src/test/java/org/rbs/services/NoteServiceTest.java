package org.rbs.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rbs.data.dto.NoteDto;
import org.rbs.data.entities.Note;
import org.rbs.data.mappers.NoteMapper;
import org.rbs.data.repositories.NoteRepository;
import org.rbs.services.NoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.rbs.data.constants.Errors.EMPTY_NOTE;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {
    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NoteMapper noteMapper;

    @InjectMocks
    private NoteService noteService;

    @Test
    public void getNotes_emptyNotes_returnsEmptyList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Note> notesPage = Page.empty();

        when(this.noteRepository.findAll(pageable)).thenReturn(notesPage);
        List<NoteDto> emptyNoteList =  this.noteService.getNotes(pageable);

        Assertions.assertEquals(emptyNoteList.size(), notesPage.toList().size());
    }

    @Test
    public void getNotes_withNotesStored_returnsStoredNotes() {
        Pageable pageable = PageRequest.of(0, 10);
        Note note = this.getNote("test note");
        List<Note> noteList = new ArrayList<>();
        noteList.add(note);
        Page<Note> notePage = new PageImpl<>(noteList);

        NoteDto noteDto = this.getNoteDto("test note");

        when(this.noteRepository.findAll(pageable)).thenReturn(notePage);
        when(this.noteMapper.mapToDto(note)).thenReturn(noteDto);
        List<NoteDto> noteDtos = this.noteService.getNotes(pageable);

        Assertions.assertEquals(noteDtos.size(), notePage.toList().size());
        Assertions.assertEquals(noteDtos.get(0).getText(), notePage.toList().get(0).getText());
    }

    @Test
    public void getNoteById_gettingSavedNote_returnsSavedNoteWithThatId() {
        final String noteText = "test note";
        final Note note = this.getNote(noteText);
        final NoteDto noteDto = this.getNoteDto(noteText);

        when(this.noteRepository.findById(note.getId())).thenReturn(Optional.of(note));
        when(this.noteMapper.mapToDto(note)).thenReturn(noteDto);
        final NoteDto noteDtoById = this.noteService.getNoteById(note.getId());

        Assertions.assertEquals(noteDtoById.getId(), noteDto.getId());
    }

    @Test
    public void getNoteById_gettingNonExistNote_returnsNull() {
        when(this.noteRepository.findById(anyLong())).thenReturn(Optional.empty());
        final NoteDto noteDtoById = this.noteService.getNoteById(1L);

        Assertions.assertNull(noteDtoById);
    }

    @Test
    public void saveNote_emptyNoteText_throwsException() {
        NoteDto noteDto = this.getNoteDto("");
        Exception exception = Assertions.assertThrows(Exception.class, () -> this.noteService.saveNote(noteDto));
        Assertions.assertEquals(exception.getMessage(), EMPTY_NOTE);
    }

    @Test
    public void saveNote_validNoteText_savesNote() {
        final String noteText = "test saving note";
        final NoteDto noteDto = this.getNoteDto(noteText);
        final Note note = this.getNote(noteText);
        final ArgumentCaptor<Note> argumentCaptor = ArgumentCaptor.forClass(Note.class);

        when(this.noteMapper.mapToEntity(noteDto)).thenReturn(note);
        this.noteService.saveNote(noteDto);

        verify(this.noteRepository, times(1)).save(argumentCaptor.capture());
        Assertions.assertEquals(argumentCaptor.getValue().getText(), noteText);
    }

    private Note getNote(String text) {
        Note note = new Note();
        note.setId(1L);
        note.setText(text);
        return note;
    }

    private NoteDto getNoteDto(String text) {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(1L);
        noteDto.setText(text);
        return noteDto;
    }
}
