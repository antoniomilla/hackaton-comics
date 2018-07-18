
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Author;

@Repository
public interface AutorRepository extends JpaRepository<Author, Integer> {

	@Query("select a from Autor a where a.id=?1")
	Author findById(int id);

}
