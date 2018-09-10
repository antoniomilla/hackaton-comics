
package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

import domain.Actor;
import domain.Comic;
import domain.ComicCharacter;
import domain.User;
import domain.UserComic;
import exceptions.ResourceNotFoundException;
import exceptions.ResourceNotUniqueException;
import repositories.ComicComicCharacterRepository;
import domain.ComicComicCharacter;
import utilities.CheckUtils;
import utilities.PolicyCheckUtils;

@Service
@Transactional
public class ComicComicCharacterService {
	@Autowired private ComicComicCharacterRepository repository;
	@Autowired private ActorService actorService;
	@PersistenceContext private EntityManager entityManager;
	@Autowired private Validator validator;

	public ComicComicCharacter create(ComicComicCharacter comicComicCharacter) throws ResourceNotUniqueException
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		CheckUtils.checkNotExists(comicComicCharacter);

		if (repository.findByComicAndComicCharacter(comicComicCharacter.getComic(), comicComicCharacter.getComicCharacter()) != null) {
			throw new ResourceNotUniqueException();
		}

		return repository.save(comicComicCharacter);
	}

	public ComicComicCharacter bindForUpdate(ComicComicCharacter comicComicCharacter, BindingResult binding)
	{
		// Hibernate is in the dirty habit of automatically persisting any managed entities
		// at the end of the transaction, even if it was never saved. An attacker can force
		// the system to load a managed entity using a parameter (which gets parsed by the converter which loads the entity),
		// which can later be modified by the bound model attributes before our code is even called.
		// We're not going to save this entity, so detach it just in case.
		entityManager.detach(comicComicCharacter);

		ComicComicCharacter oldComicComicCharacter = getById(comicComicCharacter.getId());
		CheckUtils.checkSameVersion(comicComicCharacter, oldComicComicCharacter);
		oldComicComicCharacter.setRole(comicComicCharacter.getRole());

		validator.validate(oldComicComicCharacter, binding);
		if (binding.hasErrors()) entityManager.detach(oldComicComicCharacter);

		return oldComicComicCharacter;
	}

	public ComicComicCharacter getById(int id)
	{
		ComicComicCharacter result = findById(id);
		if (result == null) throw new ResourceNotFoundException();
		return result;
	}

	private ComicComicCharacter findById(int id)
	{
		return repository.findOne(id);
	}

	public void update(ComicComicCharacter comicComicCharacter)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		CheckUtils.checkExists(comicComicCharacter);
		repository.save(comicComicCharacter);
	}

	public void delete(int id)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		repository.delete(id);
	}

	public ComicComicCharacter getByIdForEdit(int id)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		return getById(id);
	}



	public List<Pair<ComicComicCharacter, UserComic>> findComicComicCharactersAndUserComicsForComicCharacterAndPrincipal(ComicCharacter comicCharacter)
	{
		Integer userId = null;
		Actor principal = actorService.findPrincipal();
		if (principal instanceof User) userId = principal.getId();

		List<Object[]> rawResult = repository.findComicComicCharactersAndUserComicsForComicCharacterAndUserOrderByNameAsc(comicCharacter, userId);
		List<Pair<ComicComicCharacter, UserComic>> result = new ArrayList<>();
		for (Object[] objects : rawResult) {
			if (objects[1] == null && principal instanceof User) {
				objects[1] = new UserComic((User) principal, ((ComicComicCharacter) objects[0]).getComic());
			}
			result.add(Pair.of((ComicComicCharacter) objects[0], (UserComic) objects[1]));
		}
		return result;
	}
}
