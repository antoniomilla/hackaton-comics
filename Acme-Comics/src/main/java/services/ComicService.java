
package services;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.lucene.search.Query;
import org.hibernate.search.errors.EmptyQueryException;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import domain.Actor;
import domain.Author;
import domain.Comic;
import domain.ComicCharacter;
import domain.Publisher;
import domain.User;
import domain.UserComic;
import exceptions.ResourceNotFoundException;
import repositories.ComicRepository;
import utilities.CheckUtils;
import utilities.PolicyCheckUtils;

@Service
@Transactional
public class ComicService {

    @Autowired private ComicRepository repository;
    @Autowired private ActorService actorService;
    @PersistenceContext private EntityManager entityManager;
    @Autowired private Validator validator;

    public List<Comic> findAll()
    {
        final List<Comic> res = this.repository.findAll();
        Assert.notNull(res);

        return res;
    }

    public Comic findById(int Id)
    {
        return this.repository.findOne(Id);
    }

    public void delete(int id)
    {
        PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
        repository.delete(id);
    }

    public Comic getById(int id)
    {
        Comic comic = findById(id);
        if (comic == null) throw new ResourceNotFoundException();
        return comic;
    }

    public Comic getByIdForEdit(int id)
    {
        PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
        return getById(id);
    }

    @SuppressWarnings("unchecked")
    public List<Comic> search(String searchTerms)
    {
        if (searchTerms != null && !searchTerms.trim().isEmpty()) {
            try {
                FullTextEntityManager ftem = Search.getFullTextEntityManager(entityManager);
                QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Comic.class).get();
                Query query = qb.keyword().onFields("name", "description", "tags", "authorName", "publisherName", "characterMainAliases").matching(searchTerms).createQuery();

                return (List<Comic>) ftem.createFullTextQuery(query, Comic.class).getResultList();
            } catch (EmptyQueryException ex) {
                // This happens if you search by a very common word that Lucene rejects for being too common.
                // If it's the only word in the query the query becomes empty and that's not allowed.
                // It's okay, just fall through.
            }
        }

        // Unlike in other search methods, we return nothing here.
        // This is because if there's no search a different logic is used in the controller.
        return null;
    }

    public Comic update(Comic comic)
    {
        PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
        CheckUtils.checkExists(comic);
        return repository.save(comic);
    }

    public Comic bindForUpdate(Comic comic, BindingResult binding)
    {
        // Hibernate is in the dirty habit of automatically persisting any managed entities
        // at the end of the transaction, even if it was never saved. An attacker can force
        // the system to load a managed entity using a parameter (which gets parsed by the converter which loads the entity),
        // which can later be modified by the bound model attributes before our code is even called.
        // We're not going to save this entity, so detach it just in case.
        entityManager.detach(comic);

        Comic oldComic = getById(comic.getId());
        CheckUtils.checkSameVersion(comic, oldComic);
        oldComic.setName(comic.getName());
        oldComic.setDescription(comic.getDescription());
        oldComic.setImage(comic.getImage());
        oldComic.setAuthor(comic.getAuthor());
        oldComic.setPublisher(comic.getPublisher());
        oldComic.setTags(comic.getTags());

        validator.validate(comic, binding);
        if (binding.hasErrors()) entityManager.detach(oldComic);

        return oldComic;
    }

    public Comic create(Comic comic)
    {
        PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
        CheckUtils.checkNotExists(comic);
        return repository.save(comic);
    }



    public List<Pair<Comic, UserComic>> findComicsAndUserComicsForAuthorAndPrincipal(Author author)
    {
        Integer userId = null;
        Actor principal = actorService.findPrincipal();
        if (principal instanceof User) userId = principal.getId();

        List<Object[]> rawResult = repository.findComicsAndUserComicsForAuthorAndUserOrderByNameAsc(author, userId);
        List<Pair<Comic, UserComic>> result = new ArrayList<>();
        for (Object[] objects : rawResult) {
            if (objects[1] == null && principal instanceof User) {
                objects[1] = new UserComic((User) principal, (Comic) objects[0]);
            }
            result.add(Pair.of((Comic) objects[0], (UserComic) objects[1]));
        }
        return result;
    }

    public List<Pair<Comic, UserComic>> findComicsAndUserComicsForPrincipal()
    {
        Integer userId = null;
        Actor principal = actorService.findPrincipal();
        if (principal instanceof User) userId = principal.getId();

        List<Object[]> rawResult = repository.findComicsAndUserComicsForUser(userId);
        List<Pair<Comic, UserComic>> result = new ArrayList<>();
        for (Object[] objects : rawResult) {
            if (objects[1] == null && principal instanceof User) {
                objects[1] = new UserComic((User) principal, (Comic) objects[0]);
            }
            result.add(Pair.of((Comic) objects[0], (UserComic) objects[1]));
        }
        return result;
    }

    void updateVolumeCount(Comic comic)
    {
        comic.setVolumeCount(comic.getVolumes().size());
        repository.save(comic);
    }

    public List<Pair<Comic, UserComic>> findComicsAndUserComicsForPublisherAndPrincipal(Publisher publisher)
    {
        Integer userId = null;
        Actor principal = actorService.findPrincipal();
        if (principal instanceof User) userId = principal.getId();

        List<Object[]> rawResult = repository.findComicsAndUserComicsForPublisherAndUserOrderByNameAsc(publisher, userId);
        List<Pair<Comic, UserComic>> result = new ArrayList<>();
        for (Object[] objects : rawResult) {
            if (objects[1] == null && principal instanceof User) {
                objects[1] = new UserComic((User) principal, (Comic) objects[0]);
            }
            result.add(Pair.of((Comic) objects[0], (UserComic) objects[1]));
        }
        return result;
    }
}
