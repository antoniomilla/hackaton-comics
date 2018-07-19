
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
	private Author						autor;
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

	public void setName(final String nombre) {
		this.name = nombre;
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

	public void setCharacters(final Collection<ComicCharacter> personajes) {
		this.characters = personajes;
	}

	@ManyToOne(optional = false)
	public Author getAutor() {
		return this.autor;
	}

	public void setAutor(final Author autor) {
		this.autor = autor;
	}

	@URL
	public String getImage() {
		return this.image;
	}

	public void setImage(final String imagen) {
		this.image = imagen;
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
