package com.recipe.recipe.service;

import static org.hamcrest.CoreMatchers.containsString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


import com.recipe.recipe.bean.Recipe;
import com.recipe.recipe.bean.RecipeIngredients;
import com.recipe.recipe.dao.RecipeDAO;
import com.recipe.recipe.exception.RecipeAlreadyExistsException;
import com.recipe.recipe.exception.RecipeNotFoundException;

@Transactional
@Component(value = "recipeSearchService")
public class RecipeServiceImpl implements RecipeService {
	@Autowired
	private RecipeDAO recipeDao;

	@Override
	public long createRecipe(Recipe recipe) {
		// TODO Auto-generated method stub

		//	System.out.println(" recipe attributes " + recipe.getAuthor().getAuthorName() + recipe.getDishName() + recipe.getId());
		if(Objects.isNull(recipe)) {
			throw new RecipeNotFoundException("Recipe to be saved is null ");
		}
		Recipe recipetemp = recipeDao.findByDishNameAndAuthorAuthorName(recipe.getDishName(), recipe.getAuthor().getAuthorName());
		if(Objects.nonNull(recipetemp)) {
			throw new RecipeAlreadyExistsException("Recipe already exists.. ");
		}
		Recipe result = recipeDao.save(recipe);
		return result.getId();

	}
	@Override
	public boolean updateRecipe(Recipe recipe) {
		// TODO Auto-generated method stub
		if(Objects.nonNull(recipe)) {
			Recipe recipetemp = recipeDao.findByDishNameAndAuthorAuthorName(recipe.getDishName(), recipe.getAuthor().getAuthorName());
			if(Objects.nonNull(recipetemp)) {
				recipeDao.deleteByDishName(recipe.getDishName());
				recipeDao.flush();
				recipeDao.save(recipe);
			} else
				throw new RecipeAlreadyExistsException("Recipe does not exist");	
		}else return false;

		return true;	
	}
	@Override
	public boolean deleteRecipeById(Long id) {
		// TODO Auto-generated method stub
		recipeDao.deleteById(id);
		return true;

	}

	@Override
	public boolean deleteRecipeByName(String name) {
		// TODO Auto-generated method stub
		recipeDao.deleteByDishName(name);
		return true;

	}
	@Override
	public List<Recipe> getAllRecipes() {
		// TODO Auto-generated method stub
		System.out.println(recipeDao.findAll().size());
		List<Recipe> list = recipeDao.findAll();
		if(Objects.nonNull(list) && list.size()>0){
			return list;
		}else {
			throw new RecipeNotFoundException("No recipes found in database..");
		}
	}
	@Override
	public List<Recipe> getRecipeByName(String dishName) {
		Recipe recipe = recipeDao.findByDishName(dishName);
		List<Recipe> recipeList = new ArrayList<Recipe>();
		if(recipe == null) {
			throw new RecipeNotFoundException(" No recipe exists with given name");
		} else {
			recipeList.add(recipe);
		}
		return recipeList ;
	}

	@Override
	public List<Recipe> getRecipeBasedOnSearchTerm(Boolean isVeg,Integer servings,String ingredients, Boolean include, String searchTerm) {
		// TODO Auto-generated method stub
		List<Recipe> masterList = recipeDao.findAll();
		List<Recipe> filteredSetWithSearchTerm  = masterList;
		List<Recipe> filteredSetWithIngredients1  = masterList;
		List<Recipe> filteredSetWithIngredients = new ArrayList<Recipe>();
		List<Recipe> filteredSet = masterList;
		if(Objects.nonNull(isVeg) && Objects.nonNull(servings)) {
			filteredSet = masterList.stream().filter(recipe -> recipe.isVeg() == isVeg 
					&& recipe.getServings() == servings).collect(Collectors.toList());
		}else if(Objects.nonNull(isVeg) && Objects.isNull(servings)) {
			filteredSet = masterList.stream().filter(recipe -> recipe.isVeg() == isVeg).collect(Collectors.toList());
		}else if(Objects.isNull(isVeg) && Objects.nonNull(servings)) {
			filteredSet = masterList.stream().filter(recipe -> recipe.getServings() == servings).collect(Collectors.toList());
		}
		filteredSetWithIngredients1 = filteredSet;
		filteredSetWithIngredients = filteredSet;
		if(Objects.nonNull(ingredients)) {
			List<String> ingList1 = Arrays.asList(ingredients.split(","));
			final List<String> ingList = ingList1.stream().map(item -> item.toLowerCase()).collect(Collectors.toList());
			if(Objects.isNull(include) || include == true) {
				filteredSetWithIngredients=filteredSetWithIngredients1.stream().
						filter(item -> { List<RecipeIngredients> listIng =item.getIngredients();
						ArrayList<String> recipeIng = new ArrayList();
						listIng.forEach(item2 -> recipeIng.add(item2.getIngredientName().toLowerCase()));
						return recipeIng.containsAll(ingList);
						}).collect(Collectors.toList());

			}else {
				filteredSetWithIngredients=filteredSetWithIngredients1.stream().
						filter(item -> { List<RecipeIngredients> listIng =item.getIngredients();
						ArrayList<String> recipeIng = new ArrayList();
						listIng.forEach(item2 -> recipeIng.add(item2.getIngredientName().toLowerCase()));
						return !listIng.containsAll(recipeIng);
						}).collect(Collectors.toList());
			}
		}
		filteredSetWithSearchTerm = filteredSetWithIngredients;
		if(Objects.nonNull(searchTerm)) {
			Pattern searchPattern = Pattern.compile(searchTerm);
			Predicate<String> predicate =  searchPattern.asPredicate();
			filteredSetWithSearchTerm = filteredSetWithIngredients.stream().filter(recipe ->predicate.test(recipe.getRecipeBody()))
					.collect(Collectors.toList());
		}	

		//	List<Recipe> recipelist = filteredSetWithSearchTerm.size() == 0 ? 
		//			filteredSetWithIngredients.size() == 0 ? filteredSet.size() ==0 ? null:
		//				filteredSet:filteredSetWithIngredients:filteredSetWithSearchTerm;

		if(Objects.nonNull(filteredSetWithSearchTerm) && filteredSetWithSearchTerm.size()>0){
			return filteredSetWithSearchTerm;
		}else {
			throw new RecipeNotFoundException("No recipes found with given parameters...");
		}

	}

}
