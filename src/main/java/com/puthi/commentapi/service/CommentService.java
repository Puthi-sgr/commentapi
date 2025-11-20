package com.puthi.commentapi.service;

import com.puthi.commentapi.controller.CommentController;
import com.puthi.commentapi.dto.comment.CommentCreateDto;
import com.puthi.commentapi.dto.comment.CommentReadDto;
import com.puthi.commentapi.entity.CommentEntity;
import com.puthi.commentapi.entity.NoteEntity;
import com.puthi.commentapi.repository.CommentRepository;
import com.puthi.commentapi.repository.NoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepo;
    private final NoteRepository noteRepo;
    private final CommentPathService commentPathService;

    public CommentService(CommentRepository commentRepo, NoteRepository noteRepo, CommentPathService commentPathService) {
        this.commentRepo = commentRepo;
        this.noteRepo = noteRepo;
        this.commentPathService = commentPathService;
    }

    public CommentReadDto create(CommentCreateDto dto, Long noteId, Long parentId){
        //we need to extract user aswell in the future
        //e.setAuthorId(dto.authorId());
        //Check if parent comment exists if parentId is provided
        CommentEntity parent = null;
        if(parentId != null){
            parent = commentRepo.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent comment not found: " + parentId));
        }
        //also check if note exits
        NoteEntity note = noteRepo.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found: " + noteId));

        var e = new CommentEntity();
        e = commentPathService.createComment(
                note.getId(),
                parent != null ? parent.getId() : null,
                dto.content());

        return toRead(commentRepo.save(e));
    }

    public CommentReadDto get(Long id){
        var e = commentRepo.findById(id).orElseThrow(() -> new RuntimeException("Comment not found: " + id));
        return toRead(e);
    }

    public Page<CommentReadDto> getTopLevel(Long noteId, Pageable pageable){
        return commentRepo.findByNote_IdAndParent_IdIsNullOrderByCreatedAtDesc(noteId, pageable)
                .map(this::toRead); //Use map cuz its a page
    }

    public Page<CommentReadDto> getReplies(Long noteId, Long parentId, Pageable pageable){

        return commentRepo.findByNote_IdAndParent_IdOrderByCreatedAtDesc(noteId, parentId, pageable)
                .map(this::toRead);
    }
    public CommentReadDto toRead(CommentEntity e){
        return commentRepo.findCommentReadDtoById(e.getId());
    }
}
