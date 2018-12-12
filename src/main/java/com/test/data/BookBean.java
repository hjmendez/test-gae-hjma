package com.test.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Book bean
 * @author hjmendez
 */

@Entity
@ApiModel("Book")
//@Index
public class BookBean {

	@Id
    @ApiModelProperty(value = "ID of book", required=true)
	private Long id;
	
	@ApiModelProperty(value = "name of book", required=true)
	private String name;
	
	@ApiModelProperty(value = "author of book", required=true)
	@Index
	private String author;
	
	@ApiModelProperty(value = "year of book", required=true)
	private Integer year;
	
	@ApiModelProperty(value = "genre of book", required=true)
	private String genre;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	
	
}
