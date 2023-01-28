package edu.neu.coe.info6205.sort;

import edu.neu.coe.info6205.util.*;

import java.util.Random;

import static edu.neu.coe.info6205.util.Utilities.formatWhole;

/**
 * Helper class for sorting methods with instrumentation of compares and swaps, and in addition, bounds checks.
 * This Helper class may be used for analyzing sort methods but will run at slightly slower speeds than the super-class.
 *
 * @param <X> the underlying type (must be Comparable).
 */
public class InstrumentedHelper<X extends Comparable<X>> extends BaseHelper<X> {

    final static LazyLogger logger = new LazyLogger(InstrumentedHelper.class);

    public boolean instrumented() {
        return true;
    }

    /**
     * Method to determine if one X value is less than another.
     *
     * @param v the candidate element.
     * @param w the comparand element.
     * @return true only if v is less than w.
     */
    @Override
    public boolean less(X v, X w) {
        incrementCompares();
        return super.less(v, w);
    }

    /**
     * Compare values xs[i] and w and return true if xs[i] is less than w.
     *
     * @param xs the array.
     * @param i  the index of the first value.
     * @param w  the second value.
     * @return true if v is less than w.
     */
    @Override
    public boolean less(X[] xs, int i, X w) {
        incrementHits(1);
        return less(xs[i], w);
    }

    /**
     * Compare values xs[i] and w and return true if xs[i] is less than w.
     *
     * @param xs the array.
     * @param i  the index of the first value.
     * @param j  the index of the second value.
     * @return true if v is less than w.
     */
    @Override
    public boolean less(X[] xs, int i, int j) {
        incrementHits(1);
        return less(xs, i, xs[j]);
    }

    /**
     * Swap the elements of array xs at indices i and j.
     *
     * @param xs the array.
     * @param i  one of the indices.
     * @param j  the other index.
     */
    public void swap(X[] xs, int i, int j) {
        incrementSwaps(1);
        X v = xs[i];
        X w = xs[j];
        incrementHits(4);
        if (countFixes) enumerateFixes(xs, i, j, Integer.signum(v.compareTo(w)));
        xs[i] = w;
        xs[j] = v;
    }

    /**
     * Method to perform a stable swap using half-exchanges,
     * i.e. between xs[i] and xs[j] such that xs[j] is moved to index i,
     * and xs[i] thru xs[j-1] are all moved up one.
     * This type of swap is used by insertion sort.
     *
     * @param xs the array of Xs.
     * @param i  the index of the destination of xs[j].
     * @param j  the index of the right-most element to be involved in the swap.
     */
    @Override
    public void swapInto(X[] xs, int i, int j) {
        incrementSwaps(j - i);
        incrementFixes(j - i);
        incrementHits((j - i + 1) * 2);
        super.swapInto(xs, i, j);
    }

    /**
     * Method to perform a stable swap using half-exchanges, and binary search.
     * i.e. x[i] is moved leftwards to its proper place and all elements from
     * the destination of x[i] thru x[i-1] are moved up one place.
     * This type of swap is used by insertion sort.
     *
     * @param xs the array of X elements, whose elements 0 thru i-1 MUST be sorted.
     * @param i  the index of the element to be swapped into the ordered array xs[0...i-1].
     */
    @Override
    public void swapIntoSorted(X[] xs, int i) {
        int j = binarySearch(xs, 0, i, xs[i]);
        incrementHits(1 + (int) Utilities.lg(xs.length));
        if (j < 0) j = -j - 1;
        if (j < i) swapInto(xs, j, i);
    }

    /**
     * Method to enumerate the number of inversions fixed by the swap of i and j elements in the array xs.
     * NOTE: this may not be accurate when there are duplicates.
     *
     * @param xs    the array.
     * @param i     the lower index.
     * @param j     the upper index.
     * @param sense the sense of "fix."
     */
    private void enumerateFixes(X[] xs, int i, int j, int sense) {
        incrementFixes(sense);
        X v = xs[i];
        X w = xs[j];
        for (int k = i + 1; k < j; k++) {
            X x = xs[k];
            if (w.compareTo(x) < 0 && x.compareTo(v) < 0) incrementFixes(2 * sense);
        }
    }

    /**
     * This version of binarySearch is copied from Arrays, but is generalized to operate on X.
     * Furthermore, it does not check its indexes like the Arrays public method.
     *
     * @param xs   the array of X elements.
     * @param from the 'from' index.
     * @param to   the to index.
     * @param key  the key.
     * @return the index of the element where key was found, otherwise the index where it would have been found.
     */
    private int binarySearch(X[] xs, int from, int to, X key) {
        int low = from;
        int high = to - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = compare(xs[mid], key);
            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.
    }

