package com.example.task_2.Program.MyLists;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class MyLinkedList<T> implements MyList<T> {

    private static class Node<T> {

        private T value;
        private Node<T> next;
        private Node<T> prev;

        private Node(T value) {
            this.value = value;
            this.next = null;
            this.prev = null;
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
    private Node<T> tail;
    private int size;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public void add(T value) {
        Node<T> node = new Node<>(value);
        if (isEmpty()) {
            head = node;
            head.setNext(null);
            head.setPrev(null);
            tail = node;
            tail.setPrev(null);
        } else {
            tail.setNext(node);
            node.setPrev(tail);
            tail = node;
        }
        tail.setNext(null);
        size++;
    }

    @Override
    public void add(int index, T value) {
        if (index > size() || index < 0) {
            throw new IndexOutOfBoundsException();
        } else {
            if (isEmpty() || index == size) {
                add(value);
            } else {
                Node<T> node = new Node<>(value);
                if (index == 0) {
                    node.setNext(head);
                    head.setPrev(node);
                    node.setPrev(null);
                    head = node;
                } else {
                    Node<T> prev = getNode(index - 1);
                    node.setNext(prev.getNext());
                    prev.getNext().setPrev(node);
                    prev.setNext(node);
                    node.setPrev(prev);
                }
                size++;
            }
        }
    }

    @Override
    public void addAll(int index, Collection<? extends T> collection) {
        if (index > size() || index < 0) {
            throw new IndexOutOfBoundsException();
        } else if (!collection.isEmpty()) {
            Node<T> previous = index == 0 ? null : getNode(index - 1);
            Node<T> end = previous != null ? previous.getNext() : isEmpty() ? null : getNode(0);
            for (T o : collection) {
                Node<T> node = new Node<>(o);
                node.setPrev(previous);
                if (previous != null) {
                    previous.setNext(node);
                } else {
                    head = node;
                }
                previous = node;
                size++;
            }
            previous.setNext(end);
            if (end != null) {
                end.setPrev(previous);
            } else {
                tail = previous;
            }
        }
    }

    @Override
    public void clear() {
        if (!isEmpty()) {
            head.setNext(null);
            head = null;
            tail.setPrev(null);
            tail = null;
            size = 0;
        }
    }

    @Override
    public Iterator<T> descendingIterator() {
        return new Iterator<T>() {

            private Node<T> current = tail;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T val = current.getValue();
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
        MyList<T> clone = new MyLinkedList<>();
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
            return tail.getValue();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T val = current.getValue();
                current = current.getNext();
                return val;
            }
        };
    }

    @Override
    public Integer lastIndexOf(T value) {
        int counter = size() - 1;
        Iterator<T> iterator = descendingIterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(value)) {
                return counter;
            }
            counter--;
        }
        return null;
    }

    @Override
    public void remove(int index) {
        Node<T> node = getNode(index);
        if (index == 0 && size() == 1) {
            clear();
            return;
        } else if (index == size() - 1) {
            tail = node.getPrev();
            tail.setNext(null);
        } else if (index == 0) {
            head = node.getNext();
            head.setPrev(null);
        } else {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
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
                    if (current != head) {
                        current.getPrev().setNext(current.getNext());
                    } else {
                        head = current.getNext();
                    }
                    current.getNext().setPrev(current.getPrev());
                    current.setPrev(current.getNext());
                    current.setNext(current.getNext().getNext());
                    current.getPrev().setNext(current);
                    if (current.getNext() != null) {
                        current.getNext().setPrev(current);
                    } else {
                        tail = current;
                    }
                } else {
                    current = current.getNext();
                }
            }
        }
    }

    private Node<T> getNode(int index) {
        if (index >= size() || index < 0 || isEmpty()) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> current = index <= size() / 2 ? head : tail;
            if (current == head) {
                for (int i = 0; i < index; i++) {
                    current = current.getNext();
                }
            } else {
                for (int i = size - 1; i > index; i--) {
                    current = current.getPrev();
                }
            }
            return current;
        }
    }

    // ...
}
