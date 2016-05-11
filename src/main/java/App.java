import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("categories", Category.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/categories", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("categories", Category.all());
      model.put("template", "templates/categories.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/recipes", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("recipes", Recipe.all());
      model.put("template", "templates/recipes.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/recipes", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      String rating = request.queryParams("rating");
      String the_recipe = request.queryParams("the_recipe");
      Recipe newRecipe = new Recipe(name, rating, the_recipe);
      newRecipe.save();
      response.redirect("/recipes");
      return null;
    });

    post("/categories", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Category newCategory = new Category(name);
      newCategory.save();
      response.redirect("/categories");
      return null;
    });

    get("/recipes/:id", (request,response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Recipe recipe = Recipe.find(Integer.parseInt(request.params("id")));
      model.put("recipe", recipe);
      model.put("allCategories", Category.all());
      model.put("template", "templates/recipe.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/categories/:id", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params("id")));
      model.put("category", category);
      model.put("allRecipes", Recipe.all());
      model.put("template", "templates/category.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add_recipes", (request, response) -> {
      int recipeId = Integer.parseInt(request.queryParams("recipe_id"));
      int categoryId = Integer.parseInt(request.queryParams("category_id"));
      Category category = Category.find(categoryId);
      Recipe recipe = Recipe.find(recipeId);
      category.addRecipe(recipe);
      response.redirect("/categories/" + categoryId);
      return null;
    });

    post("/add_categories", (request, response) -> {
      int recipeId = Integer.parseInt(request.queryParams("recipe_id"));
      int categoryId = Integer.parseInt(request.queryParams("category_id"));
      Category category = Category.find(categoryId);
      Recipe recipe = Recipe.find(recipeId);
      recipe.addCategory(category);
      response.redirect("/recipes/" + recipeId);
      return null;
    });

    get("/recipes/:id/edit", (request,response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Recipe recipe = Recipe.find(Integer.parseInt(request.params("id")));
      model.put("recipe", recipe);
      model.put("template", "templates/recipe-edit.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/recipes/:id", (request,response) -> {
      int recipeId = Integer.parseInt(request.params("id"));
      Recipe recipe = Recipe.find(recipeId);
      String newName = request.queryParams("name");
      recipe.update(newName);
      response.redirect("/recipes/" + recipeId);
      return null;
    });

    post("/recipes/:id/delete", (request,response) -> {
      int recipeId = Integer.parseInt(request.params("id"));
      Recipe recipe = Recipe.find(recipeId);
      recipe.delete();
      response.redirect("/recipes");
      return null;
    });

    get("/categories/:id/edit", (request,response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params("id")));
      model.put("category", category);
      model.put("template", "templates/category-edit.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/categories/:id", (request,response) -> {
      int categoryId = Integer.parseInt(request.params("id"));
      Category category = Category.find(categoryId);
      String newName = request.queryParams("name");
      category.update(newName);
      response.redirect("/categories/" + categoryId);
      return null;
    });

    post("/categories/:id/delete", (request,response) -> {
      int categoryId = Integer.parseInt(request.params("id"));
      Category category = Category.find(categoryId);
      category.delete();
      response.redirect("/categories");
      return null;
    });


  }
}