    /**
     * Method to perform a stable swap, but only if xs[i] is less than xs[i-1], i.e. out of order.
     *
     * @param xs the array of elements under consideration
     * @param i  the index of the lower element.
     * @param j  the index of the upper element.
     * @return true if there was an inversion (i.e. the order was wrong and had to be fixed).
     */
    @Override
    public boolean swapConditional(X[] xs, int i, int j) {
        if (i == j) return false;
        if (i > j) return swapConditional(xs, j, i);
        incrementHits(2);
        final X v = xs[i];
        final X w = xs[j];
        incrementCompares();
        int cf = v.compareTo(w);
        if (cf > 0) {
            incrementHits(2);
            if (countFixes) enumerateFixes(xs, i, j, Integer.signum(v.compareTo(w)));
            xs[i] = w;
            xs[j] = v;
            incrementSwaps(1);
        }
        return cf > 0;
    }

    /**
     * Method to perform a stable swap, but only if xs[i] is less than xs[i-1], i.e. out of order.
     *
     * @param xs the array of elements under consideration
     * @param i  the index of the upper element.
     * @return true if there was an inversion (i.e. the order was wrong and had to be fixed).
     */
    @Override
    public boolean swapStableConditional(X[] xs, int i) {
        final X v = xs[i - 1];
        final X w = xs[i];
        incrementHits(2);
        boolean result = w.compareTo(v) < 0;
        incrementCompares();
        if (result) {
            xs[i] = v;
            xs[i - 1] = w;
            incrementSwaps(1);
            incrementHits(2);
            incrementFixes(1);
        }
        return result;
    }

    /**
     * Copy the element at source[j] into target[i]
     *
     * @param source the source array.
     * @param i      the target index.
     * @param target the target array.
     * @param j      the source index.
     */
    @Override
    public void copy(X[] source, int i, X[] target, int j) {
        incrementCopies(1);
        incrementHits(2);
        target[j] = source[i];
    }

    /**
     * Compare v and w
     *
     * @param v the first X.
     * @param w the second X.
     * @return the result of comparing v and w.
     */
    @Override
    public int compare(X v, X w) {
        incrementCompares();
        return v.compareTo(w);
    }

    @Override
    public int compare(X[] xs, int i, X w) {
        incrementHits(1);
        return compare(xs[i], w);
    }

    /**
     * Compare elements of an array.
     *
     * @param xs the array.
     * @param i  one of the indices.
     * @param j  the other index.
     * @return the result of compare(xs[i], xs[j]).
     */
    public int compare(X[] xs, int i, int j) {
        incrementHits(1);
        return compare(xs, i, xs[j]);
    }

    /**
     * Get the configured cutoff value.
     *
     * @return a value for cutoff.
     */
    @Override
    public int cutoff() {
        // NOTE that a cutoff value of 0 or less will result in an infinite recursion for any recursive method that uses it.
        return (cutoff >= 1) ? cutoff : super.cutoff();
    }

    @Override
    public String toString() {
        return "Instrumenting helper for " + description + " with " + formatWhole(n) + " elements";
    }

    /**
     * Initialize this Helper.
     *
     * @param n the size to be managed.
     */
    public void init(int n) {
        compares = 0;
        swaps = 0;
        copies = 0;
        fixes = 0;
        hits = 0;
        // NOTE: it's an error to reset the StatPack if we've been here before
        if (n == this.n && statPack != null) return;
        super.init(n);
        statPack = new StatPack(Statistics.NORMALIZER_LINEARITHMIC_NATURAL, n, COMPARES, SWAPS, COPIES, INVERSIONS, FIXES, HITS);
    }

    /**
     * Method to do any required preProcessing.
     *
     * @param xs the array to be sorted.
     * @return the array after any pre-processing.
     */
    @Override
    public X[] preProcess(X[] xs) {
        final X[] result = super.preProcess(xs);
        // NOTE: because counting inversions is so slow, we only do if for a (configured) number of samples.
        if (countInversions-- > 0) {
            if (statPack != null) statPack.add(INVERSIONS, inversions(result));
            else throw new RuntimeException("InstrumentedHelper.postProcess: no StatPack");
        }
        return result;
    }

    /**
     * Method to post-process the array xs after sorting.
     * By default, this method checks that an array is sorted.
     *
     * @param xs the array to be tested.
     *           TODO log the message
     *           TODO show the number of inversions
     */
    @Override
    public void postProcess(X[] xs) {
        super.postProcess(xs);
        if (!sorted(xs)) throw new BaseHelper.HelperException("Array is not sorted");
        gatherStatistic();
    }

    private void gatherStatistic() {
        if (statPack == null) throw new RuntimeException("InstrumentedHelper.postProcess: no StatPack");
        if (countCompares)
            statPack.add(COMPARES, compares);
        if (countSwaps)
            statPack.add(SWAPS, swaps);
        if (countCopies)
            statPack.add(COPIES, copies);
        if (countFixes)
            statPack.add(FIXES, fixes);
        if (countHits)
            statPack.add(HITS, hits);
    }

    @Override
    public void registerDepth(int depth) {
        if (depth > maxDepth) maxDepth = depth;
    }

