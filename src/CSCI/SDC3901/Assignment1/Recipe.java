package CSCI.SDC3901.Assignment1;

import java.util.ArrayList;

public class Recipe {

    private ArrayList<Ingredient> ingredients;
    private String instructions;

    Recipe(ArrayList<Ingredient> ingredients, String instructions) {
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

}
