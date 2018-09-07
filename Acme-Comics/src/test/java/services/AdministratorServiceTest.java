package services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import domain.Administrator;
import repositories.ActorRepository;
import repositories.AdministratorRepository;
import security.UserAccountService;
import utilities.AbstractTest;

/**
 * Tests the following use cases from the requirements document:
 * - Change his own [...] profile information.
 */
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AdministratorServiceTest extends AbstractTest {
    @Autowired private AdministratorService administratorService;

    // Test successful profile information change.
    @Test
    public void testUpdate()
    {
        authenticate("admin");

        Administrator principal = (Administrator) getPrincipal();
        principal.setNickname("NEW NICKNAME");
        principal.setDescription("NEW DESCRIPTION");
        administratorService.update(principal);

        flushTransaction();

        // Ensure that the information has been updated.
        Administrator newPrincipal = (Administrator) getPrincipal();
        Assert.assertTrue(newPrincipal != principal);

        Assert.assertTrue(newPrincipal.getNickname().equals("NEW NICKNAME"));
        Assert.assertTrue(newPrincipal.getDescription().equals("NEW DESCRIPTION"));

    }

    // Test profile information change fails if not principal.
    @Test(expected = AccessDeniedException.class)
    public void testUpdateFailsIfWrongActor()
    {
        authenticate("admin2");

        Administrator principal = (Administrator) getActor("admin");
        principal.setNickname("NEW NICKNAME");
        principal.setDescription("NEW DESCRIPTION");
        administratorService.update(principal);

        flushTransaction();

        // Shouldn't reach this point.
    }

    // Test profile information change fails if bad data is given.
    @Test(expected = ConstraintViolationException.class)
    public void testUpdateFailsIfValidationFails()
    {
        authenticate("admin");

        Administrator principal = (Administrator) getPrincipal();
        principal.setNickname("");
        principal.setDescription("NEW DESCRIPTION");
        administratorService.update(principal);

        flushTransaction();

        // Shouldn't reach this point.
    }
}
