
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AutorRepository;
import domain.Autor;

@Service
@Transactional
public class AutorService {

	@Autowired
	private AutorRepository	autorRepository;


	public AutorService() {
		super();
	}

	public Autor create() {
		final Autor res = new Autor();

		return res;
	}

	public Collection<Autor> findAll() {
		final Collection<Autor> res = this.autorRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public Autor findOne(final int Id) {
		final Autor res = this.autorRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public Autor save(final Autor autor) {
		Assert.notNull(autor);

		final Autor res = this.autorRepository.save(autor);

		return res;
	}

	public void delete(final Autor autor) {
		Assert.notNull(autor);
		Assert.isTrue(autor.getId() != 0);

		this.autorRepository.delete(autor);
	}

}
