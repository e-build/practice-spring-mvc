package com.bussiness.recipe.controller;

import com.bussiness.recipe.dao.RecipeDao;
import com.framework.http.Controller;
import com.framework.http.HttpRequest;
import com.framework.http.HttpResponse;

public class RecipeListApiController implements Controller {

    private final RecipeDao recipeDao = new RecipeDao();

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        response.responseBody(recipeDao.selectRecipeList());
    }
}
