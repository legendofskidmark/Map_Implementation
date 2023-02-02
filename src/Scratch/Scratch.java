package Scratch;

import java.io.*;

public class Scratch {
    public static void main(String[] args) {


        try {
            //PrintWriter demo
            File file = new File("test.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            PrintWriter pw = new PrintWriter(file);
            pw.println("Boon is in Canada");
            pw.println("Hi everyone, howz it goin ?");
            pw.close(); //todo: imp

            //BufferedReader demo
            FileReader fr = null;
            fr = new FileReader("test.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close(); //todo: imp
        } catch (Exception e) { //make the exception more specific later
            throw new RuntimeException(e);
        }


    }
}
