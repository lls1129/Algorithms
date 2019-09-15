

import java.io.*;
import java.util.*;
import java.math.*;
//To calculate math expression given by String: 
//https://github.com/fasseg/exp4j/tree/master/src/main/java/net/objecthunter/exp4j
import net.objecthunter.exp4j.*;




public class ComplexityValidator {
    
    //For convience, define a function to copy array. 
    public static double[] copy(double[] a) {
        double[] b = new double[a.length];
        for (int i = 0; i < a.length; i++){
            b[i] = 0. + a[i];
        }
        return b;
    }
    
    public static double[] copy(double[] a, int s, int e) {
        double[] b = new double[e - s];
        for (int i = 0; i < e - s; i++) {
            b[i] = 0. + a[i];
        }
        return b;
    }
    
    //In order to compare expression time and testing time, data should be normalized. 
    public static double GetNormalizeRatio(double[] a, double[] b) {
        int length = Math.min(a.length, b.length);
        int i = 2;
        double sum_a = 0;
        double sum_b = 0;
        while (i < length) {
            sum_a = sum_a + a[i];
            sum_b = sum_b + b[i];
            i = i + 1;
        }
        double normalizeRatio = sum_a/sum_b;
        return normalizeRatio; //Return a ratio and then normalize b by b times ratio. 
    }
    
    //Rnew is a statistic to valuate how close the predictions are to true values. The better prediction means it should return a value close to 1. (negative RNew means very bad prediction.)
    //This function could only be used when comparing the experienment runtime with theoretical time given by specific mathematical expressions with correct parameters such as 0.4*x^2*log(x)+0.9*x. 
    public static double Rnew(double[] a, double[] b) { 
        int length = Math.min(a.length, b.length);
        double ratio = GetNormalizeRatio(a, b);
        double[] bb = copy(b);
        double[] bbb = copy(b);
        for (int i = 0; i < length; i++) {
            bb[i] = b[i] * ratio;
            bbb[i] = Math.log(bb[i]);
        }
        double m = Mean(bbb);
        double Q = 0; 
        double S = 0;
        //Ignore the first 3 outcome which are often outliers for regression. 
        for (int i = 3; i < length; i++) {
            //Q = Q + Math.pow((a[i] - bb[i]), 2);
            //S = S + Math.pow((bb[i]),2);
            Q = Q + Math.pow((Math.log(a[i]) - Math.log(bb[i])), 2);
            S = S + Math.pow((Math.log(bb[i]) - m),2);
        }
        if (S != 0) {
            double R = 1 - Math.pow(Q/S, 0.5);
            return R;
        }
        return 0.;
    }
    
    //Some statistics calculations: (Could also define a subclass of array to override all these methods. )
    public static double Sum(double[] a){
        double sum = 0;
        for (int i = 0; i < a.length; i++){
            sum = sum + a[i];
        }
        return sum;
    }
    public static double Mean(double[] a){
        return Sum(a)/a.length;
    }
    public static double SD(double[] a){
        double[] b = new double[a.length];
        double mean = Mean(a);
        for (int i = 0; i < a.length; i++){
            b[i] = Math.pow(a[i] - mean, 2); 
        }
        return Math.pow(Mean(b),0.5);
    }
    public static double GetMax(double[] a){
        double max = 0;
        for (int i = 0; i < a.length; i++) {
            max = Math.max(max, a[i]);
        }
        return max;
    }
    public static double GetMin(double[] a){
        double min = GetMax(a);
        for (int i = 0; i < a.length; i++) {
            min = Math.min(min, a[i]);
        }
        return min;
    }
    public static double Range(double[] a){
        return GetMax(a) - GetMin(a);
    }
    public static double Correlation(double[] a, double[] b){
        double[] aa = copy(a);
        double[] bb = copy(b);
        double sa = 0;
        double sb = 0;
        double saa = 0;
        double sbb = 0;
        double sab = 0;
        int n = Math.min(a.length, b.length);
        for (int i = 0; i < n; i++) {
            sa = sa + a[i];
            sb = sb + b[i];
            saa = saa + a[i] * a[i];
            sbb = sbb + b[i] * b[i];
            sab = sab + a[i] * b[i];
        }
        double cov = sab / n - sa * sb / n / n;
        double sigma_a = Math.pow(saa / n - sa * sa / n / n, 0.5);
        double sigma_b = Math.pow(sbb / n - sb * sb / n / n, 0.5);
        //System.out.println("cov = " + cov);
        //System.out.println("sigma_a = " + sigma_a);
        //System.out.println("sigma_b = " + sigma_b);
        return cov / sigma_a / sigma_b; 
    }
    
