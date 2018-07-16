
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Personaje extends DomainEntity {

	private String				nombre;
	private String				alias;
	private String				ciudad;
	private Editorial			editorial;
	private Collection<Comic>	apareceEn;


	//private String imagen;

	public Personaje() {
		super();
	}

	@NotBlank
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	@NotBlank
	public String getAlias() {
		return this.alias;
	}

	public void setAlias(final String alias) {
		this.alias = alias;
	}

	public String getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(final String ciudad) {
		this.ciudad = ciudad;
	}

	@ManyToOne(optional = false)
	public Editorial getEditorial() {
		return this.editorial;
	}

	public void setEditorial(final Editorial editorial) {
		this.editorial = editorial;
	}

	@ManyToMany
	public Collection<Comic> getApareceEn() {
		return this.apareceEn;
	}

	public void setApareceEn(final Collection<Comic> apareceEn) {
		this.apareceEn = apareceEn;
	}

}
