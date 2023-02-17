package Scratch;

import java.io.*;
import java.util.Arrays;

public class Scratch {
    public static void main(String[] args) {


        String[] a = {"1", "2", "3", "4", "5", "6", "7", "8"};

        for(int i = 0 ; i+1 < a.length ; i++) {
            a[i] = a[i+1];
        }

        a[a.length - 1] = null;

        System.out.println(Arrays.toString(a));
        ////
        for(int i = 2 ; i+1 < a.length ; i++) {
            a[i] = a[i+1];
        }

        a[a.length - 1] = null;
        System.out.println(Arrays.toString(a));
        ////
        for(int i = 2 ; i+1 < a.length ; i++) {
            a[i] = a[i+1];
        }

        a[a.length - 1] = null;
        System.out.println(Arrays.toString(a));
        ////
        for(int i = 2 ; i+1 < a.length ; i++) {
            a[i] = a[i+1];
        }

        a[a.length - 1] = null;
        System.out.println(Arrays.toString(a));


    }
}
