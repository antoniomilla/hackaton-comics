package utilities;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;

import java.text.MessageFormat;

import javax.persistence.OptimisticLockException;

import domain.Actor;
import domain.Author;
import domain.DomainEntity;
import domain.User;
import security.Authority;
import security.LoginService;
import security.UserAccount;

public class PolicyCheckUtils {
    public static void checkCanEditContent(Actor principal)
    {
        CheckUtils.checkPrincipalAuthority(Authority.ADMINISTRATOR, Authority.USER);
        if (principal instanceof User) {
            CheckUtils.checkTrue(((User) principal).getTrusted());
        }
    }
}
