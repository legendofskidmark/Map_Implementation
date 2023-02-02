package CSCI.SDC3901.Assignment1;


import java.io.*;
import java.util.Scanner;

public class A1 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter unit conversion file name : ");
        String unitConversionFileName = scanner.next();

        System.out.println("Enter recipe content's file name : ");
        String recipeFileName = scanner.next();

        RecipeBook recipeBook = new RecipeBook();

        FileReader frUC;
        FileReader frRecipe;
        try {
            frUC = new FileReader(unitConversionFileName);
            frRecipe = new FileReader(recipeFileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader brUC = new BufferedReader(frUC);
        BufferedReader brR = new BufferedReader(frRecipe);
        recipeBook.unitConversion(brUC); //first file

        PrintWriter pw;
        try {
            File file = new File("output.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            pw = new PrintWriter(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        recipeBook.recipe("Metric", brR);
        recipeBook.convert("Egg Curry", "imperial", 2, pw);

        pw.close();

    }
}
