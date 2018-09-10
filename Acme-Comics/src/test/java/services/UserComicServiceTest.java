package services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import domain.Comic;
import domain.User;
import domain.UserComic;
import domain.UserComicStatus;
import domain.Volume;
import exceptions.VolumesNotReadException;
import repositories.ComicRepository;
import utilities.AbstractTest;

/**
 * Tests the following use cases from the requirements document:
 * - Rate a comic series or remove a previous rating, star or unstar it, and change his status with it.
 * - Mark a comic volume as read or unread.
 */
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UserComicServiceTest extends AbstractTest {
    @Autowired private UserComicService userComicService;
    @Autowired private ComicRepository comicRepository;

    @Test
    public void testStar()
    {
        authenticate("user1");

        Comic comic = comicRepository.findByName("Flashpoint");
        userComicService.star(comic);

        flushTransaction();
        UserComic userComic = userComicService.getByUserAndComic((User) getPrincipal(), comic);
        Assert.assertTrue(userComic.getStarred());

        userComicService.unstar(comic);

        flushTransaction();
        userComic = userComicService.getByUserAndComic((User) getPrincipal(), comic);
        Assert.assertFalse(userComic.getStarred());

        userComicService.star(comic);

        flushTransaction();
        userComic = userComicService.getByUserAndComic((User) getPrincipal(), comic);
        Assert.assertTrue(userComic.getStarred());
    }

    @Test
    public void testRate()
    {
        authenticate("user1");

        Comic comic = comicRepository.findByName("Flashpoint");
        userComicService.rate(comic, 6);

        flushTransaction();
        UserComic userComic = userComicService.getByUserAndComic((User) getPrincipal(), comic);
        Assert.assertTrue(userComic.getScore().equals(6));

        userComicService.rate(comic, 8);

        flushTransaction();
        userComic = userComicService.getByUserAndComic((User) getPrincipal(), comic);
        Assert.assertTrue(userComic.getScore().equals(8));

        userComicService.unrate(comic);

        flushTransaction();
        userComic = userComicService.getByUserAndComic((User) getPrincipal(), comic);
        Assert.assertTrue(userComic.getScore() == null);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testRateFailsIfOutOfRange1()
    {
        authenticate("user1");

        Comic comic = comicRepository.findByName("Flashpoint");
        userComicService.rate(comic, 11);

        flushTransaction();
    }

    @Test(expected = ConstraintViolationException.class)
    public void testRateFailsIfOutOfRange2()
    {
        authenticate("user1");

        Comic comic = comicRepository.findByName("Flashpoint");
        userComicService.rate(comic, -1);

        flushTransaction();
    }

    @Test
    public void testSetStatus() throws Exception
    {
        authenticate("user1");

        Comic comic = comicRepository.findByName("Flashpoint");

        UserComic userComic;

        for (UserComicStatus status: Arrays.asList(UserComicStatus.NONE, UserComicStatus.PLANNING_TO_READ, UserComicStatus.READING, UserComicStatus.DROPPED)) {
            userComicService.setStatus(comic, status);
            flushTransaction();
            comic = comicRepository.findByName("Flashpoint");
            userComic = userComicService.getByUserAndComic((User) getPrincipal(), comic);
            Assert.assertTrue(userComic.getStatus().equals(status));
        }

    }

    @Test
    public void testSetStatus2() throws Exception
    {
        authenticate("user1");

        UserComic userComic;

        Comic comic = comicRepository.findByName("Flashpoint");

        Assert.assertTrue(comic.getVolumes().size() > 0);
        for (Volume volume : comic.getVolumes()) {
            userComicService.readVolume(volume);
        }

        flushTransaction();
        comic = comicRepository.findByName("Flashpoint");
        userComicService.setStatus(comic, UserComicStatus.COMPLETED);
        flushTransaction();
        comic = comicRepository.findByName("Flashpoint");
        userComic = userComicService.getByUserAndComic((User) getPrincipal(), comic);
        Assert.assertTrue(userComic.getStatus().equals(UserComicStatus.COMPLETED));

        userComicService.unreadVolume(comic.getVolumes().get(0));

        flushTransaction();
        comic = comicRepository.findByName("Flashpoint");
        userComic = userComicService.getByUserAndComic((User) getPrincipal(), comic);
        Assert.assertFalse(userComic.getStatus().equals(UserComicStatus.COMPLETED));
    }

    @Test(expected = VolumesNotReadException.class)
    public void testSetStatusCompletedFailsIfNotAllVolumesRead() throws Exception
    {
        authenticate("user1");

        Comic comic = comicRepository.findByName("Flashpoint");

        Assert.assertTrue(comic.getVolumes().size() > 0);
        for (Volume volume : comic.getVolumes()) {
            userComicService.unreadVolume(volume);
        }

        flushTransaction();
        comic = comicRepository.findByName("Flashpoint");
        userComicService.setStatus(comic, UserComicStatus.COMPLETED);

        flushTransaction();
    }
}
