
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;

@Service
@Transactional
public class ActorService {

	//Repositorios
	@Autowired
	private ActorRepository		actorRepository;

	//Servicios

	@Autowired
	private UserAccountService	userAccountService;


	public ActorService() {
		super();
	}

	public Collection<Actor> findAll() {
		final Collection<Actor> res = this.actorRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Actor findOne(final int id) {
		Assert.isTrue(id != 0);
		final Actor a = this.actorRepository.findOne(id);
		Assert.notNull(a);
		return a;
	}

	public Actor save(final Actor a) {
		Assert.notNull(a);
		final Actor res = this.actorRepository.save(a);
		return res;
	}

	public void delete(final Actor a) {
		Assert.notNull(a);
		Assert.isTrue(a.getId() != 0);
		//Assert.isTrue(this.actorRepository.findAll().contains(a));
		Assert.isTrue(this.actorRepository.exists(a.getId()));
		this.actorRepository.delete(a);
	}

	public UserAccount findUserAccount(final Actor a) {
		Assert.notNull(a);

		final UserAccount res = this.userAccountService.findByActor(a);

		return res;
	}
}
