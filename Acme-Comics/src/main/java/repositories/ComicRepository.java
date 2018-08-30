
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Author;
import domain.Comic;
import domain.ComicCharacter;
import domain.Publisher;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Integer> {
	@Query("select c, uc from Comic c left join c.userComics uc with uc.user.id = ?1")
	List<Object[]> findComicsAndUserComicsForUser(Integer userId);

	@Query("select c, uc from Comic c left join c.userComics uc with uc.user.id = ?2 where c.author = ?1 order by c.name asc")
	List<Object[]> findComicsAndUserComicsForAuthorAndUserOrderByNameAsc(Author author, Integer userId);

	@Query("select c, uc from Comic c left join c.userComics uc with uc.user.id = ?2 where c.publisher = ?1 order by c.name asc")
	List<Object[]> findComicsAndUserComicsForPublisherAndUserOrderByNameAsc(Publisher publisher, Integer userId);
}
