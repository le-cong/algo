import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

  private static final int INITIAL_SIZE = 4;
  private Object[] items = new Object[INITIAL_SIZE];
  private int size = 0;

  // construct an empty randomized queue
  public RandomizedQueue() {
  }

  // is the randomized queue empty?
  public boolean isEmpty() {
    return size == 0;
  }

  // return the number of items on the randomized queue
  public int size() {
    return size;
  }

  // add the item
  public void enqueue(Item item) {
    if (item == null) {
      throw new IllegalArgumentException();
    }
    if (size == items.length) {
      items = Arrays.copyOf(items, size * 2);
    }
    items[size] = item;
    size++;
  }

  // remove and return a random item
  public Item dequeue() {
    if (size == 0) {
      throw new NoSuchElementException();
    }

    int randomIdx = StdRandom.uniform(size);
    Item item = (Item) items[randomIdx];

    items[randomIdx] = items[size - 1];
    items[size - 1] = null;

    size--;
    if (size <= items.length / 4 && size > INITIAL_SIZE) {
      items = Arrays.copyOf(items, items.length / 2);
    }

    return item;
  }

  // return a random item (but do not remove it)
  public Item sample() {
    if (size == 0) {
      throw new NoSuchElementException();
    }

    int randomIdx = StdRandom.uniform(size);
    Item item = (Item) items[randomIdx];

    return item;
  }

  // return an independent iterator over items in random order
  public Iterator<Item> iterator() {
    return new Iterator<Item>() {
      int itSize = size;
      Object[] itItems = Arrays.copyOf(items, size);

      public boolean hasNext() {
        return itSize > 0;
      }

      public Item next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        int idx = StdRandom.uniform(itSize);
        Item item = (Item) itItems[idx];
        itItems[idx] = itItems[itSize - 1];
        itItems[itSize - 1] = null;
        itSize--;
        return item;
      }
    };
  }

  // unit testing (required)
  public static void main(String[] args) {

    for (int i = 0; i < 3; i++) {
      StdOut.println("############################################ " + i);

      RandomizedQueue<Integer> ranq = new RandomizedQueue<>();
      StdOut.println("=================================== Create");
      StdOut.printf("is empty? %b \n", ranq.isEmpty());

      StdOut.println("=================================== Add");
      ranq.enqueue(1);
      ranq.enqueue(2);
      ranq.enqueue(3);
      ranq.enqueue(4);
      StdOut.printf("is empty? %b \n", ranq.isEmpty());
      StdOut.printf("size= %d \n", ranq.size());

      StdOut.println("=================================== Sample");
      for (int j = 0; j < 3; j++) {
        StdOut.printf("sample #%d = %d\n", j, ranq.sample());
      }

      StdOut.println("=================================== Iterate");
      Iterator<Integer> it = ranq.iterator();
      while (it.hasNext()) {
        StdOut.printf("it.next = %d \n", it.next());
      }

      StdOut.println("=================================== Remove");
      StdOut.printf("dequeue= %d \n", ranq.dequeue());
      StdOut.printf("dequeue= %d \n", ranq.dequeue());
      StdOut.printf("dequeue= %d \n", ranq.dequeue());
      StdOut.printf("dequeue= %d \n", ranq.dequeue());
      StdOut.printf("is empty? %b \n", ranq.isEmpty());
      StdOut.printf("size= %d \n", ranq.size());
    }

  }

}