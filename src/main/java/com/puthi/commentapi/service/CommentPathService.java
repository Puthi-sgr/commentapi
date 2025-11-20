package com.puthi.commentapi.service;

import org.springframework.stereotype.Service;
import com.puthi.commentapi.entity.CommentEntity;
import com.puthi.commentapi.entity.NoteEntity;
import com.puthi.commentapi.repository.CommentRepository;
import com.puthi.commentapi.repository.NoteRepository;

@Service
public class CommentPathService {

    private static final int SEGMENT_WIDTH = 4;      // 0001, 0002, ...
    private static final String SEGMENT_FORMAT = "%0" + SEGMENT_WIDTH + "d"; //%04d
    private static final String SEPARATOR = "/";

    private final CommentRepository commentRepository;
    private final NoteRepository noteRepository;

    public CommentPathService(CommentRepository commentRepository, NoteRepository noteRepository) {
        this.commentRepository = commentRepository;
        this.noteRepository = noteRepository;
    }

    public CommentEntity createComment(Long noteId,
                                       Long parentId,   // null for root
                                       String content
                                       /*Long authorId*/) {

        NoteEntity note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found: " + noteId));

        CommentEntity comment = new CommentEntity();
        comment.setNote(note);
        comment.setContent(content);
        //comment.setAuthorId(authorId);

        if (parentId == null) {
            // root comment
            comment.setParent(null);
            comment.setDepth(0);
            String segment = nextRootSegment(noteId); //Method will resolve to "0001"
            comment.setPath(SEPARATOR + segment + SEPARATOR); // will be "/0001/"
        }else{
            // reply

            //Ensure the parent exists
            CommentEntity parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("Parent not found: " + parentId));

            //Ensure the parents noteId matches the provided noteId queried above
            if (!parent.getNote().getId().equals(noteId)) {
                throw new IllegalArgumentException("Parent belongs to another note");
            }

            comment.setParent(parent); //Comment have parent field
            comment.setDepth(parent.getDepth() + 1); //eg. 1(parent) + 1 = 2

            String segment = nextChildSegment(noteId, parentId);
            comment.setPath(parent.getPath() + segment + SEPARATOR); // "/0001/0003/"
        }

        return commentRepository.save(comment);
    }

    //This method focuses on the root comments only
    private String nextRootSegment(Long noteId) {
        CommentEntity lastRoot =
                commentRepository.findTopByNoteIdAndParentIsNullOrderByPathDesc(noteId);
        int nextNumber = extractNextNumber(lastRoot); //This will only return 1 cuz the last root is null
        return String.format(SEGMENT_FORMAT, nextNumber); //eg "%04d", 1 -> "0001"
    }

    //This method focuses on the child comments only (Replies)
    private String nextChildSegment(Long noteId, Long parentId) {
        CommentEntity lastChild =
                commentRepository.findTopByNoteIdAndParentIdOrderByPathDesc(noteId, parentId);
        int nextNumber = extractNextNumber(lastChild);
        return String.format(SEGMENT_FORMAT, nextNumber);
    }

    private int extractNextNumber(CommentEntity lastSibling) {

        //Check if it is the first comment
        if (lastSibling == null || lastSibling.getPath() == null) {
            return 1; // first comment
            //will look like /0001/
        }

        String path = lastSibling.getPath();          // e.g. "/0001/0003/"
        if (path.endsWith(SEPARATOR)) { //Does the path end with a "/"
            //cleans the path by removing the last "/"
            path = path.substring(0, path.length() - 1); // "/0001/0003"
        }
        int idx = path.lastIndexOf(SEPARATOR);        // index of last "/"
        String lastSegment = (idx >= 0) ? path.substring(idx + 1) : path; // "0003"

        try {
            //Parse the last segment("0003" -> 3)to an integer and increment by 1
            int current = Integer.parseInt(lastSegment);
            return current + 1;
        } catch (NumberFormatException ex) {
            // fallback if path is corrupted
            return 1;
        }
    }
}
