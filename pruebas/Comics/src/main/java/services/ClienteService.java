
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ClienteRepository;
import security.LoginService;
import security.UserAccount;
import domain.Cliente;
import domain.Comic;

@Service
@Transactional
public class ClienteService {

	//Repositorios

	@Autowired
	private ClienteRepository	clienteRepository;

	@Autowired
	private ComicService		comicService;


	public ClienteService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Cliente create() {
		final Cliente res = new Cliente();

		return res;
	}

	public Collection<Cliente> findAll() {
		final Collection<Cliente> res = this.clienteRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public Cliente findOne(final int Id) {
		final Cliente res = this.clienteRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public Cliente save(final Cliente cliente) {
		Assert.notNull(cliente);

		final Cliente res = this.clienteRepository.save(cliente);

		return res;
	}

	public void delete(final Cliente cliente) {
		Assert.notNull(cliente);
		Assert.isTrue(cliente.getId() != 0);

		this.clienteRepository.delete(cliente);
	}

	public Cliente findByPrincipal() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Cliente res = this.findByUserAccount(userAccount);
		Assert.notNull(res);

		return res;
	}

	public Cliente findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		final Cliente res = this.clienteRepository.findByUserAccountId(userAccount.getId());

		return res;
	}

	public void leerComic(final Comic comic) {
		final Cliente c = this.findByPrincipal();
		c.getComicsLeidos().add(comic);
	}

	public boolean verSiLeido(final Comic comic) {
		final Cliente c = this.findByPrincipal();
		final boolean res = c.getComicsLeidos().contains(comic);
		return res;
	}

	public Collection<Comic> comicsLeidos() {
		final Cliente c = this.findByPrincipal();
		final Collection<Comic> comics = this.comicService.findAll();
		for (final Comic comic : comics)
			if (this.verSiLeido(comic))
				c.getComicsLeidos().add(comic);
		final Collection<Comic> res = c.getComicsLeidos();
		return res;
	}
}
