
package domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import cz.jirutka.validator.collection.constraints.EachNotBlank;
import cz.jirutka.validator.collection.constraints.EachNotNull;
import validators.NullOrNotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class ComicCharacter extends DomainEntity {

	private String							name;
	private String							alias;
	private String							city;
	private String							image;
	private String							description;
	private Publisher						publisher;
	private List<ComicComicCharacter> comicComicCharacters = new ArrayList<>();
	private List<String>				otherAliases = new ArrayList<>();
	private String							firstAppearance;
	private List<Comment>				comments = new ArrayList<>();

	@NotBlank
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getAlias() {
		return this.alias;
	}
	public void setAlias(final String alias) {
		this.alias = alias;
	}

	@NullOrNotBlank
	public String getCity() {
		return this.city;
	}
	public void setCity(final String city) {
		this.city = city;
	}

	@URL
	@NullOrNotBlank
	public String getImage() {
		return this.image;
	}
	public void setImage(final String image) {
		this.image = image;
	}

	@NullOrNotBlank
	public String getFirstAppearance() {
		return this.firstAppearance;
	}
	public void setFirstAppearance(final String firstAppearance) {
		this.firstAppearance = firstAppearance;
	}

	@Valid
	@ElementCollection
	@EachNotNull
	@EachNotBlank
	public List<String> getOtherAliases() {
		return this.otherAliases;
	}
	public void setOtherAliases(final List<String> otherAliases) {
		this.otherAliases = otherAliases;
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
	@OneToMany(mappedBy = "comicCharacter")
	@Cascade(CascadeType.DELETE)
	public List<ComicComicCharacter> getComicComicCharacters() {
		return this.comicComicCharacters;
	}
	public void setComicComicCharacters(final List<ComicComicCharacter> comicComicCharacters) {
		this.comicComicCharacters = comicComicCharacters;
	}

	@NotNull
	@OneToMany(mappedBy = "comicCharacter")
	@Cascade(CascadeType.DELETE)
	public List<Comment> getComments() {
		return this.comments;
	}
	public void setComments(final List<Comment> comments) {
		this.comments = comments;
	}

	@NotNull
	@ManyToOne(optional = false)
	public Publisher getPublisher() {
		return this.publisher;
	}
	public void setPublisher(final Publisher publisher) {
		this.publisher = publisher;
	}
}
