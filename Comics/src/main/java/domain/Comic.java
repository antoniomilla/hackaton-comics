
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Comic extends DomainEntity {

	private String						name;
	private Publisher					publisher;
	private Author						author;
	private Collection<ComicCharacter>	characters;
	private String						image;
	private String						description;
	private Collection<String>			tags;


	public Comic() {
		super();
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@ManyToOne(optional = false)
	public Publisher getPublisher() {
		return this.publisher;
	}

	public void setPublisher(final Publisher publisher) {
		this.publisher = publisher;
	}

	@ManyToMany
	//@NotEmpty
	public Collection<ComicCharacter> getCharacters() {
		return this.characters;
	}

	public void setCharacters(final Collection<ComicCharacter> comicCharacters) {
		this.characters = comicCharacters;
	}

	@ManyToOne(optional = false)
	public Author getAuthor() {
		return this.author;
	}

	public void setAutor(final Author author) {
		this.author = author;
	}

	@URL
	public String getImage() {
		return this.image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotBlank
	//@Pattern("[^\s]")
	public Collection<String> getTags() {
		return this.tags;
	}

	public void setTags(final Collection<String> tags) {
		this.tags = tags;
	}

}
