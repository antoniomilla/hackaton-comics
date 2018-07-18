
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdministradorRepository;
import domain.Admin;
import domain.Author;
import domain.User;
import domain.Comic;
import domain.Publisher;
import domain.ComicCharacter;

@Service
@Transactional
public class AdministradorService {

	@Autowired
	private AdministradorRepository	administradorRepository;
	@Autowired
	private ComicService			comicService;
	@Autowired
	private AutorService			autorService;
	@Autowired
	private PublishingCompanyService		editorialService;
	@Autowired
	private PersonajeService		personajeService;
	@Autowired
	private UserService			clienteService;


	public AdministradorService() {
		super();
	}

	public Collection<Admin> findAll() {
		final Collection<Admin> res = this.administradorRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public void registrarCliente(final User c) {
		final Collection<User> clientes = this.clienteService.findAll();
		this.clienteService.save(c);
		clientes.add(c);
	}

	public void añadirAutor(final Author a) {
		final Collection<Author> autores = this.autorService.findAll();
		this.autorService.save(a);
		autores.add(a);
	}

	public void añadirPersonaje(final ComicCharacter p) {
		final Collection<ComicCharacter> personajes = this.personajeService.findAll();
		this.personajeService.save(p);
		personajes.add(p);
	}

	public void añadirEditorial(final Publisher e) {
		final Collection<Publisher> editoriales = this.editorialService.findAll();
		this.editorialService.save(e);
		editoriales.add(e);
	}

	public void añadirComic(final Comic c) {
		final Collection<Comic> comics = this.comicService.findAll();
		this.comicService.save(c);
		comics.add(c);
	}

}
