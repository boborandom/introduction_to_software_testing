package edu.ncsu.csc326.coffeemaker;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;

public class CoffeeMakerTest {

    private CoffeeMaker coffeeMaker;
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;

    @Before
    public void setUp() throws RecipeException {
        coffeeMaker = new CoffeeMaker();

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

        coffeeMaker.addRecipe(recipe1);
        coffeeMaker.addRecipe(recipe2);
        coffeeMaker.addRecipe(recipe3);
    }

    @Test
    public void testAddInventoryValid() throws InventoryException {
        String result = coffeeMaker.addInventory("3", "3", "0", "1");
        assertEquals("Inventory successfully added", result);
    }

    @Test(expected = InventoryException.class)
    public void testAddInventoryInvalid() throws InventoryException {
        coffeeMaker.addInventory("-1", "0", "0", "0");
    }

    @Test
    public void testMakeCoffeeExactChange() {
        int price = recipe1.getPrice();
        int change = coffeeMaker.makeCoffee(0, price);
        assertEquals(0, change);
        assertEquals(14, coffeeMaker.checkInventory().getCoffee());
    }

    @Test
    public void testMakeCoffeeNotEnoughMoney() {
        int change = coffeeMaker.makeCoffee(1, 10);
        assertEquals(10, change);
        assertEquals(15, coffeeMaker.checkInventory().getMilk());
    }

    @Test
    public void testAddRecipeDuplicateName() {
        Recipe dup = new Recipe();
        dup.setName("Coffee");
        dup.setAmtCoffee(1);
        dup.setAmtMilk(1);
        dup.setAmtSugar(1);
        dup.setAmtChocolate(1);

        assertFalse(coffeeMaker.addRecipe(dup));
    }

    @Test
    public void testDeleteRecipe() {
        Recipe deleted = coffeeMaker.deleteRecipe(1);
        assertEquals("Mocha", deleted.getName());
        assertNull(coffeeMaker.getRecipes()[1]);
    }
}
