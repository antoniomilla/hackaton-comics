
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Publisher extends DomainEntity {

	private String						name;
	private Date						foundationDate;
	private String						description;
	private Collection<Comic>			comics;
	private Collection<ComicCharacter>	characters;
	private String						image;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String nombre) {
		this.name = nombre;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String descripcion) {
		this.description = descripcion;
	}

	@OneToMany(mappedBy = "publishingCompany")
	public Collection<Comic> getComics() {
		return this.comics;
	}

	public void setComics(final Collection<Comic> comics) {
		this.comics = comics;
	}

	@OneToMany(mappedBy = "publishingCompany")
	public Collection<ComicCharacter> getCharacters() {
		return this.characters;
	}

	public void setCharacters(final Collection<ComicCharacter> personajes) {
		this.characters = personajes;
	}

	@NotBlank
	@URL
	public String getImage() {
		return this.image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	@Past
	public Date getFoundationDate() {
		return this.foundationDate;
	}

	public void setFoundationDate(final Date foundationDate) {
		this.foundationDate = foundationDate;
	}

}
