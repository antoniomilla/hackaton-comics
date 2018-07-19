
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
	private UserRepository	userRepository;

	@Autowired
	private ComicService	comicService;


	public UserService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public User create() {
		final User res = new User();

		return res;
	}

	public Collection<User> findAll() {
		final Collection<User> res = this.userRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public User findOne(final int Id) {
		final User res = this.userRepository.findOne(Id);
		Assert.notNull(res);

		return res;
	}

	public User save(final User user) {
		Assert.notNull(user);

		final User res = this.userRepository.save(user);

		return res;
	}

	public void delete(final User user) {
		Assert.notNull(user);
		Assert.isTrue(user.getId() != 0);

		this.userRepository.delete(user);
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

		final User res = this.userRepository.findByUserAccountId(userAccount.getId());

		return res;
	}

	public void setRead(final int comicId) {
		Assert.isTrue(comicId != 0);

		final User user = this.findByPrincipal();
		Assert.notNull(user);
		final Comic comic = this.comicService.findOne(comicId);
		Assert.notNull(comic);

		user.getComicsRead().add(comic);

		this.userRepository.save(user);
	}

	public void setUnread(final int comicId) {
		Assert.isTrue(comicId != 0);
		final User user = this.findByPrincipal();
		Assert.notNull(user);
		final Comic comic = this.comicService.findOne(comicId);
		Assert.notNull(comic);

		user.getComicsRead().remove(comic);

		this.userRepository.save(user);
	}
}
