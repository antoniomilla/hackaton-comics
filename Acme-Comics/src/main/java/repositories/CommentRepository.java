
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import domain.Author;
import domain.Comic;
import domain.ComicCharacter;
import domain.Comment;
import domain.Publisher;
import domain.Sale;
import domain.User;
import domain.Volume;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByVolumeOrderByCreationTimeDesc(Volume volume);
    List<Comment> findByComicOrderByCreationTimeDesc(Comic comic);
    List<Comment> findByPublisherOrderByCreationTimeDesc(Publisher publisher);
    List<Comment> findByAuthorOrderByCreationTimeDesc(Author author);
    List<Comment> findByComicCharacterOrderByCreationTimeDesc(ComicCharacter comicCharacter);

    List<Comment> findByUserOrderByCreationTimeDesc(User user);

    List<Comment> findBySaleOrderByCreationTimeDesc(Sale sale);
}
