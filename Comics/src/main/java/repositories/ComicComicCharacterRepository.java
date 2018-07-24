
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.ComicComicCharacter;

@Repository
public interface ComicComicCharacterRepository extends JpaRepository<ComicComicCharacter, Integer> {

	@Query("select ccc from ComicComicCharacter ccc where ccc.id=?1")
	ComicComicCharacter findById(int id);

}
