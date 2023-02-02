package CSCI.SDC3901.Assignment1;

public class RecipeUtility {

    public static String normalizeString(String str) {
        if (str != null) {
            return str.toLowerCase();
        }
        return str;
    }

    public static boolean isVarianceGood(Double existingSize1, Double existingSize2, Double size1, Double size2) {
        double minRange = 0.0;
        double maxRange = 0.0;

        double xFactor = (size2 * 1.0)/existingSize2;

        double conversionAsPerFact = xFactor * size1;

        double delta = (existingSize1 * 5) / 100.0;
        minRange = existingSize1 - delta;
        maxRange = existingSize1 + delta;

        if (minRange <= conversionAsPerFact && conversionAsPerFact <= maxRange) {
            return true;
        } else {
            return false;
        }
    }
}
