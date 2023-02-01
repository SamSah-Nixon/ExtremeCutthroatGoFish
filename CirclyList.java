/*
 * Sam Sah-Nixon
 * Date Created: 1/10/23
 * Last Modified: 1/27/23
 * Description: A circly linked list
 */
import java.util.ArrayList;

public class CirclyList<E extends Comparable<? super E>> implements MyList<E>{
    
    private CirclyNode<E> head;
    private int size;
    
    public CirclyList(){
        head = new CirclyNode<>();
        size = 0;
    }

    private static class CirclyNode<E>{
        private E info;
        private CirclyNode<E> prev;
        private CirclyNode<E> next;

        public CirclyNode(){
            info = null;
            prev = null;
            next = null;
        }
        public CirclyNode(E info, CirclyNode<E> prev, CirclyNode<E> next){
            this.info = info;
            this.prev = prev;
            this.next = next;
        }
    }

    /**
     * Adds a new item to the beginning of the list.
     * @param item The Item to add
     */
    public void add(E item) {
        insert(0, item);
    }

    /**
     * Adds a new item to the end of the list making it the last item in the collection.
     * @param item The Item to add
     */
    public void append(E item) {
        insert(size, item);
    }
    
    /**
     * Adds a new item to the list at position pos. Make sure there are enough existing items to have position pos.
     * @param pos The position to add the item
     * @param item The Item to add
     */
    public void insert(int pos, E item) {
        //Make sure the position is not greater that the size
        if(pos > size || pos < 0)
            return;
        CirclyNode<E> current = head;
        //Navigate to the node before the new node
        for(int i = 0; i < pos; i++){
            current = current.next;
        }
        current.next = new CirclyNode<>(item, current, current.next);
        if(size == 0 || pos == size){
            head.prev = current.next;
        }
        else if(pos == 0){
            head.next = current.next;
        }
        if(pos == 0 || pos == size){
            head.prev.next = head.next;
            head.next.prev = head.prev;
        }
        current.next.next.prev = current.next;
        size++;
    }

    /**
     * Removes the item from the list.
     * @param item The item to remove
     * @return The item removed or null if not found
     */
    public E remove(E item) {
        return pop(indexOf(item));
    }

    /**
     * Removes all occurrences of the item from the list.
     * @param item The item to remove
     */
    public void removeAll(E item) {
        while(contains(item)) {
            remove(item);
        }
    }
    
    /**
     * Removes and returns the first item.
     * @return The item removed
     */
    public E pop(){
        return pop(0);
    }

    /**
     * Removes and returns the last item.
     * @return The item removed
     */
    public E popRear() {
        return pop(size-1);
    }
    
    /**
     * Removes and returns the item at position pos.
     * @param pos The position to remove
     * @return The item removed
     */
    public E pop(int pos) {
        if(pos >= size || pos < 0)
            return null;
        CirclyNode<E> current = head.next;
        //Navigate to the node to be removed
        for(int i = 0; i < pos; i++){
            current = current.next;
        }
        //Set the prev node's next pointer to the next node
        current.prev.next = current.next;
        //If the node to be removed is the first node
        if(pos == 0)
        //Set the head's next pointer to the next node
            head.next = current.next;
        //Set the next node's prev pointer to the prev node
        current.next.prev = current.prev;
        if(pos == size-1)
        //If the deleted node is the last node
        //Set the head's prev pointer to the prev node
            head.prev = current.prev;
        //Disconnect Node
        current.prev = null;
        current.next = null;
        size--;
    
        return current.info;
    }

    /**
     * Swaps two nodes at certain indexes
     * @param index1 the smaller index of the 2 nodes
     * @param index2 the larger index of the 2 nodes
     */
    public void swap(int index1, int index2){
        E temp = valueAt(index1);
        E temp2 = valueAt(index2);
        CirclyNode<E> current = head.next;
        for(int i = 0; i < index1; i++){
            current = current.next;
        }
        current.info = temp2;
        current = head.next;
        for(int i = 0; i < index2; i++){
            current = current.next;
        }
        current.info = temp;
    }

    /**
     * Returns the position of item in the list.
     * @param item The item to find
     * @return The position of the item or -1 if not found
     */
    public int indexOf(E item){
        CirclyNode<E> current = head.next;
        int index = 0;
        for(int i = 0; i < size; i++){
            if(current.info.equals(item))
                return index;
            current = current.next;
            index++;
        }
        return -1;
    }

    /**
     * Returns a list of positions of all the occurrences of item in the list.
     * @param item The item to find
     * @return A list of positions of the item
     */
    public ArrayList<Integer> indicesOf(E item) {
        ArrayList<Integer> list = new ArrayList<>();
        CirclyNode<E> current = head.next;
        int index = 0;
        for(int i = 0; i < size; i++){
            if(current.info.equals(item))
                list.add(index);
            current = current.next;
            index++;
        }
        return list;
    }

    /**
     * Returns the item at a specified position in the list.
     * @param pos The position to get the item
     * @return The item at the position or null if not found
     */
    public E valueAt(int pos) {
        if(pos >= size || pos < 0)
            return null;
        CirclyNode<E> current = head.next;
        for(int i = 0; i < pos; i++){
            current = current.next;
        }
        return current.info;
    }

    /**
     * Searches for the item in the list.
     * @param item the to search for
     * @return true if the item is in the list, false otherwise
     */
    public boolean contains(E item) {
        return indexOf(item) != -1;
    }

    /**
     * Tests to see whether the list is empty.
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return head.next == null;
    }

    /**
     * Returns the first item in the list
     * @return the first item in the list
     */
    public E front() {
        return head.next.info;
    }

    /**
     * Returns the last item in the list.
     * @return last item in the list
     */
    public E rear() {
        return head.prev.info;
    }
    
    /**
     * Returns the number of items in the list.
     *
     * @return the size
     */
    public int size() {
        return size;
    }

    /**
     * Returns the next item in the list of the one inputted
     * @param item the item to find the next of
     * @return the next item in the list
     */
    public E next(E item) {
        CirclyNode<E> current = head.next;
        for(int i = 0; i < size; i++) {
            if(current.info.equals(item)) {
                return current.next.info;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Returns a str of the doubly linked list from first to last in arraylist format
     * @return a string version of the list
     */
    public String toString() {
        if(isEmpty())
            return "[]";
        StringBuilder string = new StringBuilder("[");
        CirclyNode<E> current = head.next;
        for(int i = 0; i < size; i++) {
            string.append(current.info).append(", ");
            current = current.next;
        }
        string = new StringBuilder(string.substring(0, string.length() - 2));
        return string+"]";
    }

    /**
     * Returns a str of the doubly linked list from last to first in arraylist format
     * @return a string version of the list but backwards
     */
    public String reverseToString() {
        if (isEmpty())
            return "[]";
        StringBuilder string = new StringBuilder("[");
        CirclyNode<E> current = head.prev;
        for (int i = 0; i < size; i++) {
            string.append(current.info).append(", ");
            current = current.prev;
        }
        string = new StringBuilder(string.substring(0, string.length() - 2));
        return string + "]";
    }
}