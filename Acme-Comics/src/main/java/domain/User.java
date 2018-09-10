
package domain;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import validators.CustomValidator;
import validators.HasCustomValidators;
import validators.NullOrNotBlank;
import validators.PastOrPresent;

@Entity
@Access(AccessType.PROPERTY)
@HasCustomValidators
@Indexed
public class User extends Actor {

    private String level;
    private List<UserComic> userComics = new ArrayList<>();
    private boolean blocked;
    private String blockReason;
    private boolean trusted;
    private Date lastLevelUpdateTime;
    private boolean onlyFriendsCanSendDms;
    private List<User> friends = new ArrayList<>();
    private List<Comment> userComments = new ArrayList<>();
    private List<Sale> sales = new ArrayList<>();

    @CustomValidator
    @Transient
    public boolean isValid()
    {
        if (!blocked && blockReason != null) return false;
        return true;
    }

    @NotBlank
    @Size(min = 1, max = 1)
    public String getLevel()
    {
        return this.level;
    }

    public void setLevel(final String level)
    {
        this.level = level;
    }

    @NotNull
    @OneToMany(mappedBy = "user")
    public List<UserComic> getUserComics()
    {
        return this.userComics;
    }

    public void setUserComics(final List<UserComic> userComics)
    {
        this.userComics = userComics;
    }

    public boolean getBlocked()
    {
        return this.blocked;
    }

    public void setBlocked(final boolean blocked)
    {
        this.blocked = blocked;
    }

    @NullOrNotBlank
    public String getBlockReason()
    {
        return this.blockReason;
    }

    public void setBlockReason(final String blockReason)
    {
        this.blockReason = blockReason;
    }

    public boolean getTrusted()
    {
        return this.trusted;
    }

    public void setTrusted(final boolean trusted)
    {
        this.trusted = trusted;
    }

    @NotNull
    @PastOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    public Date getLastLevelUpdateTime()
    {
        return this.lastLevelUpdateTime;
    }

    public void setLastLevelUpdateTime(final Date lastLevelUpdateTime)
    {
        this.lastLevelUpdateTime = lastLevelUpdateTime;
    }

    public boolean getOnlyFriendsCanSendDms()
    {
        return this.onlyFriendsCanSendDms;
    }

    public void setOnlyFriendsCanSendDms(final boolean onlyFriendsCanSendDms)
    {
        this.onlyFriendsCanSendDms = onlyFriendsCanSendDms;
    }

    @NotNull
    @ManyToMany
    public List<User> getFriends()
    {
        return this.friends;
    }

    public void setFriends(final List<User> friends)
    {
        this.friends = friends;
    }

    @NotNull
    @OneToMany(mappedBy = "user")
    public List<Comment> getUserComments()
    {
        return this.userComments;
    }
    public void setUserComments(final List<Comment> comments)
    {
        this.userComments = comments;
    }

    @Transient
    @Override
    public boolean isUser()
    {
        return true;
    }

    @NotNull
    @OneToMany(mappedBy = "user")
    public List<Sale> getSales()
    {
        return this.sales;
    }
    public void setSales(final List<Sale> sales)
    {
        this.sales = sales;
    }
}
