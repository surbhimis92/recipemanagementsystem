package com.recipe.recipe.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.recipe.recipe.bean.Recipe;
import com.recipe.recipe.exception.RecipeAlreadyExistsException;
import com.recipe.recipe.exception.RecipeNotFoundException;
import com.recipe.recipe.service.RecipeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/recipeHome")
public class RecipeIInterceptor {
	@Autowired
	RecipeService recipeSearchService;

	public String loadRecipePage() {
		checkPermission();
		return "/RecipeHome.xhtml";
	}

	private void checkPermission() {
		// Details omitted
	}

	@Operation(summary = "Get all recipes by recipe name")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Recipe Fetched Successfully", 
					content = { @Content(mediaType = "application/json", 
					schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Recipe.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid path iused", 
			content = @Content), 
			@ApiResponse(responseCode = "404", description = "Recipes not found", 
			content = @Content),
			@ApiResponse(responseCode = "500", description = "OOPs! Service not available", 
			content = @Content)
	})
	@GetMapping("/fetchRecipceByTitle")
	public ResponseEntity<List<Recipe>> getRecipeByName(@Parameter(description = "title of recipe") @RequestParam(required = false) String title) {
		if(Objects.nonNull(title)) {
			return new ResponseEntity<List<Recipe>>(recipeSearchService.getRecipeByName(title),HttpStatus.OK);
		}
		else return new ResponseEntity<List<Recipe>>(recipeSearchService.getAllRecipes(),HttpStatus.OK);
	}

	@Operation(summary = "Get all recipes")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "All Recipes Fetched Successfully", 
					content = { @Content(mediaType = "application/json", 
					schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Recipe.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid path iused", 
			content = @Content), 
			@ApiResponse(responseCode = "404", description = "Recipes not found", 
			content = @Content),
			@ApiResponse(responseCode = "500", description = "OOPs! Service not available", 
			content = @Content)
	})
	@GetMapping("/fetchRecipe")
	public ResponseEntity<List<Recipe>> getAllRecipes() {
		return new ResponseEntity<List<Recipe>>(recipeSearchService.getAllRecipes(),HttpStatus.OK);

	}

	@Operation(summary = "api call to update an existing recipe")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Recipe updated Successfully", 
					content = { @Content(mediaType = "application/json", 
					schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Recipe.class)) }),
			@ApiResponse(responseCode = "201", description = "Recipe doesnt existed, so created a new entry!", 
			content = { @Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400", description = "Invalid path iused", 
			content = @Content), 
			@ApiResponse(responseCode = "404", description = "Page currently not available", 
			content = @Content),
			@ApiResponse(responseCode = "500", description = "OOPs! Service not available", 
			content = @Content)
	})
	@PutMapping("/updateRecipe")
	public ResponseEntity<String> updateRecipes(@Parameter(description = "recipe to be updated") @RequestParam Recipe recipe) {
		recipeSearchService.updateRecipe(recipe);
		return new ResponseEntity<String>("recipe updated successfully!", HttpStatus.OK);
	}

	@Operation(summary = "api call to search a recipe based on search params")
	@GetMapping("/searchItem")
	public ResponseEntity<List<Recipe>> getAllSearchItems(@Parameter(description = "is the dish vegetarian?") @RequestParam(required = false) Boolean isVeg,
			@Parameter(description = "number of servings") @RequestParam(required = false) Integer servings, 
			@Parameter(description = "ingredients") @RequestParam(required = false) String ingredients, 
			@Parameter(description = "please select true if search term is to be included") @RequestParam(required = false) Boolean include,
			@Parameter(description = "search terms to be included, separated by ','") @RequestParam(required = false) String searchTerm) {
		return new ResponseEntity<List<Recipe>>(recipeSearchService.getRecipeBasedOnSearchTerm(isVeg,servings,ingredients,include,searchTerm),HttpStatus.OK);

	}

	@Operation(summary = "api call to delete a recipe based on input params")
	@DeleteMapping("/deleteRecipe")
	public ResponseEntity<String> deleteRecipe(@Parameter(description = "name of the dish to be deleted ") @RequestParam(required = false) String name,
			@Parameter(description = "id of the dish to be deleted ") @RequestParam(required = false) Long id) {
		try{
			if(Objects.nonNull(id))
				recipeSearchService.deleteRecipeById(id);
			else if(Objects.nonNull(name))
				recipeSearchService.deleteRecipeByName(name);
			else 
				return new ResponseEntity<String>("Recipe Not Found ",HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>("Recipe not found in records ",HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("Recipe deleted successfully ",HttpStatus.OK);

	}   

	@Operation(summary = "api call to create a recipe")
	@PostMapping("/createRecipe")
	public ResponseEntity<Long> createRecipe(@Parameter(description = "values for recipe ") @RequestBody Recipe recipe) {
		if(recipe == null) {
			return new ResponseEntity<Long>((long)0, HttpStatus.NOT_ACCEPTABLE);
		}
		recipe.getAuthor().getRecipe().add(recipe);
		System.out.println("created recipe from controller "+ recipe.getAuthor().getAuthorName() + "  "+ recipe.getId() + recipe.getDishName());
		long recipeId = recipeSearchService.createRecipe(recipe);			
		return new ResponseEntity<Long>(recipeId, HttpStatus.CREATED);

	}

}

