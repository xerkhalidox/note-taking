package org.rbs.data.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.rbs.data.dto.NoteDto;
import org.rbs.data.entities.Note;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    NoteDto mapToDto(Note note);

    Note mapToEntity(NoteDto noteDto);
}
