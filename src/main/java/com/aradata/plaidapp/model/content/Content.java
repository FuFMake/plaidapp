package com.aradata.plaidapp.model.content;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedList;

public class Content {

	@Id
	protected String id;

	@NotNull
	@NotBlank
	protected String title;

	@NotNull
	protected Type type;

	@NotNull
	@NotBlank
	protected String url;

	@NotNull
	@NotBlank
	protected String description;

	@NotNull
	@NotBlank
	protected String text;

	protected int likes = 0;

	protected LinkedList<Image> images;

	public LinkedList<Image> getImages() {
		return images;
	}

	public void setImages(LinkedList<Image> images) {
		this.images = images;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	protected Date createdAt = new Date();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void addImage(Image image) {
		if (images == null)
			images = new LinkedList<>();
		images.add(image);
	}
}
