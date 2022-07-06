package com.recipe.recipe.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Recipe already exists")
public class RecipeAlreadyExistsException extends RuntimeException{
	private String message;
	public RecipeAlreadyExistsException() {
	}

	public RecipeAlreadyExistsException(String msg)
	{
		super(msg);
		this.message = msg;
	}

}
