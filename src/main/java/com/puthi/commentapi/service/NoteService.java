package com.puthi.commentapi.service;


import com.puthi.commentapi.dto.NoteCreateDto;
import com.puthi.commentapi.dto.NoteResponseDto;
import com.puthi.commentapi.entity.NoteEntity;
import com.puthi.commentapi.repository.NoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    private final NoteRepository repo;

    public NoteService(NoteRepository repo){
        this.repo = repo;
    }

    public NoteResponseDto create(Long ownerId, NoteCreateDto dto){
        var e = new NoteEntity();
        e.setOwnerId(ownerId);
        e.setTitle(dto.title());
        e.setContent(dto.content());

        var saved = repo.save(e);
        return toRead(saved);
    }

    public NoteResponseDto get(Long id) {
        var e = repo.findById(id).orElseThrow(() -> new RuntimeException("Note not found: " + id));
        return toRead(e);
    }

    public Page<NoteResponseDto> listByOwnerId(Long ownerId, Pageable pageable) {
        return repo.findByOwnerIdOrderByIdDesc(ownerId, pageable).map(this::toRead);
    }

    private NoteResponseDto toRead(NoteEntity e) {
        return new NoteResponseDto(e.getId(), e.getTitle(), e.getContent(), e.getOwnerId(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
