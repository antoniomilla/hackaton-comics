
package domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Comic extends DomainEntity {

	private String							name;
	private Publisher						publisher;
	private Author							author;
	private String							image;
	private Collection<ComicComicCharacter>	comicComicCharacter;
	private Collection<UserComic>			userComics;
	private String							description;
	private Collection<String>				tags;


	public Comic() {
		super();
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@ManyToOne(optional = false)
	public Publisher getPublisher() {
		return this.publisher;
	}

	public void setPublisher(final Publisher publisher) {
		this.publisher = publisher;
	}

	@ManyToOne(optional = false)
	public Author getAuthor() {
		return this.author;
	}

	public void setAutor(final Author author) {
		this.author = author;
	}

	@URL
	public String getImage() {
		return this.image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotBlank
	//@Pattern("[^\s]")
	public Collection<String> getTags() {
		return this.tags;
	}

	public void setTags(final Collection<String> tags) {
		this.tags = tags;
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

	public Collection<ComicCharacter> getCharacters() {
		final Collection<ComicCharacter> res = new ArrayList<>();
		for (final ComicComicCharacter c : this.comicComicCharacter)
			res.add(c.getComicCharacter());
		return res;

	}
}
