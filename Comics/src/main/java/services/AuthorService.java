
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AuthorRepository;
import domain.Author;

@Service
@Transactional
public class AuthorService {

	@Autowired
	private AuthorRepository	authorRepository;


	public AuthorService() {
		super();
	}

	public Author create() {
		final Author res = new Author();

		return res;
	}

	public Collection<Author> findAll() {
		final Collection<Author> res = this.authorRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public Author findOne(final int Id) {
		final Author res = this.authorRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public Author save(final Author author) {
		Assert.notNull(author);

		final Author res = this.authorRepository.save(author);

		return res;
	}

	public void delete(final Author author) {
		Assert.notNull(author);
		Assert.isTrue(author.getId() != 0);

		this.authorRepository.delete(author);
	}

}
