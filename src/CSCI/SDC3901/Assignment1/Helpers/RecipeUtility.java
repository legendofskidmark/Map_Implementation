package CSCI.SDC3901.Assignment1.Helpers;

import CSCI.SDC3901.Assignment1.Models.Ingredient;
import CSCI.SDC3901.Assignment1.Models.MeasurementSystemParams;
import CSCI.SDC3901.Assignment1.Models.RecipeBookContent;

import java.io.PrintWriter;
import java.math.BigInteger;

/**
 *  A Utility class for the Recipe Conversion system
 *
 * @author boon
 */
public class RecipeUtility {

    /**
     * converts string to lowercase
     *
     * @param str any term in the recipe content
     * @return a string that is turned into lowercase
     */
    public static String normalizeString(String str) {
        if (str == null) {
            throw new RuntimeException("Invalid Unit in the conversion file");
        }
        return str.toLowerCase();
    }

    /**
     *  Computes variance between two conversions
     *
     * @param varianceThresholdPercentage the maximum amount of variance allowed
     * @param existingSize1 conversion quantity of the first measurement system
     * @param existingSize2 conversion quantity of the second measurement system
     * @param size1 potential conversion quantity of the first measurement system
     * @param size2 potential conversion quantity of the first measurement system
     * @return A boolean value indicating if the variance is under the limit
     */
    public static boolean isVarianceGood(double varianceThresholdPercentage, Double existingSize1, Double existingSize2, Double size1, Double size2) {
        double minRange = 0.0;
        double maxRange = 0.0;

        double xFactor = (existingSize2 * 1.0)/size2;

        double conversionAsPerFact = xFactor * size1;

        double delta = (existingSize1 * varianceThresholdPercentage) / 100.0;
        minRange = existingSize1 - delta;
        maxRange = existingSize1 + delta;

        if (minRange <= conversionAsPerFact && conversionAsPerFact <= maxRange) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *  converts decimal number to Integer representation
     *
     * @param conversionNodeData parameters of the measurement system we have to use
     * @param finalUnit selected ingredient unit based on the criteria
     * @param quantity decimal representation of the quantity
     * @param index index of the fraction integer array given in the measurement system parameters
     * @return An Integer which is equivalent to the double value after applying the rules in the measurement system
     */
    public static String getIntegerRepresentation(MeasurementSystemParams conversionNodeData, Ingredient finalUnit, double quantity, int index) {
        String integerRepresentation;
        if (quantity <= conversionNodeData.getFractionIntegers().get(index)) { //i1
            integerRepresentation = conversionNodeData.getFractionIntegers().get(index) + "";
            finalUnit.setQuantityRepresentation(integerRepresentation);
        } else {
            double multiple = quantity / conversionNodeData.getFractionIntegers().get(index);
            multiple = Math.ceil(multiple);
            multiple = multiple * conversionNodeData.getFractionIntegers().get(index);
            integerRepresentation = (int)multiple + "";
        }
        return integerRepresentation;
    }

    /**
     * converts decimal number to mixed fraction representation
     *
     * @param conversionNodeData parameters of the measurement system we have to use
     * @param quantity decimal representation of the quantity
     * @param index index of the fraction integer array given in the measurement system parameters
     * @return A mixed fraction which is equivalent to the double value after applying the rules in the measurement system
     */
    public static String getMixedFraction(MeasurementSystemParams conversionNodeData, double quantity, int index) {
        double roundedQuantity = Math.floor(quantity);
        double decimalPart = quantity - roundedQuantity;
        //if decimal part is 0
        //else
        double numerator = Math.ceil(conversionNodeData.getFractionIntegers().get(index) * decimalPart);
        String mixedFraction = "";

        if (roundedQuantity != 0.0) mixedFraction = ((int)roundedQuantity) + " ";
        if (numerator != 0.0) {
            long denominator = conversionNodeData.getFractionIntegers().get(index);
            int gcd = BigInteger.valueOf((long)numerator).gcd(BigInteger.valueOf(denominator)).intValue(); //taken from : https://stackoverflow.com/a/4009230
            if (gcd != 1) {
                numerator /= gcd;
                denominator /= gcd;
            }
            mixedFraction += (int)numerator + "/" + denominator;
        }
        return mixedFraction;
    }

    /**
     * Writes the converted recipe to a file using PrintWriter
     *
     * @param output RecipeContentBook object which is having the converted recipe
     * @param convertedRecipe PrintWriter object to write the recipe content to a file
     */
    public static void writeConvertedRecipeToPrintWriter(RecipeBookContent output, PrintWriter convertedRecipe) {
        convertedRecipe.println(output.getTitle());
        System.out.println(output.getTitle());

        convertedRecipe.println("");
        System.out.println("");

        for(Ingredient ingredient: output.getIngredients()) {
            convertedRecipe.println(ingredient);
            System.out.println(ingredient);
        }
        convertedRecipe.println("");
        System.out.println("");

        convertedRecipe.println(output.getInstructions());
        System.out.println(output.getInstructions());
    }

    /**
     *  Applies rules to the newly derived unit chosen amongst multiple choices
     *
     * @param systemParams parameters of the measurement system we have to use
     * @param finalIngredient selected ingredient before applying the measuring system rules
     */
    public static void applyMeasurementSystemRule(MeasurementSystemParams systemParams, Ingredient finalIngredient) {
        double quantity = finalIngredient.getQuantity();
        if (systemParams.getMinWeight() == 0.0) {
            // either fraction or integer will exist
            if (systemParams.getFractionIntegers().get(0) != 0) { //f1
                //represent in fractions
                // get nearest 1/[0]th fraction of quantity
                String mixedFraction = getMixedFraction(systemParams, quantity, 0);
                finalIngredient.setQuantityRepresentation(mixedFraction);
            } else {
                //represent in the nearest integers
                String integerRepresentation = getIntegerRepresentation(systemParams, finalIngredient, quantity, 1);
                finalIngredient.setQuantityRepresentation(integerRepresentation);
            }
        } else {
            //min wt exists
            if (finalIngredient.getQuantity() <= systemParams.getMinWeight()) {
                //use f1 i1
                if (systemParams.getFractionIntegers().get(0) != 0) {
                    //use f1 - 0
                    String mixedFraction = getMixedFraction(systemParams, quantity, 0);
                    finalIngredient.setQuantityRepresentation(mixedFraction);
                } else {
                    //use i1 - 1
                    String integerRepresentation = getIntegerRepresentation(systemParams, finalIngredient, quantity, 1);
                    finalIngredient.setQuantityRepresentation(integerRepresentation);
                }
            } else {
                //use f2 i2
                if (systemParams.getFractionIntegers().get(2) != 0) {
                    //use f2
                    String mixedFraction = getMixedFraction(systemParams, quantity, 2);
                    finalIngredient.setQuantityRepresentation(mixedFraction);
                } else {
                    //use i2
                    String integerRepresentation = getIntegerRepresentation(systemParams, finalIngredient, quantity, 3);
                    finalIngredient.setQuantityRepresentation(integerRepresentation);
                }
            }
        }
    }
}
