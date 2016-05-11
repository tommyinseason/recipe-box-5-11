import org.sql2o.*;
import org.junit.*;
import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.junit.Assert.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Recipes");
  }

  @Test
  public void categoryIsCreatedTest() {
    goTo("http://localhost:4567/");
    click("a", withText("Categories"));
    fill("#name").with("Italian");
    submit(".btn");
    assertThat(pageSource()).contains("Italian");
  }

  @Test
  public void recipeIsCreatedTest() {
    goTo("http://localhost:4567/");
    click("a", withText("Recipes"));
    fill("#name").with("Chicken Parm");
    fillSelect("#rating").withValue("4");
    fill("#the_recipe").with("Cook the chicken");
    submit(".btn");
    assertThat(pageSource()).contains("Chicken Parm");
  }

  @Test
  public void categoryShowPageDisplaysName() {
    Category testCategory = new Category("Italian");
    testCategory.save();
    String url = String.format("http://localhost:4567/categories/%d", testCategory.getId());
    goTo(url);
    assertThat(pageSource()).contains("Italian");
  }

  @Test
  public void recipeShowPageDisplaysDescription() {
    Recipe testRecipe = new Recipe("Chicken Parm", "5", "Cook the chicken");
    testRecipe.save();
    String url = String.format("http://localhost:4567/recipes/%d", testRecipe.getId());
    goTo(url);
    assertThat(pageSource()).contains("Chicken Parm");
  }

  @Test
  public void recipeIsAddedToCategory() {
    Category testCategory = new Category("Italian");
    testCategory.save();
    Recipe testRecipe = new Recipe("Chicken Parm", "5", "Cook the chicken");
    testRecipe.save();
    String url = String.format("http://localhost:4567/categories/%d", testCategory.getId());
    goTo(url);
    fillSelect("#recipe_id").withText("Chicken Parm");
    submit(".btn");
    assertThat(pageSource()).contains("<li>");
    assertThat(pageSource()).contains("Chicken Parm");
  }

  @Test
  public void categoryIsAddedToRecipe() {
    Category testCategory = new Category("Italian");
    testCategory.save();
    Recipe testRecipe = new Recipe("Chicken Parm", "5", "Cook the chicken");
    testRecipe.save();
    String url = String.format("http://localhost:4567/recipes/%d", testRecipe.getId());
    goTo(url);
    fillSelect("#category_id").withText("Italian");
    submit(".btn");
    assertThat(pageSource()).contains("<li>");
    assertThat(pageSource()).contains("Italian");
  }

}
