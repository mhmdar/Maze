
public class Field {
	private boolean up, down, left, right, visited;
//	boolean solution;
	char mark;
	
	Field(){
		this.up   = true;
		this.down    = true;
		this.left   = true;
		this.right   = true;
		this.visited = false;
//		this.solution = false;
	}
	
	public boolean getUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}


	public boolean getDown() {
		return down;
	}

	public void setDown(boolean value) {
		this.down = value;
	}

	public boolean getLeft() {
		return left;
	}
	
	public void setLeft(boolean left) {
		this.left = left;
	}
	
	public boolean getRight() {
		return right;
	}

	public void setRight(boolean value) {
		this.right = value;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean value) {
		this.visited = value;
	}
	
}
