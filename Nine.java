package com.udbac.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;



public class Nine{
    public static void main(String[] args){
        int n = 1000000;//set a base width. 
        double[] a; 
        double[] b;
        a = b = new double[(int)Math.pow(10,8)+1];
        for (int i = 1; i < n+1; i++){
            a[i] = Math.random();
            b[i] = Math.random();
        }
        //ramdonly generate two arrays long enough for the experienment.
        int[] x;
        double[] y;
        x = new int[100];
        y = new double[100];
     //set two variables for the storage and output later for statistics analysis such as plots. 
        int test = 1;//each time the input size will be one width larger than the previous one. 
        while (test < 100){
            double j = 2; 
            double sum = 0;
            int m = n * test;//input size for this time
            long startTime = System.nanoTime();
            while (j < m){
                double k = j;
                while (k < m){
                    sum += a[(int)k]*b[(int)k];
                    k = k * k;
                    }
                j += Math.log(k);
                }
            long endTime = System.nanoTime();
            System.out.println("The Runtime for n = "+m+" is "+(endTime-startTime)+"ns");
        //The screen will print out the run time for each test with growing input size. 
            x[test] = m;
            y[test] = endTime-startTime;//Then the result will be stored.
            test += 1;
        }
        try{
            File file = new File("/Users/fantacy-macintosh/Desktop/result.csv");
            //Change the directory if needed! 
            //Change the directory if needed!
            FileWriter out = new FileWriter(file);
            for (int ii=1;ii<test;ii++){
                out.write(x[ii]+","+y[ii]+"\r");
            }
            out.close();
        } catch(IOException e){
            e.printStackTrace();
        }//All the results are gathered and output to a csv file and then it would be easier to use for making plots or other statistical tests. 
    }
}