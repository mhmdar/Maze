import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class _Main extends Application{
	
	Maze m;
	static Pane root;
	static Scene scene;
	int row=15, column=17, offset=15, shiftH=200, shiftV=50;
	Rectangle rectangle;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		Pane root = new Pane();
		Scene scene = new Scene(root,850,600);
		primaryStage.setTitle("Maze generation and solving");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		Button buttonExit = new Button("      Exit      ");
		buttonExit.setLayoutX(30);
		buttonExit.setLayoutY(550);
		root.getChildren().addAll(buttonExit);
		buttonExit.setOnAction(e -> System.exit(0));
		
		Label rowsLabel = new Label("No. of Rows");
		rowsLabel.setLayoutX(10);
		rowsLabel.setLayoutY(30);
		root.getChildren().add(rowsLabel);
		
		TextField noOfRows = new TextField();
		noOfRows.setLayoutX(100);
		noOfRows.setLayoutY(30);
		noOfRows.setPrefColumnCount(5);
		noOfRows.setFont(Font.font("Consolas",12));
		noOfRows.setText(Integer.toString(row));
		root.getChildren().add(noOfRows);
		
		Label columnsLabel = new Label("No. of Columns");
		columnsLabel.setLayoutX(10);
		columnsLabel.setLayoutY(60);
		root.getChildren().add(columnsLabel);
		
		TextField noOfcolumns = new TextField();
		noOfcolumns.setLayoutX(100);
		noOfcolumns.setLayoutY(60);
		noOfcolumns.setPrefColumnCount(5);
		noOfcolumns.setFont(Font.font("Consolas",12));
		noOfcolumns.setText(Integer.toString(column));
		root.getChildren().add(noOfcolumns);
		
		Label offsetLabel = new Label("Size of squares:");
		offsetLabel.setLayoutX(10);
		offsetLabel.setLayoutY(100);
		root.getChildren().add(offsetLabel);
		
		Label info = new Label("");
		info.setLayoutX(10);
		info.setLayoutY(400);
		info.setFont(Font.font("Consolas",12));
		info.setTextFill(Color.RED);
		info.setPrefWidth(150);
		info.setWrapText(true);
		
		Slider slider = new Slider(5, 25, offset);
		slider.setLayoutX(20);
		slider.setLayoutY(120);
		slider.setShowTickMarks(true);
		slider.setBlockIncrement(5);
		root.getChildren().add(slider);
		slider.valueProperty().addListener(e -> {
								offset = (int)slider.getValue();
								System.out.println("offset changed to "+offset);
								});
		
		Button generateMaze = new Button("Generate Maze");
		generateMaze.setLayoutX(40);
		generateMaze.setLayoutY(190);
		generateMaze.setDisable(true);
		root.getChildren().add(generateMaze);
		
		
		Button solveButton = new Button("Solve maze");
		solveButton.setLayoutX(40);
		solveButton.setLayoutY(230);
		solveButton.setDisable(true);
		root.getChildren().add(solveButton);
		
		Button blankMazeButton = new Button("Create blank maze");
		blankMazeButton.setLayoutX(40);
		blankMazeButton.setLayoutY(150);
		root.getChildren().add(blankMazeButton);
		
		solveButton.setOnAction(e -> {
			this.m.solveMaze();
			this.m.print();
			int gridx, gridy;
			for (int col=0 ; col<column ; col++){
				for (int ro=0 ; ro<row ; ro++){
					gridx = shiftH+2*offset*col;
					gridy = shiftV+2*offset*ro;
					if (m.array[ro][col].mark == 'x')
						root.getChildren().add(new Circle(gridx,gridy,4,Color.RED));
				}
			}
			gridx = shiftH+2*offset*m.getEndY();
			gridy = shiftV+2*offset*m.getEndX();
			root.getChildren().addAll(new Circle(gridx,gridy,6,Color.GREEN));
			while(m.stack.isEmpty()!=true){
				int col = m.stack.top();
				System.out.println(m.stack.top());
				m.stack.pop();
				int ro = m.stack.top();
				System.out.println(m.stack.top());
				m.stack.pop();
				gridx = shiftH+2*offset*col;
				gridy = shiftV+2*offset*ro;
				root.getChildren().addAll(new Circle(gridx,gridy,6,Color.GREEN));
			}
			solveButton.setDisable(true);
			generateMaze.setDisable(true);
		});
		
		blankMazeButton.setOnAction(e -> {
			this.row = Integer.valueOf(noOfRows.getText());
			this.column = Integer.valueOf(noOfcolumns.getText());
			this.m = new Maze(row,column);
//			this.m.createMaze();
			this.m.print();
//			solveButton.setDisable(false);
			root.getChildren().clear();
			root.getChildren().addAll(rowsLabel, columnsLabel, noOfRows, noOfcolumns,
					blankMazeButton, generateMaze, solveButton, buttonExit,
					offsetLabel, slider);
			int gridx, gridy;
			Line L;
			for (int col=0 ; col<column ; col++){
				for (int ro=0 ; ro<row ; ro++){
					gridx = shiftH+2*offset*col;
					gridy = shiftV+2*offset*ro;
					if (m.array[ro][col].getUp() == true){
						L = new Line(gridx-offset, gridy-offset, gridx+offset, gridy-offset);
						L.setStrokeWidth(3);
						root.getChildren().add(L);
						L.setOnMousePressed(new EventHandler<MouseEvent>() {
						    public void handle(MouseEvent mouseEvent) {
						    	root.getChildren().remove(info);
						        int x=(int)mouseEvent.getX();
						        int y=(int)mouseEvent.getY();
						        System.out.println("row "+(((float)y-shiftV)/2/offset+0.5)+"    column "+(((float)x-shiftH)/2/offset));
						        int colClicked = (int)Math.round(((float)x-shiftH)/2/offset);
						        int rowClicked = (int)Math.round(((float)y-shiftV)/2/offset + 0.5);
						        if (m.getStartX() == -1){
						        	if (rowClicked == 0){
						        		m.setStartX(rowClicked);
						        		m.setStartY(colClicked);
						        		m.array[rowClicked][colClicked].setUp(false);
						        		System.out.println("startX "+m.getStartX()+"    startY "+m.getStartY());
						        		root.getChildren().remove(mouseEvent.getTarget());
						        	} else {
							        	System.out.println("Please choose start at the borders");
							        	info.setText("Please choose start at the borders");
							        	root.getChildren().add(info);
							        }
						        } 
						        else 
						        	if(m.getStartX()!=-1 && m.getEndX()==-1){
						        		if (rowClicked == 0){
						        		m.setEndX(rowClicked);
						        		m.setEndY(colClicked);
						        		m.array[rowClicked][colClicked].setUp(false);
						        		System.out.println("endX "+m.getEndX()+"    endY "+m.getEndY());
						        		root.getChildren().remove(mouseEvent.getTarget());
						        		info.setText("");
						        		generateMaze.setDisable(false);
						        	}
						        	else {
						        		System.out.println("Please choose an end at the borders");
						        		info.setText("Please choose an end at the borders");
						        		root.getChildren().add(info);
						        	}
						        } // end of if-else statements 
						    } // end of handle method
						}); // end of setOnMousePressed
					}
					if (m.array[ro][col].getDown() == true){
						L = new Line(gridx-offset, gridy+offset, gridx+offset, gridy+offset);
						L.setStrokeWidth(3);
						root.getChildren().add(L);
						L.setOnMousePressed(new EventHandler<MouseEvent>() {
						    public void handle(MouseEvent mouseEvent) {
						    	root.getChildren().remove(info);
						        int x=(int)mouseEvent.getX();
						        int y=(int)mouseEvent.getY();
						        System.out.println("row "+(((float)y-shiftV)/2/offset-0.5)+"    column "+(((float)x-shiftH)/2/offset));
						        int colClicked = (int)Math.round(((float)x-shiftH)/2/offset);
						        int rowClicked = (int)Math.round(((float)y-shiftV)/2/offset - 0.5);
						        if (m.getStartX() == -1){
						        	if (rowClicked == m.getRows()-1){
						        		m.setStartX(rowClicked);
						        		m.setStartY(colClicked);
						        		m.array[rowClicked][colClicked].setDown(false);
						        		System.out.println("startX "+m.getStartX()+"    startY "+m.getStartY());
						        		root.getChildren().remove(mouseEvent.getTarget());
						        	} else {
							        	System.out.println("Please choose start at the borders");
							        	info.setText("Please choose start at the borders");
							        	root.getChildren().add(info);
							        }
						        } 
						        else 
						        	if(m.getStartX()!=-1 && m.getEndX()==-1){
						        		if (rowClicked == m.getRows()-1){
						        		m.setEndX(rowClicked);
						        		m.setEndY(colClicked);
						        		m.array[rowClicked][colClicked].setDown(false);
						        		System.out.println("endX "+m.getEndX()+"    endY "+m.getEndY());
						        		root.getChildren().remove(mouseEvent.getTarget());
						        		info.setText("");
						        		generateMaze.setDisable(false);
						        	}
						        	else {
						        		System.out.println("Please choose an end at the borders");
						        		info.setText("Please choose an end at the borders");
						        		root.getChildren().add(info);
						        	}
						        } // end of if-else statements 
						    } // end of handle method
						}); // end of setOnMousePressed
					}
					if (m.array[ro][col].getLeft() == true){
						L = new Line(gridx-offset, gridy-offset, gridx-offset, gridy+offset);
						L.setStrokeWidth(3);
						root.getChildren().add(L);
						L.setOnMousePressed(new EventHandler<MouseEvent>() {
						    public void handle(MouseEvent mouseEvent) {
						    	root.getChildren().remove(info);
						        int x=(int)mouseEvent.getX();
						        int y=(int)mouseEvent.getY();
						        System.out.println("row "+(((float)y-shiftV)/2/offset)+"    column "+(((float)x-shiftH)/2/offset + 0.5));
						        int colClicked = (int)Math.round(((float)x-shiftH)/2/offset + 0.5);
						        int rowClicked = (int)Math.round(((float)y-shiftV)/2/offset);
						        if (m.getStartX() == -1){
						        	if (colClicked == 0){
						        		m.setStartX(rowClicked);
						        		m.setStartY(colClicked);
						        		m.array[rowClicked][colClicked].setLeft(false);
						        		System.out.println("startX "+m.getStartX()+"    startY "+m.getStartY());
						        		root.getChildren().remove(mouseEvent.getTarget());
						        	} else {
							        	System.out.println("Please choose start at the borders");
							        	info.setText("Please choose start at the borders");
							        	root.getChildren().add(info);
							        }
						        } 
						        else 
						        	if(m.getStartX()!=-1 && m.getEndX()==-1){
						        		if (colClicked == 0){
						        		m.setEndX(rowClicked);
						        		m.setEndY(colClicked);
						        		m.array[rowClicked][colClicked].setLeft(false);
						        		System.out.println("endX "+m.getEndX()+"    endY "+m.getEndY());
						        		root.getChildren().remove(mouseEvent.getTarget());
						        		info.setText("");
						        		generateMaze.setDisable(false);
						        	}
						        	else {
						        		System.out.println("Please choose an end at the borders");
						        		info.setText("Please choose an end at the borders");
						        		root.getChildren().add(info);
						        	}
						        } // end of if-else statements 
						    } // end of handle method
						}); // end of setOnMousePressed
					}
					
					if (m.array[ro][col].getRight() == true){
						L = new Line(gridx+offset, gridy-offset, gridx+offset, gridy+offset);
						L.setStrokeWidth(3);
						root.getChildren().add(L);
						L.setOnMousePressed(new EventHandler<MouseEvent>() {
						    public void handle(MouseEvent mouseEvent) {
						    	root.getChildren().remove(info);
						        int x=(int)mouseEvent.getX();
						        int y=(int)mouseEvent.getY();
						        System.out.println("row "+(((float)y-shiftV)/2/offset)+"    column "+(((float)x-shiftH)/2/offset - 0.5));
						        int colClicked = (int)Math.round(((float)x-shiftH)/2/offset - 0.5);
						        int rowClicked = (int)Math.round(((float)y-shiftV)/2/offset);
						        if (m.getStartX() == -1){
						        	if (colClicked == m.getColumns()-1){
						        		m.setStartX(rowClicked);
						        		m.setStartY(colClicked);
						        		m.array[rowClicked][colClicked].setRight(false);
						        		System.out.println("startX "+m.getStartX()+"    startY "+m.getStartY());
						        		root.getChildren().remove(mouseEvent.getTarget());
						        	} else {
							        	System.out.println("Please choose start at the borders");
							        	info.setText("Please choose start at the borders");
							        	root.getChildren().add(info);
							        }
						        } 
						        else 
						        	if(m.getStartX()!=-1 && m.getEndX()==-1){
						        		if (colClicked == m.getColumns()-1){
						        		m.setEndX(rowClicked);
						        		m.setEndY(colClicked);
						        		m.array[rowClicked][colClicked].setRight(false);
						        		System.out.println("endX "+m.getEndX()+"    endY "+m.getEndY());
						        		root.getChildren().remove(mouseEvent.getTarget());
						        		info.setText("");
						        		generateMaze.setDisable(false);
						        	}
						        	else {
						        		System.out.println("Please choose an end at the borders");
						        		info.setText("Please choose an end at the borders");
						        		root.getChildren().add(info);
						        	}
						        } // end of if-else statements 
						    } // end of handle method
						}); // end of setOnMousePressed
					}
				}
			}
			
			
		});
		
		generateMaze.setOnAction(e -> {
			if (m.getStartX()!=-1 && m.getEndX()!=-1){
	        	info.setText("");
	        	for (int col=0 ; col<column ; col++){
					for (int ro=0 ; ro<row ; ro++){
						m.array[ro][col].setUp(true);
						m.array[ro][col].setDown(true);
						m.array[ro][col].setLeft(true);
						m.array[ro][col].setRight(true);
						m.array[ro][col].setVisited(false);
					}
	        	}
	        	if (m.getStartX() == 0)
	        		m.array[m.getStartX()][m.getStartY()].setUp(false);
	        	if (m.getStartX() == m.getRows()-1)
	        		m.array[m.getStartX()][m.getStartY()].setDown(false);
	        	if (m.getStartY() == 0)
	        		m.array[m.getStartX()][m.getStartY()].setLeft(false);
	        	if (m.getStartY() == m.getColumns()-1)
	        		m.array[m.getStartX()][m.getStartY()].setRight(false);
	        	
	        	if (m.getEndX() == 0)
	        		m.array[m.getEndX()][m.getEndY()].setUp(false);
	        	if (m.getEndX() == m.getRows()-1)
	        		m.array[m.getEndX()][m.getEndY()].setDown(false);
	        	if (m.getEndY() == 0)
	        		m.array[m.getEndX()][m.getEndY()].setLeft(false);
	        	if (m.getEndY() == m.getColumns()-1)
	        		m.array[m.getEndX()][m.getEndY()].setRight(false);
	        	
	        	System.out.println("Create maze requested");
	        	m.createMaze();
	        	m.print();
	        	root.getChildren().clear();
	        	root.getChildren().addAll(rowsLabel, columnsLabel, noOfRows, noOfcolumns,
	        			blankMazeButton, generateMaze, solveButton, buttonExit, offsetLabel, slider);
	        	Line L2;
	        	int gridx, gridy;
	        	for (int col=0 ; col<column ; col++){
					for (int ro=0 ; ro<row ; ro++){
						if (m.array[ro][col].getUp() == true){
							gridx = shiftH+2*offset*col;
							gridy = shiftV+2*offset*ro;
							L2 = new Line(gridx-offset, gridy-offset, gridx+offset, gridy-offset);
							L2.setStrokeWidth(3);
							root.getChildren().add(L2);
						}
						if (m.array[ro][col].getDown() == true){
							gridx = shiftH+2*offset*col;
							gridy = shiftV+2*offset*ro;
							L2 = new Line(gridx-offset, gridy+offset, gridx+offset, gridy+offset);
							L2.setStrokeWidth(3);
							root.getChildren().add(L2);
						}
						if (m.array[ro][col].getLeft() == true){
							gridx = shiftH+2*offset*col;
							gridy = shiftV+2*offset*ro;
							L2 = new Line(gridx-offset, gridy-offset, gridx-offset, gridy+offset);
							L2.setStrokeWidth(3);
							root.getChildren().add(L2);
						}
						if (m.array[ro][col].getRight() == true){
							gridx = shiftH+2*offset*col;
							gridy = shiftV+2*offset*ro;
							L2 = new Line(gridx+offset, gridy-offset, gridx+offset, gridy+offset);
							L2.setStrokeWidth(3);
							root.getChildren().add(L2);
						}
					}
	        	}
	        }
			solveButton.setDisable(false);
		});
		
		
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent mouseEvent) {
		    	if (m!=null && m.getStartX()!=-1 && m.getEndX()!=-1){
		    		int col = Math.round(((float)mouseEvent.getX() - shiftH) / (2 * offset));
		    		int ro = Math.round(((float)mouseEvent.getY() - shiftV) / (2 * offset));
		    		int gridx = shiftH+2*offset*col;
					int gridy = shiftV+2*offset*ro;
		    		System.out.println(ro+" "+col);
					rectangle = new Rectangle(gridx-offset/2,gridy-offset/2,10,10);
					rectangle.setFill(Color.BLUE);
					root.getChildren().add(rectangle);
		    	}
		    	else
		    		System.out.println("");
		    }
		});
				
	}
	
}
