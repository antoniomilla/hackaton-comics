
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Editorial extends DomainEntity {

	private String					nombre;
	private Date					fundacion;
	private String					descripcion;
	private Collection<Comic>		comics;
	private Collection<Personaje>	personajes;


	//private String logo;

	@NotBlank
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getFundacion() {
		return this.fundacion;
	}

	public void setFundacion(final Date fundacion) {
		this.fundacion = fundacion;
	}

	@NotBlank
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(final String descripcion) {
		this.descripcion = descripcion;
	}

	@OneToMany(mappedBy = "editorial")
	public Collection<Comic> getComics() {
		return this.comics;
	}

	public void setComics(final Collection<Comic> comics) {
		this.comics = comics;
	}

	@OneToMany(mappedBy = "editorial")
	public Collection<Personaje> getPersonajes() {
		return this.personajes;
	}

	public void setPersonajes(final Collection<Personaje> personajes) {
		this.personajes = personajes;
	}

}
