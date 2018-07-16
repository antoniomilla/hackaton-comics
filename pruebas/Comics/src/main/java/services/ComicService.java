
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ComicRepository;
import domain.Comic;

@Service
@Transactional
public class ComicService {

	@Autowired
	private ComicRepository	comicRepository;


	public ComicService() {
		super();
	}

	public Comic create() {
		final Comic res = new Comic();

		return res;
	}

	public Collection<Comic> findAll() {
		final Collection<Comic> res = this.comicRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public Comic findOne(final int Id) {
		final Comic res = this.comicRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public Comic save(final Comic comic) {
		Assert.notNull(comic);

		final Comic res = this.comicRepository.save(comic);

		return res;
	}

	public void delete(final Comic comic) {
		Assert.notNull(comic);
		Assert.isTrue(comic.getId() != 0);

		this.comicRepository.delete(comic);
	}

}
