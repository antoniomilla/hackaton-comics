
package repositories;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import domain.Actor;
import domain.Author;
import domain.Comic;
import domain.User;
import domain.UserComic;
import domain.Volume;

@Repository
public interface VolumeRepository extends JpaRepository<Volume, Integer> {
    List<Volume> findByComicOrderByOrderNumberAscNameAsc(Comic comic);

    @Query("select v, uc from Volume v left join v.comic c left join c.userComics uc with uc.user.id = ?2 where v.author = ?1 order by v.releaseDate desc")
    List<Object[]> findVolumesAndUserComicsForAuthorAndUserOrderByReleaseDateDesc(Author author, Integer userId);
}
