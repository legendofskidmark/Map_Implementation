package CSCI.SDC3901.Assignment1.Models;

import java.util.ArrayList;

/**
 * The model class to encompass all the data about a recipe
 *
 * @author boon
 */
public class RecipeBookContent {

    private String givenSystemName;
    private String title;
    private ArrayList<Ingredient> ingredients;
    private String instructions;
    private RecipeBookContent recipeCopy;

    /** Parameterized constructor to copy a Recipe object
     * @param recipe The Recipe object which we want to create a copy of
     */
    public RecipeBookContent(RecipeBookContent recipe) { //src : https://stackoverflow.com/a/869078
        this.recipeCopy = recipe;
    }

    public RecipeBookContent(String givenSystemName, String title, ArrayList<Ingredient> ingredients, String instructions) {
        this.givenSystemName = givenSystemName;
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getGivenSystemName() {
        return givenSystemName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public RecipeBookContent getRecipeCopy() {
        return recipeCopy;
    }
}
