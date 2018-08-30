
package services;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import domain.User;
import domain.UserComic;
import domain.UserComicStatus;
import exceptions.ResourceNotFoundException;
import exceptions.ResourceNotUniqueException;
import repositories.UserRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import utilities.CheckUtils;

@Service
@Transactional
public class UserService {
    @Autowired private UserRepository repository;
    @Autowired private ComicService comicService;
    @Autowired private MessageFolderService messageFolder;
    @Autowired private UserAccountService userAccountService;
    @Autowired private MessageFolderService messageFolderService;

    public List<User> findAll()
    {
        return repository.findAll();
    }

    public User getById(int id)
    {
        User user = repository.findOne(id);
        if (user == null) throw new ResourceNotFoundException();
        return user;
    }

    public User getByIdForShow(int id)
    {
        return getById(id);
    }

    public User updateUserLevelForAdmin(User user)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);
        internalUpdateUserLevel(user);
        return repository.save(user);
    }

    public User updateUserLevelIfRequired(User user)
    {
        if (DateUtils.isSameDay(new Date(), user.getLastLevelUpdateTime())) return user;

        internalUpdateUserLevel(user);
        return repository.save(user);
    }

    private void internalUpdateUserLevel(User user)
    {
        int score = 0;

        boolean hasStarred = false;

        for (UserComic userComic : user.getUserComics()) {
            if (userComic.getStatus() == UserComicStatus.PLANNING_TO_READ) score += 1;
            if (userComic.getStatus() == UserComicStatus.READING) score += 1;
            if (userComic.getStatus() == UserComicStatus.DROPPED) score += 1;
            if (userComic.getStatus() == UserComicStatus.COMPLETED) score += 3;
            score += userComic.getReadVolumeCount();
            if (userComic.getScore() != null) score += 1;

            if (userComic.getStarred()) hasStarred = true;
        }

        score += user.getFriends().size();
        if (hasStarred) score += 5;

        if (score <= 15) {
            user.setLevel("C");
        } else if (score <= 25) {
            user.setLevel("B");
        } else if (score <= 40) {
            user.setLevel("A");
        } else {
            user.setLevel("S");
        }
        user.setLastLevelUpdateTime(new Date());
    }


    public User create(String username, String password, String nickname) throws ResourceNotUniqueException
    {
        CheckUtils.checkUnauthenticated();

        User user = new User();
        user.setNickname(nickname);
        user.setUserAccount(this.userAccountService.create(username, password, Authority.USER));
        internalUpdateUserLevel(user);

        User result = repository.save(user);
        messageFolderService.createSystemFoldersForNewActor(result);
        return result;
    }

    public User findPrincipal()
    {
        if (!LoginService.isAuthenticated()) return null;

        final UserAccount userAccount = LoginService.getPrincipal();
        if (userAccount == null) return null;

        return repository.findByUserAccount(userAccount);
    }

    public User getPrincipal()
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        User user = findPrincipal();
        Assert.notNull(user);
        return user;
    }

    public void friend(User friend)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        CheckUtils.checkExists(friend);

        User user = getPrincipal();

        CheckUtils.checkFalse(user.equals(friend));
        user.getFriends().add(friend);

        repository.save(user);
    }

    public void unfriend(User friend)
    {
        CheckUtils.checkPrincipalAuthority(Authority.USER);
        CheckUtils.checkExists(friend);

        User user = getPrincipal();
        user.getFriends().remove(friend);

        repository.save(user);
    }

    public void trust(User user)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);
        CheckUtils.checkExists(user);

        user.setTrusted(true);

        repository.save(user);
    }

    public void untrust(User user)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);
        CheckUtils.checkExists(user);

        user.setTrusted(false);

        repository.save(user);
    }

    public void block(User user, String blockReason)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);
        CheckUtils.checkExists(user);

        user.setBlocked(true);
        user.setBlockReason(blockReason);

        repository.save(user);
    }

    public void unblock(User user)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);
        CheckUtils.checkExists(user);

        user.setBlocked(false);
        user.setBlockReason(null);

        repository.save(user);
    }

    public User getByIdForBlock(int id)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR);
        return getById(id);
    }
}

