package edu.neu.coe.info6205.sort;
import edu.neu.coe.info6205.sort.linearithmic.MergeSort;
import edu.neu.coe.info6205.sort.linearithmic.QuickSort;
import edu.neu.coe.info6205.sort.linearithmic.QuickSort_DualPivot;
import edu.neu.coe.info6205.sort.elementary.HeapSort;
import edu.neu.coe.info6205.util.Config;
import edu.neu.coe.info6205.util.StatPack;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;
import edu.neu.coe.info6205.util.Config;
import edu.neu.coe.info6205.util.Timer;

public class HW6 {

	private void startTest(Sort<Integer> s, int length, BaseHelper<Integer> helper) {
		
		Source intS= new Source(length,length);
		Supplier intsSup = intS.intsSupplier(3);
		//Integer[] ints = (Integer[]) intsSup.get();
		
		//Config config = Config.setupConfig("true", "0", "1", "1", "");
		//BaseHelper<Integer> helper = new InstrumentedHelper<>("test",config);
        s.init(length);
		Function<Integer[],Integer[]> function = t -> {
			return s.sort(t);
        };
        /*
        UnaryOperator<Integer[]> preFun = t->{
        	return helper.preProcess((Integer[])t);
        };*/
        Consumer<Integer[]> postFun=t->{
        	helper.postProcess((Integer[])t);
        };
        double result = new Timer().repeat(100, intsSup,  function,null,postFun);
        /*
        for(int i = 0; i < ints.length;i++) {
        	System.out.println(ints[i]);
        }*/
        System.out.println();
        System.out.println("ArraySize = "+length);
        System.out.println( helper.showStats());
        System.out.println( helper);
        System.out.println( "time: "+result+" msec");
        System.out.println();
		
	}
	
	public void getResult(int length) {
		Config config = Config.setupConfig("false", "0", "1", "40", "");
		HW6 hw6 = new HW6();
		
		//System.out.println("-------Test Starts ------");
		BaseHelper<Integer> helper7 = new InstrumentedHelper<>("test",config);
		BaseHelper<Integer> helper8 = new InstrumentedHelper<>("test",config);
		BaseHelper<Integer> helper9 = new InstrumentedHelper<>("test",config);
		Sort<Integer> sorter7 = new QuickSort_DualPivot<>(helper7);
		Sort<Integer> sorter8 = new MergeSort<>(helper8);
		Sort<Integer> sorter9 = new HeapSort<>(helper9);
		System.out.println("Merge Sort :");
		hw6.startTest(sorter8, length, helper8);
		System.out.println("Quick sort Dual Pivot:");
		hw6.startTest(sorter7, length, helper7);
		System.out.println("Heap Sort : ");
		hw6.startTest(sorter9, length, helper9);
	}
	
	public static void main(String[] args) {
		
		//Config config = Config.setupConfig("true", "0", "1", "40", "");
		HW6 hw6 = new HW6();
		//warm up
		//hw6.getResult(100000);
		hw6.getResult(200000);
		System.out.println("-------Test Starts ------");
		int length = 10000;
		while(length <= 256000) {
			hw6.getResult(length);
			length*=2;
		}
		
		
		
		
		
		
		
		
	}

	
}
