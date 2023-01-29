package edu.neu.coe.info6205.threesum;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import static org.junit.Assert.*;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import edu.neu.coe.info6205.util.TimeLogger;
import edu.neu.coe.info6205.util.Utilities;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import edu.neu.coe.info6205.util.Timer;
import org.junit.Test;

public class UnitTest {
	void printTriples(Triple[] r) {
    	System.out.println("\nTriples = ");
        if(r.length!=0) {
        	
        	 for(int i =0;i<r.length;i++) {
        		 System.out.println(" "+r[i]);
        	 }
        }
    }
	@Test
	public void testThreeSumBenchmark1() {
	    	int[] test=new int[15];
	    	int start = -5;
	    	for (int i = 0;i<test.length;i++) {
	    		test[i]= start;
	    		System.out.print(" "+test[i]);
	    		start++;
	    	}
	    	System.out.print(" \n");
	    	Triple[] r = new ThreeSumQuadratic(test).getTriples();
	    	Triple[] r2 = new ThreeSumQuadraticWithCalipers(test).getTriples();
	    	Triple[] r3 = new ThreeSumQuadrithmic(test).getTriples();
	    	Triple[] r4 = new ThreeSumCubic(test).getTriples();
	    	printTriples(r);
	    	assertEquals(r2.length, r.length);
	    	assertEquals(r2.length, r3.length);
	    	assertEquals(r3.length, r4.length);
	    }
	@Test
	public void testThreeSumBenchmark2() {
		//test ramdom array 100 times
		for(int i = 0;i<100;i++) {
			Supplier<int[]> intsSupplier = new Source(200,200).intsSupplier(10);
	       int[] test = intsSupplier.get();
	       Triple[] r = new ThreeSumQuadratic(test).getTriples();
	       Triple[] r2 = new ThreeSumQuadraticWithCalipers(test).getTriples();
	       Triple[] r3 = new ThreeSumQuadrithmic(test).getTriples();
	       Triple[] r4 = new ThreeSumCubic(test).getTriples();
	       assertEquals(r2.length, r.length);
	    	assertEquals(r2.length, r3.length);
	    	assertEquals(r3.length, r4.length);
	       
		}
		 
	}
	@Test
	public void testThreeSumBenchmark4() {
		//test ramdom array 100 times
		for(int i = 0;i<100;i++) {
			Supplier<int[]> intsSupplier = new Source(400,400).intsSupplier(10);
	       int[] test = intsSupplier.get();
	       Triple[] r = new ThreeSumQuadratic(test).getTriples();
	       Triple[] r2 = new ThreeSumQuadraticWithCalipers(test).getTriples();
	       Triple[] r3 = new ThreeSumQuadrithmic(test).getTriples();
	       Triple[] r4 = new ThreeSumCubic(test).getTriples();
	       assertEquals(r2.length, r.length);
	    	assertEquals(r2.length, r3.length);
	    	assertEquals(r3.length, r4.length);
	       
		}
		 
	}
	@Test
	public void testThreeSumBenchmark5() {
		//test ramdom array 100 times
		for(int i = 0;i<100;i++) {
			Supplier<int[]> intsSupplier = new Source(800,800).intsSupplier(10);
	       int[] test = intsSupplier.get();
	       Triple[] r = new ThreeSumQuadratic(test).getTriples();
	       Triple[] r2 = new ThreeSumQuadraticWithCalipers(test).getTriples();
	       Triple[] r3 = new ThreeSumQuadrithmic(test).getTriples();
	       Triple[] r4 = new ThreeSumCubic(test).getTriples();
	       assertEquals(r2.length, r.length);
	    	assertEquals(r2.length, r3.length);
	    	assertEquals(r3.length, r4.length);
	       
		}
		 
	}
	

}
