package com.puthi.commentapi.repository;


import com.puthi.commentapi.entity.CommentEntity; //Orm stuff
import com.puthi.commentapi.dto.comment.CommentReadDto;
import org.springframework.data.jpa.repository.JpaRepository; //Provide built in crud operations
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//The  real implementation will be created during runtime by Spring Data JPA
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    //Reason we use note.id because note is an entity inside comment entity
    // last root under a note (parent IS NULL)
    // query the the last comment to
    //We use this for insertion operation
    @Query("""
           SELECT c
           FROM CommentEntity c
           WHERE c.note.id = :noteId
             AND c.parent.id IS NULL
           ORDER BY c.path DESC
           """)
    CommentEntity findTopByNoteIdAndParentIsNullOrderByPathDesc(@Param("noteId") Long noteId);
    //Name of this method only represents the jpql

    // last child under a parent
    /*
        |   path             |
        |--------------------|
        |0001/0002/0003/0004| <- last child used to build path for next reply
        |0001/0002/0003     |
        |0001/0002         |
        |0001             |
    * */
    //This is use for insertion operation
    @Query("""
           SELECT c
           FROM CommentEntity c
           WHERE c.note.id = :noteId
             AND c.parent.id = :parentId
           ORDER BY c.path DESC
           """)
    CommentEntity findTopByNoteIdAndParentIdOrderByPathDesc(@Param("noteId") Long noteId,
                                                            @Param("parentId") Long parentId);

    @Query("""
           SELECT new com.puthi.commentapi.dto.comment.CommentReadDto(
               c.id,
               c.note.id,
               c.parent.id,
               c.content,
               c.authorId,
               null,
               c.createdAt,
               c.updatedAt,
               COUNT(r.id)
           )
           FROM CommentEntity c
           LEFT JOIN CommentEntity r ON r.parent.id = c.id
           WHERE c.id = :id
           GROUP BY c.id, c.note.id, c.parent.id, c.content,
                    c.authorId, c.createdAt, c.updatedAt
           """) //The query for the findCommentReadDtoById method
    CommentReadDto findCommentReadDtoById(@Param("id") Long id); //Name of this method only represents the jpql query above
    //Find comments by note id only parent comment
    Page<CommentEntity> findByNote_IdAndParent_IdIsNullOrderByCreatedAtDesc(Long noteId, Pageable pageable);
    //Find comment by parent comment id And the correct note id
    //Smaller sub comments inside a comment
    Page<CommentEntity> findByNote_IdAndParent_IdOrderByCreatedAtDesc(Long noteId, Long parentId, Pageable pageable);
    //param in this repo is used to bind the value -> :id

}
