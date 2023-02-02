package CSCI.SDC3901.Assignment1.Helpers;

import CSCI.SDC3901.Assignment1.Models.Ingredient;
import CSCI.SDC3901.Assignment1.Models.MeasurementSystemParams;
import CSCI.SDC3901.Assignment1.Models.RecipeBookContent;

import java.io.PrintWriter;
import java.math.BigInteger;

public class RecipeUtility {

    public static String normalizeString(String str) {
        if (str == null) {
            throw new RuntimeException("Invalid Unit in the conversion file");
        }
        return str.toLowerCase();
    }

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

    public static void writeConvertedRecipeToPrintWriter(RecipeBookContent output, PrintWriter convertedRecipe) {
        convertedRecipe.println(output.getTitle());
        convertedRecipe.println("");
        for(Ingredient ingredient: output.getIngredients()) {
            convertedRecipe.println(ingredient);
        }
        convertedRecipe.println("");
        convertedRecipe.println(output.getInstructions());
    }

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
