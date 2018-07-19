
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import domain.Admin;

@Service
@Transactional
public class AdministratorService {

	@Autowired
	private AdministratorRepository	administratorRepository;


	public AdministratorService() {
		super();
	}

	public Collection<Admin> findAll() {
		final Collection<Admin> res = this.administratorRepository.findAll();
		Assert.notNull(res);

		return res;
	}

}