    //For most cases, the log value would be way more useful when comparing. 
    //When input size is getting larger, the log value of the experiment runtime should converge to the log value of theoretical time regardless of parameters. 
    //LogRnew should be close to 1 if two arrays in terms of N are similar. PS: it could be negative when they are far from each other. 
    public static double LogRnew(double[] a, double[] b, double[] N) { 
        int length = Math.min(a.length, b.length);
        length = Math.min(length, N.length);
        double Q = 0; 
        double S = 0;
        double aa[] = copy(a,3,length);
        double bb[] = copy(b,3,length);
        double NN[] = copy(N,3,length);
        double R = 1;
        for (int i = 0; i < length-3; i++) {
            NN[i] = Math.log(N[i+3] + 0.01);
            aa[i] = Math.log(a[i+3] + 0.01) / NN[i];
            bb[i] = Math.log(b[i+3] + 0.01) / NN[i];
            Q = Q + Math.pow((aa[i] - bb[i]), 2);
            S = S + Math.pow(bb[i],2);
            //System.out.println("aa[" + i +"] = " + aa[i]);
            //System.out.println("bb[" + i +"] = " + bb[i]);
        }
        if (S != 0) {
            R = 1 - Math.pow(Q/S, 0.5);
            //System.out.println("R = " + R);
        }
        //System.out.println("Correlation = " + Correlation(a,b));
        return Correlation(aa,bb) * R;
    }

    

        
    
    public static boolean R95(double[] a, double[] b) { //Return true if Rnew is more than 0.95. 
        return Rnew(a, b) >= 0.95;
    }
    
    public static boolean R90(double[] a, double[] b) { //Return true if Rnew is more than 0.9. 
        return Rnew(a, b) >= 0.90;
    }
    
    public static boolean RBiggerThanN(double[] a, double[] b, double N) { //Return true if Rnew is more than N. 
        return Rnew(a, b) >= N;
    }
    
    
    
    //To compute a math expression from a string with a variable. 
    public static double ComplexityExpression (String cexpr, double RunningTimes) {
        Expression e = new ExpressionBuilder(cexpr)
            .variables("N") //Variable could be N, n or x but only one of them. 
            .variables("n")
            .variables("x")
            .build()
            .setVariable("N", RunningTimes)
            .setVariable("n", RunningTimes)
            .setVariable("x", RunningTimes);
        double result = e.evaluate();
        return result; 
    }
    
    //ComplexityExpression class with two methods. 
    public static class ComplexityExpression {
        Expression expression; 
        public ComplexityExpression(String s) { //when initial a ComplexityExpression, it should be compiled into math expression. 
            this.expression = new ExpressionBuilder(s)
                .variables("N")
                .variables("n")
                .variables("x")
                .build();
        }
        public double run(double RunningTimes){ //Then it could run by given variable values. 
            expression.setVariable("N", RunningTimes)
                .setVariable("n", RunningTimes)
                .setVariable("x", RunningTimes);
            return expression.evaluate();
        }
        public double runFromString (String cexpr, double RunningTimes) { //Or given both String and variables, thus initial and compute together. 
            Expression e = new ExpressionBuilder(cexpr)
                .variables("N")
                .variables("n")
                .variables("x")
                .build()
                .setVariable("N", RunningTimes)
                .setVariable("n", RunningTimes)
                .setVariable("x", RunningTimes);
            double result = e.evaluate();
            return result; 
        }
    }
    
    
    
    
    //The following two class need to be implemented. 
    public static class Algorithm {
        Object object; 
        Algorithm(Object o) {
            this.object = o; 
        }
        public void run(double N){
            for (int i = 1; i <= N; i++){
                ; //run the algorithm with input size of N; 
            }
        }
    }
    
