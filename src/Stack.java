
public class Stack {
	Node first, last;
	int  size;

	public Stack(){		// default constructor
	}

	//-----| push |-------------------------------------
	public void push(int x){
		addFirst(x);
	}

	//-----| pop |--------------------------------------
	public void pop(){
		removeFirst();
	}
	//-----| top |--------------------------------------
	public int top(){
		return getFirst();
	}

	//-----| isEmpty |----------------------------------
	public boolean isEmpty(){
		if (size==0)
			return true;
		else
			return false;
	}

	//-----| addFirst |---------------------------------
	private void addFirst(int s){
		Node temp;
		temp = new Node(s);
		if (size == 0)
			first = last = temp;
		else {
			temp.next = first;
			first = temp;
		}
		size++;
//		System.out.println("["+s[0][0]+"]["+s[0][1]+"] pushed. size="+size);
	}

	//-----| removeFirst |------------------------------
	private boolean removeFirst() {
		if (size == 0) return false;
		else {
			if (size == 1)
				first = last = null;
			else
				first = first.next;
			size--;
			System.out.println("pop. size="+size);
			return true;
		}
	}

	//-----| getFirst |--------------------------------- 
	private int getFirst(){
		if (size == 0) return -1;
//		System.out.println("["+first.data[0][0]+"]["+first.data[0][1]+"] is top");
		return first.data;
	}

}
