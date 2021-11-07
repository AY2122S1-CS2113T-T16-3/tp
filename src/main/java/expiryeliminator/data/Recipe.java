package expiryeliminator.data;

import java.util.TreeMap;

import expiryeliminator.data.exception.DuplicateDataException;
import expiryeliminator.data.exception.IllegalValueException;
import expiryeliminator.data.exception.NotFoundException;

/**
 * Represents a recipe.
 */
public class Recipe {
    private String name;
    private final TreeMap<String, IngredientQuantity> ingredientQuantities = new TreeMap<>();

    /**
     * Initialises a recipe.
     *
     * @param name The name of the recipe
     */
    public Recipe(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the recipe.
     *
     * @return Name of the recipe.
     */
    public String getName() {
        return name;
    }

    public void setName(String inputName) {
        name = inputName;
    }

    public void setIngredientQuantities(String ingredientName, IngredientQuantity ingredientQuantity) {
        ingredientQuantities.put(ingredientName, ingredientQuantity);
    }


    /**
     * Returns the ingredients and the respective quantities of the recipe.
     *
     * @return Ingredients and the respective quantities of the recipe.
     */
    public TreeMap<String, IngredientQuantity> getIngredientQuantities() {
        return ingredientQuantities;
    }

    //@@author vincentlauhl

    /**
     * Adds an ingredient and its associated quantity to the recipe.
     *
     * @param ingredientName Name of ingredient to be added.
     * @param quantity Quantity of ingredient to be added.
     * @param ingredientRepository Ingredient repository.
     * @throws DuplicateDataException If ingredient already exists in the recipe.
     * @throws IllegalValueException If quantity is less than or equal to 0.
     */
    public String addIngredient(String ingredientName, int quantity, IngredientRepository ingredientRepository)
            throws DuplicateDataException, IllegalValueException {
        String ingredientNameIfNotInList = "";
        final IngredientStorage ingredientStorage = ingredientRepository.findWithNullReturn(ingredientName);
        Ingredient ingredient;
        if (ingredientStorage == null) {
            ingredientRepository.add(ingredientName);
            ingredientNameIfNotInList = ingredientName + "\n";
            ingredient = new Ingredient(ingredientName);
        } else {
            ingredient = ingredientStorage.getIngredient();
        }
        final IngredientQuantity ingredientQuantity = new IngredientQuantity(ingredient, quantity);
        assert quantity > 0 : "Quantity for an ingredient in the recipe cannot be zero";
        if (ingredientQuantities.containsKey(ingredientQuantity.getName())) {
            throw new DuplicateDataException();
        }
        ingredientQuantities.put(ingredientQuantity.getName(), ingredientQuantity);
        return ingredientNameIfNotInList;
    }
    //@@author

    /**
     * Delete ingredients in a recipe.
     *
     * @param ingredientName Name of ingredient to be added.
     */
    public void delete(String ingredientName) throws IllegalValueException, NotFoundException {
        if (!contains(ingredientName)) {
            throw new NotFoundException();
        }
        if (ingredientQuantities.size() <= 1) {
            throw new IllegalValueException();
        }

        ingredientQuantities.remove(ingredientName);
    }

    /**
     * Updates the quantity of ingredients in a recipe.
     *
     * @param ingredientName Name of ingredient to be added.
     * @param quantity quantity of ingredient to be updated to.
     */
    public void update(String ingredientName, int quantity) throws NotFoundException, IllegalValueException {
        if (!contains(ingredientName)) {
            throw new NotFoundException();
        }
        IngredientQuantity ingredientQuantity = ingredientQuantities.get(ingredientName);
        ingredientQuantity.setQuantity(quantity);
    }


    /**
     * Checks if the recipe contains the given ingredient name.
     *
     * @return True if the recipe contains the given ingredient name; false otherwise.
     */
    public Boolean contains(String ingredientName) {
        return ingredientQuantities.containsKey(ingredientName);
    }

    //@@author vincentlauhl
    @Override
    public String toString() {
        assert ingredientQuantities.size() > 0 : "Recipe must contain at least one ingredient.";
        StringBuilder ingredientsWithQuantities = new StringBuilder(name + "\n");
        for (IngredientQuantity ingredientQuantity : ingredientQuantities.values()) {
            ingredientsWithQuantities.append("- ").append(ingredientQuantity).append("\n");
        }
        return ingredientsWithQuantities.toString();
    }
}
