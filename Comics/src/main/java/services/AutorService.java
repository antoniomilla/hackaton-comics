
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AutorRepository;
import domain.Author;

@Service
@Transactional
public class AutorService {

	@Autowired
	private AutorRepository	autorRepository;


	public AutorService() {
		super();
	}

	public Author create() {
		final Author res = new Author();

		return res;
	}

	public Collection<Author> findAll() {
		final Collection<Author> res = this.autorRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public Author findOne(final int Id) {
		final Author res = this.autorRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public Author save(final Author autor) {
		Assert.notNull(autor);

		final Author res = this.autorRepository.save(autor);

		return res;
	}

	public void delete(final Author autor) {
		Assert.notNull(autor);
		Assert.isTrue(autor.getId() != 0);

		this.autorRepository.delete(autor);
	}

}
