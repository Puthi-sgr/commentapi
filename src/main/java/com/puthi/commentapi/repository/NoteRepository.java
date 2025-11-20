package com.puthi.commentapi.repository;

import com.puthi.commentapi.entity.NoteEntity; //Orm stuff
import org.springframework.data.jpa.repository.JpaRepository; //Provide built in crud operations
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteRepository extends JpaRepository<NoteEntity, Long> /*<Entity type and its id>*/ {
    Page<NoteEntity> findAll(Pageable pageable); //Built in method from JpaRepository for pagination
    Page<NoteEntity> findByOwnerIdOrderByIdDesc(Long ownerId, Pageable pageable); //Pageable for pagination LIMIT(Size) and OFFSET (starting point)
    //Spring uses method name to generate query, the method name follow from the entity field names
}
