package CSCI.SDC3901.A1.Helpers;

import CSCI.SDC3901.A1.Ingredient;
import CSCI.SDC3901.A1.RecipeBookContent;
import CSCI.SDC3901.A1.UnitConversionData;

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

    public static String getIntegerRepresentation(UnitConversionData conversionNodeData, Ingredient finalUnit, double quantity, int index) {
        String integerRepresentation;
        if (quantity <= conversionNodeData.getTargetMeasurementParams().getFractionIntegers().get(index)) { //i1
            integerRepresentation = conversionNodeData.getTargetMeasurementParams().getFractionIntegers().get(index) + "";
            finalUnit.setQuantityRepresentation(integerRepresentation);
        } else {
            double multiple = quantity / conversionNodeData.getTargetMeasurementParams().getFractionIntegers().get(index);
            multiple = Math.ceil(multiple);
            multiple = multiple * conversionNodeData.getTargetMeasurementParams().getFractionIntegers().get(index);
            integerRepresentation = (int)multiple + "";
        }
        return integerRepresentation;
    }

    public static String getMixedFraction(UnitConversionData conversionNodeData, double quantity, int index) {
        double roundedQuantity = Math.floor(quantity);
        double decimalPart = quantity - roundedQuantity;
        //if decimal part is 0
        //else
        double numerator = Math.ceil(conversionNodeData.getTargetMeasurementParams().getFractionIntegers().get(index) * decimalPart);
        String mixedFraction = "";

        if (roundedQuantity != 0.0) mixedFraction = ((int)roundedQuantity) + " ";
        if (numerator != 0.0) {
            long denominator = conversionNodeData.getTargetMeasurementParams().getFractionIntegers().get(index);
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
}
