
package domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import validators.CustomValidator;
import validators.HasCustomValidators;

@Entity
@Access(AccessType.PROPERTY)
@HasCustomValidators
@Table(indexes = {
        @Index(columnList = "user_id, comic_id", unique = true),
        @Index(columnList = "user_id, status") // findByUserAndStatus
})
public class UserComic extends DomainEntity {
    private boolean starred;
    private Integer score;
    private UserComicStatus status = UserComicStatus.NONE;
    private User user;
    private Comic comic;
    private List<Volume> readVolumes = new ArrayList<>();
    private int readVolumeCount;

    public UserComic()
    {

    }

    public UserComic(User user, Comic comic)
    {
        this.user = user;
        this.comic = comic;
    }

    public boolean getStarred()
    {
        return this.starred;
    }
    public void setStarred(final boolean starred)
    {
        this.starred = starred;
    }

    @Range(min = 0, max = 10)
    public Integer getScore()
    {
        return this.score;
    }
    public void setScore(final Integer score)
    {
        this.score = score;
    }

    @Enumerated(EnumType.STRING)
    public UserComicStatus getStatus()
    {
        return this.status;
    }
    public void setStatus(final UserComicStatus status)
    {
        this.status = status;
    }

    @NotNull
    @ManyToOne(optional = false)
    public User getUser()
    {
        return this.user;
    }
    public void setUser(final User user)
    {
        this.user = user;
    }

    @NotNull
    @ManyToOne(optional = false)
    public Comic getComic()
    {
        return this.comic;
    }
    public void setComic(final Comic comic)
    {
        this.comic = comic;
    }

    @NotNull
    @ManyToMany
    public List<Volume> getReadVolumes()
    {
        return readVolumes;
    }
    public void setReadVolumes(List<Volume> readVolumes)
    {
        this.readVolumes = readVolumes;
    }

    /** Cached read volume count, for performance reasons. (to avoid pulling in the entire list when we need the count only) */
    public int getReadVolumeCount()
    {
        return readVolumeCount;
    }
    public void setReadVolumeCount(int readVolumeCount)
    {
        this.readVolumeCount = readVolumeCount;
    }

    @CustomValidator
    @Transient
    public boolean isValidReadVolumeCount()
    {
        return readVolumes.size() == readVolumeCount;
    }
}
