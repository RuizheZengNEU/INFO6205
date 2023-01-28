package edu.neu.coe.info6205.threesum;

import static org.junit.Assert.*;

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
	public void testThreeSumBenchmark() {
	    	int[] test=new int[15];
	    	int start = -5;
	    	for (int i = 0;i<test.length;i++) {
	    		test[i]= start;
	    		System.out.print(" "+test[i]);
	    		start++;
	    	}
	    	System.out.print(" \n");
	    	Triple[] r = new ThreeSumQuadratic(test).getTriples();
	    	printTriples(r);
	    }

}
