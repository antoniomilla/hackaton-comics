
package services;

import org.apache.lucene.search.Query;
import org.hibernate.search.errors.EmptyQueryException;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import domain.Actor;
import exceptions.OldPasswordDoesntMatchException;
import exceptions.ResourceNotFoundException;
import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import utilities.CheckUtils;

@Service
@Transactional
public class ActorService {
    @Autowired private ActorRepository repository;
    @PersistenceContext private EntityManager entityManager;
    @Autowired private UserAccountService userAccountService;

    public Actor findPrincipal()
    {
        if (!LoginService.isAuthenticated()) return null;

        final UserAccount userAccount = LoginService.getPrincipal();
        if (userAccount == null) return null;

        return repository.findByUserAccount(userAccount);
    }

    public Actor getPrincipal()
    {
        CheckUtils.checkAuthenticated();
        final Actor principal = this.findPrincipal();
        Assert.notNull(principal);
        return principal;
    }

    public Actor findByUsername(final String username)
    {
        return repository.findByUsername(username);
    }

    public Actor getByUsername(final String username)
    {
        final Actor actor = this.findByUsername(username);
        Assert.notNull(actor);
        return actor;
    }

    public List<Actor> findAll()
    {
        return repository.findAll();
    }

    @SuppressWarnings("unchecked")
    public List<Actor> search(String searchTerms)
    {
        if (searchTerms != null && !searchTerms.trim().isEmpty()) {
            try {
                FullTextEntityManager ftem = Search.getFullTextEntityManager(entityManager);
                QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Actor.class).get();
                Query query = qb.keyword().onFields("nickname", "description").matching(searchTerms).createQuery();

                return (List<Actor>) ftem.createFullTextQuery(query, Actor.class).getResultList();
            } catch (EmptyQueryException ex) {
                // This happens if you search by a very common word that Lucene rejects for being too common.
                // If it's the only word in the query the query becomes empty and that's not allowed.
                // It's okay, just fall through to returning everything.
            }
        }
        return findAll();
    }

    public Actor getById(int id)
    {
        Actor result = findById(id);
        if (result == null) throw new ResourceNotFoundException();
        return result;
    }

    private Actor findById(int id)
    {
        return repository.findOne(id);
    }


    public Actor updateOwnPassword(final Actor actor, final String oldPassword, final String newPassword) throws OldPasswordDoesntMatchException
    {
        CheckUtils.checkAuthenticated();
        Actor currentActor = getPrincipal();
        CheckUtils.checkEquals(currentActor, actor);
        CheckUtils.checkSameVersion(actor, currentActor);

        if (!userAccountService.passwordMatchesAccount(actor.getUserAccount(), oldPassword)) {
            throw new OldPasswordDoesntMatchException();
        }

        currentActor.setUserAccount(this.userAccountService.updatePassword(currentActor.getUserAccount(), newPassword));
        return repository.save(currentActor);
    }
}
