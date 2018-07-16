
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PersonajeRepository;
import domain.Personaje;

@Service
@Transactional
public class PersonajeService {

	@Autowired
	private PersonajeRepository	personajeRepository;


	public PersonajeService() {
		super();
	}

	public Personaje create() {
		final Personaje res = new Personaje();

		return res;
	}

	public Collection<Personaje> findAll() {
		final Collection<Personaje> res = this.personajeRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public Personaje findOne(final int Id) {
		final Personaje res = this.personajeRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public Personaje save(final Personaje personaje) {
		Assert.notNull(personaje);

		final Personaje res = this.personajeRepository.save(personaje);

		return res;
	}

	public void delete(final Personaje personaje) {
		Assert.notNull(personaje);
		Assert.isTrue(personaje.getId() != 0);

		this.personajeRepository.delete(personaje);
	}

}
