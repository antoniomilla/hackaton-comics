
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Actor;
import domain.Comic;
import domain.Publisher;
import domain.User;
import domain.UserComic;
import domain.Volume;
import exceptions.ResourceNotFoundException;
import repositories.AuthorRepository;
import domain.Author;
import repositories.PublisherRepository;
import utilities.CheckUtils;
import utilities.PolicyCheckUtils;

@Service
@Transactional
public class AuthorService {
	@Autowired private AuthorRepository repository;
	@Autowired private ActorService actorService;
	@Autowired private Validator validator;
	@PersistenceContext private EntityManager entityManager;

	public List<Author> findAll()
	{
		return repository.findAll();
	}

	public Author getById(int id)
	{
		Author result = findById(id);
		if (result == null) throw new ResourceNotFoundException();
		return result;
	}

	public Author findById(int id)
	{
		return repository.findOne(id);
	}

	public Author create(Author author)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		CheckUtils.checkNotExists(author);
		return repository.save(author);
	}

	public Author getByIdForEdit(int id)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		return getById(id);
	}

	public Author bindForUpdate(Author author, BindingResult binding)
	{
		// Hibernate is in the dirty habit of automatically persisting any managed entities
		// at the end of the transaction, even if it was never saved. An attacker can force
		// the system to load a managed entity using a parameter (which gets parsed by the converter which loads the entity),
		// which can later be modified by the bound model attributes before our code is even called.
		// We're not going to save this entity, so detach it just in case.
		entityManager.detach(author);

		Author oldAuthor = getById(author.getId());
		CheckUtils.checkSameVersion(author, oldAuthor);

		oldAuthor.setBirthDate(author.getBirthDate());
		oldAuthor.setBirthPlace(author.getBirthPlace());
		oldAuthor.setDescription(author.getDescription());
		oldAuthor.setImage(author.getImage());
		oldAuthor.setName(author.getName());

		validator.validate(oldAuthor, binding);
		if (binding.hasErrors()) entityManager.detach(oldAuthor);

		return oldAuthor;
	}

	public Author update(Author author)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		CheckUtils.checkExists(author);
		return repository.save(author);
	}

	public void delete(int id)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		repository.delete(id);
	}
}
