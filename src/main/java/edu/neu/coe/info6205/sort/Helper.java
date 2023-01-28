package edu.neu.coe.info6205.sort;

import edu.neu.coe.info6205.sort.linearithmic.MergeSortBasic;

import static java.util.Arrays.binarySearch;

/**
 * Helper interface.
 * <p>
 * A Helper provides all of the utilities that are needed by sort methods, for example, compare and swap.
 * <p>
 * CONSIDER having the concept of a current sub-array, then we could dispense with the lo, hi parameters.
 *
 * @param <X>
 */
public interface Helper<X extends Comparable<X>> extends GenericHelper<X> {

    /**
     * Compare value v with value w.
     *
     * @param v the first value.
     * @param w the second value.
     * @return -1 if v is less than w; 1 if v is greater than w; otherwise 0.
     */
    default int compare(X v, X w) {
        return v.compareTo(w);
    }

    /**
     * Compare element i of xs with w.
     *
     * @param xs the array.
     * @param i  the index of the first comparand.
     * @param w  the other value.
     * @return the result of comparing xs[i] to w.
     */
    default int compare(X[] xs, int i, X w) {
        return xs[i].compareTo(w);
    }

    /**
     * Compare elements i and j of xs within the subarray lo...hi
     *
     * @param xs the array.
     * @param i  one of the indices.
     * @param j  the other index.
     * @return the result of comparing xs[i] to xs[j]
     */
    default int compare(X[] xs, int i, int j) {
        return xs[i].compareTo(xs[j]);
    }

    /**
     * Compare values v and w and return true if v is less than w.
     *
     * @param v the first value.
     * @param w the second value.
     * @return true if v is less than w.
     */
    default boolean less(X v, X w) {
        return v.compareTo(w) < 0;
    }

    /**
     * Compare values xs[i] and w and return true if xs[i] is less than w.
     *
     * @param xs the array.
     * @param i  the index of the first value.
     * @param w  the second value.
     * @return true if v is less than w.
     */
    default boolean less(X[] xs, int i, X w) {
        return less(xs[i], w);
    }

    /**
     * Compare values xs[i] and xs[j] and return true if xs[i] is less than xs[j].
     *
     * @param xs the array.
     * @param i  the index of the first value.
     * @param j  the index of the second value.
     * @return true if v is less than w.
     */
    default boolean less(X[] xs, int i, int j) {
        return less(xs, i, xs[j]);
    }

    /**
     * Method to perform a stable swap, but only if xs[i] is less than xs[j], i.e. out of order.
     *
     * @param xs the array of elements under consideration
     * @param i  the index of the lower element.
     * @param j  the index of the upper element.
     * @return true if there was an inversion (i.e. the order was wrong and had to be fixed).
     */
    default boolean swapConditional(X[] xs, int i, int j) {
        if (i == j) return false;
        if (i > j) return swapConditional(xs, j, i);
        final X v = xs[i];
        final X w = xs[j];
        int cf = v.compareTo(w);
        if (cf > 0) {
            xs[i] = w;
            xs[j] = v;
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
    default boolean swapStableConditional(X[] xs, int i) {
        final X v = xs[i - 1];
        final X w = xs[i];
        boolean result = w.compareTo(v) < 0;
        if (result) {
            xs[i] = v;
            xs[i - 1] = w;
        }
        return result;
    }

    /**
     * Method to perform a stable swap using half-exchanges,
     * i.e. between xs[i] and xs[j] such that xs[j] is moved to index i,
     * and xs[i] thru xs[j-1] are all moved up one.
     * This type of swap is used by insertion sort.
     * <p>
     * TODO this method does not seem to work.
     *
     * @param xs the array of Xs.
     * @param i  the index of the destination of xs[j].
     * @param j  the index of the right-most element to be involved in the swap.
     */
    void swapInto(X[] xs, int i, int j);

    /**
     * Method to perform a stable swap using half-exchanges, and binary search.
     * i.e. x[i] is moved leftwards to its proper place and all elements from
     * the destination of x[i] thru x[i-1] are moved up one place.
     * This type of swap is used by insertion sort.
     *
     * @param xs the array of X elements, whose elements 0 thru i-1 MUST be sorted.
     * @param i  the index of the element to be swapped into the ordered array xs[0...i-1].
     */
    default void swapIntoSorted(X[] xs, int i) {
        int j = binarySearch(xs, 0, i, xs[i]);
        if (j < 0) j = -j - 1;
        if (j < i) swapInto(xs, j, i);
    }

    /**
     * CONSIDER eliminate this method as it has been superseded by swapConditional. However, maybe the latter is a better name.
     * Method to fix a potentially unstable inversion.
     *
     * @param xs the array of X elements.
     * @param i  the index of the lower of the two elements to be swapped.
     * @param j  the index of the higher of the two elements to be swapped.
     */
    default void fixInversion(X[] xs, int i, int j) {
        swapConditional(xs, i, j);
    }

    /**
     * CONSIDER eliminate this method as it has been superseded by swapStableConditional. However, maybe the latter is a better name.
     * Method to fix a stable inversion.
     *
     * @param xs the array of X elements.
     * @param i  the index of the higher of the two adjacent elements to be swapped.
     */
    default void fixInversion(X[] xs, int i) {
        swapStableConditional(xs, i);
    }

    /**
     * Return true if xs is sorted, i.e. has no inversions.
     *
     * @param xs an array of Xs.
     * @return true if there are no inversions, else false.
     */
    boolean sorted(X[] xs);

    /**
     * Count the number of inversions of this array.
     *
     * @param xs an array of Xs.
     * @return the number of inversions.
     */
    default int inversions(X[] xs) {
        return MergeSortBasic.countInversions(xs);
    }

    /**
     * Method to post-process the array xs after sorting.
     *
     * @param xs the array that has been sorted.
     */
    void postProcess(X[] xs);

    default int cutoff() {
        return 7;
    }

    /**
     * Initialize this Helper with the size of the array to be managed.
     *
     * @param n the size to be managed.
     */
    void init(int n);

    /**
     * If instrumenting, increment the number of copies by n.
     *
     * @param n the number of copies made.
     */
    default void incrementCopies(int n) {
        // do nothing.
    }

    /**
     * If instrumenting, increment the number of fixes by n.
     *
     * @param n the number of copies made.
     */
    default void incrementFixes(int n) {
        // do nothing.
    }

    default void incrementHits(int i) {
        // do nothing.
    }

    /**
     * Method to do any required preProcessing.
     *
     * @param xs the array to be sorted.
     * @return the array after any pre-processing.
     */
    default X[] preProcess(X[] xs) {
        // CONSIDER invoking init from here.
        return xs;
    }

    default void registerDepth(int depth) {
    }

    default int maxDepth() {
        return 0;
    }

    default String showStats() {
        return "";
    }

    default String showFixes(X[] xs) {
        return "";
    }
}
