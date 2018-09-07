package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import domain.Actor;
import exceptions.OldPasswordDoesntMatchException;
import repositories.ActorRepository;
import security.UserAccountService;
import utilities.AbstractTest;

/**
 * Tests the following use cases from the requirements document:
 * - Search for users and administrators registered in the system.
 * - Change his own account [...] information.
 */
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ActorServiceTest extends AbstractTest {
    @Autowired private ActorService actorService;
    @Autowired private UserAccountService userAccountService;

    // Test successful search of an user.
    @Test
    public void testSearch() throws Exception
    {
        // This use case has no authentication requirements.
        unauthenticate();

        List<Actor> actors = actorService.search("user1");
        Assert.isTrue(actors.size() == 1);
        Assert.isTrue(actors.get(0).getUserAccount().getUsername().equals("user1"));

        authenticate("user1");

        actors = actorService.search("user2");
        Assert.isTrue(actors.size() == 1);
        Assert.isTrue(actors.get(0).getUserAccount().getUsername().equals("user2"));

        authenticate("user2");

        actors = actorService.search("Administrator");
        Assert.isTrue(actors.size() == 1);
        Assert.isTrue(actors.get(0).getUserAccount().getUsername().equals("admin"));

        authenticate("admin");

        actors = actorService.search("user3");
        Assert.isTrue(actors.size() == 1);
        Assert.isTrue(actors.get(0).getUserAccount().getUsername().equals("user3"));
    }

    // Test that a blank search returns all of the actors.
    @Test
    public void testSearch2() throws Exception
    {
        unauthenticate();
        Assert.isTrue(actorService.search("").equals(actorService.findAll()));
    }

    // Test that bogus search terms return nothing.
    @Test
    public void testSearch3() throws Exception
    {
        unauthenticate();
        Assert.isTrue(actorService.search("NONEXISTENTUSER").isEmpty());
    }

    // Test that extremely common search terms such as "this" don't throw an exception.
    // This is an easy mistake to make, as if the search terms become empty after Lucene
    // removes extremely common words from the query it throws an exception. Our service
    // should handle this transparently.
    @Test
    public void testSearch4() throws Exception
    {
        unauthenticate();
        actorService.search("this");
    }

    // Test successful password update.
    @Test
    public void testUpdateOwnPassword() throws Exception
    {
        authenticate("user1");

        // As user1, change his own password.
        actorService.updateOwnPassword("user1", "user1newpassword");

        flushTransaction();

        // Test that user1's password was changed.
        userAccountService.passwordMatchesAccount(getPrincipal().getUserAccount(), "user1newpassword");

        authenticate("admin");

        // As admin, change his own password.
        actorService.updateOwnPassword("admin", "adminnewpassword");

        flushTransaction();

        // Test that admin's password was changed.
        userAccountService.passwordMatchesAccount(getPrincipal().getUserAccount(), "adminnewpassword");
    }

    // Test password update fails if bad previous password is given.
    @Test(expected = OldPasswordDoesntMatchException.class)
    public void testUpdateOwnPasswordFailsIfBadOldPassword() throws Exception
    {
        authenticate("user1");

        // As user1, change his own password, providing a bad previous password.
        actorService.updateOwnPassword("BAD PASSWORD", "user1newpassword");

        flushTransaction();
    }

    // Test password update fails if bad new password is given.
    @Test(expected = ConstraintViolationException.class)
    public void testUpdateOwnPasswordFailsIfBadNewPassword() throws Exception
    {
        authenticate("user1");

        // As user1, change his own password, providing a bad (too short) new password.
        actorService.updateOwnPassword("user1", "1");

        flushTransaction();
    }
}