    public static Algorithm GetAlgorithm(String s) {
        return null; //Temporary return nothing. 
    }
    
    public static class TCConfirmableAlgorithm {
        Algorithm algorithm; 
        TCConfirmableAlgorithm(String s) {
            this.algorithm = GetAlgorithm(s);
        }
        public double run(double N) {
            long startTime = System.nanoTime();
            algorithm.run(N);
            long endTime = System.nanoTime();
            return (endTime - startTime); 
        }
    }
    
    public static boolean validateTheta(TCConfirmableAlgorithm algorithm, ComplexityExpression cexpr) {
        double[] RunningTimes = new double[10];
        double[] ExperimentTime = new double[10];
        double[] TheoreticalTime = new double[10];
        for (int i = 0; i <= 9; i++) {
            RunningTimes[i] = Math.pow(10, i+1);
            ExperimentTime[i] = algorithm.run(RunningTimes[i]);
            TheoreticalTime[i] = cexpr.run(RunningTimes[i]);
        }
		return R90(ExperimentTime, TheoreticalTime);
	}
    
    
    
    //TCReport data class, which required to contain two arrays of double, the first one is the input size, the second one is the report time. 
    public static class TCReport{
        int length; 
        double[] RunningTimes; 
        double[] ReportTime; 
        
        public TCReport(double[] N, double[] t) {
            this.length = N.length;
            this.RunningTimes = N; 
            this.ReportTime = t;
        }
    }
    
    
    public static boolean validateTheta(TCReport tcReport, ComplexityExpression cexpr) {
        int length = tcReport.RunningTimes.length; 
        double[] ExpressionTime = new double[length];
        for (int i = 0; i < length; i++) {
            //cexpr.expression.setVariable("N",tcReport.RunningTimes[i])
            //    .setVariable("n",tcReport.RunningTimes[i])
            //    .setVariable("x",tcReport.RunningTimes[i]);
            //ExpressionTime[i] = cexpr.expression.evaluate();
            ExpressionTime[i] = cexpr.run(tcReport.RunningTimes[i]); 
        }
        return R90(tcReport.ReportTime, ExpressionTime);
    }
    
    public static void main(String[] args) {
        //Some simple test: 
        double[] a = {10,100,1000,10000,100000,1000000,10000000,100000000};
        double[] b = {5,100,1500,20000,250000,3000000,35000000,400000000};
        double[] aa = {1,5,10,50,100,500,1000,5000};
        double[] d = {5,102,1505,20100,260000,3080000,34900000,401000000};
        ComplexityExpression expr = new ComplexityExpression("x+x^5");
        TCReport report = new TCReport(a,b);
        //System.out.println(validateTheta(report,expr));
        double[] c = {0.002506,0.125594,6.294627,315.478627,15811.3883,792446.5962,39716411.74,1990535853};
        double[] e = new double[8];
        double[] f = new double[8];
        double[] g = copy(a,1,6);
        for(int i = 0; i < 8; i++){
            e[i] = expr.run(aa[i]);
            f[i] = Math.pow(1.1,aa[i]);
        }

        System.out.println("LogRnew: " + LogRnew(f,e,aa));
        System.out.println("LogRnew: " + LogRnew(e,f,aa));
        /*
        System.out.println("Rnew: " + Rnew(b,d));
        System.out.println("Rnew: " + Rnew(d,b));
        System.out.println("LogRnew: " + LogRnew(d,b,a));
        System.out.println("LogRnew: " + LogRnew(b,d,a));
        */
        double[] z = {1,10,100,1000,10000};
        double[] x = {2,3,4,5,6};
        double[] y = {16,81,256,625,1296};
        System.out.println(Correlation(x,y));
        
        System.out.println("LogRnew: " + LogRnew(x,y,z));
        
    }
}
