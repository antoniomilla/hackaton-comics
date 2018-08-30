
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
import domain.Author;
import domain.Comic;
import domain.User;
import domain.UserComic;
import exceptions.ResourceNotFoundException;
import repositories.VolumeRepository;
import domain.Volume;
import utilities.CheckUtils;
import utilities.PolicyCheckUtils;

@Service
@Transactional
public class VolumeService {
	@Autowired private ActorService actorService;
	@Autowired private UserComicService userComicService;
	@Autowired private ComicService comicService;
	@Autowired private VolumeRepository repository;
	@PersistenceContext private EntityManager entityManager;
	@Autowired private Validator validator;


	public void delete(int id) {
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		Volume volume = getById(id);
		volume.getComic().getVolumes().remove(volume);
		comicService.updateVolumeCount(volume.getComic());
		repository.delete(id);
	}

	public Volume getById(int id)
	{
		Volume result = findById(id);
		if (result == null) throw new ResourceNotFoundException();
		return result;
	}

	public Volume findById(int id)
	{
		return repository.findOne(id);
	}

	public Volume create(Volume volume)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		CheckUtils.checkNotExists(volume);
		userComicService.uncompleteUsers(volume.getComic().getUserComics());

		if (!volume.getComic().getVolumes().contains(volume)) {
			volume.getComic().getVolumes().add(volume);
		}
		comicService.updateVolumeCount(volume.getComic());

		return repository.save(volume);
	}

	public Volume getByIdForEdit(int volumeId)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		return getById(volumeId);
	}

	public Volume bindForUpdate(Volume volume, BindingResult binding)
	{
		// Hibernate is in the dirty habit of automatically persisting any managed entities
		// at the end of the transaction, even if it was never saved. An attacker can force
		// the system to load a managed entity using a parameter (which gets parsed by the converter which loads the entity),
		// which can later be modified by the bound model attributes before our code is even called.
		// We're not going to save this entity, so detach it just in case.
		entityManager.detach(volume);

		Volume oldVolume = getById(volume.getId());
		CheckUtils.checkSameVersion(volume, oldVolume);

		oldVolume.setAuthor(volume.getAuthor());
		oldVolume.setChapterCount(volume.getChapterCount());
		oldVolume.setDescription(volume.getDescription());
		oldVolume.setImage(volume.getImage());
		oldVolume.setOrderNumber(volume.getOrderNumber());
		oldVolume.setName(volume.getName());
		oldVolume.setReleaseDate(volume.getReleaseDate());

		validator.validate(oldVolume, binding);
		if (binding.hasErrors()) entityManager.detach(oldVolume);

		return oldVolume;
	}

	public Volume update(Volume volume)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		CheckUtils.checkExists(volume);
		return repository.save(volume);
	}

	public List<Volume> findForListInComic(Comic comic)
	{
		return repository.findByComicOrderByOrderNumberAscNameAsc(comic);
	}

	public List<Pair<Volume, UserComic>> findVolumesAndUserComicsForAuthorAndPrincipal(Author author)
	{
		Integer userId = null;
		Actor principal = actorService.findPrincipal();
		if (principal instanceof User) userId = principal.getId();

		List<Object[]> rawResult = repository.findVolumesAndUserComicsForAuthorAndUserOrderByReleaseDateDesc(author, userId);
		List<Pair<Volume, UserComic>> result = new ArrayList<>();
		for (Object[] objects : rawResult) {
			if (objects[1] == null && principal instanceof User) {
				objects[1] = new UserComic((User) principal, ((Volume) objects[0]).getComic());
			}
			result.add(Pair.of((Volume) objects[0], (UserComic) objects[1]));
		}
		return result;
	}
}

