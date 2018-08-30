
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import domain.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

}
