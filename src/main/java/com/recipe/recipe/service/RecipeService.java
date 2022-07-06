package com.recipe.recipe.service;

import java.util.List;
import java.util.Set;

import com.recipe.recipe.bean.Recipe;

public interface RecipeService {
	public abstract long createRecipe(Recipe recipe);
	public abstract boolean updateRecipe(Recipe recipe);
	public abstract boolean deleteRecipeById(Long id);
	public boolean deleteRecipeByName(String name);
	public abstract List<Recipe> getAllRecipes();
	public abstract List<Recipe> getRecipeByName(String dishName);
	public abstract List<Recipe> getRecipeBasedOnSearchTerm(Boolean isVeg,Integer servings,String ingredients, Boolean include, String searchTerm);
}
