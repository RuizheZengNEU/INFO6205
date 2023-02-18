package edu.neu.coe.info6205.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * CONSIDER tidy it up a bit.
 */
public class Main {

    public static void main(String[] args) {
        //processArgs(args);
    	//update numOfThreads here
    	int numOfThreads = 16;
    	int arraySize = 2000000;
        //System.out.println("Degree of parallelism: " + numOfThreads);
        //Random random = new Random();
        //int[] array = new int[arraySize];
        ArrayList<Long> timeList = new ArrayList<>();
        
        /*for (int j = 50; j < 100; j++) {
            ParSort.cutoff = 10000 * (j + 1);
            ParSort.numOfThreads = numOfThreads;
            // for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
            long time;
            long startTime = System.currentTimeMillis();
            for (int t = 0; t < 10; t++) {
                for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                ParSort.sort(array, 0, array.length);
            }
            long endTime = System.currentTimeMillis();
            time = (endTime - startTime);
            timeList.add(time);


            System.out.println("cutoff：" + "Degree of parallelism: " + (ParSort.numOfThreads)+(ParSort.cutoff) + "\t\t10times Time:" + time + "ms");

        }*/
        for (int i = 3; i <= 6; i++) {
        	// for each array size
        	arraySize = 1000000*(int)Math.pow(2, i);
        	int[] array = new int[arraySize];
        	Random random = new Random();
        	//ArrayList<Long> timeList = new ArrayList<>();
        	for (int j = 1; j <= 10; j++) {
        	//for each percent of cutoff
        		 ParSort.cutoff = arraySize/10*j;
        		 for(int z = 1;z <=5;z++) {
        			 //for each z power of 2 num of threads (2-32)
        			 ParSort.numOfThreads = (int)Math.pow(2,z);
        			 long time;
        	         long startTime = System.currentTimeMillis();
        	         for (int t = 0; t < 10; t++) {
        	                for (int y = 0; y < array.length; y++) array[y] = random.nextInt(10000000);
        	                ParSort.sort(array, 0, array.length);
        	            }
        	         long endTime = System.currentTimeMillis();
        	         time = (endTime - startTime);
        	         timeList.add(time);
        	         System.out.println("Array size : "+arraySize+" \t\tcutoff： "+(ParSort.cutoff) +" \t\tPercentage: "+j+"/10 " + " \t\tDegree of parallelism: " + (ParSort.numOfThreads) + "\t\t10times Time:" + time + "ms");
        		 }
        		
        	}
        }
        
        
        try {
            FileOutputStream fis = new FileOutputStream("./src/result.csv");
            OutputStreamWriter isr = new OutputStreamWriter(fis);
            BufferedWriter bw = new BufferedWriter(isr);
            int j = 0;
            for (long i : timeList) {
                String content = (double) 10000 * (j + 1) / 2000000 + "," + (double) i / 10 + "\n";
                j++;
                bw.write(content);
                bw.flush();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processArgs(String[] args) {
    	System.out.println("!!!1!!!!");
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
    	System.out.println("!!!!!2!!!!!");
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        System.out.println("!!!!"+xs[0]+ " "+xs[1]);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}
