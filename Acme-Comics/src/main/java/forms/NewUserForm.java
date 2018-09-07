package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import domain.Actor;
import domain.User;
import security.Authority;
import security.UserAccount;
import validators.CustomValidator;
import validators.HasCustomValidators;
import validators.UseConstraintsFrom;

@Access(AccessType.PROPERTY)
@HasCustomValidators
public class NewUserForm {
    private String username;
    private String password;
    private String repeatPassword;
    private String nickname;

    private boolean agreesToTerms;

    public NewUserForm()
    {

    }

    @CustomValidator(message = "{misc.error.passwordDoesNotMatch}", applyOn = "repeatPassword")
    public boolean isValidPasswordsMatch()
    {
        if (getPassword() == null || getRepeatPassword() == null) return false;
        return getRepeatPassword().equals(getPassword());
    }

    @UseConstraintsFrom(klazz = UserAccount.class, property = "username")
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @UseConstraintsFrom(klazz = UserAccount.class, property = "password")
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @UseConstraintsFrom(klazz = UserAccount.class, property = "password")
    public String getRepeatPassword()
    {
        return repeatPassword;
    }
    public void setRepeatPassword(String repeatPassword)
    {
        this.repeatPassword = repeatPassword;
    }

    @UseConstraintsFrom(klazz = Actor.class, property = "nickname")
    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    @AssertTrue(message = "{users.mustAgreeToTerms}")
    public boolean getAgreesToTerms()
    {
        return agreesToTerms;
    }

    public void setAgreesToTerms(boolean agreesToTerms)
    {
        this.agreesToTerms = agreesToTerms;
    }
}
