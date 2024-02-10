package org.rbs.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rbs.ClientApplication;
import org.rbs.ServerApplication;
import org.rbs.data.dto.NoteDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static org.rbs.data.constants.Apis.*;

@SpringBootTest
@ContextConfiguration(classes = ClientApplication.class)
public class NoteTakingServerTest {
    @Value("${server.base.url}")
    protected String baseUrl;

    @Test
    public void saveNotes_printsSuccessToConsole() {
        try {
            List<NoteDto> notesToBeSaved = new ArrayList<>();
            final var note1 = this.getNoteDto("test saving note 1");
            final var note2 = this.getNoteDto("test saving note 2");
            notesToBeSaved.add(note1);
            notesToBeSaved.add(note2);

            notesToBeSaved.forEach(n -> WebTestClient
                    .bindToServer()
                    .baseUrl(this.baseUrl)
                    .build()
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path(API_V1 + NOTE)
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(n)
                    .exchange()
                    .expectStatus().isCreated()
            );
            System.out.println("Success");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void getNotes_returnsAndPrintsNotesToConsole() {
        try {
            var notes = WebTestClient
                    .bindToServer()
                    .baseUrl(this.baseUrl)
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(API_V1 + NOTE)
                            .queryParam("page", 0)
                            .build())
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().valueEquals("Content-Type", "application/json")
                    .expectBodyList(NoteDto.class)
                    .returnResult()
                    .getResponseBody();
            processNotes(notes);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void processNotes(final List<NoteDto> notes) {
        if (notes != null) {
            notes.forEach(n -> {
                System.out.println(getNotePrintingText(n));
            });
        }
    }

    private NoteDto getNoteDto(String text) {
        NoteDto noteDto = new NoteDto();
        noteDto.setText(text);
        return noteDto;
    }

    private String getNotePrintingText(NoteDto noteDto) {
        return String.format("Note #%d: %s", noteDto.getId(), noteDto.getText());
    }
}
