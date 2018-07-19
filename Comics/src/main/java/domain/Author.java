
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
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Author extends DomainEntity {

	private String				name;
	private Date				birthDate;
	private String				birthPlace;
	private Collection<Comic>	comics;
	private String				description;
	private String				image;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String nombre) {
		this.name = nombre;
	}

	@Past
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getBirthDate() {
		return this.birthDate;

	}

	public void setBirthDate(final Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthPlace() {
		return this.birthPlace;
	}

	public void setBirthPlace(final String lugarNacimiento) {
		this.birthPlace = lugarNacimiento;
	}

	@OneToMany(mappedBy = "autor")
	public Collection<Comic> getComics() {
		return this.comics;
	}

	public void setComics(final Collection<Comic> comics) {
		this.comics = comics;
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

}
