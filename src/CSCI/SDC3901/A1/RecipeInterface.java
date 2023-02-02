package CSCI.SDC3901.A1;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface RecipeInterface {
    public Boolean unitConversion(BufferedReader unitMatches);
    public Boolean recipe(String originalSystem, BufferedReader recipeContent);
    public int convert( String recipeName, String targetUnits, double scaleFactor, PrintWriter convertedRecipe);
}
