
package domain;

import org.hibernate.search.annotations.Field;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import security.UserAccount;
import validators.NullOrNotBlank;
import validators.PastOrPresent;

@Entity
@Access(AccessType.PROPERTY)
public abstract class Actor extends DomainEntity {

    private String nickname;
    private String description;
    private Date creationTime;
    private UserAccount userAccount;
    private List<MessageFolder> messageFolders = new ArrayList<>();

    @NotBlank
    @Field
    public String getNickname()
    {
        return this.nickname;
    }
    public void setNickname(final String nickname)
    {
        this.nickname = nickname;
    }

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    public UserAccount getUserAccount()
    {
        return this.userAccount;
    }
    public void setUserAccount(final UserAccount userAccount)
    {
        this.userAccount = userAccount;
    }

    @NotNull
    @PastOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    public Date getCreationTime()
    {
        return this.creationTime;
    }
    public void setCreationTime(final Date creationTime)
    {
        this.creationTime = creationTime;
    }

    @NotNull
    @OneToMany(mappedBy = "actor")
    public List<MessageFolder> getMessageFolders()
    {
        return this.messageFolders;
    }
    public void setMessageFolders(final List<MessageFolder> folders)
    {
        this.messageFolders = folders;
    }

    @NullOrNotBlank
    @Lob
    @Field
    public String getDescription()
    {
        return this.description;
    }
    public void setDescription(final String description)
    {
        this.description = description;
    }

    @Transient
    public boolean isUser() { return false; }

    @Transient
    public boolean isAdministrator() { return false; }
}
