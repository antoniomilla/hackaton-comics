
package services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.ComicCharacter;
import exceptions.ResourceNotFoundException;
import repositories.ComicCharacterRepository;
import utilities.CheckUtils;
import utilities.PolicyCheckUtils;

@Service
@Transactional
public class ComicCharacterService {
	@Autowired private ComicCharacterRepository repository;
	@Autowired private ActorService actorService;
	@Autowired private Validator validator;
	@PersistenceContext private EntityManager entityManager;

	public List<ComicCharacter> findAll()
	{
		return repository.findAll();
	}

	public ComicCharacter getById(int id)
	{
		ComicCharacter result = findById(id);
		if (result == null) throw new ResourceNotFoundException();
		return result;
	}

	public ComicCharacter findById(int id)
	{
		return repository.findOne(id);
	}

	public ComicCharacter create(ComicCharacter comicCharacter)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		CheckUtils.checkNotExists(comicCharacter);
		return repository.save(comicCharacter);
	}

	public ComicCharacter getByIdForEdit(int id)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		return getById(id);
	}

	public ComicCharacter bindForUpdate(ComicCharacter comicCharacter, BindingResult binding)
	{
		entityManager.detach(comicCharacter);

		ComicCharacter oldComicCharacter = getById(comicCharacter.getId());
		CheckUtils.checkSameVersion(comicCharacter, oldComicCharacter);

		oldComicCharacter.setAlias(comicCharacter.getAlias());
		oldComicCharacter.setName(comicCharacter.getName());
		oldComicCharacter.setCity(comicCharacter.getCity());
		oldComicCharacter.setFirstAppearance(comicCharacter.getFirstAppearance());
		oldComicCharacter.setImage(comicCharacter.getImage());
		oldComicCharacter.setPublisher(comicCharacter.getPublisher());
		oldComicCharacter.setOtherAliases(comicCharacter.getOtherAliases());
		oldComicCharacter.setDescription(comicCharacter.getDescription());

		validator.validate(oldComicCharacter, binding);
		if (binding.hasErrors()) entityManager.detach(oldComicCharacter);

		return oldComicCharacter;
	}

	public ComicCharacter update(ComicCharacter comicCharacter)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		CheckUtils.checkExists(comicCharacter);
		return repository.save(comicCharacter);
	}

	public void delete(int id)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		repository.delete(id);
	}
}
