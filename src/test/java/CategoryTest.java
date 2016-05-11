import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class CategoryTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Category_instantiatesCorrectly_true() {
    Category myCategory = new Category("Italian");
    assertEquals(true, myCategory instanceof Category);
  }

  @Test
  public void getName_categoryInstantiatesWithName_String() {
    Category myCategory = new Category("Italian");
    assertEquals("Italian", myCategory.getName());
  }

  @Test
  public void all_emptyAtFirst_0() {
    assertEquals(0, Category.all().size());
  }

  @Test
  public void equals_returnsTrueIfNamesAretheSame_true() {
    Category firstCategory = new Category("Italian");
    Category secondCategory = new Category("Italian");
    assertTrue(firstCategory.equals(secondCategory));
  }

  @Test
  public void save_savesObjectIntoDatabase_true() {
    Category myCategory = new Category("Italian");
    myCategory.save();
    assertTrue(Category.all().get(0).equals(myCategory));
  }

  @Test
  public void save_assignsIdToObject_int() {
    Category myCategory = new Category("Italian");
    myCategory.save();
    Category savedCategory = Category.all().get(0);
    assertEquals(myCategory.getId(), savedCategory.getId());
  }

  @Test
  public void find_findCategoryInDatabase_true() {
    Category myCategory = new Category("Italian");
    myCategory.save();
    Category savedCategory = Category.find(myCategory.getId());
    assertTrue(myCategory.equals(savedCategory));
  }

  @Test
  public void addRecipe_addsRecipeToCategory_true() {
    Category myCategory = new Category("Italian");
    myCategory.save();
    Recipe myRecipe = new Recipe("Chicken Parm", "5", "Cook the chicken");
    myRecipe.save();
    myCategory.addRecipe(myRecipe);
    Recipe savedRecipe = myCategory.getRecipes().get(0);
    assertTrue(myRecipe.equals(savedRecipe));
  }

  @Test
  public void getRecipes_returnsAllRecipes_List() {
    Category myCategory = new Category("Italian");
    myCategory.save();
    Recipe myRecipe = new Recipe("Chicken Parm", "5", "Cook the chicken");
    myRecipe.save();
    myCategory.addRecipe(myRecipe);
    List savedRecipes = myCategory.getRecipes();
    assertEquals(1, savedRecipes.size());
  }

  @Test
  public void delete_deletesAllRecipesAndCategoriesAssociations() {
    Category myCategory = new Category("Italian");
    myCategory.save();
    Recipe myRecipe = new Recipe("Chicken Parm", "5", "Cook the chicken");
    myRecipe.save();
    myCategory.addRecipe(myRecipe);
    myCategory.delete();
    assertEquals(0, myRecipe.getCategories().size());
  }

}
