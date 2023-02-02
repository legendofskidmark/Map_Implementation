package CSCI.SDC3901.A1;


import java.util.ArrayList;

public class RecipeBookContent {

    private String givenSystemName;
    private String title;
    private ArrayList<Ingredient> ingredients;
    private String instructions;

    RecipeBookContent(String givenSystemName, String title, ArrayList<Ingredient> ingredients, String instructions) {
        this.givenSystemName = givenSystemName;
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getGivenSystemName() {
        return givenSystemName;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }
}
