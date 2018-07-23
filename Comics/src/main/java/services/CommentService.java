
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import domain.Comment;
import domain.User;

@Service
@Transactional
public class CommentService {

	@Autowired
	private CommentRepository	commentRepository;
	@Autowired
	private UserService			userService;


	public CommentService() {
		super();
	}

	public Comment create() {
		final Comment res = new Comment();
		final User user = this.userService.findByPrincipal();
		res.setUser(user);

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
		final Date now = new Date();
		comment.setCreationTime(now);

		final Comment res = this.commentRepository.save(comment);

		return res;
	}

	public void delete(final Comment comment) {
		Assert.notNull(comment);
		Assert.isTrue(comment.getId() != 0);

		this.commentRepository.delete(comment);
	}

}
