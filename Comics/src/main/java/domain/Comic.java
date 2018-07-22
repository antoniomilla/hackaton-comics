
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
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
	private Collection<Volume>				volumes;
	private Collection<Comment>				comments;


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

	public void setAuthor(final Author author) {
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

	//@Pattern(regexp = "[^\\s]+")
	//@NotBlank
	@ElementCollection
	public Collection<String> getTags() {
		return this.tags;
	}

	@ElementCollection
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

	@OneToMany(mappedBy = "comic")
	public Collection<Volume> getVolumes() {
		return this.volumes;
	}

	public void setVolumes(final Collection<Volume> volumes) {
		this.volumes = volumes;
	}

	@OneToMany(mappedBy = "comic")
	public Collection<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}

	/*
	 * public Collection<ComicCharacter> getComicCharacters() {
	 * final Collection<ComicCharacter> res = new ArrayList<>();
	 * for (final ComicComicCharacter c : this.comicComicCharacter)
	 * res.add(c.getComicCharacter());
	 * return res;
	 * 
	 * }
	 */
}
