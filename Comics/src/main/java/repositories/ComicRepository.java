
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comic;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Integer> {

	@Query("select c from Comic c where c.id=?1")
	Comic findById(int id);

	@Query("select c from Comic c where c.publisher.name=?1")
	Collection<Comic> getComicsByPublisher(String name);

}
