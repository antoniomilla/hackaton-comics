
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Comic extends DomainEntity {

	private String					name;
	private int						numPages;
	private Publisher		publishingCompany;
	private Author					autor;
	private Collection<ComicCharacter>	characters;
	private String					image;


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

	@Min(1)
	public int getNumPages() {
		return this.numPages;
	}

	public void setNumPages(final int numPaginas) {
		this.numPages = numPaginas;
	}

	@ManyToOne(optional = false)
	public Publisher getPublishingCompany() {
		return this.publishingCompany;
	}

	public void setPublishingCompany(final Publisher editorial) {
		this.publishingCompany = editorial;
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

}
