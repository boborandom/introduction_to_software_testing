package edu.ncsu.csc326.coffeemaker;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CoffeeMakerTest {

    /**
     * The object under test.
     */
    private CoffeeMaker coffeeMaker;

    // Sample recipes to use in testing.
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;
    private Recipe recipe4;
    private Recipe recipe5;
    private Recipe recipe6;
    private Recipe recipe7;

    @Before
    public void setUp() throws RecipeException {
        coffeeMaker = new CoffeeMaker();

        // Set up for recipe1: (Coffee: 3, Milk: 1, Sugar: 1, Chocolate: 0, Price: 50)
        recipe1 = new Recipe();
        recipe1.setName("Coffee");
        recipe1.setAmtCoffee("3");
        recipe1.setAmtMilk("1");
        recipe1.setAmtSugar("1");
        recipe1.setAmtChocolate("0");
        recipe1.setPrice("50");

        // Set up for recipe2: (Coffee: 4, Milk: 2, Sugar: 1, Chocolate: 0, Price: 75)
        recipe2 = new Recipe();
        recipe2.setName("Mocha");
        recipe2.setAmtCoffee("4");
        recipe2.setAmtMilk("2");
        recipe2.setAmtSugar("1");
        recipe2.setAmtChocolate("0");
        recipe2.setPrice("75");

        // Set up for recipe3: (Coffee: 4, Milk: 0, Sugar: 1, Chocolate: 1, Price: 100)
        recipe3 = new Recipe();
        recipe3.setName("HotChocolate");
        recipe3.setAmtCoffee("4");
        recipe3.setAmtMilk("0");
        recipe3.setAmtSugar("1");
        recipe3.setAmtChocolate("1");
        recipe3.setPrice("100");

        // Set up for recipe4: (Coffee: 0, Milk: 1, Sugar: 1, Chocolate: 4, Price: 65)
        recipe4 = new Recipe();
        recipe4.setName("ChocoDelight");
        recipe4.setAmtCoffee("0");
        recipe4.setAmtMilk("1");
        recipe4.setAmtSugar("1");
        recipe4.setAmtChocolate("4");
        recipe4.setPrice("65");

        // For testing “same name” or “edit” behavior:
        recipe5 = new Recipe();
        recipe5.setName("Duplicate");
        recipe5.setAmtCoffee("1");
        recipe5.setAmtMilk("1");
        recipe5.setAmtSugar("1");
        recipe5.setAmtChocolate("1");
        recipe5.setPrice("10");

        // For testing editing out‐of‐bounds, negative values, etc.
        recipe6 = new Recipe();
        recipe6.setName("EditTest");
        recipe6.setAmtCoffee("2");
        recipe6.setAmtMilk("2");
        recipe6.setAmtSugar("2");
        recipe6.setAmtChocolate("2");
        recipe6.setPrice("20");

        // Another “new” recipe for replacing during edit
        recipe7 = new Recipe();
        recipe7.setName("Replacement");
        recipe7.setAmtCoffee("3");
        recipe7.setAmtMilk("3");
        recipe7.setAmtSugar("3");
        recipe7.setAmtChocolate("3");
        recipe7.setPrice("30");
    }

    // ===========================================
    // UC1: ADD RECIPE
    // ===========================================

    /**
     * Test that we can add up to 3 distinct recipes. The return value should
     * be true for the first three calls; false on the fourth.
     */
    @Test
    public void testAddUpToThreeRecipes() {
        assertTrue(coffeeMaker.addRecipe(recipe1));
        assertTrue(coffeeMaker.addRecipe(recipe2));
        assertTrue(coffeeMaker.addRecipe(recipe3));

        // Fourth one should fail (only 3 slots).
        assertFalse(coffeeMaker.addRecipe(recipe4));
    }

    /**
     * If we try to add a recipe whose name already exists, addRecipe should
     * return false, even if there is a free slot.
     */
    @Test
    public void testAddRecipeSameNameFails() throws RecipeException {
        Recipe rA = new Recipe();
        rA.setName("Alpha");
        rA.setAmtCoffee("1");
        rA.setAmtMilk("1");
        rA.setAmtSugar("1");
        rA.setAmtChocolate("1");
        rA.setPrice("10");

        Recipe rB = new Recipe();
        rB.setName("Alpha");  // same name
        rB.setAmtCoffee("2");
        rB.setAmtMilk("2");
        rB.setAmtSugar("2");
        rB.setAmtChocolate("2");
        rB.setPrice("20");

        assertTrue(coffeeMaker.addRecipe(rA));
        // Even though slot 1 is free, name duplicates, so should return false.
        assertFalse(coffeeMaker.addRecipe(rB));
    }

    // ===========================================
    // UC2: EDIT RECIPE
    // ===========================================

    /**
     * If we edit a valid recipe index, editRecipe should return the old
     * recipe’s name, and slot should be replaced with the new recipe.
     */
    @Test
    public void testEditRecipeSuccess() {
        coffeeMaker.addRecipe(recipe1);         // slot 0
        coffeeMaker.addRecipe(recipe5);         // slot 1

        // Replace slot 1 (name="Duplicate") with recipe7
        String oldName = coffeeMaker.editRecipe(1, recipe7);
        assertEquals("Duplicate", oldName);     // editRecipe returns old name

        Recipe[] arr = coffeeMaker.getRecipes();
        assertEquals("Replacement", arr[1].getName());
        assertEquals(3, arr[1].getAmtCoffee());
        assertEquals(3, arr[1].getAmtMilk());
        assertEquals(3, arr[1].getAmtSugar());
        assertEquals(3, arr[1].getAmtChocolate());
        assertEquals(30, arr[1].getPrice());
    }

    /**
     * Editing a non‐existent index (e.g., -1 or >= 3) should return null
     * and make no changes.
     */
    @Test
    public void testEditRecipeInvalidIndex() {
        coffeeMaker.addRecipe(recipe1);
        // Out of bounds: index = 3
        assertNull(coffeeMaker.editRecipe(3, recipe7));

        // Negative index
        assertNull(coffeeMaker.editRecipe(-1, recipe7));

        // Slot 2 is still null, slot 1 is null, so they remain unchanged.
        assertNull(coffeeMaker.getRecipes()[2]);
        assertNull(coffeeMaker.getRecipes()[1]);
    }

    // ===========================================
    // UC3: DELETE RECIPE
    // ===========================================

    /**
     * Deleting an existing recipe should return the name, and the slot becomes null.
     */
    @Test
    public void testDeleteRecipeSuccess() {
        coffeeMaker.addRecipe(recipe1); // slot 0
        String deleted = coffeeMaker.deleteRecipe(0);
        assertEquals("Coffee", deleted);
        assertNull(coffeeMaker.getRecipes()[0]);
    }

    /**
     * If we delete at an invalid index (or an empty slot), deleteRecipe should return null.
     */
    @Test
    public void testDeleteRecipeInvalidIndex() {
        // No recipes in any slot yet
        assertNull(coffeeMaker.deleteRecipe(0));
        assertNull(coffeeMaker.deleteRecipe(2));
        assertNull(coffeeMaker.deleteRecipe(-1));
    }

    // ===========================================
    // UC4: ADD INVENTORY
    // ===========================================

    /**
     * Adding well‐formed inventory should not throw; inventory should increment.
     */
    @Test
    public void testAddInventoryNormal() throws InventoryException {
        // Default inventory is (Coffee=15, Milk=15, Sugar=15, Chocolate=15)
        coffeeMaker.addInventory("1", "2", "3", "4");

        Inventory inv = coffeeMaker.checkInventory();
        assertEquals(16, inv.getCoffee());      // 15 + 1
        assertEquals(17, inv.getMilk());        // 15 + 2
        assertEquals(18, inv.getSugar());       // 15 + 3
        assertEquals(19, inv.getChocolate());   // 15 + 4
    }

    @Test(expected = InventoryException.class)
    public void testAddInventoryNonNumericCoffee() throws InventoryException {
        coffeeMaker.addInventory("a", "1", "1", "1");
    }

    @Test(expected = InventoryException.class)
    public void testAddInventoryNegativeSugar() throws InventoryException {
        coffeeMaker.addInventory("1", "1", "-5", "1");
    }

    @Test(expected = InventoryException.class)
    public void testAddInventoryNonNumericMilk() throws InventoryException {
        coffeeMaker.addInventory("1", "pop", "1", "1");
    }

    @Test(expected = InventoryException.class)
    public void testAddInventoryNonNumericChocolate() throws InventoryException {
        coffeeMaker.addInventory("1", "1", "1", "hi");
    }

    // ===========================================
    // UC5: CHECK INVENTORY
    // ===========================================

    /**
     * The default inventory, immediately after construction, should be all 15’s.
     */
    @Test
    public void testCheckInventoryDefault() {
        Inventory inv = coffeeMaker.checkInventory();
        assertEquals(15, inv.getCoffee());
        assertEquals(15, inv.getMilk());
        assertEquals(15, inv.getSugar());
        assertEquals(15, inv.getChocolate());
    }

    // ===========================================
    // UC6: GET RECIPES
    // ===========================================

    /**
     * After adding two recipes, getRecipes() should return an array in which
     * exactly those two slots are non‐null.
     */
    @Test
    public void testGetRecipesArrayContents() {
        coffeeMaker.addRecipe(recipe1);
        coffeeMaker.addRecipe(recipe2);

        Recipe[] arr = coffeeMaker.getRecipes();
        // Exactly two non‐null slots (0 and 1).
        assertEquals("Coffee", arr[0].getName());
        assertEquals("Mocha", arr[1].getName());
        assertNull(arr[2]);
    }

    // ===========================================
    // UC7: PURCHASE BEVERAGE
    // ===========================================

    /**
     * Paying exactly the right amount: change should be 0, inventory decremented.
     */
    @Test
    public void testMakeCoffeeExactFunds() {
        coffeeMaker.addRecipe(recipe1);   // cost = 50
        int change = coffeeMaker.makeCoffee(0, 50);
        assertEquals(0, change);

        // Inventory was (15,15,15,15); recipe1 uses (3,1,1,0):
        Inventory invAfter = coffeeMaker.checkInventory();
        assertEquals(12, invAfter.getCoffee());
        assertEquals(14, invAfter.getMilk());
        assertEquals(14, invAfter.getSugar());
        assertEquals(15, invAfter.getChocolate());
    }

    /**
     * Paying more than cost: change = paid – price, inventory decremented.
     */
    @Test
    public void testMakeCoffeeMoreThanCost() {
        coffeeMaker.addRecipe(recipe2);   // cost = 75
        int change = coffeeMaker.makeCoffee(0, 100);
        assertEquals(25, change);

        Inventory invAfter = coffeeMaker.checkInventory();
        assertEquals(11, invAfter.getCoffee());   // 15 – 4
        assertEquals(13, invAfter.getMilk());     // 15 – 2
        assertEquals(14, invAfter.getSugar());    // 15 – 1
        assertEquals(15, invAfter.getChocolate());// 15 – 0
    }

    /**
     * Insufficient funds: should return all money paid, inventory unchanged.
     */
    @Test
    public void testMakeCoffeeInsufficientFunds() {
        coffeeMaker.addRecipe(recipe3);   // cost = 100
        Inventory before = coffeeMaker.checkInventory();
        int change = coffeeMaker.makeCoffee(0, 50);
        assertEquals(50, change);

        // Inventory must be unchanged
        Inventory after = coffeeMaker.checkInventory();
        assertEquals(before.getCoffee(), after.getCoffee());
        assertEquals(before.getMilk(), after.getMilk());
        assertEquals(before.getSugar(), after.getSugar());
        assertEquals(before.getChocolate(), after.getChocolate());
    }

    /**
     * Trying to buy with a recipe index that’s empty or out of range:
     * should return the entire amount back.
     */
    @Test
    public void testMakeCoffeeInvalidIndex() {
        // No recipes at all yet
        int change = coffeeMaker.makeCoffee(2, 60);
        assertEquals(60, change);

        // Put one in slot 0, but ask for slot 1
        coffeeMaker.addRecipe(recipe1);
        change = coffeeMaker.makeCoffee(1, 100);
        assertEquals(100, change);
    }

    /**
     * Inventory not sufficient for that recipe: entire payment returned.
     */
    @Test
    public void testMakeCoffeeInsufficientInventory() {
        // Create a recipe that uses 20 coffee but inventory only has 15
        Recipe bigCoffee = new Recipe();
        bigCoffee.setName("TooBig");
        bigCoffee.setAmtCoffee("20");   // more than stock
        bigCoffee.setAmtMilk("0");
        bigCoffee.setAmtSugar("0");
        bigCoffee.setAmtChocolate("0");
        bigCoffee.setPrice("50");

        coffeeMaker.addRecipe(bigCoffee);
        // Even if we pay 100, we cannot make it. Should return full 100, no inventory change.
        Inventory before = coffeeMaker.checkInventory();
        int change = coffeeMaker.makeCoffee(0, 100);
        assertEquals(100, change);

        Inventory after = coffeeMaker.checkInventory();
        assertEquals(before.getCoffee(), after.getCoffee());
        assertEquals(before.getMilk(), after.getMilk());
        assertEquals(before.getSugar(), after.getSugar());
        assertEquals(before.getChocolate(), after.getChocolate());
    }

    /**
     * Multiple recipes present: selecting the “third” recipe must work correctly.
     */
    @Test
    public void testMakeCoffeeMultipleRecipes() {
        coffeeMaker.addRecipe(recipe1); // slot 0, cost=50
        coffeeMaker.addRecipe(recipe2); // slot 1, cost=75
        coffeeMaker.addRecipe(recipe4); // slot 2, cost=65

        // Buy slot 2 with 100 → change = 35, inventory deducted appropriately.
        int change = coffeeMaker.makeCoffee(2, 100);
        assertEquals(35, change);

        // recipe4 uses (0 coffee, 1 milk, 1 sugar, 4 chocolate)
        Inventory inv = coffeeMaker.checkInventory();
        assertEquals(15, inv.getCoffee());      // still 15
        assertEquals(14, inv.getMilk());        // 15 – 1
        assertEquals(14, inv.getSugar());       // 15 – 1
        assertEquals(11, inv.getChocolate());   // 15 – 4
    }

    // ===========================================
    // UC8: RECIPE NAMES (toString)
    // ===========================================

    /**
     * A Recipe’s toString() should return its name.
     */
    @Test
    public void testRecipeToStringReturnsName() throws RecipeException {
        Recipe r = new Recipe();
        r.setName("Cappuccino");
        assertEquals("Cappuccino", r.toString());
    }
}
