package CSCI.SDC3901.Assignment1;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//
//
// todo: normalize strings everywhere before storing



public class RecipeBook {

    private List<MeasurementSystem> availableSystemParams;
    private List<String> availableSystemNames;
    private List<String> availableUnits;
    private List<String> availableRecipes;
    private ArrayList<ConversionTableNode> conversionTable;
    private HashMap<String, String> conversionUnitMap;
    private HashMap<String, Recipe> recipeMap;
    private HashMap<String, String> systemRecipeMap;


    public RecipeBook() {
        availableSystemParams = new ArrayList<>();
        availableSystemNames = new ArrayList<>();
        availableUnits = new ArrayList<>();
        conversionTable = new ArrayList<>();
        conversionUnitMap = new HashMap<>();
        recipeMap = new HashMap<>();
        systemRecipeMap = new HashMap<>();

        FileReader fr = null;
        try {
            fr = new FileReader("test.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(fr);

//        unitConversion(br); //todo: remove later- added for testing only
        recipe("", br);

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


//        convert("Egg Omlette", "Imperial", 1, pw);
//        br.close();
//        fr.close();
        pw.close();
    }


    /***
     * format :
     * __Measurement System parameters__
     * 1    system_name    min_wt    fraction1    integer1    [fraction2    integer2]
     * 2    system_name    min_wt    fraction1    integer1    [fraction2    integer2]    todo: ignore 2 lines for now
     * __Unit conversions__
     * 3    size1    unit1    size2    unit2
     * 4    size1    unit1    size2    unit2
     * 5    size1    unit1    size2    unit2
     * ...
     * .....
     * and so on
     *
     *
     * todo: my assumptions : sizeI can only be double - not fractions --- for now
     *
     *
     *
     * @param unitMatches
     * @return
     */
    public Boolean unitConversion(BufferedReader unitMatches) {
        int lineNo = 1;
        String line;

        try {
            while ((line = unitMatches.readLine()) != null) {
                String[] sizeUnits = line.split(" ");
                if (lineNo <= 2) {
                    // __Measurement System parameters__
                    //todo: assumption only 2 Measurement System parameters will be given for unit conversion
                    if (sizeUnits.length < 4) {
                        throw new RuntimeException("Invalid Measurement System parameters");
                    }

                    sizeUnits[0] = RecipeUtility.normalizeString(sizeUnits[0]);

                    ArrayList<String> fractionIntegersStr = new ArrayList<> (Arrays.asList(sizeUnits).subList(2, sizeUnits.length));

                    //validation for Measurement System Parameters
                    //todo: assumption : if the min wt = 0 then f1 and I1 will always be there
                    if (Integer.valueOf(sizeUnits[1]).equals(0)) {
                        if (fractionIntegersStr.size() > 2) {
                            throw new RuntimeException("Invalid Measurement System parameters");
                        }
                    } else {
                        if (fractionIntegersStr.size() != 4) {
                            throw new RuntimeException("Invalid Measurement System parameters");
                        }
                    }

                    ArrayList<Integer> fractionIntegers = new ArrayList<>();

                    for (String str: fractionIntegersStr) {
                        fractionIntegers.add(Integer.valueOf(str)); //todo: input validation is taken care by Java here, if there's wrong input, an exception will be thrown
                    }


                    MeasurementSystem msp = new MeasurementSystem(sizeUnits[0], Integer.valueOf(sizeUnits[1]), fractionIntegers);
                    availableSystemNames.add(msp.getSystemName());
                    availableSystemParams.add(msp);
                } else {
                    //validation

                    if (sizeUnits.length != 4) {
                        throw new RuntimeException("Invalid Conversion Table input");
                    }


                    //normalise strings
                    sizeUnits[1] = RecipeUtility.normalizeString(sizeUnits[1]);
                    sizeUnits[3] = RecipeUtility.normalizeString(sizeUnits[3]);
                    ConversionTableNode conversionUnit = new ConversionTableNode(Double.valueOf(sizeUnits[0]), sizeUnits[1],
                                                                                 Double.valueOf(sizeUnits[2]), sizeUnits[3]);


                    //step 1 : check if the conversion is already there in map
                    if (conversionUnitMap.get(sizeUnits[1]) != null && conversionUnitMap.get(sizeUnits[1]).equals(sizeUnits[3])) {
                        //step 2: verify for 5% variance
                        //get the existing value from table.
                        //todo: assumption : only same unit conversions appear twice in the input
                        for (ConversionTableNode node: conversionTable) {
                            if (node.getSystem1_units().equals(sizeUnits[1]) && node.getSystem2_units().equals(sizeUnits[3])) { //todo: assumption : once if a unit is given as spelling, it remains the same throughout the completion of flow even in the instructions.
                                boolean isVarianceAllowed = RecipeUtility.isVarianceGood(node.getSystem1_size(), node.getSystem2_size(), Double.valueOf(sizeUnits[0]), Double.valueOf(sizeUnits[2]));
                                if (isVarianceAllowed) {
                                    conversionTable.add(conversionUnit);
                                } else {
                                    //todo: what should I do
                                    //todo: flag the return value as false
                                }
                                break;
                            }
                        }
                    } else { //if its not there, i.e., "it's a new conversion fact" then add to map and table
                        conversionUnitMap.put(sizeUnits[1], sizeUnits[3]);
                        conversionTable.add(conversionUnit);
                        availableUnits.add(sizeUnits[1]);
                        availableUnits.add(sizeUnits[3]); //todo: should i send ml or metrics
                    }
                }
                lineNo++; //increment line no.
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public Boolean recipe(String originalSystem, BufferedReader recipeContent) {
        //read Recipe title
        int lineNo = 1;
        int blankLineCount = 0;
        String line;
        String recipeTitle = "";
        ArrayList<Ingredient> recipeIngredients = new ArrayList<>();
        String instructions = "";

        try {
            while ((line = recipeContent.readLine()) != null) {
                if (lineNo == 1) {
                    recipeTitle = line; //todo: must be unique
                } else if (line.equals("\n") || line.equals("")) {
                    blankLineCount++;
                    continue;
                }

                if (blankLineCount == 1) { //todo: handle multiple blank lines case
                    String[] ingredient = line.split(" ");
                    if (ingredient.length < 3) { //todo: handle "1 egg" case
                        throw new RuntimeException("Invalid Ingredient");
                    }

                    Ingredient formattedIngredient;
                    double quantity;
                    String units = "";
                    String name = "";
                    int index = 0;
                    if (ingredient[1].contains("/")) {
                        //case 2: <i> <fr> <u> <name>
                        Integer a = Integer.valueOf(ingredient[0]);
                        String[] numeratorDenominator = ingredient[1].split("/");
                        Integer b = Integer.valueOf(numeratorDenominator[0]);
                        Integer c = Integer.valueOf(numeratorDenominator[1]); //todo: edge case
                        quantity = (1.0 * (a * c + b)) / c;
                        units = ingredient[2];
                        index = 3;
                    } else {
                        //case 1: <i> <u> <name>
                        quantity = Double.valueOf(ingredient[0]);
                        units = ingredient[1];
                        index = 2;
                    }
                    for(int i = index ; i < ingredient.length ; i++) name += ingredient[i] + " ";

                    formattedIngredient = new Ingredient(quantity, units, name);
                    recipeIngredients.add(formattedIngredient);
                } else if (blankLineCount > 1) {
                    //read instructions
                    instructions += line + "\n";
                }

                lineNo++;
            }

            Recipe currentRecipe = new Recipe(recipeIngredients, instructions);
            recipeMap.put(recipeTitle, currentRecipe);
        } catch (Exception e) {

        }

        systemRecipeMap.put(originalSystem, recipeTitle);
        return false;
    }


    /*
        list -
            empty - if none
            something - if present
        null - error todo: when does the error occur
        todo: my assumption : not persistent data.... new data for each run
     */
    public List<String> availableUnits() {
        System.out.println(availableUnits);
        return this.availableUnits;
//        return null;
    }


    /*
    list -
            empty - if none
            something - if present
        null - error todo: when does the error occur
        todo: my assumption : not persistent data.... new data for each run
     */
    public List<String> availableRecipes() {
        return availableRecipes;
//        return null;
    }


    public int convert(String recipeName, String targetUnits, double scaleFactor, PrintWriter convertedRecipe) {



        return 0;
    }
}
