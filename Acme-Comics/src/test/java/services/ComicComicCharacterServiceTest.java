package services;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import domain.Comic;
import domain.ComicCharacter;
import domain.ComicComicCharacter;
import domain.Publisher;
import exceptions.ResourceNotUniqueException;
import repositories.ComicRepository;
import utilities.AbstractTest;

/**
 * Tests the following use cases from the requirements document:
 * - Edit a comic. More specifically, associate/dissociate characters to comics.
 */
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ComicComicCharacterServiceTest extends AbstractTest {
    @Autowired private ComicRepository comicRepository;
    @Autowired private ComicService comicService;
    @Autowired private ComicCharacterService comicCharacterService;
    @Autowired private ComicComicCharacterService comicComicCharacterService;

    // Test to associate a character with a comic.
    @Test
    public void testCreate() throws Exception
    {
        authenticate("admin");

        Comic comic = comicRepository.findByName("The Walking Dead");
        ComicCharacter character = comicCharacterService.findAll().get(0);

        ComicComicCharacter ccc = new ComicComicCharacter();
        ccc.setComic(comic);
        ccc.setComicCharacter(character);
        ccc.setRole("My role");

        ccc = comicComicCharacterService.create(ccc);

        flushTransaction();

        // Ensure the character saved in the database matches the data we entered.
        Assert.assertTrue(comicService.getById(comic.getId()).getComicComicCharacters().contains(ccc));
    }

    // Test that only trusted users can do that.
    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsIfNotTrusted() throws Exception
    {
        authenticate("user2"); // user2 is not trusted.

        Comic comic = comicRepository.findByName("The Walking Dead");
        ComicCharacter character = comicCharacterService.findAll().get(0);

        ComicComicCharacter ccc = new ComicComicCharacter();
        ccc.setComic(comic);
        ccc.setComicCharacter(character);
        ccc.setRole("My role");

        ccc = comicComicCharacterService.create(ccc);

        flushTransaction();

        // Shouldn't reach this point.
    }

    // Test that you cannot associate the same character twice.
    @Test(expected = ResourceNotUniqueException.class)
    public void testCreateFailsIfNotUnique() throws Exception
    {
        authenticate("admin");

        Comic comic = comicRepository.findByName("The Walking Dead");
        ComicCharacter character = comicCharacterService.findAll().get(0);

        ComicComicCharacter ccc = new ComicComicCharacter();
        ccc.setComic(comic);
        ccc.setComicCharacter(character);
        ccc.setRole("My role");

        ccc = comicComicCharacterService.create(ccc);

        ccc = new ComicComicCharacter();
        ccc.setComic(comic);
        ccc.setComicCharacter(character);
        ccc.setRole("My role 2");

        ccc = comicComicCharacterService.create(ccc);

        flushTransaction();

        // Shouldn't reach this point.
    }

    // Test to update a character role in a comic.
    @Test
    public void testUpdate() throws Exception
    {
        authenticate("admin");

        Comic comic = comicRepository.findByName("Flashpoint");

        ComicComicCharacter ccc = comic.getComicComicCharacters().get(0);
        ccc.setRole("New role");

        comicComicCharacterService.update(ccc);

        flushTransaction();

        // Ensure the character saved in the database matches the data we entered.
        ComicComicCharacter ccc2 = comicComicCharacterService.getById(ccc.getId());
        Assert.assertTrue(ccc != ccc2);
        Assert.assertTrue(ccc2.getRole().equals("New role"));
    }

    // Test that it fails if not trusted.
    @Test(expected = AccessDeniedException.class)
    public void testUpdateFailsIfNotTrusted()
    {
        authenticate("user2");

        Comic comic = comicRepository.findByName("Flashpoint");

        ComicComicCharacter ccc = comic.getComicComicCharacters().get(0);
        ccc.setRole("New role");

        comicComicCharacterService.update(ccc);

        // Shouldn't reach this point.
    }

    // Test that it fails if the role is empty.
    @Test(expected = ConstraintViolationException.class)
    public void testUpdateFailsIfRoleEmpty()
    {
        authenticate("admin");

        Comic comic = comicRepository.findByName("Flashpoint");

        ComicComicCharacter ccc = comic.getComicComicCharacters().get(0);
        ccc.setRole(null);

        comicComicCharacterService.update(ccc);

        flushTransaction();

        // Shouldn't reach this point.
    }

    // Test to dissociate a character with a comic.
    @Test
    public void testDelete() throws Exception
    {
        authenticate("admin");

        Comic comic = comicRepository.findByName("Flashpoint");

        ComicComicCharacter ccc = comic.getComicComicCharacters().get(0);
        ComicCharacter character = ccc.getComicCharacter();

        comicComicCharacterService.delete(ccc.getId());

        flushTransaction();

        comic = comicRepository.findByName("Flashpoint");
        for (ComicComicCharacter ccc2 : comic.getComicComicCharacters()) {
            Assert.assertFalse(ccc2.getComicCharacter().equals(character));
        }
    }

    // Test that it fails if not trusted.
    @Test(expected = AccessDeniedException.class)
    public void testDeleteFailsIfNotTrusted()
    {
        authenticate("user2"); // user2 is not trusted

        Comic comic = comicRepository.findByName("Flashpoint");

        ComicComicCharacter ccc = comic.getComicComicCharacters().get(0);
        comicComicCharacterService.delete(ccc.getId());

        // Shouldn't reach this point.
    }

    // Test that it fails if not authenticated.
    @Test(expected = AccessDeniedException.class)
    public void testDeleteFailsIfNotAuthenticated()
    {
        unauthenticate();

        Comic comic = comicRepository.findByName("Flashpoint");
        ComicComicCharacter ccc = comic.getComicComicCharacters().get(0);

        comicComicCharacterService.delete(ccc.getId());

        flushTransaction();

        // Shouldn't reach this point.
    }
}
