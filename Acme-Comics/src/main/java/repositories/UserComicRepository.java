
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import domain.Comic;
import domain.User;
import domain.UserComic;
import domain.UserComicStatus;

@Repository
public interface UserComicRepository extends JpaRepository<UserComic, Integer> {
    UserComic findByUserAndComic(User user, Comic comic);

    @Query("select uc from UserComic uc where user = ?1 and comic in ?2")
    List<UserComic> findByUserAndComics(User user, List<Comic> comics);

    List<UserComic> findByUserAndStatus(User user, UserComicStatus status);
}
