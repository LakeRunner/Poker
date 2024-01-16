package com.example.task_2.Program.MyLists;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public interface MyList<T> extends Iterable<T> {

    void add(T value);

    void add(int index, T value);

    void addAll(int index, Collection<? extends T> collection);

    void clear();

    Iterator<T> descendingIterator();

    T get(int index);

    MyList<T> getClone();

    T getFirst();

    T getLast();

    Integer lastIndexOf(T value);

    void remove(int index);

    void set(int index, T value);

    int size();

    void sort(Comparator<T> c);

    default boolean contains(T value) {
        for (T t : this) {
            if (t.equals(value)) {
                return true;
            }
        }
        return false;
    }

    default Integer indexOf(T value) {
        int counter = 0;
        for (T t : this) {
            if (t.equals(value)) {
                return counter;
            }
            counter++;
        }
        return null;
    }

    default void remove(T value) {
        int counter = 0;
        for (T t : this) {
            if (t.equals(value)) {
                remove(counter);
                return;
            }
            counter++;
        }
    }

    default Object[] toArray() {
        Object[] arr = new Object[size()];
        int counter = 0;
        for (Object t : this) {
            arr[counter] = t;
            counter++;
        }
        return arr;
    }

    default T[] toArray(T[] arr) {
        if (arr.length != size()) {
            throw new IndexOutOfBoundsException("The length of the array and the size of the list do not match");
        } else {
            int counter = 0;
            for (T t : this) {
                arr[counter] = t;
                counter++;
            }
            return arr;
        }
    }

    default void addAll(Collection<? extends T> collection) {
        for (T t : collection) {
            add(t);
        }
    }

    default boolean isEmpty() {
        return size() == 0;
    }

    default void removeAll(Collection<? extends T> collection) {
        for (T t : collection) {
            int counter = 0;
            for (T v : this) {
                if (v.equals(t)) {
                    remove(counter);
                    counter--;
                }
                counter++;
            }
        }
    }

    default void addFirst(T element) {
        add(0, element);
    }

    default boolean containsAll(Collection<? extends T> collection) {
        for (T t : collection) {
            if (!contains(t)) {
                return false;
            }
        }
        return true;
    }

    default String toStringView() {
        StringBuilder sb = new StringBuilder("[");
        int counter = 0;
        for (T t : this) {
            sb.append(t).append(counter != size() - 1 ? ", " : "]");
            counter++;
        }
        return sb.toString();
    }

    default boolean isEqual(MyList<T> list) {
        if (list == this) {
            return true;
        } else if (size() != list.size()) {
            return false;
        } else {
            Iterator<T> iterator = list.iterator();
            for (T t : this) {
                if (!t.equals(iterator.next())) {
                    return false;
                }
            }
            return true;
        }
    }

    default void retainAll(Collection<? extends T> collection) {
        for (T t : this) {
            if (!collection.contains(t)) {
                remove(t);
            }
        }
    }
}
