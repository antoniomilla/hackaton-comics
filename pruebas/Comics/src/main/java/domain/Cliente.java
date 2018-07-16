
package domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Cliente extends Actor {

	private Date				fechaAlta;
	private char				nivel;
	private Collection<Comic>	comicsLeidos;


	public Cliente() {
		super();
		this.comicsLeidos = new HashSet<Comic>();

	}

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getFechaAlta() {
		return this.fechaAlta;
	}

	public void setFechaAlta(final Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@NotNull
	public char getNivel() {
		return this.nivel;
	}

	public void setNivel(final char nivel) {
		this.nivel = nivel;
	}

	@ManyToMany
	public Collection<Comic> getComicsLeidos() {
		return this.comicsLeidos;
	}

	public void setComicsLeidos(final Collection<Comic> comicsLeidos) {
		this.comicsLeidos = comicsLeidos;
	}

}
