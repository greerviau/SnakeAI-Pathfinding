
public class SinglyLinkedList<E>{

   
   //---------Nested Node class-----------
   public static class Node<E>{
      private E element;        // content, this could be set to final
      private Node<E> next;     // next node in the list
   
      public Node(E e, Node<E> n){// constructor
         element = e;
         next = n;
      }
      // Necessary set and get methods
      public E getElement(){ 
         return element;
      }
      public Node<E> getNext() {
         return next;
      }
      public void setNext(Node<E> n){
         next = n;
      }
   }
   //------end of Nested Node class------
      
   private Node<E> head = null;
   private Node<E> tail = null;
   private int size = 0;
   public SinglyLinkedList() {}     //Default Constructor
   
   public int size() {return size; } // Accessor methods
   
   public boolean isEmpty() { return size == 0;}
   
   public Node<E> getHead() {                // Sotiris: Added getHead() you should consider whether you need set methods also
      return head;
   }  
   public Node<E> getTail() {                // Sotiris: Added getTail()
      return tail;
   }  
   public void setSize(int n) {
 	  size = n;
   }
   public E first() {                        // Get the value of the element in the Head
      if (isEmpty()) 
         return null;
      return head.getElement();
   }
   public E last() {                         // Get the value of the element in the Tail
      if (isEmpty()) 
         return null;
      return tail.getElement();
   }        
   public void addFirst(E e){                // Create new node and added to the head
      head = new Node<>(e, head);
      if (size == 0)
         tail = head;
      size++ ;
   }
   public void addLast(E e) {                // Create new node and added to the tail
      Node<E> newest = new Node<>(e, null);
      if (isEmpty())
         head = newest;
      else
         tail.setNext(newest);
      tail = newest;
      size++;
   }
   public E removeFirst() {                 // Remove the head node, set new head
      if (isEmpty()) 
         return null;
      E answer = head.getElement();
      head = head.getNext();
      size--;
      if (size == 0)
         tail = null;
      return answer;
   }
   public void restartList() {
	   head.setNext(null);
	   size = 1;
	   tail = head;
   }
   public void newHead(E e, Node<E> previous) {
	   Node<E> newest = new Node<>(e, previous);
	   head = newest;
	   size++;
   }
   public void addAfter(E e, Node<E> previous) {
	   Node<E> newest;
	   if(previous.getNext() != null) {
			newest = new Node<>(e,previous.getNext());
		} else {
			newest = new Node<>(e,null);
			tail = newest;
		}
		previous.setNext(newest);
		size++;
   }
   public Node <E> removeAfter(Node<E> previous) {
		if(previous.getNext() != null) {
			Node <E> after = previous.getNext();
			if(after == tail)
				tail = previous;
			previous.setNext(after.getNext());
			size--;
			return after;
		} else
			return null;
   }
   // You may need to create more methods
}
   
   
   


