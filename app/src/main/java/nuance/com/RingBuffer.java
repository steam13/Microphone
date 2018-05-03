package nuance.com;

import java.util.Iterator;
import java.util.NoSuchElementException;

class RingBuffer<Item> implements Iterable<Item> {
    private int f11N = 0;
    private final Item[] f12a;
    private int first = 0;
    private int last = 0;

    private class RingBufferIterator implements Iterator<Item> {
        private int f10i;

        private RingBufferIterator() {
            this.f10i = 0;
        }

        public boolean hasNext() {
            return this.f10i < RingBuffer.this.f11N;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (hasNext()) {
                Object[] access$1 = RingBuffer.this.f12a;
                int i = this.f10i;
                this.f10i = i + 1;
                return (Item) access$1[i];
            }
            throw new NoSuchElementException();
        }
    }

    public RingBuffer(int capacity) {
        this.f12a = (Item[]) new Object[capacity];
    }

    public synchronized boolean isEmpty() {
        return this.f11N == 0;
    }

    public synchronized boolean isFull() {
        return this.f11N == this.f12a.length;
    }

    public synchronized int size() {
        return this.f11N;
    }

    public synchronized void enqueue(Item item) throws RuntimeException {
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        this.f12a[this.last] = item;
        this.last = (this.last + 1) % this.f12a.length;
        this.f11N++;
    }

    public synchronized Item dequeue() throws RuntimeException {
        Item item;
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        item = this.f12a[this.first];
        this.f12a[this.first] = null;
        this.f11N--;
        this.first = (this.first + 1) % this.f12a.length;
        return item;
    }

    public Iterator<Item> iterator() {
        return new RingBufferIterator();
    }
}
