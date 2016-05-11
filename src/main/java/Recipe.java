import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Recipe {
  private int id;
  private String name;
  private String rating;
  private String the_recipe;

  public Recipe(String name, String rating, String the_recipe) {
    this.name = name;
    this.rating = rating;
    this.the_recipe = the_recipe;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }
  public String getRating() {
    return rating;
  }
  public String getRecipe() {
    return the_recipe;
  }

  public static List<Recipe> all() {
    String sql = "SELECT id, name, rating, the_recipe FROM recipes";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Recipe.class);
    }
  }

  @Override
  public boolean equals(Object otherRecipe){
    if (!(otherRecipe instanceof Recipe)) {
      return false;
    } else {
      Recipe newRecipe = (Recipe) otherRecipe;
      return this.getName().equals(newRecipe.getName()) &&
             this.getId() == newRecipe.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO recipes(name, rating, the_recipe) VALUES (:name, :rating, :the_recipe)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("rating", this.rating)
        .addParameter("the_recipe", this.the_recipe)
        .executeUpdate()
        .getKey();
    }
  }

  public static Recipe find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM recipes where id=:id";
      Recipe recipe = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Recipe.class);
      return recipe;
    }
  }

  public void update(String newName) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE recipes SET name = :name, rating = :rating, the_recipe = :the_recipe WHERE id = :id ";
      con.createQuery(sql)
        .addParameter("name", newName)
        .addParameter("rating", this.rating)
        .addParameter("the_recipe", this.the_recipe)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void addCategory(Category category) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories_recipes (category_id, recipe_id) VALUES (:category_id, :recipe_id)";
      con.createQuery(sql)
        .addParameter("category_id", category.getId())
        .addParameter("recipe_id", this.getId())
        .executeUpdate();
    }
  }

  public List<Category> getCategories() {
    try(Connection con = DB.sql2o.open()){
      String joinQuery = "SELECT category_id FROM categories_recipes WHERE recipe_id = :recipe_id";
      List<Integer> categoryIds = con.createQuery(joinQuery)
        .addParameter("recipe_id", this.getId())
        .executeAndFetch(Integer.class);

      List<Category> categories = new ArrayList<Category>();

      for (Integer categoryId : categoryIds) {
        String recipeQuery = "SELECT * FROM categories WHERE id = :categoryId";
        Category category = con.createQuery(recipeQuery)
          .addParameter("categoryId", categoryId)
          .executeAndFetchFirst(Category.class);
        categories.add(category);
      }
      return categories;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM recipes WHERE id = :id;";
        con.createQuery(deleteQuery)
          .addParameter("id", this.getId())
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM categories_recipes WHERE recipe_id = :recipeId";
        con.createQuery(joinDeleteQuery)
          .addParameter("recipeId", this.getId())
          .executeUpdate();
    }
  }

}
