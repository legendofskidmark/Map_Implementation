package CSCI.SDC3901.Assignment1;

import CSCI.SDC3901.Assignment1.Helpers.RecipeUtility;
import CSCI.SDC3901.Assignment1.Interfaces.RecipeInterface;
import CSCI.SDC3901.Assignment1.Models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static CSCI.SDC3901.Assignment1.Helpers.RecipeUtility.*;

/**
 * The RecipeBook class is to demonstrate the unit conversion of a recipe to a target measurement system by appropriately rounding and/or converting the quantities based on the measurement system rules
 *
 * @author boon
 */
public class RecipeBook implements RecipeInterface {

    /**
     * A static final value denoting the allowed variance percentage
     */
    private final static double varianceThresholdPercentage = 5.0;

    private final HashMap<String, ArrayList<GraphNode>> adjacencyList;
    private final HashMap<String, RecipeBookContent> recipes;


    public RecipeBook() {
        adjacencyList = new HashMap<>();
        recipes = new HashMap<>();
    }

    /**
     * Reads the unit conversion content from a file
     * @param unitMatches The Unit Conversion file's content
     * @return A boolean value denoting if the conversion is successful and under the given tolerance level
     */
    @Override
    public Boolean unitConversion(BufferedReader unitMatches) {
        int lineNo = 1;
        String line;

        // This is the _Node a_ in the graph
        MeasurementSystemParams sourceMeasurementParams = new MeasurementSystemParams();

        // This is the _Node b_ in the graph
        MeasurementSystemParams targetMeasurementParams = new MeasurementSystemParams();

        //list to store conversions from _Node a_ to _Node b_
        ArrayList<ConversionLine> sourceToTargetUnits = new ArrayList<>();

        //list to store conversions from _Node b_ to _Node a_
        ArrayList<ConversionLine> targetToSourceUnits = new ArrayList<>();

        try {
            while ((line = unitMatches.readLine()) != null) {

                String[] sizeUnits = line.split(" ");
                if (lineNo <= 2) { //sizeUnits = metric, 5, 2, 0, 0, 25 or imperial, 0, 8, 0
                    String systemName = RecipeUtility.normalizeString(sizeUnits[0]);
                    Integer minWeight = Integer.valueOf(sizeUnits[1]);

                    if (minWeight < 0) return false;

                    ArrayList<Integer> fractionsIntegers = new ArrayList<>();

                    for (int i = 2; i < sizeUnits.length; i++) {
                        Integer ruleValue = Integer.valueOf(sizeUnits[i]);
                        if (ruleValue < 0) return false;
                        fractionsIntegers.add(ruleValue);
                    }

                    //data validation of the unit conversion file
                    if (minWeight.equals(0)) {
                        // if insufficient number of fractions and integers are provided
                        if (fractionsIntegers.size() != 2) {
                            return false;
                        }

                        // if minimum weight = 0 and both the fraction1 and integer1 are 0 or non-zero
                        if ((fractionsIntegers.get(0) != 0 && fractionsIntegers.get(1) != 0) || (fractionsIntegers.get(0) == 0 && fractionsIntegers.get(1) == 0)) {
                            return false;
                        }
                    } else {
                        // if insufficient number of fractions and integers are provided when minimum weight > 0
                        if (fractionsIntegers.size() != 4) {
                            return false;
                        }

                        // if both the fraction1 and integer1 are 0 or non-zero
                        if ((fractionsIntegers.get(0) != 0 && fractionsIntegers.get(1) != 0) || (fractionsIntegers.get(0) == 0 && fractionsIntegers.get(1) == 0)) {
                            return false;
                        }

                        // if both the fraction2 and integer2 are 0 or non-zero
                        if ((fractionsIntegers.get(2) != 0 && fractionsIntegers.get(3) != 0) || (fractionsIntegers.get(2) == 0 && fractionsIntegers.get(3) == 0)) {
                            return false;
                        }
                    }

                    if (lineNo == 1) {
                        sourceMeasurementParams = new MeasurementSystemParams(systemName, minWeight, fractionsIntegers);
                    } else {
                        targetMeasurementParams = new MeasurementSystemParams(systemName, minWeight, fractionsIntegers);
                    }
                } else {
                    if (sizeUnits.length != 4) {
                        return false;
                    }

                    sizeUnits[1] = RecipeUtility.normalizeString(sizeUnits[1]);
                    sizeUnits[3] = RecipeUtility.normalizeString(sizeUnits[3]);
                    UnitData sourceUnitData = new UnitData(Double.parseDouble(sizeUnits[0]), sizeUnits[1]);
                    UnitData targetUnitData = new UnitData(Double.parseDouble(sizeUnits[2]), sizeUnits[3]);


                    // create path from Node_a to Node_b : BEGIN
                    // Store the given data as a path in the graph : Node_a --> Node_b
                    ConversionLine conversionData = null;
                    for (ConversionLine conversionEntries: sourceToTargetUnits) {
                        if (conversionEntries.getSourceUnitData().getUnitName().equals(sizeUnits[1]) && conversionEntries.getTargetUnitData().getUnitName().equals(sizeUnits[3])) {
                            conversionData = conversionEntries;
                            break;
                        }
                    }

                    if (conversionData != null) {
                        //check for variance
                        boolean isAllowed = RecipeUtility.isVarianceGood(varianceThresholdPercentage, conversionData.getSourceUnitData().getQuantity(), conversionData.getTargetUnitData().getQuantity(), sourceUnitData.getQuantity(), targetUnitData.getQuantity());
                        if (isAllowed) {
                            ConversionLine conversionEntry = new ConversionLine(sourceUnitData, targetUnitData);
                            sourceToTargetUnits.add(conversionEntry);
                        } else {
                            return false;
                        }
                    } else {
                        ConversionLine conversionEntry = new ConversionLine(sourceUnitData, targetUnitData);
                        sourceToTargetUnits.add(conversionEntry);
                    }
                    // create path from Node_a to Node_b : END


                     // Store the given data as a path in the graph : Node_b --> Node_a : BEGIN
                    conversionData = null;
                    for (ConversionLine conversionEntries: targetToSourceUnits) {
                        if (conversionEntries.getSourceUnitData().getUnitName().equals(sizeUnits[3]) && conversionEntries.getTargetUnitData().getUnitName().equals(sizeUnits[1])) {
                            conversionData = conversionEntries;
                            break;
                        }
                    }

                    if (conversionData != null) {
                        //check for variance
                        boolean isAllowed = RecipeUtility.isVarianceGood(varianceThresholdPercentage, conversionData.getSourceUnitData().getQuantity(), conversionData.getTargetUnitData().getQuantity(), targetUnitData.getQuantity(), sourceUnitData.getQuantity());
                        if (isAllowed) { //if variance is below the tolerance level
                            ConversionLine conversionEntry = new ConversionLine(targetUnitData, sourceUnitData);
                            targetToSourceUnits.add(conversionEntry);
                        } else { //variance exceeded the tolerance level
                            return false;
                        }
                    } else {
                        ConversionLine conversionEntry = new ConversionLine(targetUnitData, sourceUnitData);
                        targetToSourceUnits.add(conversionEntry);
                    }
                    // Store the given data as a path in the graph : Node_b --> Node_a : END
                }
                lineNo++;
            }
        } catch (NullPointerException | IOException e) {
            return false;
        }


        //Store the unit conversion data as _Node a_ --> _Node b_
        UnitConversionData abConversionData = new UnitConversionData(sourceMeasurementParams, targetMeasurementParams, sourceToTargetUnits);

        //Store the unit conversion data as _Node b_ --> _Node a_
        UnitConversionData baConversionData = new UnitConversionData(targetMeasurementParams, sourceMeasurementParams, targetToSourceUnits);

        // path : _Node a_ --> _Node b_
        GraphNode oneWayNode = new GraphNode(targetMeasurementParams.getSystemName(), abConversionData);

        // path : _Node b_ --> _Node a_
        GraphNode otherWayNode = new GraphNode(sourceMeasurementParams.getSystemName(), baConversionData);

        if (adjacencyList.get(sourceMeasurementParams.getSystemName()) != null) {
            //If the key already exists in the graph
            adjacencyList.get(sourceMeasurementParams.getSystemName()).add(oneWayNode);
        } else {
            //No such node exists with the given measurement system
            ArrayList<GraphNode> graphNode = new ArrayList<>();
            graphNode.add(oneWayNode);
            adjacencyList.put(sourceMeasurementParams.getSystemName(), graphNode);
        }

        //----------- symmetrical insertion as this is an undirected graph --------------
        if (adjacencyList.get(targetMeasurementParams.getSystemName()) != null) {
            //If the key already exists in the graph
            adjacencyList.get(targetMeasurementParams.getSystemName()).add(otherWayNode);
        } else {
            //No such node exists with the given measurement system
            ArrayList<GraphNode> graphNode = new ArrayList<>();
            graphNode.add(otherWayNode);
            adjacencyList.put(targetMeasurementParams.getSystemName(), graphNode);
        }
        return true;
    }

