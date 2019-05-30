import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeAI  extends JPanel implements ActionListener {
	
	static JFrame jf;
	boolean looking = false;
	Timer tm = new Timer(5, this);	//TIMER USED TO REPAINT THE SCENE
	Random r = new Random();	//RANDOM OBJECT USED FOR CREATING REDX AND REDY
	final int SIZE = 16;	//SIZE OF ALL THE SQUARES
	int redX, redY = 0;		//RED SQUARE X AND Y
	Color leadColor = Color.ORANGE;	//SETS HEAD COLOR
	SinglyLinkedList<Square> snake = new SinglyLinkedList<>();	//CREATES A LINK LIST OF SQUARES
	DoublyLinkedList<Square> path = new DoublyLinkedList<>();
	public SnakeAI() {
		
		//SET UP THE GAME
		tm.start();		//START THE TIMER
		createRed();	//CREATE A RED SQUARE
		setBackground(Color.BLACK);	
		snake.addFirst(new Square(32,32));	//ADD THE FIRST SQUARE OF THE SNAKE
		AStarish();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//DRAW THE WALLS
		for(int i = 0; i < 32; i++) {
			g.setColor(Color.GRAY);		//WALL COLOR
			g.fillRect(i*16, 0 , SIZE, SIZE);	//TOP WALL
			g.fillRect(i*16, 496 , SIZE, SIZE*4);	//BOTTOM WALL
			g.fillRect(0, i*16 , SIZE, SIZE);	//LEFT WALL
			g.fillRect(496, i*16 , SIZE, SIZE);	//RIGHT WALL
		}
		//DRAW THE LENGTH
		g.setColor(Color.BLACK);	//FONT COLOR
		g.setFont(new Font("default", Font.BOLD, 16));	//SET FONT
		g.drawString("Length : " + Integer.toString(snake.size()), 400, 528);	//DRAWS THE LENGTH
		
		//DRAW THE PATH
		DoublyLinkedList.Node<Square> nodes = path.getHeader();
		while(nodes != null) {
			g.setColor(Color.GREEN);
			g.fillRect(nodes.getElement().getX(), nodes.getElement().getY(), SIZE, SIZE);
			g.setColor(Color.BLACK);
			g.drawRect(nodes.getElement().getX(), nodes.getElement().getY(), SIZE, SIZE);
			nodes = nodes.getNext();
		}
		
		//DRAW THE SNAKE TAIL
		SinglyLinkedList.Node<Square> node = snake.getHead().getNext();
		while(node != null) {
			g.setColor(Color.WHITE);
			g.fillRect(node.getElement().getX(), node.getElement().getY(), SIZE, SIZE);
			g.setColor(Color.BLACK);
			g.drawRect(node.getElement().getX(), node.getElement().getY(), SIZE, SIZE);
			node = node.getNext();
		}
		//DRAW HEAD
		g.setColor(leadColor);
		g.fillRect(snake.getHead().getElement().getX(), snake.getHead().getElement().getY(), SIZE, SIZE);
		g.setColor(Color.BLACK);
		g.drawRect(snake.getHead().getElement().getX(), snake.getHead().getElement().getY(), SIZE, SIZE);
		
		//DRAW THE RED SQUARE
		g.setColor(Color.RED);
		g.fillRect(redX, redY, SIZE, SIZE);
	}
	
	//RUNS EVERY STEP
	public void actionPerformed(ActionEvent e) {
		
		Square head = snake.getHead().getElement();		//SET HEAD ELEMENT TO VARIABLE FOR EASY ACCESS
		int tempX = head.getX();		//SETS HEADS X AND Y TO TEMP VARIABLES	
		int tempY = head.getY();
		if(path.getTrailer() != null) {
			head.setX(path.getTrailer().getElement().getX());
			head.setY(path.getTrailer().getElement().getY());
		} else
			reset();
		path.removeLast();
		
		//TRAVERSE THE LIST AND UPDATE THE REST OF THE SNAKES POSITION
		SinglyLinkedList.Node<Square> node = snake.getHead().getNext();		//START AT SQUARE AFTER THE HEAD
		while(node != null) {
			int tempX2 = node.getElement().getX();	//SET THE X AND Y OF THE SQUARE TO TEMP2
			int tempY2 = node.getElement().getY();
			node.getElement().setX(tempX);			//SET THE X AND Y OF THE SQUARE TO TEMP
			node.getElement().setY(tempY);
			tempX = tempX2;							//ASSIGN TEMP2 TO THE ORIGIONAL TEMP
			tempY = tempY2;
			node = node.getNext();
		}
		//CHECK TO SEE IF THE SNAKE COLLIDES WITH THE RED SQUARE
		if(head.getX() == redX && head.getY() == redY) {
			createRed();	//CREATE A NEW RED SQUARE
			for(int i = 0; i < 5; i++) {	//ADD FIVE SQUARES
				Square newS = new Square(snake.getTail().getElement().getX(),snake.getTail().getElement().getY());
				snake.addAfter(newS, snake.getTail());
			}
			AStarish();
		}
		//CHECK TO SEE IF SNAKE HITS THE WALL
		if(head.getX() <= 0 || head.getX() >= 495 || head.getY() <= 0 || head.getY() >= 495) {
			reset();	//CALL RESET METHOD
		}
		
		repaint();	//REPAINTS THE SCENE
		delay();	//ADDS A DELAY BETWEEN STEPS
		
	}
	
	//PATHFINDING AI
	public void AStarish() {
		path = new DoublyLinkedList<>();
		path.addFirst(snake.getHead().getElement());
		ArrayList<Square> cracks = new ArrayList<>();
		looking = true;
		int steps = 1;
		while(looking) {
			System.out.println("looking");
			DoublyLinkedList.Node<Square> current = path.getHeader();
			Square toAdd = getClosest(current.getElement(), cracks, steps);
			while(toAdd == null) {
				System.out.println("null "+steps);
				cracks.add(current.getElement());
				current = current.getNext();
				steps-=1;
				if(steps < 1) {
					System.out.println("exited");
					looking = false;
					break;
				}
				toAdd = getClosest(current.getElement(), cracks, steps);
			}
			if(toAdd != null && current != null) {
				path.newHead(toAdd, current);
				steps+=1;
				if(toAdd.getX() == redX && toAdd.getY()== redY)
					looking = false;
			}
		}
		System.out.println("Finished");
		System.out.println("Path Size "+steps);
		System.out.println("Tail Dist "+path.getTrailer().getElement().getDistance(redX, redY));
	}
	
	public Square getClosest(Square current, ArrayList<Square> cracks, int steps) {
		Square closest = null;
		for(int a = -1; a <= 1; a++) {
			for(int b = -1; b <= 1; b++) {
				if((a == 0 && b != 0) || (a != 0 && b == 0)) {
					int xbound = current.getX()+(a*SIZE);
					int ybound = current.getY()+(b*SIZE);
					if((xbound > 0 && xbound < 495) && (ybound > 0 && ybound < 495)) {
						Square check = new Square(xbound, ybound);
						if(!snakeCollision(check, steps) && !pathCollision(check) && !crackCollision(check, cracks)) {
							if(closest == null) {
								closest = check;
							} else if(check.getDistance(redX, redY) <= closest.getDistance(redX, redY)) {
								closest = check;
							}
						}
					}
				}
			}
		}
		return closest;
	}
	
	public boolean snakeCollision(Square head, int steps) {
		SinglyLinkedList.Node<Square> node = snake.getHead();
		int count = 0;
		while(node != null && count < snake.size()-steps) {
			if((head.getX() == node.getElement().getX()) && (head.getY() == node.getElement().getY())) {
				return true;
			}
			node = node.getNext();
			count++;
		}
		return false;
	}
	
	public boolean pathCollision(Square head) {
		DoublyLinkedList.Node<Square> node = path.getHeader();
		while(node != null) {
			if((head.getX() == node.getElement().getX()) && (head.getY() == node.getElement().getY())) {
				return true;
			}
			node = node.getNext();
		}
		return false;
	}
	
	public boolean crackCollision(Square head, ArrayList<Square> cracks) {
		for(int i = 0; i < cracks.size(); i++) {
			if(head.getX() == cracks.get(i).getX() && head.getY() == cracks.get(i).getY()) {
				return true;
			}
		}
		return false;
	}

	//ADD A DELAY BETWEEN MOVEMENTS
	public static void delay() {
		try {
			Thread.sleep(75);
		}
		catch(Exception e) {
			
		}
	}
	
	//RESET THE GAME
	public void reset() {
		leadColor = Color.GRAY;			//SET THE LEAD COLOR TO GRAY
		repaint();
		tm.stop();
		Object[] option = {"Play Again?"};
		int n = JOptionPane.showOptionDialog(jf, new JLabel("You Died :(",JLabel.CENTER), "", JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, new ImageIcon(""), option, option[0] );
		if(n == 0) {
			leadColor = Color.ORANGE;				//SET THE LEAD COLOR BACK TO WHITE
			snake.setSize(1);						//SET THE SIZE OF THE LIST BACK TO 1
			snake.restartList();					//RESTART THE LIST (SET THE TAIL OF THE LIST TO THE HEAD AND POINT THE HEAD TO NULL)
			snake.getHead().getElement().setX(32);	//SET THE X AND Y OF THE HEAD BACK TO 32
			snake.getHead().getElement().setY(32);
			createRed();	//CREATE A NEW RED SQUARE
			AStarish();
			tm.start();		//START THE TIMER AGAIN
			
		}
	}
	
	//CREATE THE RED SQUARE
	public void createRed() {
		boolean ok = true;
		do {
			ok = true;
			//MAKE THE SQUARE INSIDE THE GAME AREA
			redX = 0;
			redY = 0;
			redX = (r.nextInt(30)*16)+16; 
			redY = (r.nextInt(30)*16)+16;
			//CHECK TO SEE IF THE RED SQUARE WILL BE MADE ONTOP OF THE snake
			//IF SO KEEP TRYING TO PLACE THE RED SQUARE
			SinglyLinkedList.Node<Square> node = snake.getHead();
			while(node != null) {
				if(redX == node.getElement().getX() && redY == node.getElement().getY()) {
					ok = false;
				}
				node = node.getNext();
			}
		} while(!ok);
	}
	
	//MAIN METHOD
	public static void main (String []args) {
		//MAKES THE FRAME AND THE GAME AND SETS UP THE FRAME PROPERTIES
		jf = new JFrame();
		SnakeAI game = new SnakeAI();		//CREATES THE GAME PANEL
		jf.setTitle("Snake AI");
		jf.setSize(518, 576);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setResizable(false);
		jf.add(game);		//ADD THE GAME TO THE FRAME
		jf.setVisible(true);
		
	}


}
