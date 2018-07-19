
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Comic extends DomainEntity {

	private String							name;
	private Integer							numPages;
	private Publisher						publishingCompany;
	private Author							autor;
	private String							image;
	private Collection<ComicComicCharacter>	comicComicCharacter;
	private Collection<UserComic>			userComics;


	public Comic() {
		super();
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String nombre) {
		this.name = nombre;
	}

	@Min(1)
	public int getNumPages() {
		return this.numPages;
	}

	public void setNumPages(final int numPaginas) {
		this.numPages = numPaginas;
	}

	@ManyToOne(optional = false)
	public Publisher getPublishingCompany() {
		return this.publishingCompany;
	}

	public void setPublishingCompany(final Publisher editorial) {
		this.publishingCompany = editorial;
	}

	@ManyToOne(optional = false)
	public Author getAutor() {
		return this.autor;
	}

	public void setAutor(final Author autor) {
		this.autor = autor;
	}

	@URL
	public String getImage() {
		return this.image;
	}

	public void setImage(final String imagen) {
		this.image = imagen;
	}
	@OneToMany(mappedBy = "comic")
	public Collection<ComicComicCharacter> getComicComicCharacter() {
		return this.comicComicCharacter;
	}

	public void setComicComicCharacter(final Collection<ComicComicCharacter> comicComicCharacter) {
		this.comicComicCharacter = comicComicCharacter;
	}

	@OneToMany(mappedBy = "comic")
	public Collection<UserComic> getUserComics() {
		return this.userComics;
	}

	public void setUserComics(final Collection<UserComic> userComics) {
		this.userComics = userComics;
	}

	public void setNumPages(final Integer numPages) {
		this.numPages = numPages;
	}

}
