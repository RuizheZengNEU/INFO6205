/**
 * Original code:
 * Copyright © 2000–2017, Robert Sedgewick and Kevin Wayne.
 * <p>
 * Modifications:
 * Copyright (c) 2017. Phasmid Software
 */
package edu.neu.coe.info6205.union_find;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
/**
 * Height-weighted Quick Union with Path Compression
 */
public class UF_HWQUPC implements UF {
    /**
     * Ensure that site p is connected to site q,
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     */
    public void connect(int p, int q) {
        if (!isConnected(p, q)) union(p, q);
    }

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     *
     * @param n               the number of sites
     * @param pathCompression whether to use path compression
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public UF_HWQUPC(int n, boolean pathCompression) {
        count = n;
        parent = new int[n];
        height = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            height[i] = 1;
        }
        this.pathCompression = pathCompression;
    }

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     * This data structure uses path compression
     *
     * @param n the number of sites
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public UF_HWQUPC(int n) {
        this(n, true);
    }

    public void show() {
        for (int i = 0; i < parent.length; i++) {
            System.out.printf("%d: %d, %d\n", i, parent[i], height[i]);
        }
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between {@code 1} and {@code n})
     */
    public int components() {
        return count;
    }

    /**
     * Returns the component identifier for the component containing site {@code p}.
     *
     * @param p the integer representing one site
     * @return the component identifier for the component containing site {@code p}
     * @throws IllegalArgumentException unless {@code 0 <= p < n}
     */
    public int find(int p) {
        validate(p);
        int root = p;
        while(parent[root]!=root) {
        	if(pathCompression) {
        		this.doPathCompression(root);
        	}
        	root = parent[root];
        }
        // FIXME
        // END 
        return root;
    }

    /**
     * Returns true if the the two sites are in the same component.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
     * {@code false} otherwise
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * Merges the component containing site {@code p} with the
     * the component containing site {@code q}.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public void union(int p, int q) {
        // CONSIDER can we avoid doing find again?
        mergeComponents(find(p), find(q));
        count--;
        //check if p and q are connected?
    }

    @Override
    public int size() {
        return parent.length;
    }

    /**
     * Used only by testing code
     *
     * @param pathCompression true if you want path compression
     */
    public void setPathCompression(boolean pathCompression) {
        this.pathCompression = pathCompression;
    }

    @Override
    public String toString() {
        return "UF_HWQUPC:" + "\n  count: " + count +
                "\n  path compression? " + pathCompression +
                "\n  parents: " + Arrays.toString(parent) +
                "\n  heights: " + Arrays.toString(height);
    }

    // validate that p is a valid index
    private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    private void updateParent(int p, int x) {
        parent[p] = x;
    }

    private void updateHeight(int p, int x) {
        height[p] += height[x];
    }

    /**
     * Used only by testing code
     *
     * @param i the component
     * @return the parent of the component
     */
    private int getParent(int i) {
        return parent[i];
    }

    private final int[] parent;   // parent[i] = parent of i
    private final int[] height;   // height[i] = height of subtree rooted at i
    private int count;  // number of components
    private boolean pathCompression;

    private void mergeComponents(int i, int j) {
        // FIXME make shorter root point to taller one
    	int heightI  = height[i];
    	int heightJ = height[j];
    	int rootI = find(i);
    	int rootJ = find(j);
    	if(heightI>=heightJ) {
    		parent[rootJ]=rootI;
    		int maxHeight= height[rootI];
    		if(height[rootJ]+1>maxHeight) {
    			height[rootI]=height[rootJ]+1;
    		}
    	}
    	else {
    		parent[rootI]=rootJ;
    		int maxHeight= height[rootJ];
    		if(height[rootI]+1>maxHeight) {
    			height[rootJ]=height[rootI]+1;
    		}
    	}
        // END 
    }

    /**
     * This implements the single-pass path-halving mechanism of path compression
     */
    private void doPathCompression(int i) {
        // FIXME update parent to value of grandparent
        // END
    	
    	int parentI = parent[i];
    	//move first 
    	parent[i] = parent[parent[i]];
    	Boolean isLast=true;
    	for(int j =0;j<parent.length;j++) {
    		if(parent[j]==parentI) {
    			isLast=false;
    		}
    	}
    	//last node on this level should change height
    	if(isLast) {
    		int parentTmp = parentI;
    		while(parent[parentTmp]!=parentTmp) {
    			//System.out.println(parentTmp);
    			int maxHeight =1;
    			for(int j = 0;j<parent.length;j++) {
    				// find all the nodes points to parenTmp !!!!!!!!exclude parentTmp itsef
    				if(parent[j]==parentTmp &&j!=parentTmp && height[j]+1>maxHeight) {
    					maxHeight = height[j]+1;
    				}
    				
    			}
    			//update its height and move to its parent
    			height[parentTmp]=maxHeight;
    			parentTmp = parent[parentTmp];
    		}
    		int root = parentTmp;
    		int maxHeight =1;
    		for(int j = 0;j<parent.length;j++) {
    			if(parent[j]==root && height[j]+1>maxHeight &&j!=root) {
    				maxHeight = height[j]+1;
    			}
    		}
    		height[root] = maxHeight;
    		
    		
    	}
    	
    	
    	
    }
    static int getRandom(int n) {
    	return ThreadLocalRandom
                .current()
                .nextInt(0, n);
    }
    public boolean allConnected() {
    	boolean result = true;
    	
    	return result;
    }
    public void ufClient() {
    	
    	int n = parent.length;
    	System.out.println("");
    	System.out.println("Client with "+n+" sites starts");
    	int numOfConnection = 0;
    	int numOfValidConnection = 0;
    	while(true) {
    		if(count ==1) {
    			System.out.println("Graph is all connected");
    			System.out.println("num of connect(a,b) function called "+numOfConnection+" times");
    			System.out.println("num of of valid connect(a,b) function called "+ numOfValidConnection+ " times");
    			System.out.println("(connect(a,b) is not valid if a b is already connected before the function is called)");
    			//System.out.println(this);
    			System.out.println("Client ends");
    			System.out.println("");
    			break;
    		}
    		int a = UF_HWQUPC.getRandom(n);
    		int b = UF_HWQUPC.getRandom(n);
    		if(isConnected(a,b)) {
    			numOfValidConnection--;
    		}
    		numOfConnection++;
    		numOfValidConnection++;
    		connect(a,b);
    	}
    }
    static public void main(String[] args) {
    	UF_HWQUPC test = new UF_HWQUPC(100,true);
    	test.ufClient();
    	UF_HWQUPC test1 = new UF_HWQUPC(200,true);
    	test1.ufClient();
    	UF_HWQUPC test2 = new UF_HWQUPC(400,true);
    	test2.ufClient();
    	UF_HWQUPC test3 = new UF_HWQUPC(800,true);
    	test3.ufClient();
    	UF_HWQUPC test4 = new UF_HWQUPC(1600,true);
    	test4.ufClient();
    	UF_HWQUPC test5 = new UF_HWQUPC(3200,true);
    	test5.ufClient();
    	UF_HWQUPC test6 = new UF_HWQUPC(6400,true);
    	test6.ufClient();
    	UF_HWQUPC test7 = new UF_HWQUPC(12800,true);
    	test7.ufClient();
    	UF_HWQUPC test8 = new UF_HWQUPC(25600,true);
    	test8.ufClient();
    	UF_HWQUPC test9 = new UF_HWQUPC(51200,true);
    	test9.ufClient();
    	UF_HWQUPC test10 = new UF_HWQUPC(102400,true);
    	test10.ufClient();
    	UF_HWQUPC test11 = new UF_HWQUPC(204800,true);
    	test11.ufClient();
    	
    }
}
