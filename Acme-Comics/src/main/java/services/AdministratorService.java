
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import domain.Administrator;

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

	public Collection<Administrator> findAll() {
		final Collection<Administrator> res = this.administratorRepository.findAll();
		Assert.notNull(res);

		return res;
	}

}
