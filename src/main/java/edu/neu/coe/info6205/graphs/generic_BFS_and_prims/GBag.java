package edu.neu.coe.info6205.graphs.generic_BFS_and_prims;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  The {@code Bag} class represents a bag (or multiset) of
 *  generic items. It supports insertion and iterating over the
 *  items in arbitrary order.
 *  <p>
 *  This implementation uses a singly linked list with a static nested class Node.
 *  textbook that uses a non-static nested class.
 *  The <em>add</em>, <em>isEmpty</em>, and <em>size</em> operations
 *  take constant time. Iteration takes time proportional to the number of items.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/13stacks">Section 1.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *
 *               @param <Item> the generic type of an item in this bag
 *               @param <T> the generic type of an item in this bag
 */
public class GBag<T> implements Iterable<T> {

    private Node<T> first;    // beginning of bag
    private int n;               // number of elements in bag

    // helper linked list class
    private static class Node<T> {
        private T item;
        private Node<T> next;
    }

    /**
     * Initializes an empty bag.
     */
    public GBag() {
        first = null;
        n = 0;
    }

    /**
     * Returns true if this bag is empty.
     *
     * @return {@code true} if this bag is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Returns the number of items in this bag.
     *
     * @return the number of items in this bag
     */
    public int size() {
        return n;
    }

    /**
     * Adds the item to this bag.
     *
     * @param  item the item to add to this bag
     */
    public void add(T item) {
        Node<T> oldfirst = first;
        first = new Node<T>();
        first.item = item;
        first.next = oldfirst;
        n++;
    }


    /**
     * Returns an iterator that iterates over the items in this bag in arbitrary order.
     *
     * @return an iterator that iterates over the items in this bag in arbitrary order
     */
    public Iterator<T> iterator()  {
        return new LinkedIterator(first);
    }

    // an iterator, doesn't implement remove() since it's optional
    private class LinkedIterator implements Iterator<T> {
        private Node<T> current;

        public LinkedIterator(Node<T> first) {
            current = first;
        }

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T item = current.item;
            current = current.next;
            return item;
        }
    }
}


/*Copyright © 2000–2019, Robert Sedgewick and Kevin Wayne.*/

