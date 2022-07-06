package com.recipe.recipe.bean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "Ingredient")
@Getter
@Setter
@Accessors
public class RecipeIngredients {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ingredientId")
	@SequenceGenerator(name = "ingredientIdGenerator", sequenceName = "ingredientIdGenerator", allocationSize = 1)
	@Column(name ="ingredientId")
	private long ingredientId;
	
	@Column(nullable =false, name ="ingredientName")
	private String ingredientName;
	
	@Column(nullable =false, name ="quantity")
	private int quantity;
	
	@Column(nullable =false, name ="quantityType")
	private String quantityType;
	
	@OneToMany(mappedBy="ingredients", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) 
	private List<Recipe> recipe;

}
