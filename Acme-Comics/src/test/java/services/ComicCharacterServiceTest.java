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

import domain.ComicCharacter;
import domain.Publisher;
import utilities.AbstractTest;

/**
 * Tests the following use cases from the requirements document:
 * Create, edit and delete a character.
 */
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ComicCharacterServiceTest extends AbstractTest {
    @Autowired private PublisherService publisherService;
    @Autowired private ComicCharacterService comicCharacterService;

    // Test to create a comic character.
    @Test
    public void testCreate() throws Exception
    {
        for (String username: Arrays.asList("user1", "admin")) {
            authenticate(username);

            Publisher publisher = publisherService.findAll().get(0);

            ComicCharacter character = new ComicCharacter();
            character.setName("Bruce Wayne");
            character.setAlias("The Batman");
            character.setPublisher(publisher);
            character.setCity("Gotham");
            character.setFirstAppearance("Volume 1");
            character.setImage("http://example.com/image.png");

            // Ensure the description field can hold long texts as expected, forgetting the @Lob annotation is a common mistake.
            character.setDescription("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000));

            character = comicCharacterService.create(character);

            flushTransaction();

            // Ensure the character saved in the database matches the data we entered.
            ComicCharacter character2 = comicCharacterService.findById(character.getId());
            Assert.assertTrue(character != character2);
            Assert.assertTrue(character2.getName().equals("Bruce Wayne"));
            Assert.assertTrue(character2.getAlias().equals("The Batman"));
            Assert.assertTrue(character2.getPublisher().equals(publisher));
            Assert.assertTrue(character2.getCity().equals("Gotham"));
            Assert.assertTrue(character2.getFirstAppearance().equals("Volume 1"));
            Assert.assertTrue(character2.getDescription().equals("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000)));
        }
    }

    // Test that untrusted users cannot create a comic character.
    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsIfNotTrusted()
    {
        authenticate("user2"); // user2 is not trusted.

        Publisher publisher = publisherService.findAll().get(0);

        ComicCharacter character = new ComicCharacter();
        character.setName("Bruce Wayne");
        character.setAlias("The Batman");
        character.setPublisher(publisher);
        character.setCity("Gotham");
        character.setFirstAppearance("Volume 1");
        character.setImage("http://example.com/image.png");
        character.setDescription("Description here");

        comicCharacterService.create(character);

        // Shouldn't reach this point.
    }

    // Test that creating a character fails if the name is empty.
    @Test(expected = ConstraintViolationException.class)
    public void testCreateFailsIfNameEmpty()
    {
        authenticate("admin");

        Publisher publisher = publisherService.findAll().get(0);

        ComicCharacter character = new ComicCharacter();
        character.setName(null);
        character.setAlias("The Batman");
        character.setPublisher(publisher);
        character.setCity("Gotham");
        character.setFirstAppearance("Volume 1");
        character.setImage("http://example.com/image.png");
        character.setDescription("Description here");

        comicCharacterService.create(character);

        flushTransaction();

        // Shouldn't reach this point.
    }

    // Test to update a comic character.
    @Test
    public void testUpdate() throws Exception
    {
        for (String username: Arrays.asList("user1", "admin")) {
            authenticate(username);

            Publisher publisher = publisherService.findAll().get(0);

            ComicCharacter character = comicCharacterService.findAll().get(0);
            character.setName("Bruce Wayne " + username);
            character.setAlias("The Batman");
            character.setPublisher(publisher);
            character.setCity("Gotham");
            character.setFirstAppearance("Volume 1");
            character.setImage("http://example.com/image.png");

            // Ensure the description field can hold long texts as expected, forgetting the @Lob annotation is a common mistake.
            character.setDescription("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000));

            character = comicCharacterService.update(character);

            flushTransaction();

            // Ensure the character saved in the database matches the data we entered.
            ComicCharacter character2 = comicCharacterService.findById(character.getId());
            Assert.assertTrue(character != character2);
            Assert.assertTrue(character2.getName().equals("Bruce Wayne " + username));
            Assert.assertTrue(character2.getAlias().equals("The Batman"));
            Assert.assertTrue(character2.getPublisher().equals(publisher));
            Assert.assertTrue(character2.getCity().equals("Gotham"));
            Assert.assertTrue(character2.getFirstAppearance().equals("Volume 1"));
            Assert.assertTrue(character2.getDescription().equals("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000)));
        }
    }

    // Test that updating a comic character fails if not trusted.
    @Test(expected = AccessDeniedException.class)
    public void testUpdateFailsIfNotTrusted()
    {
        authenticate("user2"); // user2 is not trusted.

        Publisher publisher = publisherService.findAll().get(0);

        ComicCharacter character = comicCharacterService.findAll().get(0);
        character.setName("Bruce Wayne");
        character.setAlias("The Batman");
        character.setPublisher(publisher);
        character.setCity("Gotham");
        character.setFirstAppearance("Volume 1");
        character.setImage("http://example.com/image.png");
        character.setDescription("Description here");

        comicCharacterService.update(character);

        // Shouldn't reach this point.
    }

    // Test that updating a character fails if the alias is empty.
    @Test(expected = ConstraintViolationException.class)
    public void testUpdateFailsIfAliasEmpty()
    {
        authenticate("admin");

        Publisher publisher = publisherService.findAll().get(0);

        ComicCharacter character = comicCharacterService.findAll().get(0);
        character.setName("Bruce Wayne");
        character.setAlias(null);
        character.setPublisher(publisher);
        character.setCity("Gotham");
        character.setFirstAppearance("Volume 1");
        character.setImage("http://example.com/image.png");
        character.setDescription("Description here");

        comicCharacterService.update(character);

        flushTransaction();

        // Shouldn't reach this point.
    }

    // Test that deleting a character works
    @Test
    public void testDelete() throws Exception
    {
        for (String username: Arrays.asList("user1", "admin")) {
            authenticate(username);

            ComicCharacter character = comicCharacterService.findAll().get(0);

            int id = character.getId();
            comicCharacterService.delete(id);

            flushTransaction();

            // Ensure the character no longer remains in the database..
            ComicCharacter character2 = comicCharacterService.findById(id);
            Assert.assertTrue(character2 == null);
        }
    }

    // Test that deleting a character fails if untrusted.
    @Test(expected = AccessDeniedException.class)
    public void testDeleteFailsIfNotTrusted()
    {
        authenticate("user2"); // user2 is not trusted.

        ComicCharacter character = comicCharacterService.findAll().get(0);
        comicCharacterService.delete(character.getId());

        // Shouldn't reach this point.
    }

    // Test that deleting an unexisting character fails properly.
    @Test(expected = EmptyResultDataAccessException.class)
    public void testDeleteFailsIfCharacterDoesntExist()
    {
        authenticate("admin");

        comicCharacterService.delete(-1);

        flushTransaction();

        // Shouldn't reach this point.
    }
}
