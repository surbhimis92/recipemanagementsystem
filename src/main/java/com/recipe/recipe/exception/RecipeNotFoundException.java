package com.recipe.recipe.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Recipe Not Found")
public class RecipeNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private String message;
	public RecipeNotFoundException() {
	}

	public RecipeNotFoundException(String msg)
	{
		super(msg);
		this.message = msg;
	}

}
