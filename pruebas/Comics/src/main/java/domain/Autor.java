
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Autor extends DomainEntity {

	private String				nombre;
	private Date				fechaNacimiento;
	private String				lugarNacimiento;
	private Collection<Comic>	comics;


	//private String imagen;

	@NotBlank
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	@Past
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getFechaNacimiento() {
		return this.fechaNacimiento;
	}

	public void setFechaNacimiento(final Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getLugarNacimiento() {
		return this.lugarNacimiento;
	}

	public void setLugarNacimiento(final String lugarNacimiento) {
		this.lugarNacimiento = lugarNacimiento;
	}

	@OneToMany(mappedBy = "autor")
	public Collection<Comic> getComics() {
		return this.comics;
	}

	public void setComics(final Collection<Comic> comics) {
		this.comics = comics;
	}

}
