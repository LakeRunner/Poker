package com.example.task_2.Program.MyLists;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class MyLoopList<T> implements MyList<T> {

    private static class Node<T> {

        private T value;
        private Node<T> next;
        private Node<T> prev;

        private Node(T value) {
            this.value = value;
            this.next = this;
            this.prev = this;
        }

        private T getValue() {
            return value;
        }

        private void setValue(T value) {
            this.value = value;
        }

        private Node<T> getNext() {
            return next;
        }

        private void setNext(Node<T> next) {
            this.next = next;
        }

        private Node<T> getPrev() {
            return prev;
        }

        private void setPrev(Node<T> prev) {
            this.prev = prev;
        }
    }

    private Node<T> head;
    private int size;

    public MyLoopList() {
        head = null;
        size = 0;
    }

    @Override
    public void add(T value) {
        Node<T> node = new Node<>(value);
        if (isEmpty()) {
            head = node;
        } else {
            head.getPrev().setNext(node);
            node.setPrev(head.getPrev());
            node.setNext(head);
            head.setPrev(node);
        }
        size++;
    }

    @Override
    public void add(int index, T value) {
        if (isEmpty()) {
            if (index != 0) {
                throw new IndexOutOfBoundsException("List is empty");
            } else {
                add(value);
            }
        } else {
            Node<T> node = new Node<>(value);
            Node<T> next = getNode(index >= 0 ? index : index + 1);
            next.getPrev().setNext(node);
            node.setPrev(next.getPrev());
            next.setPrev(node);
            node.setNext(next);
            if (index == 0) {
                head = node;
            }
            size++;
        }
    }

    @Override
    public void addAll(int index, Collection<? extends T> collection) {
        if (isEmpty() && index != 0) {
            throw new IndexOutOfBoundsException("List is empty");
        } else if (!collection.isEmpty()) {
            Node<T> previous = getNode(index >= 0 ? index - 1 : index);
            Node<T> end = previous != null ? previous.getNext() : null;
            boolean flag = index == 0;
            for (T o : collection) {
                Node<T> node = new Node<>(o);
                if (previous != null) {
                    previous.setNext(node);
                    node.setPrev(previous);
                    if (flag) {
                        head = node;
                        flag = false;
                    }
                } else {
                    head = node;
                    end = head;
                    flag = false;
                }
                previous = node;
                size++;
            }
            previous.setNext(end);
            end.setPrev(previous);
        }
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public Iterator<T> descendingIterator() {
        return new Iterator<T>() {

            private Node<T> lastReturned = null;
            private Node<T> current = head.getPrev();

            @Override
            public boolean hasNext() {
                return lastReturned != head;
            }

            @Override
            public T next() {
                T val = current.getValue();
                lastReturned = current;
                current = current.getPrev();
                return val;
            }
        };
    }

    @Override
    public T get(int index) {
        return getNode(index).getValue();
    }

    @Override
    public MyList<T> getClone() {
        MyList<T> clone = new MyLoopList<>();
        for (T t : this) {
            clone.add(t);
        }
        return clone;
    }

    @Override
    public T getFirst() {
        if (isEmpty()) {
            throw new NullPointerException("List is empty");
        } else {
            return head.getValue();
        }
    }

    @Override
    public T getLast() {
        if (isEmpty()) {
            throw new NullPointerException("List is empty");
        } else {
            return head.getPrev().getValue();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private Node<T> lastReturned = null;
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return head != null && lastReturned != head.getPrev();
            }

            @Override
            public T next() {
                T val = current.getValue();
                lastReturned = current;
                current = current.getNext();
                return val;
            }
        };
    }

    @Override
    public Integer lastIndexOf(T value) {
        int counter = size() == 1 ? 0 : -1;
        Iterator<T> iterator = descendingIterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(value)) {
                if (Math.abs(counter) > size() / 2) {
                    counter += size();
                }
                return counter;
            }
            counter--;
        }
        return null;
    }

    @Override
    public void remove(int index) {
        if (size() == 1) {
            clear();
            return;
        } else {
            Node<T> node = getNode(index);
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
            if (node == head) {
                head = head.getNext();
            }
            node.setNext(null);
            node.setPrev(null);
        }
        size--;
    }

    @Override
    public void set(int index, T value) {
        getNode(index).setValue(value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void sort(Comparator<T> c) {
        for (int i = size - 1; i > 0; i--) {
            Node<T> current = head;
            for (int j = 0; j < i; j++) {
                if (c.compare(current.getValue(), current.getNext().getValue()) > 0) {
                    current.getPrev().setNext(current.getNext());
                    current.getNext().setPrev(current.getPrev());
                    current.setPrev(current.getNext());
                    current.setNext(current.getNext().getNext());
                    current.getPrev().setNext(current);
                    current.getNext().setPrev(current);
                    if (current == head) {
                        head = head.getPrev();
                    }
                } else {
                    current = current.getNext();
                }
            }
        }
    }

    @Override
    public Integer indexOf(T value) {
        return MyList.super.indexOf(value);
    }

    public void setBeginning(int index) {
        head = getNode(index);
    }

    private Node<T> getNode(int index) {
        if (isEmpty()) {
            return null;
        } else {
            Node<T> current = head;
            index %= size();
            if (index != 0 && size() != 1) {
                int middleIndex = size() / 2;
                if (Math.abs(index) > middleIndex) {
                    index = index < 0 ? size() + index : index - size();
                }
                if (index > 0) {
                    for (int i = 0; i < index; i++) {
                        current = current.getNext();
                    }
                } else {
                    for (int i = 0; i > index; i--) {
                        current = current.getPrev();
                    }
                }
            }
            return current;
        }
    }

    // ...
}
