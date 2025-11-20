package com.puthi.commentapi.service;

import com.puthi.commentapi.dto.note.NoteCreateDto;
import com.puthi.commentapi.repository.NoteRepository;
import com.puthi.commentapi.entity.NoteEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito; //Mock objects
import java.util.Optional;

public class NoteServiceTest {
    @Test
    void create_maps_and_persists(){
        var repo = Mockito.mock(NoteRepository.class);
        var saved = new NoteEntity();
        saved.setId(123L);
        saved.setOwnerId(9L);
        saved.setTitle("T");

        Mockito.when(repo.save(any(NoteEntity.class))).thenReturn(saved);
        //when repo save with param noteEntity is called, then it should return the predefined saved object

        var service = new NoteService(repo);
        var dto = new NoteCreateDto("lol", "content");
        var result = service.create(9L, dto);

        assertThat(result.id()).isEqualTo(123L);
        assertThat(result.ownerId()).isEqualTo(9L);
        assertThat(result.title()).isEqualTo("T");
    }

    @Test
    void get_fetches_by_id(){
        var repo = Mockito.mock(NoteRepository.class);
        var found = new NoteEntity();
        found.setId(55L);
        found.setOwnerId(8L);
        found.setTitle("Found note");

        Mockito.when(repo.findById(55L)).thenReturn(Optional.of(found)); //Simulates an on success found

        var service = new NoteService(repo);
        var result = service.get(55L);

        assertThat(result.id()).isEqualTo(55L);
        assertThat(result.ownerId()).isEqualTo(8L);
        assertThat(result.title()).isEqualTo("Found note");
    }

    @Test
    void get_throws_when_not_found(){
        var repo = Mockito.mock(NoteRepository.class);

        Mockito.when(repo.findById(99L)).thenReturn(Optional.empty()); //Simulates not found

        var service = new NoteService(repo);

        assertThatThrownBy(() ->
                service.get(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Note not found: 99");
    }
}
