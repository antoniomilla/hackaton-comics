
package domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import validators.NullOrNotBlank;
import validators.PastOrPresent;

@Entity
@Access(AccessType.PROPERTY)
public class Author extends DomainEntity {

    private String name;
    private Date birthDate;
    private String birthPlace;
    private List<Comic> comics = new ArrayList<>();
    private String description;
    private String image;
    private List<Volume> volumes = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();


    @NotBlank
    public String getName()
    {
        return this.name;
    }
    public void setName(final String name)
    {
        this.name = name;
    }

    @PastOrPresent
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    public Date getBirthDate()
    {
        return this.birthDate;
    }
    public void setBirthDate(final Date birthDate)
    {
        this.birthDate = birthDate;
    }

    @NullOrNotBlank
    public String getBirthPlace()
    {
        return this.birthPlace;
    }
    public void setBirthPlace(final String birthPlace)
    {
        this.birthPlace = birthPlace;
    }

    @NotNull
    @OneToMany(mappedBy = "author")
    @Cascade(CascadeType.DELETE)
    public List<Comic> getComics()
    {
        return this.comics;
    }
    public void setComics(final List<Comic> comics)
    {
        this.comics = comics;
    }

    @URL
    @NullOrNotBlank
    public String getImage()
    {
        return this.image;
    }
    public void setImage(final String imagen)
    {
        this.image = imagen;
    }

    @NullOrNotBlank
    @Lob
    public String getDescription()
    {
        return this.description;
    }
    public void setDescription(final String description)
    {
        this.description = description;
    }

    @NotNull
    @OneToMany(mappedBy = "author")
    @Cascade(CascadeType.DELETE)
    public List<Volume> getVolumes()
    {
        return this.volumes;
    }
    public void setVolumes(final List<Volume> volumes)
    {
        this.volumes = volumes;
    }

    @NotNull
    @OneToMany(mappedBy = "author")
    @Cascade(CascadeType.DELETE)
    public List<Comment> getComments()
    {
        return this.comments;
    }
    public void setComments(final List<Comment> comments)
    {
        this.comments = comments;
    }

}
