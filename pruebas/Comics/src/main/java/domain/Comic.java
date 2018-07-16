
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Comic extends DomainEntity {

	private String					nombre;
	private int						numPaginas;
	private Editorial				editorial;
	private Autor					autor;
	private Collection<Personaje>	personajes;


	//private String imagen;

	public Comic() {
		super();
	}

	@NotBlank
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	@Min(1)
	public int getNumPaginas() {
		return this.numPaginas;
	}

	public void setNumPaginas(final int numPaginas) {
		this.numPaginas = numPaginas;
	}

	@ManyToOne(optional = false)
	public Editorial getEditorial() {
		return this.editorial;
	}

	public void setEditorial(final Editorial editorial) {
		this.editorial = editorial;
	}

	@ManyToMany
	//@NotEmpty
	public Collection<Personaje> getPersonajes() {
		return this.personajes;
	}

	public void setPersonajes(final Collection<Personaje> personajes) {
		this.personajes = personajes;
	}

	@ManyToOne(optional = false)
	public Autor getAutor() {
		return this.autor;
	}

	public void setAutor(final Autor autor) {
		this.autor = autor;
	}

}
