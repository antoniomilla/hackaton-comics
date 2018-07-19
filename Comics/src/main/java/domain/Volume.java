
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Volume extends DomainEntity {

	private Integer				order;
	private String				name;
	private Date				releaseDate;
	private Integer				chapterCount;
	private String				description;
	private String				image;
	private Comic				comic;
	private Collection<Author>	authors;
	private Collection<User>	users;
	private Collection<Comment>	comments;


	public Integer getOrder() {
		return this.order;
	}

	public void setOrder(final Integer order) {
		this.order = order;
	}
	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	@Past
	public Date getReleaseDate() {
		return this.releaseDate;
	}

	public void setReleaseDate(final Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Min(1)
	public Integer getChapterCount() {
		return this.chapterCount;
	}

	public void setChapterCount(final Integer chapterCount) {
		this.chapterCount = chapterCount;
	}
	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
	@URL
	public String getImage() {
		return this.image;
	}

	public void setImage(final String image) {
		this.image = image;
	}
	@ManyToOne(optional = false)
	public Comic getComic() {
		return this.comic;
	}

	public void setComic(final Comic comic) {
		this.comic = comic;
	}
	@ManyToMany
	public Collection<Author> getAuthors() {
		return this.authors;
	}

	public void setAuthors(final Collection<Author> authors) {
		this.authors = authors;
	}
	@ManyToMany
	public Collection<User> getUsers() {
		return this.users;
	}

	public void setUsers(final Collection<User> users) {
		this.users = users;
	}
	@OneToMany
	public Collection<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}

}
