
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import domain.Admin;
import domain.User;

@Service
@Transactional
public class AdministratorService {

	@Autowired
	private AdministratorRepository	administratorRepository;
	@Autowired
	private UserService				userService;


	public AdministratorService() {
		super();
	}

	public Collection<Admin> findAll() {
		final Collection<Admin> res = this.administratorRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public void trust(final int userId) {
		Assert.isTrue(userId != 0);

		final User user = this.userService.findOne(userId);
		Assert.notNull(user);
		user.setTrusted(true);

		this.userService.save(user);
	}

	public void untrust(final int userId) {
		Assert.isTrue(userId != 0);

		final User user = this.userService.findOne(userId);
		Assert.notNull(user);
		user.setTrusted(false);

		this.userService.save(user);
	}

	public void block(final int userId) {
		Assert.isTrue(userId != 0);

		final User user = this.userService.findOne(userId);
		Assert.notNull(user);
		user.setBlocked(true);

		this.userService.save(user);
	}

	public void unblock(final int userId) {
		Assert.isTrue(userId != 0);

		final User user = this.userService.findOne(userId);
		Assert.notNull(user);
		user.setBlocked(false);

		this.userService.save(user);
	}

}
