import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

  private class Node<Item> {
    Node<Item> prior;
    Node<Item> next;
    Item value;
  }

  private Node<Item> first = null;
  private Node<Item> last = null;
  private int size = 0;

  // construct an empty deque
  public Deque() {

  }

  // is the deque empty?
  public boolean isEmpty() {
    return size == 0;
  }

  // return the number of items on the deque
  public int size() {
    return this.size;
  }

  // add the item to the front
  public void addFirst(Item item) {
    if (item == null) {
      throw new IllegalArgumentException();
    }

    Node<Item> newNode = new Node<Item>();
    newNode.value = item;

    if (first != null) {
      newNode.next = first;
      first.prior = newNode;
    }
    first = newNode;

    if (last == null) {
      last = newNode;
    }

    size++;
  }

  // add the item to the back
  public void addLast(Item item) {
    if (item == null) {
      throw new IllegalArgumentException();
    }

    Node<Item> newNode = new Node<Item>();
    newNode.value = item;

    if (last != null) {
      newNode.prior = last;
      last.next = newNode;
    }
    last = newNode;

    if (first == null) {
      first = newNode;
    }

    size++;
  }

  // remove and return the item from the front
  public Item removeFirst() {
    if (first == null) {
      throw new NoSuchElementException();
    }

    Item ret = first.value;
    first.value = null;

    first = first.next;
    if (first != null) {
      first.prior = null;
    } else {
      last = null;
    }

    size--;
    return ret;
  }

  // remove and return the item from the back
  public Item removeLast() {
    if (last == null) {
      throw new NoSuchElementException();
    }

    Item ret = last.value;
    last.value = null;

    last = last.prior;
    if (last != null) {
      last.next = null;
    } else {
      first = null;
    }

    size--;
    return ret;
  }

  // return an iterator over items in order from front to back
  public Iterator<Item> iterator() {
    return new Iterator<Item>() {

      Node<Item> cur = first;

      @Override
      public boolean hasNext() {
        return cur != null;
      }

      @Override
      public Item next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        Item value = cur.value;
        cur = cur.next;
        return value;
      }
    };
  }

  // unit testing (required)
  public static void main(String[] args) {
    Deque<Integer> deque = new Deque<>();
    StdOut.println("=================================== Create");
    StdOut.printf("is empty? %b \n", deque.isEmpty());

    StdOut.println("=================================== Add");
    deque.addFirst(1);
    deque.addLast(2);
    deque.addFirst(3);
    deque.addLast(4);
    StdOut.printf("is empty? %b \n", deque.isEmpty());
    StdOut.printf("size= %d \n", deque.size());

    StdOut.println("=================================== Iterate");
    Iterator<Integer> it = deque.iterator();
    while (it.hasNext()) {
      StdOut.printf("it.next = %d \n", it.next());
    }

    StdOut.println("=================================== Remove");
    StdOut.printf("removeFirst (should be 3)= %d \n", deque.removeFirst());
    StdOut.printf("removeFirst (should be 1)= %d \n", deque.removeFirst());
    StdOut.printf("removeFirst (should be 2)= %d \n", deque.removeFirst());
    StdOut.printf("removeLast (should be 4)= %d \n", deque.removeLast());
    StdOut.printf("is empty? %b \n", deque.isEmpty());
    StdOut.printf("size= %d \n", deque.size());

    // StdOut.println("=================================== Exception");
    // try {
    // deque.removeFirst();
    // } catch (Exception ex) {
    // StdOut.println("=================================== Exception caught.");
    // }

  }

}