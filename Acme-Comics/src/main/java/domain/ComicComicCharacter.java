
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes={
		@Index(columnList = "comic_id, comicCharacter_id", unique = true)
})
public class ComicComicCharacter extends DomainEntity {
	private String			role;
	private Comic			comic;
	private ComicCharacter	comicCharacter;

	public ComicComicCharacter() {}
	public ComicComicCharacter(Comic comic)
	{
		this.comic = comic;
	}

	@NotBlank
	public String getRole() {
		return this.role;
	}

	public void setRole(final String role) {
		this.role = role;
	}
	@NotNull
	@ManyToOne(optional = false)
	public Comic getComic() {
		return this.comic;
	}

	public void setComic(final Comic comic) {
		this.comic = comic;
	}
	@NotNull
	@ManyToOne(optional = false)
	public ComicCharacter getComicCharacter() {
		return this.comicCharacter;
	}

	public void setComicCharacter(final ComicCharacter comicCharacter) {
		this.comicCharacter = comicCharacter;
	}

}
