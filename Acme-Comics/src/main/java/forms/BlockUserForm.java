package forms;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import domain.User;
import validators.NullOrNotBlank;
import validators.UseConstraintsFrom;

public class BlockUserForm {
    private User user;
    private String blockReason;

    public BlockUserForm()
    {

    }

    public BlockUserForm(User user)
    {
        this.user = user;
    }

    @Valid
    @NotNull
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    @NullOrNotBlank
    public String getBlockReason()
    {
        return blockReason;
    }

    public void setBlockReason(String blockReason)
    {
        this.blockReason = blockReason;
    }
}
