
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import validators.CustomValidator;
import validators.HasCustomValidators;
import validators.PastOrPresent;

@Entity
@Access(AccessType.PROPERTY)
@HasCustomValidators
@Table(indexes = {
		@Index(columnList = "user_id, creationTime"), // CommentRepository.findByUserOrderByCreationTimeDesc
		@Index(columnList = "publisher_id, creationTime"), // CommentRepository.findByPublisherOrderByCreationTimeDesc
		@Index(columnList = "author_id, creationTime"), // CommentRepository.findByAuthorOrderByCreationTimeDesc
		@Index(columnList = "comic_id, creationTime"), // CommentRepository.findByComicOrderByCreationTimeDesc
		@Index(columnList = "volume_id, creationTime"), // CommentRepository.findByVolumeOrderByCreationTimeDesc
		@Index(columnList = "comicCharacter_id, creationTime"), // CommentRepository.findByComicCharacterOrderByCreationTimeDesc
		@Index(columnList = "sale_id, creationTime"), // CommentRepository.findBySaleOrderByCreationTimeDesc
})
public class Comment extends DomainEntity {
	private String			text;
	private Date			creationTime;
	private User			user;
	private Publisher		publisher;
	private Author			author;
	private Comic			comic;
	private Volume			volume;
	private ComicCharacter	comicCharacter;
	private Sale sale;

	@CustomValidator
	@Transient
	public boolean isValid()
	{
		int parentCount = 0;

		if (publisher != null) parentCount += 1;
		if (author != null) parentCount += 1;
		if (comic != null) parentCount += 1;
		if (volume != null) parentCount += 1;
		if (comicCharacter != null) parentCount += 1;
		if (sale != null) parentCount += 1;

		if (parentCount != 1) return false;
		return true;
	}

	@NotBlank
	@Lob
	public String getText() {
		return this.text;
	}
	public void setText(final String text) {
		this.text = text;
	}

	@NotNull
	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(final Date creationTime) {
		this.creationTime = creationTime;
	}

	@NotNull
	@ManyToOne(optional = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}
	@ManyToOne
	public Publisher getPublisher() {
		return this.publisher;
	}

	public void setPublisher(final Publisher publisher) {
		this.publisher = publisher;
	}
	@ManyToOne
	public Author getAuthor() {
		return this.author;
	}

	public void setAuthor(final Author author) {
		this.author = author;
	}
	@ManyToOne
	public Comic getComic() {
		return this.comic;
	}

	public void setComic(final Comic comic) {
		this.comic = comic;
	}
	@ManyToOne
	public Volume getVolume() {
		return this.volume;
	}

	public void setVolume(final Volume volume) {
		this.volume = volume;
	}
	@ManyToOne
	public ComicCharacter getComicCharacter() {
		return this.comicCharacter;
	}

	public void setComicCharacter(final ComicCharacter comicCharacter) {
		this.comicCharacter = comicCharacter;
	}

	@ManyToOne
	public Sale getSale()
	{
		return sale;
	}

	public void setSale(Sale sale)
	{
		this.sale = sale;
	}
}
