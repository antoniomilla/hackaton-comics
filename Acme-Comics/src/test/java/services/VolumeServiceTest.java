package services;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import domain.Author;
import domain.Comic;
import domain.Volume;
import repositories.AuthorRepository;
import repositories.ComicRepository;
import repositories.VolumeRepository;
import utilities.AbstractTest;

/**
 * Tests the following use cases from the requirements document:
 * - Create, edit and delete a comic volume.
 */
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class VolumeServiceTest extends AbstractTest {
    @Autowired private VolumeService volumeService;
    @Autowired private ComicRepository comicRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private VolumeRepository volumeRepository;

    @Test
    public void testCreate() throws Exception
    {
        authenticate("user1");

        Volume volume = new Volume();
        volume.setComic(comicRepository.findAll().get(0));
        volume.setAuthor(authorRepository.findAll().get(0));
        volume.setName("TEST VOLUME");
        volume.setImage("http://testimages.com/a.png");
        volume.setReleaseDate(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/02/1980"));

        // Ensure the description field can hold long texts as expected, forgetting the @Lob annotation is a common mistake.
        volume.setDescription("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000));
        volume = volumeService.create(volume);

        flushTransaction();

        // Ensure the volume saved in the database matches the data we entered.
        Volume volume2 = volumeService.findById(volume.getId());
        Assert.assertTrue(volume != volume2);
        Assert.assertTrue(volume2.getName().equals("TEST VOLUME"));
        Assert.assertTrue(volume2.getImage().equals("http://testimages.com/a.png"));
        Assert.assertTrue(volume2.getReleaseDate().equals(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/02/1980")));
        Assert.assertTrue(volume2.getOrderNumber() == 0);
        Assert.assertTrue(volume2.getDescription().equals("VERY LONG DESCRIPTION " + StringUtils.repeat("A", 1000)));

    }

    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsIfNotTrusted() throws Exception
    {
        authenticate("user2"); // user2 is not trusted.

        Volume volume = new Volume();
        volume.setComic(comicRepository.findAll().get(0));
        volume.setAuthor(authorRepository.findAll().get(0));
        volume.setName("TEST VOLUME");
        volume.setImage("http://testimages.com/a.png");
        volume.setReleaseDate(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/02/1980"));
        volume.setDescription("TESTDESCRIPTION");

        // Attempt to save as non trusted user.
        volumeService.create(volume);

        // Shouldn't reach this point.
    }

    @Test(expected = AccessDeniedException.class)
    public void testCreateFailsIfUnauthenticated() throws Exception
    {
        unauthenticate();

        Volume volume = new Volume();

        volume.setComic(comicRepository.findAll().get(0));
        volume.setAuthor(authorRepository.findAll().get(0));
        volume.setName("TEST VOLUME");
        volume.setImage("http://testimages.com/a.png");
        volume.setReleaseDate(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/02/1980"));
        volume.setDescription("TESTDESCRIPTION");

        volumeService.create(volume);

        flushTransaction();

        // Shouldn't reach this point.
    }

    @Test
    public void testUpdate() throws Exception
    {
        authenticate("admin");

        // Get some existing volume for editing.
        Volume volume = volumeService.getByIdForEdit(volumeRepository.findAll().get(0).getId());

        // Edit it.
        volume.setName("TEST VOLUME");
        volume.setImage("http://testimages.com/a.png");
        volume.setReleaseDate(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/02/1980"));
        volume.setDescription("TESTDESCRIPTION");

        // Update it.
        volumeService.update(volume);

        flushTransaction();

        // Ensure the volume saved in the database matches the data we entered.
        Volume volume2 = volumeService.findById(volume.getId());
        Assert.assertTrue(volume != volume2);
        Assert.assertTrue(volume2.getName().equals("TEST VOLUME"));
        Assert.assertTrue(volume2.getImage().equals("http://testimages.com/a.png"));
        Assert.assertTrue(volume2.getReleaseDate().equals(FastDateFormat.getInstance("dd/MM/yyyy").parse("01/02/1980")));
        Assert.assertTrue(volume2.getDescription().equals("TESTDESCRIPTION"));
    }

    @Test(expected = AccessDeniedException.class)
    public void testEditFailsIfNotTrusted()
    {
        authenticate("user2");

        // Get some existing volume for editing.
        volumeService.getByIdForEdit(volumeRepository.findAll().get(0).getId());

        // Shouldn't reach this point.
    }

    @Test(expected = AccessDeniedException.class)
    public void testUpdateFailsIfNotTrusted() throws Exception
    {
        authenticate("user2");

        // Lets pretend the non-trusted user is directly POSTing the update without
        // using the form.
        Volume volume = volumeRepository.findAll().get(0);

        // Edit it.
        volume.setName("TEST USER");

        // Update it.
        volumeService.update(volume);

        // Shouldn't reach this point.
    }

    @Test(expected = ConstraintViolationException.class)
    public void testUpdateFailsIfBadData() throws Exception
    {
        authenticate("user1");

        Volume volume = volumeRepository.findAll().get(0);

        // Edit it.
        volume.setName(""); // Name cannot be blank.

        // Update it.
        volumeService.update(volume);

        // Hibernate does not validate until flush, so flush.
        flushTransaction();

        // Shouldn't reach this point.
    }

    @Test
    public void testDelete()
    {
        authenticate("user1");

        // Find some volume.
        Volume volume = volumeRepository.findAll().get(0);

        // Delete it.
        int id = volume.getId();
        volumeService.delete(id);

        flushTransaction();

        // Ensure it's gone.
        Assert.assertTrue(volumeService.findById(id) == null);
    }

    @Test(expected = AccessDeniedException.class)
    public void testDeleteFailsIfNotTrusted()
    {
        authenticate("user2");

        // Find some volume.
        Volume volume = volumeRepository.findAll().get(0);

        // Delete it.
        int id = volume.getId();
        volumeService.delete(id);

        // Shouldn't reach this point
    }

    @Test(expected = AccessDeniedException.class)
    public void testDeleteFailsIfNotAuthenticated()
    {
        unauthenticate();

        // Find some volume.
        Volume volume = volumeRepository.findAll().get(0);

        // Delete it.
        int id = volume.getId();
        volumeService.delete(id);

        // Shouldn't reach this point
    }
}
