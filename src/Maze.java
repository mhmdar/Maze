
public class Maze {
	private int     rows, columns, startX=-1, startY=-1, endX=-1, endY=-1;
	Field array[][];
	Stack stack;
	
	Maze(int rows, int columns){
		this.rows    = rows;
		this.columns = columns;
		this.array = new Field[rows][columns];
		
		for (int x=0 ; x<rows ; x++){
			for (int y=0 ; y<columns ; y++){
				array[x][y] = new Field();
			}
		}
//		array[0][0].setRight(true);
//		array[rows-1][columns-1].setRight(false);
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public int getStartX() {
		return startX;
	}
	
	public void setStartX(int startX) {
		this.startX = startX;
	}
	
	public int getStartY() {
		return startY;
	}
	
	public void setStartY(int startY) {
		this.startY = startY;
	}
	
	public int getEndX() {
		return endX;
	}
	
	public void setEndX(int endX) {
		this.endX = endX;
	}
	
	public int getEndY() {
		return endY;
	}
	
	public void setEndY(int endY) {
		this.endY = endY;
	}

	public void print(){
		System.out.println("\n");
		for (int y=0 ; y<columns ; y++){
			System.out.print("+----");
		}
		System.out.print("+");
		
		for (int x=0 ; x<rows ; x++){
			System.out.print("\n|");
			for (int y=0 ; y<columns ; y++){
				if (array[x][y].getRight())
					System.out.print("  "+array[x][y].mark+" |");
				else
					System.out.print("  "+array[x][y].mark+"  ");
			}
			System.out.print("\n+");
			for (int y=0 ; y<columns ; y++){
				if (array[x][y].getDown())
					System.out.print("----+");
				else
					System.out.print("    +");
			}
		}
		System.out.println("\n");
	}
	
//	Maze generation:
//	----------------
//	• Initialize a stack with the start index (0,0)
//	  • Loop Until: stack is empty
//	  	o pop current index off the stack and mark it as visited
//	  	o If current index has any unvisited neighbors
//	  		- Choose a random unvisited neighbor index
//			- Remove the wall between the chosen neighbor index and the current index
//			- push the current index on the stack
//			- push the randomly choose neighbor index on the stack
//		o Continue the loop.
	
	public void createMaze(){
		int x=startX , y=startY, iteration=0;
		String nextMove;
		stack = new Stack();
		stack.push(x);
		stack.push(y);
		while (stack.isEmpty() == false){
			System.out.println("\n"+ ++iteration+"------------");
			y = stack.top();
			stack.pop();
			x = stack.top();
			stack.pop();
			array[x][y].setVisited(true);
			if(x==endX && y==endY){
				System.out.println("\nFinished (reached ["+endX+"]["+endY+"])");
				return;
			}
			nextMove = chooseNeighbor(x,y);
			if (nextMove != null){
				switch(nextMove){
				case "Up":		array[x-1][y].setVisited(true);
								array[x-1][y].setDown(false);
								array[x][y].setUp(false);
								stack.push(x);
								stack.push(y);
								x--;
								stack.push(x);
								stack.push(y);
								break;
				case "Down":	array[x+1][y].setVisited(true);
								array[x][y].setDown(false);
								array[x+1][y].setUp(false);
								stack.push(x);
								stack.push(y);
								x++;
								stack.push(x);
								stack.push(y);
								break;
				case "Left":	array[x][y-1].setVisited(true);
								array[x][y-1].setRight(false);
								array[x][y].setLeft(false);
								stack.push(x);
								stack.push(y);
								y--;
								stack.push(x);
								stack.push(y);
								break;
				case "Right":	array[x][y+1].setVisited(true);
								array[x][y].setRight(false);
								array[x][y+1].setLeft(false);
								stack.push(x);
								stack.push(y);
								y++;
								stack.push(x);
								stack.push(y);
								break;
				}	// end of switch statement
			}	// end of if statement
		}	// end of while loop
		System.out.println("\nwhile loop finished");
	}
	
	private String chooseNeighbor(int x, int y){
		String[] nbr = new String[3];
		int ptr = -1;
		if(x>0 && array[x-1][y].isVisited() == false)
			nbr[++ptr]="Up";
		if(x<rows-1 && array[x+1][y].isVisited() == false)
			nbr[++ptr]="Down";
		if(y>0 && array[x][y-1].isVisited() == false)
			nbr[++ptr]="Left";
		if(y<columns-1 && array[x][y+1].isVisited() == false)
			nbr[++ptr]="Right";
		
		System.out.print("array["+x+"]["+y+"]  nbrs:  ");
		
		if (ptr == -1){
			System.out.print("No unvisited neighbors !!");
			return null;
		}
		for(int i=0 ; i<=ptr ; i++)
			System.out.print(nbr[i]+" ");
		System.out.print(" -->  ");
		int rand = (int)(Math.random()*(ptr+1));
		System.out.print(nbr[rand]+"\n\n");
		return nbr[rand];
	}
	

//	Maze solving:
//	-------------
//	The basic routine works as follows:
//	• Initialize a queue with the start index (0,0)
//	• Loop Until: queue is empty
//		o Dequeue current index and mark it as visited
//		o If current index is the finish point
//			Break! We've found the end
//		o Enqueue all indexes for reachable and unvisited neighbors of the current index
//		o Continue the loop.
	
	public void solveMaze(){
		int x ,y;
		for (x=0 ; x<rows ; x++){
			for (y=0 ; y<columns ; y++){
				array[x][y].setVisited(false);;
			}
		}
		x=startX;
		y=startY;
		Queue q = new Queue();
		q.enqueue(x);
		q.enqueue(y);
		while(q.isEmpty() == false){
			x = q.dequeue();
			y = q.dequeue();
			array[x][y].setVisited(true);
			array[x][y].mark='x';
			System.out.println("["+x+"]["+y+"] marked as visited");
			if (x==endX && y==endY){
				System.out.println("Maze solved");
				return;
			}
			enqueueReachableNeighbors(x,y,q);
		}
	}
	
	private void enqueueReachableNeighbors(int x, int y, Queue q){
		System.out.println("enqueueReachableNeighbors has been called");
		if(x>0 && array[x-1][y].isVisited() == false && array[x-1][y].getDown() == false){
			q.enqueue(x-1);
			q.enqueue(y);
			System.out.println("["+(x-1)+"]["+y+"] enqueued");
		}
		if(x<rows-1 && array[x+1][y].isVisited() == false && array[x][y].getDown() == false){
			q.enqueue(x+1);
			q.enqueue(y);
			System.out.println("["+(x+1)+"]["+y+"] enqueued");
		}
		if(y>0 && array[x][y-1].isVisited() == false && array[x][y-1].getRight() == false){
			q.enqueue(x);
			q.enqueue(y-1);
			System.out.println("["+x+"]["+(y-1)+"] enqueued");
		}
		if(y<columns-1 && array[x][y+1].isVisited() == false && array[x][y].getRight() == false){
			q.enqueue(x);
			q.enqueue(y+1);
			System.out.println("["+x+"]["+(y+1)+"] enqueued");
		}
	}
		
}
