package com.puthi.commentapi.controller;


import com.puthi.commentapi.dto.NoteCreateDto;
import com.puthi.commentapi.dto.NoteResponseDto;
import com.puthi.commentapi.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Notes") //Swagger tag for grouping endpoints
@RestController
@RequestMapping("/api/notes") //Every method in this controller will have this base path
public class NoteController {
   private final NoteService noteService;

    public NoteController(NoteService noteService) {
         this.noteService = noteService;
    }

    @Operation(summary = "Create a new note", description = "Creates a new note for the authenticated user.")
    @PostMapping
    public ResponseEntity<NoteResponseDto> createNote(@Valid @RequestBody NoteCreateDto dto, @RequestParam Long ownerId /*Extract the owner id from the param*/) {
        var created = noteService.create(ownerId, dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Get a note by ID", description = "Retrieves a note by its ID.")
    @GetMapping("/{id}") //{id} is a path variable name must be the same in the method param in order to be bind
    public ResponseEntity<NoteResponseDto> getNoteById(@PathVariable Long id){
        var note = noteService.get(id);
        return ResponseEntity.ok(note);
    }

    @Operation(summary = "List notes by owner (paged)")
    @GetMapping
    public Page<NoteResponseDto> list(@RequestParam Long ownerId, //?ownerId=123
                                  @RequestParam(defaultValue = "0") int page, //?page=0
                                  @RequestParam(defaultValue = "10") int size) { //?size=10
        //full url example: /api/notes?ownerId=123&page=0&size=10
        return noteService.listByOwnerId(ownerId, PageRequest.of(page, size));
    }

}
