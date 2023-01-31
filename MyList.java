public interface MyList<E> {
  /**
   *If the List is empty, return true otherwise return false.
   *
   *@return A boolean indicating if the List is or is not empty.
   */
  public boolean isEmpty();

  /**
   *If the List is not empty, remove and return the first occurrence of the
   *element from the List.
   *@return The List object's data or null if the List is empty
   */
  public E remove(E obj);

  /**
   *Insert an Object at the appropriate place of the List, at the front
   *in an unordered list, and in ascending order in an ordered list.
   *
   *@param obj the Object being added to the List
   */
  public void add(E obj);

  /**
   *If the List is not empty, return the element that is currently at
   *the front of the List.  If the List is empty return null.  The
   *contents of the List is not changed.
   *@return The object's data at the front of the List or null if the List is empty
   */
  public E front();

  /**
   * @return the number of objects in the current List.
   */
  public int size();
}
