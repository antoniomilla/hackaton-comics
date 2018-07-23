
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import domain.Comic;
import domain.Comment;
import domain.User;

@Service
@Transactional
public class CommentService {

	@Autowired
	private CommentRepository	commentRepository;
	@Autowired
	private UserService			userService;
	@Autowired
	private ComicService		comicService;


	public CommentService() {
		super();
	}

	public Comment create(final Integer comicId) {
		final Comment res = new Comment();
		final User user = this.userService.findByPrincipal();
		res.setUser(user);
		final Comic comic = this.comicService.findOne(comicId);
		res.setComic(comic);
		final Date now = new Date();
		res.setCreationTime(now);
		Assert.notNull(res.getUser());
		Assert.notNull(res.getComic());
		res.getUser().getUserComments().add(res);
		res.getComic().getComments().add(res);

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
		Assert.notNull(comment);

		final Comment res = this.commentRepository.save(comment);

		return res;
	}

	public void delete(final Comment comment) {
		Assert.notNull(comment);
		Assert.isTrue(comment.getId() != 0);

		this.commentRepository.delete(comment);
	}

}
