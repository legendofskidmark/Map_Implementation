package CSCI.SDC3901.Assignment1;


import java.io.*;

public class A1 {
    public static void main(String[] args) {
        RecipeBook r = new RecipeBook();

        FileReader frUC;
        FileReader frRecipe;
        try {
            frUC = new FileReader("unitConversion.txt");
            frRecipe = new FileReader("recipe.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader brUC = new BufferedReader(frUC);
        BufferedReader brR = new BufferedReader(frRecipe);
        r.unitConversion(brUC); //first file

        try {
            frUC = new FileReader("unitConversion2.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        brUC = new BufferedReader(frUC);
        r.unitConversion(brUC); //second file

        PrintWriter pw;
        try {
            File file = new File("opboon.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            pw = new PrintWriter(file);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        r.recipe("Metric", brR);
        r.convert("Egg Curry", "metric", 2, pw);


        pw.close();

    }
}
