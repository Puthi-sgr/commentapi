package com.puthi.commentapi.repository;

import com.puthi.commentapi.entity.NoteEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired; //Automatically wires dependencies in Spring (Fake objects for testing)
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest //This annotation tells spring that it's a JPA test and spring will allocate in-memory database, IOC containers, and other features needed for testing JPA repositories
@TestPropertySource(properties = {
        "spring.flyway.enabled=false"
})
class NoteRepositoryTest {

    @Autowired //Will inject the NoteRepository bean into this test class
    NoteRepository repo;

    @Test
    void save_and_find_by_owner(){
        var n1 = new NoteEntity();
        n1.setOwnerId(7L);
        n1.setTitle("Note 1");
        n1.setContent("Content 1");
        repo.save(n1); //Saves the note to the in-memory database

        var n2 = new NoteEntity();
        n2.setOwnerId(7L);
        n2.setTitle("Note 2");
        n2.setContent("Content 2");
        repo.save(n2);

        var page = repo.findByOwnerIdOrderByIdDesc(7L, PageRequest.of(0, 10)); //Get first page with 10 items
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent().get(0).getTitle()).isEqualTo("Note 2"); //Check order desc
    }
}
