package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import domain.Actor;
import domain.Comic;
import domain.Sale;
import domain.SaleStatus;
import domain.User;
import exceptions.ResourceNotFoundException;
import repositories.SaleRepository;
import security.Authority;
import utilities.CheckUtils;

@Service
@Transactional
public class SaleService {
    @Autowired private SaleRepository repository;
    @Autowired private ActorService actorService;
    @PersistenceContext private EntityManager entityManager;
    @Autowired private Validator validator;


    public List<Sale> findAll()
    {
        return repository.findAll();
    }

    public List<Sale> findByStatus(SaleStatus selling)
    {
        return repository.findByStatusOrderByCreationTimeDesc(selling);
    }

    public List<Sale> findByUserOrInterestedUser(User user)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        return repository.findByUserOrInterestedUser(user);
    }

    public Sale getById(int id)
    {
        Sale result = findById(id);
        if (result == null) throw new ResourceNotFoundException();
        return result;
    }

    private Sale findById(int id)
    {
        return repository.findOne(id);
    }

    public Sale getByIdForEdit(int id)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        Sale result = getById(id);
        CheckUtils.checkIsPrincipal(result.getUser());
        CheckUtils.checkEquals(result.getStatus(), SaleStatus.SELLING);
        return result;
    }

    public Sale bindForUpdate(Sale sale, BindingResult binding)
    {
        // Hibernate is in the dirty habit of automatically persisting any managed entities
        // at the end of the transaction, even if it was never saved. An attacker can force
        // the system to load a managed entity using a parameter (which gets parsed by the converter which loads the entity),
        // which can later be modified by the bound model attributes before our code is even called.
        // We're not going to save this entity, so detach it just in case.
        entityManager.detach(sale);

        Sale oldSale = getById(sale.getId());
        CheckUtils.checkSameVersion(sale, oldSale);

        oldSale.setName(sale.getName());
        oldSale.setDescription(sale.getDescription());
        oldSale.setImages(sale.getImages());
        oldSale.setPrice(sale.getPrice());

        validator.validate(oldSale, binding);
        if (binding.hasErrors()) entityManager.detach(oldSale);

        return oldSale;
    }

    public Sale update(Sale sale)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        CheckUtils.checkIsPrincipal(sale.getUser());
        CheckUtils.checkEquals(sale.getStatus(), SaleStatus.SELLING);
        CheckUtils.checkExists(sale);

        sale.setPrice(Math.round(sale.getPrice() * 100.0) / 100.0);

        return repository.save(sale);
    }

    public void cancel(Sale sale)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        CheckUtils.checkIsPrincipal(sale.getUser());
        CheckUtils.checkEquals(sale.getStatus(), SaleStatus.SELLING);
        CheckUtils.checkExists(sale);
        sale.setStatus(SaleStatus.CANCELLED);
        repository.save(sale);
    }

    public void complete(Sale sale, User userSoldTo)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        CheckUtils.checkIsPrincipal(sale.getUser());
        CheckUtils.checkEquals(sale.getStatus(), SaleStatus.SELLING);
        CheckUtils.checkExists(sale);
        CheckUtils.checkTrue(sale.getInterestedUsers().contains(userSoldTo));
        sale.setStatus(SaleStatus.COMPLETED);
        sale.setUserSoldTo(userSoldTo);

        repository.save(sale);
    }

    public Sale create(Sale sale)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        CheckUtils.checkIsPrincipal(sale.getUser());
        CheckUtils.checkEquals(sale.getStatus(), SaleStatus.SELLING);
        CheckUtils.checkNotExists(sale);
        CheckUtils.checkTrue(sale.getInterestedUsers().isEmpty());
        CheckUtils.checkTrue(sale.getComic() != null);

        sale.setPrice(Math.round(sale.getPrice() * 100.0) / 100.0);

        return repository.save(sale);
    }

    public void setInterested(Sale sale, boolean interested)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        CheckUtils.checkEquals(sale.getStatus(), SaleStatus.SELLING);

        Actor principal = actorService.getPrincipal();
        CheckUtils.checkFalse(sale.getUser().equals(principal));

        if (interested) {
            sale.getInterestedUsers().add((User) principal);
        } else {
            sale.getInterestedUsers().remove((User) principal);
        }
        repository.save(sale);
    }

    public List<Sale> findForListInComic(Comic comic)
    {
        CheckUtils.checkAuthenticated();

        return repository.findByComicOrderByCreationTimeDesc(comic);
    }
}
