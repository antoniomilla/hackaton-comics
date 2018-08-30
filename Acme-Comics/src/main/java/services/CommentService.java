
package services;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Author;
import domain.Comic;
import domain.ComicCharacter;
import domain.Publisher;
import domain.Sale;
import domain.User;
import domain.Volume;
import exceptions.ResourceNotFoundException;
import repositories.CommentRepository;
import domain.Comment;
import security.Authority;
import utilities.CheckUtils;

@Service
@Transactional
public class CommentService {

	@Autowired private CommentRepository repository;
	@Autowired private ActorService actorService;

	public Comment findById(int id)
	{
		return repository.findOne(id);
	}

	public Comment getById(int id)
	{
		Comment comment = findById(id);
		if (comment == null) throw new ResourceNotFoundException();
		return comment;
	}

	public void delete(int id)
	{
		CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);
		repository.delete(getById(id));
	}

	public Comment create(Comment comment)
	{
		CheckUtils.checkPrincipalAuthority(Authority.USER);
		CheckUtils.checkEquals(actorService.findPrincipal(), comment.getUser());
		CheckUtils.checkNotExists(comment);

		comment.setCreationTime(new Date());

		return repository.save(comment);
	}

	public List<Comment> findForListInComic(Comic comic)
	{
		return repository.findByComicOrderByCreationTimeDesc(comic);
	}

	public List<Comment> findForListInVolume(Volume volume)
	{
		return repository.findByVolumeOrderByCreationTimeDesc(volume);
	}

	public List<Comment> findForListInPublisher(Publisher publisher)
	{
		return repository.findByPublisherOrderByCreationTimeDesc(publisher);
	}

	public List<Comment> findForListInAuthor(Author author)
	{
		return repository.findByAuthorOrderByCreationTimeDesc(author);
	}

	public List<Comment> findForListInComicCharacter(ComicCharacter comicCharacter)
	{
		return repository.findByComicCharacterOrderByCreationTimeDesc(comicCharacter);
	}

	public List<Comment> findForListInUser(User user)
	{
		return repository.findByUserOrderByCreationTimeDesc(user);
	}

	public List<Comment> findForListInSale(Sale sale)
	{
		return repository.findBySaleOrderByCreationTimeDesc(sale);
	}
}
