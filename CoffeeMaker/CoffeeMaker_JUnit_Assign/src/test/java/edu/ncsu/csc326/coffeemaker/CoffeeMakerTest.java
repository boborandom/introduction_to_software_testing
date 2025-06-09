import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CoffeeMakerTest {

    private CoffeeMaker coffeeMaker;
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;

    @Before
    public void setUp() throws RecipeException {
        // 1) Create a fresh CoffeeMaker
        coffeeMaker = new CoffeeMaker();

        // 2) Build three simple recipes
        recipe1 = new Recipe();
        recipe1.setName("Coffee");
        recipe1.setAmtCoffee(3);
        recipe1.setAmtMilk(1);
        recipe1.setAmtSugar(1);
        recipe1.setAmtChocolate(0);

        recipe2 = new Recipe();
        recipe2.setName("Mocha");
        recipe2.setAmtCoffee(2);
        recipe2.setAmtMilk(1);
        recipe2.setAmtSugar(1);
        recipe2.setAmtChocolate(2);

        recipe3 = new Recipe();
        recipe3.setName("Latte");
        recipe3.setAmtCoffee(1);
        recipe3.setAmtMilk(3);
        recipe3.setAmtSugar(1);
        recipe3.setAmtChocolate(0);

        // 3) Add them to the CoffeeMaker
        coffeeMaker.addRecipe(recipe1);
        coffeeMaker.addRecipe(recipe2);
        coffeeMaker.addRecipe(recipe3);
    }

    @Test
    public void testAddInventoryValid() {
        // start with default inventory, add some
        String result = coffeeMaker.addInventory("3", "3", "0", "1");
        assertEquals("Inventory successfully added", result);
    }

    @Test(expected = InventoryException.class)
    public void testAddInventoryInvalid() throws InventoryException {
        // negative input should throw
        coffeeMaker.addInventory("-1", "0", "0", "0");
    }

    @Test
    public void testMakeCoffeeExactChange() {
        // price of recipe1 = (3*Coffee +1*Milk +1*Sugar) = 35 cents
        int price = recipe1.getPrice();
        int change = coffeeMaker.makeCoffee(0, price);
        assertEquals(0, change);
        // inventory should have been reduced
        assertEquals(14, coffeeMaker.checkInventory().getCoffee());
    }

    @Test
    public void testMakeCoffeeNotEnoughMoney() {
        int change = coffeeMaker.makeCoffee(1, 10);  // recipe2 costs more than 10
        assertEquals(10, change);
        // inventory remains unchanged
        assertEquals(15, coffeeMaker.checkInventory().getMilk());
    }

    @Test
    public void testAddRecipeDuplicateName() {
        Recipe dup = new Recipe();
        dup.setName("Coffee");  // same as recipe1
        dup.setAmtCoffee(1);
        dup.setAmtMilk(1);
        dup.setAmtSugar(1);
        dup.setAmtChocolate(1);

        boolean added = coffeeMaker.addRecipe(dup);
        assertFalse("Should reject duplicate recipe names", added);
    }

    @Test
    public void testDeleteRecipe() {
        // remove recipe2
        Recipe deleted = coffeeMaker.deleteRecipe(1);
        assertEquals("Mocha", deleted.getName());
        // slot should now be empty
        assertNull(coffeeMaker.getRecipes()[1]);
    }
}
