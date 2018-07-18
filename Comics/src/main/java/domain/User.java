
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
public class User extends Actor {

	private Date				registerDate;
	private char				level;
	private Collection<Comic>	comicsRead;


	public User() {
		super();
		this.comicsRead = new HashSet<Comic>();

	}

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(final Date fechaAlta) {
		this.registerDate = fechaAlta;
	}

	@NotNull
	public char getLevel() {
		return this.level;
	}

	public void setLevel(final char nivel) {
		this.level = nivel;
	}

	@ManyToMany
	public Collection<Comic> getComicsRead() {
		return this.comicsRead;
	}

	public void setComicsRead(final Collection<Comic> comicsLeidos) {
		this.comicsRead = comicsLeidos;
	}

}
