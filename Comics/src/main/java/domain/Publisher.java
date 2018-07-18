
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Publisher extends DomainEntity {

	private String					name;
	private Date					foundation;
	private String					description;
	private Collection<Comic>		comics;
	private Collection<ComicCharacter>	characters;
	private String					logo;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String nombre) {
		this.name = nombre;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getFoundation() {
		return this.foundation;
	}

	public void setFoundation(final Date fundacion) {
		this.foundation = fundacion;
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

	@URL
	public String getLogo() {
		return this.logo;
	}

	public void setLogo(final String logo) {
		this.logo = logo;
	}

}
