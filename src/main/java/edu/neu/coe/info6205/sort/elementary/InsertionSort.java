/*
  (c) Copyright 2018, 2019 Phasmid Software
 */
package edu.neu.coe.info6205.sort.elementary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.function.Supplier;
import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.SortWithHelper;
import edu.neu.coe.info6205.threesum.ThreeSumQuadratic;
import edu.neu.coe.info6205.util.Config;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import edu.neu.coe.info6205.util.Timer;

public class InsertionSort<X extends Comparable<X>> extends SortWithHelper<X> {

    /**
     * Constructor for any sub-classes to use.
     *
     * @param description the description.
     * @param N           the number of elements expected.
     * @param config      the configuration.
     */
	public static Integer[] getRamdomInts(Integer[] a) {
		ArrayList<Integer> mylist = new ArrayList<Integer>();
		for(int i = 0;i<a.length;i++) {
			mylist.add(a[i]);
		}
		Collections.shuffle(mylist);
		for(int i = 0;i<a.length;i++) {
			a[i]=mylist.get(i);
		}
		return a;
	}
	public static Integer[] getRevertInts(Integer[] a) {
		ArrayList<Integer> mylist = new ArrayList<Integer>();
		for(int i = 0;i<a.length;i++) {
			mylist.add(a[i]);
		}
		Collections.reverse(mylist);
		for(int i = 0;i<a.length;i++) {
			a[i]=mylist.get(i);
		}
		return a;
	}
	public static Integer[] getPartSortedInts(Integer[] a,int from,int to) {
		//Integer[] what = Arrays.stream( a ).boxed().toArray( Integer[]::new );
		Arrays.sort(a, from, to);
		return a;
	}
	public static Integer[] getIntegers(int[] a) {
		return Arrays.stream( a ).boxed().toArray( Integer[]::new );
	}
	
	public void getReport(int runs, int len) {
		Supplier<Integer[]> supplier= new Source(len, len).intsSupplier(10);
		//Warm-up
		Supplier<Integer[]> supplierW= new Source(5000, 5000).intsSupplier(10);
		double timeWarmup = new Timer().repeat(5*runs, supplierW, (xs) -> new InsertionSort<Integer>().sort(xs), null, null);
		
		double time1 = new Timer().repeat(runs, supplier, (xs) -> new InsertionSort<Integer>().sort(xs), null, null);
		double time2 = new Timer().repeat(runs, supplier, (xs) -> new InsertionSort<Integer>().sort(xs), (xs)->getRamdomInts(xs), null);
		double time3 = new Timer().repeat(runs, supplier, (xs) -> new InsertionSort<Integer>().sort(xs), (xs)->getRevertInts(xs), null);
		int halfLen = len/2;
		double time4 = new Timer().repeat(runs, supplier, (xs) -> new InsertionSort<Integer>().sort(xs), (xs)->getPartSortedInts(getRamdomInts(xs),0,halfLen), null);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("Array with size of " + len + " run "+ runs+" times:");
		System.out.println("Sorted array : "+ (time1) +" (msec)");
		System.out.println("Random array : "+ (time2) +" (msec)");
		System.out.println("Revert array : "+ (time3) +" (msec)");
		System.out.println("First half sorted array : "+ (time4) +" (msec)");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}
	
	
	public static void main(String args[])  //static method  
	{  
		Supplier<Integer[]> supplier= new Source(20, 20).intsSupplier(10);
		Integer[] a = supplier.get();
		Integer[] b = getPartSortedInts(a,0,a.length/2);
		for(int i = 0;i<a.length;i++) {
			//System.out.print(b[i] + " ");
		}
		InsertionSort<Integer> iSort = new InsertionSort<Integer>();
		
		
		iSort.getReport(100,400);
		iSort.getReport(100,800);
		iSort.getReport(100,1600);
		iSort.getReport(100,3200);
		iSort.getReport(100,6400);
		iSort.getReport(100,12800);
		
		
	}  
    protected InsertionSort(String description, int N, Config config) {
        super(description, N, config);
    }

    /**
     * Constructor for InsertionSort
     *
     * @param N      the number elements we expect to sort.
     * @param config the configuration.
     */
    public InsertionSort(int N, Config config) {
        this(DESCRIPTION, N, config);
    }

    public InsertionSort(Config config) {
        this(new BaseHelper<>(DESCRIPTION, config));
    }

    /**
     * Constructor for InsertionSort
     *
     * @param helper an explicit instance of Helper to be used.
     */
    public InsertionSort(Helper<X> helper) {
        super(helper);
    }

    public InsertionSort() {
        this(BaseHelper.getHelper(InsertionSort.class));
    }

    /**
     * Sort the sub-array xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort
     * @param to   the index of the first element not to sort
     */
    //Method to perform a stable swap, but only if xs[i] is less than xs[i-1], i.e. out of order.
    //default boolean swapStableConditional(X[] xs, int i) {
    // public Benchmark_Timer(String description, UnaryOperator<T> fPre, Consumer<T> fRun)
    public void sort(X[] xs, int from, int to) {
        final Helper<X> helper = getHelper();
        for(int i = from;i< to;i++) {
        	int j = i;
        	while(j > from ) {
        		if(!helper.swapStableConditional(xs, j)) {
        			break;
        		}
        		
        		j--;
        	}
        }

        // FIXME
        // END 
    }
    
    public static final String DESCRIPTION = "Insertion sort";

    public static <T extends Comparable<T>> void sort(T[] ts) {
        new InsertionSort<T>().mutatingSort(ts);
    }
}
