package CSCI.SDC3901.A1;

import CSCI.SDC3901.A1.Helpers.RecipeUtility;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

//todo: report referencs of code used - academic integrity violation

import static CSCI.SDC3901.A1.Helpers.RecipeUtility.getIntegerRepresentation;
import static CSCI.SDC3901.A1.Helpers.RecipeUtility.getMixedFraction;

public class RecipeBook implements RecipeInterface {

    private HashMap<String, ArrayList<GraphNode>> adjacencyList;
    private HashMap<String, RecipeBookContent> recipes;

    private final double varianceThresholdPercentage = 5.0;

    public RecipeBook() {
        adjacencyList = new HashMap<>();
        recipes = new HashMap<>();

        FileReader frUC = null;
        FileReader frRecipe = null;
        try {
            frUC = new FileReader("unitConversion.txt");
            frRecipe = new FileReader("recipe.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader brUC = new BufferedReader(frUC);
        BufferedReader brR = new BufferedReader(frRecipe);
        unitConversion(brUC); //first file

        try {
            frUC = new FileReader("unitConversion2.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        brUC = new BufferedReader(frUC);
        unitConversion(brUC); //second file

        PrintWriter pw = null;
        try {
            File file = new File("opboon.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            pw = new PrintWriter(file);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        recipe("Metric", brR);
        convert("Egg Curry", "indian", 2, pw);


        //todo: close connections
        pw.close();
    }

    @Override
    public Boolean unitConversion(BufferedReader unitMatches) {
        int lineNo = 1;
        String line;

        MeasurementSystemParams sourceMeasurementParams = new MeasurementSystemParams();
        MeasurementSystemParams targetMeasurementParams = new MeasurementSystemParams();
        ArrayList<ConversionLine> sourceToTargetUnits = new ArrayList<>();
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

                    //data validation
                    if (minWeight.equals(0)) {
                        if (fractionsIntegers.size() != 2) {
                            return false;
                        }

                        if ((fractionsIntegers.get(0) != 0 && fractionsIntegers.get(1) != 0) || (fractionsIntegers.get(0) == 0 && fractionsIntegers.get(1) == 0)) {
                            return false;
                        }
                    } else {
                        if (fractionsIntegers.size() != 4) {
                            return false;
                        }

                        if ((fractionsIntegers.get(0) != 0 && fractionsIntegers.get(1) != 0) || (fractionsIntegers.get(0) == 0 && fractionsIntegers.get(1) == 0)) {
                            return false;
                        }

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
                    UnitData sourceUnitData = new UnitData(Double.valueOf(sizeUnits[0]), sizeUnits[1]);
                    UnitData targetUnitData = new UnitData(Double.valueOf(sizeUnits[2]), sizeUnits[3]);


                    // a --> b
                    ConversionLine conversionData = null;
                    for (ConversionLine conversionEntries: sourceToTargetUnits) {
                        if (conversionEntries.getSourceUnitData().getUnitName().equals(sizeUnits[1]) && conversionEntries.getTargetUnitData().getUnitName().equals(sizeUnits[3])) {
                            conversionData = conversionEntries;
                            break;
                        }
                    }

                    if (conversionData != null) { //todo: assumption -> even if we have multiple same unit entries only the first is taken as truth even if the below ones are less than 5%
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


                    // b --> a
                    conversionData = null;
                    for (ConversionLine conversionEntries: targetToSourceUnits) {
                        if (conversionEntries.getSourceUnitData().getUnitName().equals(sizeUnits[3]) && conversionEntries.getTargetUnitData().getUnitName().equals(sizeUnits[1])) {
                            conversionData = conversionEntries;
                            break;
                        }
                    }

                    if (conversionData != null) { //todo: assumption -> even if we have multiple same unit entries only the first is taken as truth even if the below ones are less than 5%
                        //check for variance
                        boolean isAllowed = RecipeUtility.isVarianceGood(varianceThresholdPercentage, conversionData.getSourceUnitData().getQuantity(), conversionData.getTargetUnitData().getQuantity(), targetUnitData.getQuantity(), sourceUnitData.getQuantity());
                        if (isAllowed) {
                            ConversionLine conversionEntry = new ConversionLine(targetUnitData, sourceUnitData);
                            targetToSourceUnits.add(conversionEntry);
                        } else {
                            return false;
                        }
                    } else {
                        ConversionLine conversionEntry = new ConversionLine(targetUnitData, sourceUnitData);
                        targetToSourceUnits.add(conversionEntry);
                    }
                }
                lineNo++;
            }
        } catch (NullPointerException | IOException e) {
            return false;
        }


        UnitConversionData abConversionData = new UnitConversionData(sourceMeasurementParams, targetMeasurementParams, sourceToTargetUnits);
        UnitConversionData baConversionData = new UnitConversionData(targetMeasurementParams, sourceMeasurementParams, targetToSourceUnits);
        GraphNode oneWayNode = new GraphNode(targetMeasurementParams.getSystemName(), abConversionData);
        GraphNode otherWayNode = new GraphNode(sourceMeasurementParams.getSystemName(), baConversionData);

        if (adjacencyList.get(sourceMeasurementParams.getSystemName()) != null) {
            //key exists
            adjacencyList.get(sourceMeasurementParams.getSystemName()).add(oneWayNode);
        } else {
            //new entry
            ArrayList<GraphNode> graphNode = new ArrayList<>();
            graphNode.add(oneWayNode);
            adjacencyList.put(sourceMeasurementParams.getSystemName(), graphNode);
        }

        //----------- symmetrical insertion as this is an undirected graph
        if (adjacencyList.get(targetMeasurementParams.getSystemName()) != null) {
            adjacencyList.get(targetMeasurementParams.getSystemName()).add(otherWayNode);
        } else {
            ArrayList<GraphNode> graphNode = new ArrayList<>();
            graphNode.add(otherWayNode);
            adjacencyList.put(targetMeasurementParams.getSystemName(), graphNode);
        }
        return true;
    }

    @Override
    public Boolean recipe(String originalSystem, BufferedReader recipeContent) {

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
                if (line.length() == 0 || line.equals("\n") || line.equals("\t") || line.equals("\b") || line.equals("\r")) continue;
                if (!titleRead) {
                    titleRead = true;
                    recipeTitle = RecipeUtility.normalizeString(line); //todo: must be unique

                } else if (!ingredientsRead) {
                    do {
                        if (line.length() == 0 || line.equals("\n") || line.equals("\t") || line.equals("\b") || line.equals("\r")) {
                            ingredientsRead = true;
                            break;
                        }
                        String[] ingredient = line.split("\t");
                        if (ingredient.length < 3) { //todo: handle "1 egg" case
                            return false;
                        }

                        Ingredient formattedIngredient;
                        double quantity;
                        String units = "";
                        String name = "";
                        int index = 2;
                        if (ingredient[0].contains("/")) {
                            //case 2: <i> <fr> \t <u> \t <name>
                            String[] fraction = ingredient[0].split(" ");
                            Integer a = Integer.valueOf(fraction[0]);

                            if (a < 0) return false;

                            String[] numeratorDenominator = fraction[1].split("/");
                            Integer b = Integer.valueOf(numeratorDenominator[0]);
                            Integer c = Integer.valueOf(numeratorDenominator[1]);

                            if (b == 0 || c == 0) return false;

                            quantity = (1.0 * (a * c + b)) / c;
                        } else {
                            //case 1: <i> <u> <name>
                            quantity = Double.valueOf(ingredient[0]);
                        }
                        if (quantity <= 0) return false;
                        units = ingredient[1];
                        for(int i = index ; i < ingredient.length ; i++) name += ingredient[i] + " ";

                        formattedIngredient = new Ingredient(quantity, units, name);
                        recipeIngredients.add(formattedIngredient);
                    } while ((line = recipeContent.readLine()) != null);
                } else if (!instructionsRead) {
                    //read instructions
                    instructions += line + "\n";
                }
            }

            RecipeBookContent currentRecipe = new RecipeBookContent(RecipeUtility.normalizeString(originalSystem), recipeTitle, recipeIngredients, instructions);
            recipes.put(recipeTitle, currentRecipe);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    @Override
    public int convert(String recipeName, String targetSystem, double scaleFactor, PrintWriter convertedRecipe) {

        if (targetSystem == null) return 2;
        if (targetSystem.length() == 0) return 2;
        if (scaleFactor == 0.0) return 2;
        String normalisedRecipeName = RecipeUtility.normalizeString(recipeName);
        String normalisedTargetSystem = RecipeUtility.normalizeString(targetSystem);
        String sourceSystem = recipes.get(normalisedRecipeName).getGivenSystemName();

        ArrayList<GraphNode> path = new ArrayList<>();
        ArrayList<Ingredient> ingredientsOutput = new ArrayList<>();

        UnitConversionData conversionNodeData = null;
        //check if path exists (with no hop or 1-hop)
        for(GraphNode node : adjacencyList.get(sourceSystem)) {
            if (node.getSystemName().equals(normalisedTargetSystem)) {
                path.add(node);
                conversionNodeData = node.getConversionData();
                break;
            }
            for(GraphNode oneHopNode: adjacencyList.get(node.getSystemName())) {
                if (oneHopNode.getSystemName().equals(normalisedTargetSystem)) {
                    path.add(node);
                    path.add(oneHopNode);
                    break;
                }
            }
        }

        if (path.size() < 1) {
            return 0; //todo : error returning
        }

        if (path.size() == 2) {
            //1 hop
            GraphNode a = path.get(0);
            GraphNode b = path.get(1);
            ArrayList<ConversionLine> sourceToTargetUnits = new ArrayList<>();
            UnitData src = null;
            UnitData target = null;
            for(ConversionLine col1: a.getConversionData().getSourceToTargetUnits()) {
                for(ConversionLine col2: b.getConversionData().getSourceToTargetUnits()) {
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
            conversionNodeData = new UnitConversionData(a.getConversionData().getSourceMeasurementParams(), b.getConversionData().getTargetMeasurementParams(), sourceToTargetUnits);
        }




        RecipeBookContent currentRecipe = recipes.get(normalisedRecipeName);

        //step 1 : scale ingredients
        if (scaleFactor != 1.0) {
            for(int i = 0 ; i < currentRecipe.getIngredients().size() ; i++) {
                double currentQuantity = currentRecipe.getIngredients().get(i).getQuantity();
                currentRecipe.getIngredients().get(i).setQuantity(scaleFactor * currentQuantity);
            }
        }

        //step 2 :
        if (conversionNodeData != null) {
            for(int i = 0 ; i < currentRecipe.getIngredients().size() ; i++) {
                Ingredient currentIngredient = currentRecipe.getIngredients().get(i);
                String currentIngredientUnit = currentIngredient.getUnits();

                ArrayList<Ingredient> potentialTargetUnits = new ArrayList<>();
                boolean doesConversionExistsForUnit = false;
                double roundedSumOfQuantities = 0.0;
                for(ConversionLine conversion: conversionNodeData.getSourceToTargetUnits()) {
                    if (conversion.getSourceUnitData().getUnitName().equals(currentIngredientUnit)) { //todo: 60ml - 250ml --> choosing closest neighbour - postponed
                        //compute the conversion
                        double targetUnitQuantity = (currentIngredient.getQuantity() * conversion.getTargetUnitData().getQuantity())/conversion.getSourceUnitData().getQuantity();
                        Ingredient potentialUnit = new Ingredient(targetUnitQuantity, conversion.getTargetUnitData().getUnitName(), currentIngredient.getName());
                        roundedSumOfQuantities += Math.floor(targetUnitQuantity);
                        potentialTargetUnits.add(potentialUnit);
                        doesConversionExistsForUnit = true;
                    }
                }

                if (!doesConversionExistsForUnit) {
                    //error
                    return 2;
                }
                Ingredient finalUnit = null;
                if (roundedSumOfQuantities == 0.0) {
                    //all zeroes, so choose largest fraction
                    double maxValue = Double.MIN_VALUE;

                    for(Ingredient ingredient: potentialTargetUnits) {
                        if (ingredient.getQuantity() > maxValue) {
                            finalUnit = ingredient;
                            maxValue = ingredient.getQuantity();
                        }
                    }
                } else {
                    //integers exists, so choose smallest integer
                    double minValue = Double.MAX_VALUE;

                    for(Ingredient ingredient: potentialTargetUnits) {
                        if (Math.floor(ingredient.getQuantity()) != 0.0 && ingredient.getQuantity() < minValue) {
                            finalUnit = ingredient;
                            minValue = ingredient.getQuantity();
                        }
                    }
                }

                //apply Measurement system param rules
                if (finalUnit != null) {
                    if (conversionNodeData != null) {
                        double quantity = finalUnit.getQuantity();
                        if (conversionNodeData.getTargetMeasurementParams().getMinWeight() == 0.0) {
                            // either fraction or integer will exist
                            if (conversionNodeData.getTargetMeasurementParams().getFractionIntegers().get(0) != 0) { //f1
                                //represent in fractions
                                // get nearest 1/[0]th fraction of quantity
                                String mixedFraction = getMixedFraction(conversionNodeData, quantity, 0);
                                finalUnit.setQuantityRepresentation(mixedFraction);

                            } else {
                                //represent in the nearest integers
                                String integerRepresentation = getIntegerRepresentation(conversionNodeData, finalUnit, quantity, 1);
                                finalUnit.setQuantityRepresentation(integerRepresentation);
                            }
                        } else {
                            //min wt exists
                            if (finalUnit.getQuantity() <= conversionNodeData.getTargetMeasurementParams().getMinWeight()) {
                                //use f1 i1
                                if (conversionNodeData.getTargetMeasurementParams().getFractionIntegers().get(0) != 0) {
                                    //use f1 - 0
                                    String mixedFraction = getMixedFraction(conversionNodeData, quantity, 0);
                                    finalUnit.setQuantityRepresentation(mixedFraction);
                                } else {
                                    //use i1 - 1
                                    String integerRepresentation = getIntegerRepresentation(conversionNodeData, finalUnit, quantity, 1);
                                    finalUnit.setQuantityRepresentation(integerRepresentation);
                                }
                            } else {
                                //use f2 i2
                                if (conversionNodeData.getTargetMeasurementParams().getFractionIntegers().get(2) != 0) {
                                    //use f2
                                    String mixedFraction = getMixedFraction(conversionNodeData, quantity, 2);
                                    finalUnit.setQuantityRepresentation(mixedFraction);
                                } else {
                                    //use i2
                                    String integerRepresentation = getIntegerRepresentation(conversionNodeData, finalUnit, quantity, 3);
                                    finalUnit.setQuantityRepresentation(integerRepresentation);
                                }
                            }
                        }
                    }
                } else {
                    //not possible or no target unit found
                }


                ingredientsOutput.add(finalUnit);
            }
        }




//        if (path.size() == 2) {
//            //direct
//        } else if (path.size() == 3) {
//            //one hop
//        } else {
//            //beyond one hop or no path
//        }


        convertedRecipe.println(recipeName + " (" + targetSystem + ")");
        convertedRecipe.println("");
        for(Ingredient targetUnits : ingredientsOutput) {
            convertedRecipe.println(targetUnits.toString());
        }
        convertedRecipe.println("");
        RecipeBookContent targetRecipe = recipes.get(normalisedRecipeName);
        convertedRecipe.println(targetRecipe.getInstructions());

        return 0;
    }

}
