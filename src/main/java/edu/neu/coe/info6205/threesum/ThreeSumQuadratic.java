package edu.neu.coe.info6205.threesum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of ThreeSum which follows the approach of dividing the solution-space into
 * N sub-spaces where each sub-space corresponds to a fixed value for the middle index of the three values.
 * Each sub-space is then solved by expanding the scope of the other two indices outwards from the starting point.
 * Since each sub-space can be solved in O(N) time, the overall complexity is O(N^2).
 * <p>
 * NOTE: The array provided in the constructor MUST be ordered.
 */
public class ThreeSumQuadratic implements ThreeSum {
    /**
     * Construct a ThreeSumQuadratic on a.
     * @param a a sorted array.
     */
    public ThreeSumQuadratic(int[] a) {
        this.a = a;
        length = a.length;
    }

    public Triple[] getTriples() {
        List<Triple> triples = new ArrayList<>();
        for (int i = 0; i < length; i++) triples.addAll(getTriples(i));
        Collections.sort(triples);
        
        return triples.stream().distinct().toArray(Triple[]::new);
    }

    /**
     * Get a list of Triples such that the middle index is the given value j.
     *
     * @param j the index of the middle value.
     * @return a Triple such that
     */
    public List<Triple> getTriples(int j) {
    	
        List<Triple> triples = new ArrayList<>();
        //System.out.println(j);
        for(int i =0;i<length;i++) {
        	//System.out.print(" "+a[i]);
        }
        int L = j-1;
        int M = j;
        int R = j+1;
        while(L<R && L>=0 && R<length) {
        	int sum = a[L]+a[M]+a[R];
        	if(sum==0) {
        		triples.add(new Triple(a[L],a[M],a[R]));
        		L--;
        	}
        	else if(sum > 0) {
        		L--;
        	}
        	else{
        		R++;
        	}
        	
        }
        return triples;
    }

    private final int[] a;
    private final int length;
}
