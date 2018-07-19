
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class ComicComicCharacter extends DomainEntity {

	private String			role;
	private Comic			comic;
	private ComicCharacter	comicCharacter;


	@NotBlank
	public String getRole() {
		return this.role;
	}

	public void setRole(final String role) {
		this.role = role;
	}
	@ManyToOne(optional = false)
	public Comic getComic() {
		return this.comic;
	}

	public void setComic(final Comic comic) {
		this.comic = comic;
	}
	@ManyToOne(optional = false)
	public ComicCharacter getComicCharacter() {
		return this.comicCharacter;
	}

	public void setComicCharacter(final ComicCharacter comicCharacter) {
		this.comicCharacter = comicCharacter;
	}

}
