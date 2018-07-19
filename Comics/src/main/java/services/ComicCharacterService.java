
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ComicCharacterRepository;
import domain.ComicCharacter;

@Service
@Transactional
public class ComicCharacterService {

	@Autowired
	private ComicCharacterRepository	comicCharacterRepository;


	public ComicCharacterService() {
		super();
	}

	public ComicCharacter create() {
		final ComicCharacter res = new ComicCharacter();

		return res;
	}

	public Collection<ComicCharacter> findAll() {
		final Collection<ComicCharacter> res = this.comicCharacterRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public ComicCharacter findOne(final int Id) {
		final ComicCharacter res = this.comicCharacterRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public ComicCharacter save(final ComicCharacter comicCharacter) {
		Assert.notNull(comicCharacter);

		final ComicCharacter res = this.comicCharacterRepository.save(comicCharacter);

		return res;
	}

	public void delete(final ComicCharacter comicCharacter) {
		Assert.notNull(comicCharacter);
		Assert.isTrue(comicCharacter.getId() != 0);

		this.comicCharacterRepository.delete(comicCharacter);
	}

}
