public class Queue {
	private int size;
	private Node rear, front;
	
	Queue(){
		this.rear  = this.front;
	}
	
	public boolean isEmpty(){
		if (size == 0) return true;
		else return false;
	}
	
	public void enqueue(int x) {			// similar to addLast in LinkedList
		if (size == 0)
			front = rear = new Node(x);
		else {
			rear.next = new Node(x);
			rear = rear.next;
		}
		size++;
	}

	public int dequeue() {				// similar to removeFirst in LinkedList
		if (size == 0) return -1;
		else {
			int x;
			if (size == 1){
				x = front.data;
				front = rear = null;
			}
			else{
				x = front.data;
				front = front.next;
			}
			size--;
			return x;
		}
	}
}
