
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import domain.Comic;
import domain.ComicCharacter;
import domain.ComicComicCharacter;

@Repository
public interface ComicComicCharacterRepository extends JpaRepository<ComicComicCharacter, Integer> {
    ComicComicCharacter findByComicAndComicCharacter(Comic comic, ComicCharacter comicCharacter);

    @Query("select ccc, uc from ComicComicCharacter ccc left join ccc.comic c left join c.userComics uc with uc.user.id = ?2 where ccc.comicCharacter = ?1 order by c.name asc")
    List<Object[]> findComicComicCharactersAndUserComicsForComicCharacterAndUserOrderByNameAsc(ComicCharacter comicCharacter, Integer userId);
}
