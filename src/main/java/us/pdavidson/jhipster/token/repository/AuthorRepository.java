package us.pdavidson.jhipster.token.repository;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import us.pdavidson.jhipster.token.domain.Author;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cassandra repository for the Author entity.
 */
@Repository
public class AuthorRepository {

    @Inject
    private Session session;

    private Mapper<Author> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(Author.class);
        findAllStmt = session.prepare("SELECT * FROM author");
        truncateStmt = session.prepare("TRUNCATE author");
    }

    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Author author = new Author();
                author.setId(row.getUUID("id"));
                author.setName(row.getString("name"));
                author.setBirthDate(row.getDate("birthDate"));
                return author;
            }
        ).forEach(authors::add);
        return authors;
    }

    public Author findOne(UUID id) {
        return mapper.get(id);
    }

    public void save(Author author) {
        if (author.getId() == null) {
            author.setId(UUID.randomUUID());
        }
        mapper.save(author);
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
