package CSCI.SDC3901.Assignment1.Interfaces;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;

/**
 * The Interface which the RecipeBook class has to implement
 *
 * @author boon
 */
public interface RecipeInterface {
    public Boolean unitConversion(BufferedReader unitMatches);
    public Boolean recipe(String originalSystem, BufferedReader recipeContent);
    public int convert(String recipeName, String targetUnits, double scaleFactor, PrintWriter convertedRecipe);
    public List<String> availableUnits();
    public List<String> availableRecipes();

}
