
package domain;

import java.util.ArrayList;
import java.util.Collection;
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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import validators.NullOrNotBlank;
import validators.PastOrPresent;

@Entity
@Access(AccessType.PROPERTY)
public class Publisher extends DomainEntity {

	private String						name;
	private Date						foundationDate;
	private String						description;
	private List<Comic>			comics = new ArrayList<>();
	private List<ComicCharacter>	comicCharacters = new ArrayList<>();
	private String						image;
	private List<Comment>			comments = new ArrayList<>();


	@NotBlank
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	@NullOrNotBlank
	@Lob
	public String getDescription() {
		return this.description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@OneToMany(mappedBy = "publisher")
	@Cascade(CascadeType.DELETE)
	public List<Comic> getComics() {
		return this.comics;
	}
	public void setComics(final List<Comic> comics) {
		this.comics = comics;
	}

	@NullOrNotBlank
	@URL
	public String getImage() {
		return this.image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	@PastOrPresent
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getFoundationDate() {
		return this.foundationDate;
	}
	public void setFoundationDate(final Date foundationDate) {
		this.foundationDate = foundationDate;
	}

	@NotNull
	@OneToMany(mappedBy = "publisher")
	@Cascade(CascadeType.DELETE)
	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final List<Comment> comment) {
		this.comments = comment;
	}

	@NotNull
	@OneToMany(mappedBy = "publisher")
	@Cascade(CascadeType.DELETE)
	public List<ComicCharacter> getComicCharacters() {
		return this.comicCharacters;
	}

	public void setComicCharacters(final List<ComicCharacter> comicCharacters) {
		this.comicCharacters = comicCharacters;
	}

}
