package com.puthi.commentapi.controller;

import com.puthi.commentapi.dto.comment.CommentReadDto;
import com.puthi.commentapi.dto.comment.CommentCreateDto;
import com.puthi.commentapi.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comments")
@RestController
@RequestMapping("/api/notes/{noteId}/comments") //Just the comments from a specific note
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Get top-level comments for a note (paged)")
    @GetMapping("/top-level")
    public Page<CommentReadDto> getTopLevelComments(
            @PathVariable Long noteId, //Grab the note id (Identity)
            @RequestParam(defaultValue = "0") int page, //?page=0 //Optional type shit
            @RequestParam(defaultValue = "10") int size) //?size=10 //Optional, a way to query the amount
    {
        return commentService.getTopLevel(noteId, PageRequest.of(page, size));
    }

    @Operation(summary = "Get a specific comment by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CommentReadDto> getCommentById(@PathVariable Long id) {
        var comment = commentService.get(id);
        return ResponseEntity.ok(comment);
    }

    @Operation(summary = "Get replies for a specific comment (paged)")
    @GetMapping("/{parentId}/replies") //a particular parent comment's reply
    public Page<CommentReadDto> getReplies(
            @PathVariable Long noteId,
            @PathVariable Long parentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return commentService.getReplies(noteId, parentId, PageRequest.of(page, size));
    }

    @Operation(summary = "Create a new comment")
    @PostMapping("/create")
    public ResponseEntity<CommentReadDto> createComment(
            @Valid @RequestBody CommentCreateDto dto,
            @PathVariable Long noteId
            ){
        //In a real app, you would extract the authorId from the authenticated user context
        var created = commentService.create(dto, noteId, null);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Create a reply to a specific comment")
    @PostMapping("/{parentId}/reply")
    public ResponseEntity<CommentReadDto> createReply(
            @Valid @RequestBody CommentCreateDto dto,
            @PathVariable Long noteId,
            @PathVariable Long parentId
    ) {
        var created = commentService.create(dto, noteId, parentId);
        return ResponseEntity.ok(created);
    }
}
