package com.recipe.recipe.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Author")
@Getter
@Setter
@Accessors
public class Author implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="authorIdGenerator")
	@SequenceGenerator(name = "authorIdGenerator", sequenceName = "authorIdGenerator", allocationSize = 1)
	@Column(name ="authorId")
	private long authorId;
	
	@Column(nullable =false, name ="authorName", unique= true)
	private String authorName;
	
	@Column(nullable =true, name ="contributorSince")
	private Date contributorSince;
	
	 @OneToMany(mappedBy="author", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) 
	 private List<Recipe> recipe = new ArrayList<Recipe>();

}
