
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ComicComicCharacterRepository;
import domain.ComicComicCharacter;

@Service
@Transactional
public class ComicComicCharacterService {

	@Autowired
	private ComicComicCharacterRepository	comicComicCharacterRepository;


	public ComicComicCharacterService() {
		super();
	}

	public ComicComicCharacter create() {
		final ComicComicCharacter res = new ComicComicCharacter();

		return res;
	}

	public Collection<ComicComicCharacter> findAll() {
		final Collection<ComicComicCharacter> res = this.comicComicCharacterRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public ComicComicCharacter findOne(final int Id) {
		final ComicComicCharacter res = this.comicComicCharacterRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public ComicComicCharacter save(final ComicComicCharacter comicComicCharacter) {
		Assert.notNull(comicComicCharacter);

		final ComicComicCharacter res = this.comicComicCharacterRepository.save(comicComicCharacter);

		return res;
	}

	public void delete(final ComicComicCharacter comicComicCharacter) {
		Assert.notNull(comicComicCharacter);
		Assert.isTrue(comicComicCharacter.getId() != 0);

		this.comicComicCharacterRepository.delete(comicComicCharacter);
	}

}
