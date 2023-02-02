package CSCI.SDC3901.Lab2;
// A class for a singly-connected unsorted linked list that stores strings.  The list allows for duplicate
// strings in the list.

// The linked list is managed by keeping track of the head of the linked list and of the number
// of nodes in the linked list.

public class LinkedList {
	// Store the attributes of the linked list:  the start of the list (the head) and the
	// list size (for convenience).

	private Node head;
	private int size;

	// A new list is empty.  It contains no nodes.
	
	public LinkedList() {
		head = null;
		size = 0;
	}

	// Let a caller add a string to the end of the linked list.
	
	public boolean append( String word ) {
		// Assume at the start that the append failed.

		boolean added = false;

		if (word == null) { //todo: prevent null insertions in the linked list
			System.out.println("Cannot insert null as word");
			return added;	// returns false if the word is null
		}

		Node location;
		
		if (head == null) {
			// First word in the list.
			head = new Node( word ); //todo: after inserting the head node, the next should be null. Previously it is createing a node with null string and null next as second node.
			location = head;
		} else {
			// Find the end of the list
			location = head;
			while (!location.isLast()) {
				location = location.next();
			}

			// Append to the end of the list.
			
			location.setNext(new Node( word ));
		}
		size++;
		return true; //todo: At this point, the node is inserted and it is always true here.
	}

	// Determine whether or not a string appears in the linked list.
	// If the same string occurs more than once, only the first instance is detected.

	public boolean find (String word) {
		// Assume that the string is not in the list

		boolean found = false;
		Node location;

		// Only work on lists that have at least one node in them.
		
		if (head != null) {
			// Do a linear traversal of the linked list until we either find
			// a matching string or we run off the end of the list.

			location = head;
			while (!location.getWord().equals(word) && !location.isLast()) {
				location = location.next();
			}
	
			// With two exit conditions for the linear search, determine if we
			// exited because we found the word.
		
			if (location.getWord().equals(word)) {
				found = true;
			}
		}

		return found;
		
	}

	// Print the content of a linked list.  The nodes are printed in the order in which they
	// appear in the list.

	public void print() {
		Node location;
		
		location = head;
		for (int i = 0; i < size; i++) {
			//  Print a special message if the linked list somehow contains a null string

			if (location.getWord() != null) {
				System.out.println( location.getWord() );
			} else {
				System.out.println( "no word!!" );
			}

			// Advance to the next node in the list
			location = location.next();
		}
	}
}

