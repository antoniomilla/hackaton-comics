package domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import cz.jirutka.validator.collection.constraints.EachNotBlank;
import cz.jirutka.validator.collection.constraints.EachNotNull;
import cz.jirutka.validator.collection.constraints.EachURL;
import validators.CustomValidator;
import validators.PastOrPresent;

@Entity
@Access(AccessType.PROPERTY)
public class Sale extends DomainEntity {
    private Comic comic;
    private User user;
    private String name;
    private String description;
    private double price;
    private SaleStatus status;
    private List<User> interestedUsers = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private Date creationTime;
    private User userSoldTo;

    @ManyToOne(optional = false)
    @Valid
    public Comic getComic()
    {
        return comic;
    }

    public void setComic(Comic comic)
    {
        this.comic = comic;
    }

    @ManyToOne(optional = false)
    @Valid
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    @NotBlank
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @NotBlank
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Min(0)
    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    @Enumerated(EnumType.STRING)
    public SaleStatus getStatus()
    {
        return status;
    }

    public void setStatus(SaleStatus status)
    {
        this.status = status;
    }

    @ManyToMany
    @Valid
    public List<User> getInterestedUsers()
    {
        return interestedUsers;
    }

    public void setInterestedUsers(List<User> interestedUsers)
    {
        this.interestedUsers = interestedUsers;
    }

    @ElementCollection
    @EachNotBlank
    @EachNotNull
    @EachURL
    public List<String> getImages()
    {
        return images;
    }

    public void setImages(List<String> images)
    {
        this.images = images;
    }

    @NotNull
    @PastOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    public Date getCreationTime()
    {
        return creationTime;
    }
    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }

    @ManyToOne
    @Valid
    public User getUserSoldTo()
    {
        return userSoldTo;
    }

    public void setUserSoldTo(User userSoldTo)
    {
        this.userSoldTo = userSoldTo;
    }

    @CustomValidator
    @Transient
    public boolean isValid()
    {
        if (userSoldTo == user) return false;
        if (status == SaleStatus.COMPLETED) {
            if (userSoldTo == null) return false;
        }

        return true;
    }
}

