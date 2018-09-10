package services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.method.P;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import domain.Comic;
import domain.Sale;
import domain.SaleStatus;
import domain.User;
import repositories.ComicRepository;
import repositories.SaleRepository;
import utilities.AbstractTest;

/**
 * Tests the following use cases from the requirements document:
 * Put merchandise for sale.
 * Mark or unmark himself as interested in a sale.
 * Mark a sale as complete when an user has been sold the merchandise.
 * Mark a sale as cancelled if the sale is not going to be completed.
 */
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SaleServiceTest extends AbstractTest {
    @Autowired private SaleService saleService;
    @Autowired private SaleRepository saleRepository;
    @Autowired private ComicRepository comicRepository;

    @Test
    public void testCreate()
    {
        authenticate("user1");

        Comic comic = comicRepository.findAll().get(0);

        Sale sale = new Sale();
        sale.setComic(comic);
        sale.setUser((User) getPrincipal());
        sale.setName("My sale");
        sale.setDescription("My description");
        sale.setPrice(30.00);
        sale.setStatus(SaleStatus.SELLING);
        sale.setCreationTime(new Date());

        sale = saleService.create(sale);

        flushTransaction();

        sale = saleRepository.findOne(sale.getId());
        Assert.assertTrue(Objects.equals(sale.getComic(), comic));
        Assert.assertTrue(Objects.equals(sale.getUser(), getPrincipal()));
        Assert.assertTrue(Objects.equals(sale.getName(), "My sale"));
        Assert.assertTrue(Objects.equals(sale.getDescription(), "My description"));
        Assert.assertTrue(Objects.equals(sale.getPrice(), 30.00));
        Assert.assertTrue(Objects.equals(sale.getStatus(), SaleStatus.SELLING));
    }

    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsWithBadStatus()
    {
        authenticate("user1");

        Comic comic = comicRepository.findAll().get(0);

        Sale sale = new Sale();
        sale.setComic(comic);
        sale.setUser((User) getPrincipal());
        sale.setName("My sale");
        sale.setDescription("My description");
        sale.setPrice(30.00);
        sale.setStatus(SaleStatus.COMPLETED);
        sale.setUserSoldTo((User) getActor("user2"));
        sale.setCreationTime(new Date());

        saleService.create(sale);
    }

    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsWithUserNotPrincipal()
    {
        authenticate("user1");

        Comic comic = comicRepository.findAll().get(0);

        Sale sale = new Sale();
        sale.setComic(comic);
        sale.setUser((User) getActor("user2"));
        sale.setName("My sale");
        sale.setDescription("My description");
        sale.setPrice(30.00);
        sale.setStatus(SaleStatus.SELLING);

        saleService.create(sale);
    }

    @Test
    public void testUpdate()
    {
        authenticate("user1");

        Sale sale = saleRepository.findByName("Flashpoint comic volume 1");
        sale.setName("New name");
        sale.setDescription("New description");
        sale.setPrice(99.99);

        sale = saleService.update(sale);

        flushTransaction();

        sale = saleRepository.findOne(sale.getId());
        Assert.assertTrue(Objects.equals(sale.getName(), "New name"));
        Assert.assertTrue(Objects.equals(sale.getDescription(), "New description"));
        Assert.assertTrue(Objects.equals(sale.getPrice(), 99.99));
        Assert.assertTrue(Objects.equals(sale.getStatus(), SaleStatus.SELLING));
    }

    @Test(expected = AccessDeniedException.class)
    public void testUpdateFailsForWrongPrincipal()
    {
        authenticate("user2");

        Sale sale = saleRepository.findByName("Flashpoint comic volume 1");
        sale.setName("New name");
        sale.setDescription("New description");
        sale.setPrice(99.99);

        sale = saleService.update(sale);
    }

    @Test(expected = AccessDeniedException.class)
    public void testUpdateFailsWithBadStatus()
    {
        authenticate("user1");

        Sale sale = saleRepository.findByName("Watchmen keychain");
        sale.setName("New name");
        sale.setDescription("New description");
        sale.setPrice(99.99);

        sale = saleService.update(sale);
    }

    @Test
    public void testCancel()
    {
        authenticate("user1");

        Sale sale = saleRepository.findByName("Flashpoint comic volume 1");
        saleService.cancel(sale);

        flushTransaction();

        sale = saleRepository.findOne(sale.getId());
        Assert.assertTrue(sale.getStatus().equals(SaleStatus.CANCELLED));
    }

    @Test(expected = AccessDeniedException.class)
    public void testCancelFailsForWrongPrincipal()
    {
        authenticate("user2");

        Sale sale = saleRepository.findByName("Flashpoint comic volume 1");
        saleService.cancel(sale);

        flushTransaction();
    }

    @Test(expected = AccessDeniedException.class)
    public void testCancelFailsWithBadStatus()
    {
        authenticate("user1");

        Sale sale = saleRepository.findByName("Watchmen keychain");
        saleService.cancel(sale);

        flushTransaction();
    }

    @Test
    public void testComplete()
    {
        authenticate("user1");

        Sale sale = saleRepository.findByName("Flashpoint comic volume 1");
        saleService.complete(sale, (User) getActor("user2"));

        flushTransaction();

        sale = saleRepository.findOne(sale.getId());
        Assert.assertTrue(sale.getStatus().equals(SaleStatus.COMPLETED));
        Assert.assertTrue(sale.getUserSoldTo().equals(getActor("user2")));
    }

    @Test(expected = AccessDeniedException.class)
    public void testCompleteFailsForWrongPrincipal()
    {
        authenticate("user2");

        Sale sale = saleRepository.findByName("Flashpoint comic volume 1");
        saleService.complete(sale, (User) getActor("user2"));

        flushTransaction();
    }

    @Test(expected = AccessDeniedException.class)
    public void testCompleteFailsWithBadStatus()
    {
        authenticate("user1");

        Sale sale = saleRepository.findByName("Watchmen keychain");
        saleService.complete(sale, (User) getActor("user2"));

        flushTransaction();
    }

    @Test
    public void testSetInterested()
    {
        authenticate("user2");

        Sale sale = saleRepository.findByName("Flashpoint comic volume 1");
        saleService.setInterested(sale, false);

        flushTransaction();

        sale = saleRepository.findOne(sale.getId());
        Assert.assertTrue(!sale.getInterestedUsers().contains((User) getPrincipal()));

        saleService.setInterested(sale, true);

        flushTransaction();

        sale = saleRepository.findOne(sale.getId());
        Assert.assertTrue(sale.getInterestedUsers().contains((User) getPrincipal()));
    }

    @Test(expected = AccessDeniedException.class)
    public void testSetInterestedFailsForBadStatus()
    {
        authenticate("user2");

        Sale sale = saleRepository.findByName("Watchmen keychain");
        saleService.setInterested(sale, false);

        flushTransaction();
    }

}
