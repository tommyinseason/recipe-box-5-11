import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  @Override
  protected void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/recipe_book_test", null, null);
  }

  @Override
  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String deleteRecipesQuery = "DELETE FROM recipes *;";
      String deleteCategoriesQuery = "DELETE FROM categories *;";
      String deleteCategoriesRecipesQuery = "DELETE FROM categories_recipes *;";
      con.createQuery(deleteRecipesQuery).executeUpdate();
      con.createQuery(deleteCategoriesQuery).executeUpdate();
      con.createQuery(deleteCategoriesRecipesQuery).executeUpdate();
    }
  }
}