    @Override
    public int maxDepth() {
        return maxDepth;
    }

    @Override
    public void close() {
        logger.debug(() -> "Closing Helper: " + description + " with statPack: " + statPack);
        super.close();
    }

    /**
     * Method which retrieves the current value of randomArray.
     *
     * @return an X[] unless random() was never invoked in which case we return null.
     */
    public X[] getRandomArray() {
        return randomArray;
    }

    public StatPack getStatPack() {
        return statPack;
    }

    /**
     * Constructor for explicit random number generator.
     *
     * @param description the description of this Helper (for humans).
     * @param n           the number of elements expected to be sorted. The field n is mutable so can be set after the constructor.
     * @param random      a random number generator.
     * @param config      the configuration (note that the seed value is ignored).
     */
    public InstrumentedHelper(String description, int n, Random random, Config config) {
        // CONSIDER using config.toString here somewhere.
        super(description, n, random, config);
        this.countCopies = config.getBoolean(INSTRUMENTING, COPIES);
        this.countSwaps = config.getBoolean(INSTRUMENTING, SWAPS);
        this.countCompares = config.getBoolean(INSTRUMENTING, COMPARES);
        this.countInversions = config.getInt(INSTRUMENTING, INVERSIONS, 0);
        this.countFixes = config.getBoolean(INSTRUMENTING, FIXES);
        this.countHits = config.getBoolean(INSTRUMENTING, HITS); // the number of array accesses
        this.cutoff = config.getInt("helper", "cutoff", 0);
    }

    /**
     * Constructor to create a Helper
     *
     * @param description the description of this Helper (for humans).
     * @param n           the number of elements expected to be sorted. The field n is mutable so can be set after the constructor.
     * @param config      The configuration.
     */
    public InstrumentedHelper(String description, int n, Config config) {
        this(description, n, config.getLong("helper", "seed", System.currentTimeMillis()), config);
    }

    /**
     * Constructor to create a Helper
     *
     * @param description the description of this Helper (for humans).
     * @param n           the number of elements expected to be sorted. The field n is mutable so can be set after the constructor.
     * @param seed        the seed for the random number generator.
     * @param config      the configuration.
     */
    public InstrumentedHelper(String description, int n, long seed, Config config) {
        this(description, n, new Random(seed), config);
    }

    /**
     * Constructor to create a Helper with a random seed and an n value of 0.
     * <p>
     * NOTE: this constructor is used only by unit tests
     *
     * @param description the description of this Helper (for humans).
     */
    public InstrumentedHelper(String description, Config config) {
        this(description, 0, config);
    }

    public static final String SWAPS = "swaps";
    public static final String COMPARES = "compares";
    public static final String COPIES = "copies";
    public static final String INVERSIONS = "inversions";
    public static final String FIXES = "fixes";
    public static final String HITS = "hits";
    public static final String INSTRUMENTING = "instrumenting";

    public int getCompares() {
        return compares;
    }

    public int getSwaps() {
        return swaps;
    }

    public int getFixes() {
        return fixes;
    }

    /**
     * If instrumenting, increment the number of copies by n.
     *
     * @param n the number of copies made.
     */
    @Override
    public void incrementCopies(int n) {
        if (countCopies) copies += n;
    }

    /**
     * If instrumenting, increment the number of hits by n.
     *
     * @param n the number of hits.
     */
    @Override
    public void incrementHits(int n) {
        if (countHits) hits += n;
    }

    /**
     * If instrumenting, increment the number of fixes by n.
     *
     * @param n the number of copies made.
     */
    @Override
    public void incrementFixes(int n) {
        if (countFixes) fixes += n;
//        System.out.println("incrementFixes: "+n+"; fixes: " + fixes);
    }

    @Override
    public String showStats() {
        return description + ": " + statPack.toString();
    }

    @Override
    public String showFixes(X[] xs) {
        checkFixes(xs);
        return "fixes+inversions: " + (fixes + inversions(xs));
    }

    private void incrementCompares() {
        if (countCompares)
            compares++;
    }

    private void incrementSwaps(int n) {
        if (countSwaps)
            swaps += n;
    }

    // NOTE: the following private methods are only for testing (using reflection).

    private int getHits() {
        return hits;
    }

    private int getCopies() {
        return copies;
    }

    private void checkFixes(X[] xs) {
        if (statPack != null) {
            final double initial = statPack.total(INVERSIONS);
            final int inversions = inversions(xs);
            if (fixes + inversions != initial) {
                System.err.println("inversions and fixes don't match");
            }
        }
    }

    private final int cutoff;
    private final boolean countCopies;
    private final boolean countSwaps;
    private final boolean countCompares;
    private final boolean countFixes;
    private final boolean countHits;
    private StatPack statPack;
    private int compares = 0;
    private int swaps = 0;
    private int copies = 0;
    private int fixes = 0;
    private int hits = 0;
    private int countInversions;
    private int maxDepth = 0;
}
