
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.ComicCharacter;

@Repository
public interface ComicCharacterRepository extends JpaRepository<ComicCharacter, Integer> {

	@Query("select cc from ComicCharacter cc where cc.id=?1")
	ComicCharacter findById(int id);

}
