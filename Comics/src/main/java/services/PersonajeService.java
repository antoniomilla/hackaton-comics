
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PersonajeRepository;
import domain.ComicCharacter;

@Service
@Transactional
public class PersonajeService {

	@Autowired
	private PersonajeRepository	personajeRepository;


	public PersonajeService() {
		super();
	}

	public ComicCharacter create() {
		final ComicCharacter res = new ComicCharacter();

		return res;
	}

	public Collection<ComicCharacter> findAll() {
		final Collection<ComicCharacter> res = this.personajeRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public ComicCharacter findOne(final int Id) {
		final ComicCharacter res = this.personajeRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public ComicCharacter save(final ComicCharacter personaje) {
		Assert.notNull(personaje);

		final ComicCharacter res = this.personajeRepository.save(personaje);

		return res;
	}

	public void delete(final ComicCharacter personaje) {
		Assert.notNull(personaje);
		Assert.isTrue(personaje.getId() != 0);

		this.personajeRepository.delete(personaje);
	}

}
