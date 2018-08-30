
package domain;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import validators.NullOrNotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Volume extends DomainEntity {

	private int				orderNumber;
	private String				name;
	private Date				releaseDate;
	private Integer				chapterCount;
	private String				description;
	private String				image;
	private Comic				comic;
	private Author				author;
	private List<Comment>	comments;

	public Volume() {}
	public Volume(Comic comic)
	{
		this.comic = comic;
	}


    public int getOrderNumber() {
		return this.orderNumber;
	}
	public void setOrderNumber(final int orderNumber) {
		this.orderNumber = orderNumber;
	}

	@NotBlank
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@NotNull
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
	public void setChapterCount(Integer chapterCount) {
		this.chapterCount = chapterCount;
	}

	@NullOrNotBlank
	@Lob
	public String getDescription() {
		return this.description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}

	@URL
	@NullOrNotBlank
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

	@ManyToOne(optional = false)
	public Author getAuthor() {
		return this.author;
	}
	public void setAuthor(final Author author) {
		this.author = author;
	}

	@OneToMany(mappedBy = "volume")
	@Cascade(CascadeType.DELETE)
	public List<Comment> getComments() {
		return this.comments;
	}
	public void setComments(final List<Comment> comments) {
		this.comments = comments;
	}

}
