
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.ComicCharacter;

@Repository
public interface PersonajeRepository extends JpaRepository<ComicCharacter, Integer> {

	@Query("select p from ComicCharacter p where p.id=?1")
	ComicCharacter findById(int id);

}
