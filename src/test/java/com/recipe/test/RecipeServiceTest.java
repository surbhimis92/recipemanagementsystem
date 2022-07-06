package com.recipe.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.recipe.RecipeApplication;
import com.recipe.recipe.bean.Author;
import com.recipe.recipe.bean.Recipe;
import com.recipe.recipe.bean.RecipeIngredients;
import com.recipe.recipe.exception.RecipeAlreadyExistsException;
import com.recipe.recipe.exception.RecipeNotFoundException;
import com.recipe.recipe.service.RecipeService;

import io.swagger.v3.oas.annotations.Parameter;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = RecipeApplication.class)
class RecipeServiceTest {
	private List<Recipe> recipes = new ArrayList<Recipe>();
	@Autowired
	RecipeService service ;

	Recipe recipe1;

	Recipe recipe2;

	Author author1;

	Author author2;

	RecipeIngredients ingredient1;

	RecipeIngredients ingredient2;

	private long id;

	@BeforeEach
	void setUp() throws Exception {
		author1 = new Author(1,"Sam", new Date(), null);
		author2 = new Author(2,"Vik", new Date(), null);
		List<RecipeIngredients> ingredientList = new ArrayList<RecipeIngredients>();
		ingredient1 = new RecipeIngredients(1,"sugar",1,"tea-spoon",null);
		ingredient2 = new RecipeIngredients(1,"salt",3,"tea-spoon",null);
		ingredientList.add(ingredient1);
		ingredientList.add(ingredient2);
		recipe1 =  new Recipe(1,"Pizza","Cheesy Corn Pizza","add a cup of flour to water an mix it well until it settles into smooth batter. "
				+ "Now spread it evenly on a base and add toppings like cheese, cream and corn. Bake it for 30 mins and serve hot",author1,true, 2,ingredientList);
		ingredientList.add(new RecipeIngredients(3,"salt", 1, "teaspoon", null));
		recipe2 =  new Recipe(2,"Donut","Soft Spongy Chocolate Donuts","Take a donut roll and fill it with fillings of your choice like chocolate, caramel, peanut butter etc ad bake it for 12 minutes at 250 celcius. "
				+ "Now spread it evenly on a base and add toppings like cheese, cream and corn. Bake it for 30 mins and serve hot",author2,false, 1,ingredientList);

		//		 Mockito.when(service.createRecipe(Mockito.any()))
		//	      .thenReturn((long) 1);
	}

	@AfterEach
	void tearDown() throws Exception {
		service.deleteRecipeByName(recipe1.getDishName());
		service.deleteRecipeByName(recipe2.getDishName());
	}

	@Test
	void testCreateRecipeWithValidValues() {		
		//			String jsonRecipe = objectMapper.writeValueAsString(recipe1);
		long result = service.createRecipe(recipe1);
		assertNotNull(result);


	}

