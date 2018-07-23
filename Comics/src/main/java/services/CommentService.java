
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import domain.Author;
import domain.Comic;
import domain.ComicCharacter;
import domain.Comment;
import domain.Publisher;
import domain.User;
import domain.Volume;

@Service
@Transactional
public class CommentService {

	@Autowired
	private CommentRepository		commentRepository;
	@Autowired
	private UserService				userService;
	@Autowired
	private ComicService			comicService;
	@Autowired
	private VolumeService			volumeService;
	@Autowired
	private ComicCharacterService	comicCharacterService;
	@Autowired
	private AuthorService			authorService;
	@Autowired
	private PublisherService		publisherService;


	public CommentService() {
		super();
	}

	public Comment createComic(final Integer comicId) {
		final Comment res = new Comment();

		final User user = this.userService.findByPrincipal();
		res.setUser(user);
		final Comic comic = this.comicService.findOne(comicId);
		res.setComic(comic);
		final Date now = new Date();
		res.setCreationTime(now);
		Assert.notNull(res.getUser());
		Assert.notNull(res.getComic());

		return res;
	}

	public Comment createComicCharacter(final Integer comicCharacterId) {
		final Comment res = new Comment();

		final User user = this.userService.findByPrincipal();
		res.setUser(user);
		final ComicCharacter comicCharacter = this.comicCharacterService.findOne(comicCharacterId);
		res.setComicCharacter(comicCharacter);
		final Date now = new Date();
		res.setCreationTime(now);
		Assert.notNull(res.getUser());
		Assert.notNull(res.getComicCharacter());

		return res;
	}

	public Comment createAuthor(final Integer authorId) {
		final Comment res = new Comment();

		final User user = this.userService.findByPrincipal();
		res.setUser(user);
		final Author author = this.authorService.findOne(authorId);
		res.setAuthor(author);
		final Date now = new Date();
		res.setCreationTime(now);
		Assert.notNull(res.getUser());
		Assert.notNull(res.getAuthor());

		return res;
	}

	public Comment createPublisher(final Integer publisherId) {
		final Comment res = new Comment();

		final User user = this.userService.findByPrincipal();
		res.setUser(user);
		final Publisher publisher = this.publisherService.findOne(publisherId);
		res.setPublisher(publisher);
		final Date now = new Date();
		res.setCreationTime(now);
		Assert.notNull(res.getUser());
		Assert.notNull(res.getPublisher());

		return res;
	}

	public Comment createVolume(final Integer volumeId) {
		final Comment res = new Comment();

		final User user = this.userService.findByPrincipal();
		res.setUser(user);
		final Volume volume = this.volumeService.findOne(volumeId);
		res.setVolume(volume);
		final Date now = new Date();
		res.setCreationTime(now);
		Assert.notNull(res.getUser());
		Assert.notNull(res.getVolume());

		return res;
	}

	public Collection<Comment> findAll() {
		final Collection<Comment> res = this.commentRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public Comment findOne(final int Id) {
		final Comment res = this.commentRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public Comment save(final Comment comment) {

		final Comment res = this.commentRepository.save(comment);
		res.getUser().getUserComments().add(res);

		if (res.getComic() != null)
			res.getComic().getComments().add(res);
		if (res.getPublisher() != null)
			res.getPublisher().getComments().add(res);
		if (res.getAuthor() != null)
			res.getAuthor().getComments().add(res);
		if (res.getComicCharacter() != null)
			res.getComicCharacter().getComments().add(res);
		if (res.getVolume() != null)
			res.getVolume().getComments().add(res);

		Assert.notNull(comment);
		return res;
	}

	public void delete(final Comment comment) {
		Assert.notNull(comment);
		Assert.isTrue(comment.getId() != 0);

		this.commentRepository.delete(comment);
	}

}
