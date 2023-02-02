package CSCI.SDC3901.Lab2;
// The Node class represents one node in a singly-connected linked list.

public class Node {
	// Internal attributes for the class.
	// Define the class to be a singly-connected linked list that stores one string.

	private Node next;
	private String word;

	// Create three constructors for the class:
	//   - one that accepts no arguments and so makes an empty node
	//   - one that creates a node with an initial string
	//   - one that creates a node with an initial string and has a location already in mind for the linked list

	public Node() {
		// Create an empty node

		next = null;
		word = null;
	}
	
	public Node( String start ) {
		// Create an isolated node that contains the given string
		next = null;
		word = start;
	}
	
	public Node( String start, Node nextNode ) {
		// Create a node with a given string and connect it into the linked list.
		// It is set to be the head of a linked list, with the given node as the completion of the list.

		next = nextNode;
		word = start;
	}

	// isLast identifies whether or not there are any linked list nodes that follow the current node.
	
	public boolean isLast() {
		boolean last = false;

		// The end of the linked list is characterized by no next node.

		if (next == null) {
			last = true;
		}
		return last;
	}

	// Getter and setter methods for the node.  The getters let you retrieve either the string in the node
	// or the reference to the next node.
	// The setter allows you to change the location of the node in the linked list.
	// There are no setters for the new string; instead, detach the node from the linked list and 
	// insert a new node with the new string.
	
	public void setNext( Node newNext ) {
		next = newNext;
	}
	
	public Node next() {
		return next;
	}
	
	public String getWord() {
		return word;
	}
}