    /**
     *  Reads the recipe contents from a file
     *
     * @param originalSystem The Measurement system of the given Recipe
     * @param recipeContent The file with Recipe contents
     * @return A boolean value to denote if the processing of the Recipe content file is successful
     */
    @Override
    public Boolean recipe(String originalSystem, BufferedReader recipeContent) {

        // if the original system value is null or empty string - which is invalid
        if (originalSystem == null) return false;
        if (originalSystem.length() == 0) return false;

        String line;
        String recipeTitle = "";
        ArrayList<Ingredient> recipeIngredients = new ArrayList<>();
        String instructions = "";
        boolean titleRead = false;
        boolean ingredientsRead = false;
        boolean instructionsRead = false;

        try {
            while ((line = recipeContent.readLine()) != null) {
                //if clause to check if there are blank lines in the file
                if (line.length() == 0 || line.equals("\n") || line.equals("\t") || line.equals("\b") || line.equals("\r")) continue;


                if (!titleRead) {
                    titleRead = true;
                    recipeTitle = RecipeUtility.normalizeString(line);
                } else if (!ingredientsRead) {
                    //A separate block to read ingredients till a blank line is encountered. Assuming there are no blank lines in between ingredients
                    do {
                        if (line.length() == 0 || line.equals("\n") || line.equals("\t") || line.equals("\b") || line.equals("\r")) {
                            ingredientsRead = true;
                            break;
                        }
                        String[] ingredient = line.split("\t");
                        if (ingredient.length < 3) {
                            return false;
                        }

                        Ingredient formattedIngredient;
                        double quantity;
                        String quantityRepresentation = ingredient[0];
                        String units;
                        String name = "";
                        int index = 2;
                        if (ingredient[0].contains("/")) {
                            // The case where the quantity is given as a mixed fraction
                            //<i> <fr> \t <u> \t <name>
                            String[] fraction = ingredient[0].split(" ");
                            Integer a = Integer.valueOf(fraction[0]);

                            if (a < 0) return false;

                            //case when the quantity is given as a fraction
                            String[] numeratorDenominator = fraction[1].split("/");
                            Integer b = Integer.valueOf(numeratorDenominator[0]);
                            Integer c = Integer.valueOf(numeratorDenominator[1]);

                            if (b == 0 || c == 0) return false;

                            //convert the mixed fraction to a decimal
                            quantity = (1.0 * (a * c + b)) / c;
                        } else {
                            // The case where the quantity is given as an Integer
                            //<i> <u> <name>
                            quantity = Double.parseDouble(ingredient[0]);
                        }
                        if (quantity <= 0) return false;
                        units = ingredient[1];
                        //append the name to a string variable
                        for(int i = index ; i < ingredient.length ; i++) name += ingredient[i] + " ";

                        //store the processed <quantity> <units> <name> in the Ingredient object
                        formattedIngredient = new Ingredient(quantity, units, name);

                        //set the given representation of the quantity to the ingredient
                        formattedIngredient.setQuantityRepresentation(quantityRepresentation);

                        recipeIngredients.add(formattedIngredient);
                    } while ((line = recipeContent.readLine()) != null);
                } else if (!instructionsRead) {
                    //read instructions
                    instructions += line + "\n";
                }
            }

            //Creating a RecipeBookContent object to hold all the information processed about the Recipe
            RecipeBookContent currentRecipe = new RecipeBookContent(RecipeUtility.normalizeString(originalSystem), recipeTitle, recipeIngredients, instructions);

            //store the RecipeBookContent object in the recipes hashmap
            recipes.put(recipeTitle, currentRecipe);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            //return false if the Recipe is not read successfully
            return false;
        }

        //return true if the Recipe is read successfully
        return true;
    }