	@Test
	void testCreateRecipeWithNullValues() {	
		Exception exception = assertThrows(RecipeNotFoundException.class, () -> {service.createRecipe(null);});

		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains("Recipe to be saved is null "));


	}

	@Test
	void testGetAllRecipes() {	
		service.createRecipe(recipe1);
		boolean result = service.getAllRecipes().size()>0;		
		assertEquals(result, true);
	}

	@Test
	void testDeleteRecipeByValidId() {
		long id = service.createRecipe(recipe1);
		service.deleteRecipeById((long)id);
	}

	@Test
	void testDeleteRecipeByInvalidId() {
		assertThrows(EmptyResultDataAccessException.class, () -> {service.deleteRecipeById((long)10001);});
	}

	@Test
	void testDeleteRecipeByNullId() {
		Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {service.deleteRecipeById(null);});
		assertNotNull(exception);
	}

	@Test
	void testDeleteRecipeByValidName() {
		service.createRecipe(recipe1);
		service.deleteRecipeByName("Pizza");
	}

	@Test
	void testDeleteRecipeByInvalidName() {
		assertTrue(service.deleteRecipeByName("invalid"));
		assertTrue(service.deleteRecipeByName(""));
	}

	@Test
	void testDeleteRecipeByNullName() {
		assertTrue(service.deleteRecipeByName(null));
	}

	@Test
	void testUpdateRecipeForNullRecipe() {
		assertFalse(service.updateRecipe(null));

	}

	@Test
	void testUpdateRecipeForValidRecipe() {
		long id = service.createRecipe(recipe2);
		recipe2.setId(id);
		recipe2.setRecipeHeader("Tasty Donuts in Simple steps");
		assertTrue(service.updateRecipe(recipe2));

	}

	@Test
	void testUpdateRecipeForInValidRecipe() {
		long id = service.createRecipe(recipe2);
		recipe2.setId(id);
		recipe2.setRecipeHeader("Tasty Donuts in Simple steps");
		Exception exception = assertThrows(RecipeAlreadyExistsException.class, () -> {service.updateRecipe(recipe1);});
		assertEquals(exception.getMessage(), "Recipe does not exist");

	}

	@Test
	void testgetRecipeBasedOnIsVeg() {
		service.createRecipe(recipe2);
		service.createRecipe(recipe1);		
		List<Recipe> recipeList = service.getRecipeBasedOnSearchTerm(true, null, null, null, null);
		assertNotNull(recipeList);	
	}

	@Test
	void testgetRecipeBasedOnIsNonVeg() {
		service.createRecipe(recipe2);
		service.createRecipe(recipe1);		
		List<Recipe> recipeList = service.getRecipeBasedOnSearchTerm(false, null, null, null, null);
		assertNotNull(recipeList);	
	}

	@Test
	void testgetRecipeBasedOnServings() {
		service.createRecipe(recipe2);
		service.createRecipe(recipe1);		
		List<Recipe> recipeList = service.getRecipeBasedOnSearchTerm(false, 1, null, null, null);
		assertNotNull(recipeList);	
	}

	@Test
	void testgetRecipeBasedOnValidIngredients() {
		service.createRecipe(recipe2);
		service.createRecipe(recipe1);		
		String ingredients = "";
		List<Recipe> recipeList = service.getRecipeBasedOnSearchTerm(null, null,"salt", null, null);
		assertNotNull(recipeList);	
	}

	@Test
	void testgetRecipeBasedInValidIngredients() {
		service.createRecipe(recipe2);
		service.createRecipe(recipe1);		
		String ingredients = "";
		Exception exception = assertThrows(RecipeNotFoundException.class, () -> {service.getRecipeBasedOnSearchTerm(null, null,"abcd", null, null);});
		assertEquals(exception.getMessage(), "No recipes found with given parameters...");
	}


	@Test
	void testgetRecipeBasedInValidSearchTerm() {
		service.createRecipe(recipe2);
		service.createRecipe(recipe1);		
		String searchTerm = "";
		Exception exception = assertThrows(RecipeNotFoundException.class, () -> {service.getRecipeBasedOnSearchTerm(null, null,null, null, "flour12");});
		assertEquals(exception.getMessage(), "No recipes found with given parameters...");
	}

	@Test
	void testgetRecipeBasedOnValidSearchTerm() {
		service.createRecipe(recipe2);
		service.createRecipe(recipe1);		
		String ingredients = "";
		List<Recipe> recipeList = service.getRecipeBasedOnSearchTerm(null, null,null, null, "flour");
		assertNotNull(recipeList);	
	}

	@Test
	void testgetRecipeBasedOnValidSearchRequest() {
		service.createRecipe(recipe2);
		service.createRecipe(recipe1);		
		String ingredients = "";
		List<Recipe> recipeList = service.getRecipeBasedOnSearchTerm(null, 1,null, null, "flour");
		assertNotNull(recipeList);	
	}


}
