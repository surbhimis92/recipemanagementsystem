package com.recipe.recipe.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.recipe.recipe.bean.Author;
import com.recipe.recipe.bean.Recipe;

@Repository
public interface RecipeDAO extends JpaRepository<Recipe, Long>{

	Recipe findByDishName(String name);
	Set<Recipe> findByDishNameContaining(String dishName);
	//	@Modifying
	//	@Query("delete from Recipe r where r.dishNam= :dishName")
	//	void deleteByDishNameAndAuthor(String dishName, String authorName);
	@Query("select r from Recipe r where  r.dishName = :dishName and r.author.authorName= :authorName")
	Recipe findByDishNameAndAuthorAuthorName(@Param("dishName") String dishName,@Param("authorName") String authorName);
	List<Recipe> findByServingsAndIsVeg(int servings, boolean isVeg);
	void deleteByDishName(String string);
}
