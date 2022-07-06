package com.recipe.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.recipe.recipe.RecipeApplication;
@RunWith(Suite.class)
@SpringBootTest
@ContextConfiguration(classes = RecipeApplication.class)
@SuiteClasses({ RecipeServiceTest.class })
public class AllTests {


}
