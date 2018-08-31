
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.User;
import exceptions.ResourceNotFoundException;
import repositories.AdministratorRepository;
import domain.Administrator;
import security.Authority;
import utilities.CheckUtils;

@Service
@Transactional
public class AdministratorService {
	@Autowired private AdministratorRepository repository;
	@Autowired private Validator validator;
	@PersistenceContext private EntityManager entityManager;


	public Administrator bindForUpdate(Administrator administrator, BindingResult binding)
	{
		// Hibernate is in the dirty habit of automatically persisting any managed entities
		// at the end of the transaction, even if it was never saved. An attacker can force
		// the system to load a managed entity using a parameter (which gets parsed by the converter which loads the entity),
		// which can later be modified by the bound model attributes before our code is even called.
		// We're not going to save this entity, so detach it just in case.
		entityManager.detach(administrator);

		Administrator oldAdministrator = getById(administrator.getId());
		CheckUtils.checkSameVersion(administrator, oldAdministrator);

		oldAdministrator.setNickname(administrator.getNickname());
		oldAdministrator.setDescription(administrator.getDescription());

		validator.validate(oldAdministrator, binding);
		if (binding.hasErrors()) entityManager.detach(oldAdministrator);

		return oldAdministrator;
	}

	private Administrator getById(int id)
	{
		Administrator result = findById(id);
		if (result == null) throw new ResourceNotFoundException();
		return result;
	}

	private Administrator findById(int id)
	{
		return repository.findOne(id);
	}

	public Administrator update(Administrator administrator)
	{
		CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);
		CheckUtils.checkIsPrincipal(administrator);
		return repository.save(administrator);
	}
}
