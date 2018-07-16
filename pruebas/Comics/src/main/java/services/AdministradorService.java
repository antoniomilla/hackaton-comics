
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.AdministradorRepository;
import domain.Autor;
import domain.Cliente;
import domain.Comic;
import domain.Editorial;
import domain.Personaje;

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
	private EditorialService		editorialService;
	@Autowired
	private PersonajeService		personajeService;
	@Autowired
	private ClienteService			clienteService;


	public AdministradorService() {
		super();
	}

	public void registrarCliente(final Cliente c) {
		final Collection<Cliente> clientes = this.clienteService.findAll();
		this.clienteService.save(c);
		clientes.add(c);
	}

	public void añadirAutor(final Autor a) {
		final Collection<Autor> autores = this.autorService.findAll();
		this.autorService.save(a);
		autores.add(a);
	}

	public void añadirPersonaje(final Personaje p) {
		final Collection<Personaje> personajes = this.personajeService.findAll();
		this.personajeService.save(p);
		personajes.add(p);
	}

	public void añadirEditorial(final Editorial e) {
		final Collection<Editorial> editoriales = this.editorialService.findAll();
		this.editorialService.save(e);
		editoriales.add(e);
	}

	public void añadirComic(final Comic c) {
		final Collection<Comic> comics = this.comicService.findAll();
		this.comicService.save(c);
		comics.add(c);
	}

}
