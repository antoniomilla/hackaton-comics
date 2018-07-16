
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Autor;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Integer> {

	@Query("select a from Autor a where a.id=?1")
	Autor findById(int id);

}
