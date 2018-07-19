
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	private Date					registerDate;
	private Character				level;
	private Collection<UserComic>	userComics;


	public User() {
		super();

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

	@OneToMany(mappedBy = "user")
	public Collection<UserComic> getUserComics() {
		return this.userComics;
	}

	public void setUserComics(final Collection<UserComic> userComics) {
		this.userComics = userComics;
	}

	public void setLevel(final Character level) {
		this.level = level;
	}

}
