
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.UserRepository;
import security.LoginService;
import security.UserAccount;
import domain.Comic;
import domain.User;

@Service
@Transactional
public class UserService {

	//Repositorios

	@Autowired
	private UserRepository	clienteRepository;

	@Autowired
	private ComicService		comicService;


	public UserService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public User create() {
		final User res = new User();

		return res;
	}

	public Collection<User> findAll() {
		final Collection<User> res = this.clienteRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public User findOne(final int Id) {
		final User res = this.clienteRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public User save(final User cliente) {
		Assert.notNull(cliente);

		final User res = this.clienteRepository.save(cliente);

		return res;
	}

	public void delete(final User cliente) {
		Assert.notNull(cliente);
		Assert.isTrue(cliente.getId() != 0);

		this.clienteRepository.delete(cliente);
	}

	public User findByPrincipal() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final User res = this.findByUserAccount(userAccount);
		Assert.notNull(res);

		return res;
	}

	public User findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		final User res = this.clienteRepository.findByUserAccountId(userAccount.getId());

		return res;
	}

	public void leerComic(final Comic comic) {
		final User c = this.findByPrincipal();
		c.getComicsRead().add(comic);
	}

	public boolean verSiLeido(final Comic comic) {
		final User c = this.findByPrincipal();
		final boolean res = c.getComicsRead().contains(comic);
		return res;
	}

	public Collection<Comic> comicsLeidos() {
		final User c = this.findByPrincipal();
		final Collection<Comic> comics = this.comicService.findAll();
		for (final Comic comic : comics)
			if (this.verSiLeido(comic))
				c.getComicsRead().add(comic);
		final Collection<Comic> res = c.getComicsRead();
		return res;
	}

	public void marcarLeido(final int comicId) {
		Assert.isTrue(comicId != 0);

		final User cliente = this.findByPrincipal();
		Assert.notNull(cliente);
		final Comic comic = this.comicService.findOne(comicId);
		Assert.notNull(comic);

		cliente.getComicsRead().add(comic);

		this.clienteRepository.save(cliente);
	}

	public void marcarNoLeido(final int comicId) {
		Assert.isTrue(comicId != 0);
		final User cliente = this.findByPrincipal();
		Assert.notNull(cliente);
		final Comic comic = this.comicService.findOne(comicId);
		Assert.notNull(comic);

		cliente.getComicsRead().remove(comic);

		this.clienteRepository.save(cliente);
	}
}
