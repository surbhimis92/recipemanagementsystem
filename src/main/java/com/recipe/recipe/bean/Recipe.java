package com.recipe.recipe.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Recipe")
@Getter
@Setter
@Accessors
public class Recipe{

@Id
@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="recipeIdGenerator")
@SequenceGenerator(name = "recipeIdGenerator", sequenceName = "recipeIdGenerator", allocationSize = 1)
@Column(name ="id")
private long id;

@Column(nullable =false, name ="dishName", unique=true)
private String dishName;

@Column(name ="recipeHeader")
private String recipeHeader;

@Column(nullable =false, name ="recipeBody")
@Lob
private String recipeBody;

//@OneToOne(fetch = FetchType.LAZY)

@JsonIgnore
@ManyToOne(cascade=CascadeType.ALL, fetch= FetchType.LAZY,optional=true)
@JoinColumn(name="authorId")
private Author author;

@Column(name ="isVeg")
private boolean isVeg;

@Column(name ="servings")
private long servings;

@JsonIgnore
@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
//@JoinColumn(name="ingredientId", referencedColumnName = "ingredientId", insertable = false, updatable = false)
//@OnDelete(action = OnDeleteAction.CASCADE)
private List<RecipeIngredients> ingredients;

@Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null) {
        return false;
    }
    if (getClass() != obj.getClass()) {
        return false;
    }

    final Recipe other = (Recipe) obj;
    if (!this.dishName.equalsIgnoreCase(other.dishName)) {
        return false;
    }
    
    if (this.isVeg != other.isVeg) {
        return false;
    }

    if (!this.author.getAuthorName().equalsIgnoreCase(other.author.getAuthorName())) {
        return false;
    }
    
    return true;

//    return Objects.equals(this.id, other.id);
}

}
