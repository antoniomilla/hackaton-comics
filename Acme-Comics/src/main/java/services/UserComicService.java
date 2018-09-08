package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Comic;
import domain.User;
import domain.UserComic;
import domain.UserComicStatus;
import domain.Volume;
import exceptions.VolumesNotReadException;
import repositories.UserComicRepository;
import security.Authority;
import utilities.CheckUtils;

@Service
@Transactional
public class UserComicService {
    @Autowired private UserComicRepository repository;
    @Autowired private UserService userService;

    public UserComic getByUserAndComic(User user, Comic comic)
    {
        UserComic userComic = repository.findByUserAndComic(user, comic);
        if (userComic == null) {
            userComic = new UserComic(user, comic);
        }
        return userComic;
    }

    // Returns the list of UserComics for the specified comics, in the same order,
    // creating UserComics with default values if they dont already exist in the database.
    public List<UserComic> getForComics(User user, List<Comic> comics)
    {
        // IN operator causes error if predicate list is empty.
        if (comics.isEmpty()) return new ArrayList<>();

        // IN operator causes error if predicate list is too large.
        // This is not exactly efficient, but it shouldn't be used with this many comics often.
        // With server-side list paging performance here is not an issue,
        // but as I understand it implementing server-side list paging support is not required.
        if (comics.size() > 500) {
            List<UserComic> result = new ArrayList<>();
            List<Comic> comics2 = new ArrayList<>();
            for (int i = 0; i < comics.size(); i++) {
                comics2.add(comics.get(i));
                if (i > 0 && i % 500 == 0) {
                    result.addAll(getForComics(user, comics2));
                    comics2.clear();
                }
            }
            result.addAll(getForComics(user, comics2));
            return result;
        }

        List<UserComic> result = repository.findByUserAndComics(user, comics);
        Map<Comic, UserComic> map = new HashMap<>();
        for (UserComic userComic : result) {
            map.put(userComic.getComic(), userComic);
        }
        result.clear();
        for (Comic comic : comics) {
            UserComic userComic = map.get(comic);
            if (userComic == null) {
                userComic = new UserComic(user, comic);
            }
            result.add(userComic);
        }
        return result;
    }

    public void star(Comic comic)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        UserComic userComic = getByUserAndComic(userService.getPrincipal(), comic);
        userComic.setStarred(true);
        repository.save(userComic);
    }

    public void unstar(Comic comic)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        UserComic userComic = getByUserAndComic(userService.getPrincipal(), comic);
        userComic.setStarred(false);
        repository.save(userComic);
    }

    public void setStatus(Comic comic, UserComicStatus status) throws VolumesNotReadException
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        UserComic userComic = getByUserAndComic(userService.getPrincipal(), comic);
        if (status == UserComicStatus.COMPLETED) {
            if (userComic.getReadVolumeCount() != comic.getVolumeCount()) {
                throw new VolumesNotReadException();
            }
        }
        userComic.setStatus(status);
        repository.save(userComic);
    }

    public void rate(Comic comic, int score)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        UserComic userComic = getByUserAndComic(userService.getPrincipal(), comic);
        userComic.setScore(score);
        repository.save(userComic);
    }

    public void unrate(Comic comic)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        UserComic userComic = getByUserAndComic(userService.getPrincipal(), comic);
        userComic.setScore(null);
        repository.save(userComic);
    }

    public void readVolume(Volume volume)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        UserComic userComic = getByUserAndComic(userService.getPrincipal(), volume.getComic());
        userComic.getReadVolumes().add(volume);
        userComic.setReadVolumeCount(userComic.getReadVolumes().size());
        repository.save(userComic);
    }

    public void unreadVolume(Volume volume)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        UserComic userComic = getByUserAndComic(userService.getPrincipal(), volume.getComic());
        if (userComic.getStatus() == UserComicStatus.COMPLETED) {
            userComic.setStatus(UserComicStatus.PLANNING_TO_READ);
            repository.save(userComic);
        }
        userComic.getReadVolumes().remove(volume);
        userComic.setReadVolumeCount(userComic.getReadVolumes().size());
        repository.save(userComic);
    }

    void uncompleteUsers(List<UserComic> userComics)
    {
        for (UserComic userComic : userComics) {
            if (userComic.getStatus() == UserComicStatus.COMPLETED) {
                userComic.setStatus(UserComicStatus.PLANNING_TO_READ);
                repository.save(userComic);
            }
        }
    }

    public List<UserComic> findByUserAndStatus(User user, UserComicStatus status)
    {
        return repository.findByUserAndStatus(user, status);
    }
}