    /**
     *
     * @param recipeName The name of the recipe we have to convert to the target system
     * @param targetSystem The measurement system to which we have to convert the recipe
     * @param scaleFactor The value which is used to scale the recipe
     * @param convertedRecipe The PrintWriter object to save the converted recipe contents to a file
     * @return an integer
     *          <p>0 - Conversion is successful without any issue</p>
     *          <p>1 - Conversion is successful, but few quantities in the converted recipe might be over the given tolerance level</p>
     *          <p>2 - Conversion failed. No meaningful output provided to the PrintWriter</p>
     */
    @Override
    public int convert(String recipeName, String targetSystem, double scaleFactor, PrintWriter convertedRecipe) {

        //data validation for the given function parameters
        if (targetSystem == null) return 2;
        if (targetSystem.length() == 0) return 2;
        if (scaleFactor <= 0.0) return 2;
        if (recipeName == null) return 2;
        if (recipeName.length() == 0) return 2;
        if (convertedRecipe == null) return 2;

        String normalisedRecipeName = RecipeUtility.normalizeString(recipeName);
        String normalisedTargetSystem = RecipeUtility.normalizeString(targetSystem);

        boolean isConversionAboveVarianceLimit = false;
        String sourceSystem;

        // check if a recipe exists with the given name
        if (recipes.get(normalisedRecipeName) != null) {
            sourceSystem = recipes.get(normalisedRecipeName).getGivenSystemName();
            //if the recipe name is stored as null or empty string
            if (sourceSystem == null || sourceSystem.length() == 0) return 2;
        } else {
            //the given recipe does not exist in the RecipeBook
            return 2;
        }

        // if the target measurement system is same as the original system
        if (sourceSystem.equals(normalisedTargetSystem)) {
            RecipeBookContent scaledRecipe = new RecipeBookContent(recipes.get(normalisedRecipeName));
            if (scaleFactor != 1.0) {
                // scale the quantities
                MeasurementSystemParams msp = adjacencyList.get(recipes.get(normalisedRecipeName).getGivenSystemName()).get(0).getConversionData().getSourceMeasurementParams();
                for(Ingredient ingredient: scaledRecipe.getRecipeCopy().getIngredients()) {
                    double quantity = ingredient.getQuantity();
                    ingredient.setQuantity(quantity * scaleFactor);
                    applyMeasurementSystemRule(msp, ingredient);
                }
            }
            String title = scaledRecipe.getRecipeCopy().getTitle();
            scaledRecipe.getRecipeCopy().setTitle(title + " (" + targetSystem + ")");

            // write the scaled recipe to a file via PrintWriter
            RecipeUtility.writeConvertedRecipeToPrintWriter(scaledRecipe.getRecipeCopy(), convertedRecipe);

            //return 0 to denote successful conversion
            return 0;
        }

        ArrayList<GraphNode> path = new ArrayList<>();
        ArrayList<Ingredient> ingredientsOutput = new ArrayList<>();

        UnitConversionData conversionNodeData = null;

        //check if path exists (with no hop or 1-hop)
        //traverse through the adjacent nodes of the given measurement system node
        for(GraphNode node : adjacencyList.get(sourceSystem)) {
            if (node.getSystemName().equals(normalisedTargetSystem)) {
                path.add(node);
                conversionNodeData = node.getConversionData();
                break;
            }
            //traverse the edges of the target nodes of the outer loop's graph node
            for(GraphNode oneHopNode: adjacencyList.get(node.getSystemName())) {
                if (oneHopNode.getSystemName().equals(normalisedTargetSystem)) {
                    path.add(node);
                    path.add(oneHopNode);
                    break;
                }
            }
        }

        // no path exists to target system
        if (path.size() < 1) {
            return 2;
        }

        // when there's 1-hop possible conversion
        if (path.size() == 2) {
            //1 hop
            GraphNode a = path.get(0);
            GraphNode b = path.get(1);
            ArrayList<ConversionLine> sourceToTargetUnits = new ArrayList<>();
            UnitData src;
            UnitData target;
            //create the conversion data from source system to target system via the intermediate system
            // Node_a --> Node_b --> Node_c
            for(ConversionLine col1: a.getConversionData().getSourceToTargetUnits()) {
                for(ConversionLine col2: b.getConversionData().getSourceToTargetUnits()) {
                    // find common units in the target system of Node_a and source of Node_b and compute the Node_c's target value
                    if (col1.getTargetUnitData().getUnitName().equals(col2.getSourceUnitData().getUnitName())) {
                        double newSrcQuantity = col1.getSourceUnitData().getQuantity() * col2.getSourceUnitData().getQuantity() / col1.getTargetUnitData().getQuantity();
                        String newSrcName = col1.getSourceUnitData().getUnitName();
                        src = new UnitData(newSrcQuantity, newSrcName);

                        double newTargetQuantity = col2.getTargetUnitData().getQuantity();
                        String newTargetName = col2.getTargetUnitData().getUnitName();
                        target = new UnitData(newTargetQuantity, newTargetName);

                        ConversionLine newConversion = new ConversionLine(src, target);
                        sourceToTargetUnits.add(newConversion);
                    }
                }
            }

            //create a new conversion data directly from source to target via the intermediate node
            conversionNodeData = new UnitConversionData(a.getConversionData().getSourceMeasurementParams(), b.getConversionData().getTargetMeasurementParams(), sourceToTargetUnits);
        }

        if (conversionNodeData.getSourceToTargetUnits().size() == 0) {
            //this might be the case where after the interpolation we dont have any conversion units mapping from source to target, then the conversion is not possible
            return 2;
        }

        RecipeBookContent currentRecipe = recipes.get(normalisedRecipeName);

        if (currentRecipe == null) return 2;

        //scale ingredients
        if (scaleFactor >= 1.0) {
            for(int i = 0 ; i < currentRecipe.getIngredients().size() ; i++) {
                double currentQuantity = currentRecipe.getIngredients().get(i).getQuantity();
                currentRecipe.getIngredients().get(i).setQuantity(scaleFactor * currentQuantity);
            }
        }

        //Use the conversion data extracted directly (if 0-hop) or computed (when there's 1-hop) to convert the recipe from source to target system
        if (conversionNodeData != null) {
            for(int i = 0 ; i < currentRecipe.getIngredients().size() ; i++) {
                Ingredient currentIngredient = currentRecipe.getIngredients().get(i);
                String currentIngredientUnit = currentIngredient.getUnits();

                ArrayList<Ingredient> potentialTargetUnits = new ArrayList<>();
                boolean doesConversionExistsForUnit = false;
                double roundedSumOfQuantities = 0.0;
                for(ConversionLine conversion: conversionNodeData.getSourceToTargetUnits()) {
                    if (conversion.getSourceUnitData().getUnitName().equals(currentIngredientUnit)) {
                        //compute the conversion

                        //find the equivalent amount in the target system
                        double targetUnitQuantity = (currentIngredient.getQuantity() * conversion.getTargetUnitData().getQuantity())/conversion.getSourceUnitData().getQuantity();

                        //store it as a possible unit in the target system
                        Ingredient potentialUnit = new Ingredient(targetUnitQuantity, conversion.getTargetUnitData().getUnitName(), currentIngredient.getName());

                        //a variable whose sum is used to identify if all the possible quantities have a zero integer
                        roundedSumOfQuantities += Math.floor(targetUnitQuantity);

                        boolean isVarianceAllowed = RecipeUtility.isVarianceGood(varianceThresholdPercentage, conversion.getSourceUnitData().getQuantity(), conversion.getTargetUnitData().getQuantity(), currentIngredient.getQuantity(), targetUnitQuantity);

                        //store if the variance under tolerance level as metadata with the ingredient
                        potentialUnit.setVarianceAllowed(isVarianceAllowed);
                        potentialTargetUnits.add(potentialUnit);
                        doesConversionExistsForUnit = true;
                    }
                }

                // if there's no conversion of a unit from source to target system.
                if (!doesConversionExistsForUnit) {
                    return 2;
                }

                Ingredient finalIngredient = null;
                if (roundedSumOfQuantities == 0.0) {
                    //all zeroes, so choose largest fraction
                    double maxValue = Double.MIN_VALUE;

                    for(Ingredient ingredient: potentialTargetUnits) {
                        if (ingredient.getQuantity() > maxValue) {
                            finalIngredient = ingredient;
                            maxValue = ingredient.getQuantity();
                            if (!ingredient.getVarianceAllowed()) isConversionAboveVarianceLimit = true;
                        }
                    }
                } else {
                    //integers exists, so choose smallest integer
                    double minValue = Double.MAX_VALUE;

                    for(Ingredient ingredient: potentialTargetUnits) {
                        if (Math.floor(ingredient.getQuantity()) != 0.0 && ingredient.getQuantity() < minValue) {
                            finalIngredient = ingredient;
                            minValue = ingredient.getQuantity();
                            if (!ingredient.getVarianceAllowed()) isConversionAboveVarianceLimit = true;
                        }
                    }
                }

                //apply Measurement system param rules
                if (finalIngredient != null) {
                    applyMeasurementSystemRule(conversionNodeData.getTargetMeasurementParams(), finalIngredient);
                } else {
                    //not possible or no target unit found
                    return 2;
                }
                ingredientsOutput.add(finalIngredient);
            }
        }

        RecipeBookContent targetRecipe = recipes.get(normalisedRecipeName);
        RecipeBookContent output = new RecipeBookContent(targetSystem, recipeName + " (" + targetSystem + ")", ingredientsOutput, targetRecipe.getInstructions());

        // write the converted recipe to a file
        RecipeUtility.writeConvertedRecipeToPrintWriter(output, convertedRecipe);

        // if the variance of few converted units are above tolerance level
        if (isConversionAboveVarianceLimit) return 1;

        // if the variance of all the converted units are under tolerance level
        return 0;
    }

    /**
     *
     * @return List of all the available system names used for the recipe conversion
     */
    @Override
    public List<String> availableUnits() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    /**
     * @return List of all available recipes that are processed till now
     */
    @Override
    public List<String> availableRecipes() {
        return new ArrayList<>(this.recipes.keySet());
    }

}
