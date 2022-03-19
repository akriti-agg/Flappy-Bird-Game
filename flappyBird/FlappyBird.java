package flappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener  //mouselistner bcoz after clicking mouse game starts and keylistner so that when we press spacebar it jumps
{

	public static FlappyBird flappyBird;    //static instance of flappy bird

	public final int WIDTH = 1400, HEIGHT = 800;     //final

	public Renderer renderer;      //declared variable named render

	public Rectangle bird;        //bird is of rectangle shape

	public ArrayList<Rectangle> columns;     //this arraylist is gonna contain our rectangle bird

	public int ticks, yMotion, score;    //declares the motion of bird

	public boolean gameOver, started;    

	public Random rand;                 //created a random object rand
 
	public FlappyBird()               //constructor
	{
		JFrame jframe = new JFrame();    //create a new jframe
		Timer timer = new Timer(20, this);

		renderer = new Renderer();
		rand = new Random();            //object created

		jframe.add(renderer); 
		jframe.setTitle("Flappy Bird");                    //sets title of the game
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       //when closed the frame then exit
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);          //visibility

		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);    //our bird(w/2, h/2)(we used -10 coz it represents the center of the screen)(20 is the height and width of bird)
		columns = new ArrayList<Rectangle>();

		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);

		timer.start();
	}

	public void addColumn(boolean start)      //for creating flaps in between the game
	{
		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);   //300 is the max height

		if (start)   //if its starting col then do this
		{
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));    //(lower flap)col.addnew rectangle creates rectangular flaps (we did -120 coz bird is to be above the grass)
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));   //(upper flap)(final height-height in this method)
		}
		else     //else do this
		{
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}

	public void paintColumn(Graphics g, Rectangle column)          //declared a method
	{
		g.setColor(Color.green.darker());      //color of flaps
		g.fillRect(column.x, column.y, column.width, column.height);
	}

	public void jump()      
	{
		if (gameOver) 
		{
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;

			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);

			gameOver = false;
		}

		if (!started)    //if not started then start it
		{
			started = true;
		}
		else if (!gameOver)
		{
			if (yMotion > 0)
			{
				yMotion = 0;
			}

			yMotion -= 10;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)     //necessary bcoz we implemmented actionListener
	{
		int speed = 15;   //speed of moving screen

		ticks++;          //no of clicks

		if (started)     //when game started these lines of code will happen
		{
			for (int i = 0; i < columns.size(); i++)   
			{
				Rectangle column = columns.get(i);   // flaps created and are moving with speed 10

				column.x -= speed;
			}

			if (ticks % 2 == 0 && yMotion < 15)
			{
				yMotion += 2;  //after 2 clicks it will jump
			}

			for (int i = 0; i < columns.size(); i++)
			{
				Rectangle column = columns.get(i);

				if (column.x + column.width < 0)
				{
					columns.remove(column);

					if (column.y == 0)       //if no cols are there then add col
					{
						addColumn(false);
					}
				}
			}

			bird.y += yMotion;    //adds y motion to the bird

			for (Rectangle column : columns)      //(making flaps)(for each col in col's)
			{
				if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 15 && bird.x + bird.width / 2 < column.x + column.width / 2 + 15)
				{
					score++;
				}

				if (column.intersects(bird))  //if bird touches col then game over
				{
					gameOver = true;

					if (bird.x <= column.x)
					{
						bird.x = column.x - bird.width;     //after game over bird stops only screen moves

					}
					else
					{
						if (column.y != 0)
						{
							bird.y = column.y - bird.height;
						}
						else if (bird.y < column.height)
						{
							bird.y = column.height;
						}
					}
				}
			}

			if (bird.y > HEIGHT - 120 || bird.y < 0)  //at these conditions game will be over
			{
				gameOver = true;
			}

			if (bird.y + yMotion >= HEIGHT - 120)
			{
				bird.y = HEIGHT - 120 - bird.height;
				gameOver = true;
			}
		}

		renderer.repaint();
	}

	public void repaint(Graphics g)
	{
		g.setColor(Color.cyan);         //color our bg cyan
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.orange);             //color to ground
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);     //(x,y,width,height)(width=width means it covers whole width)(all parameters are like this)

		g.setColor(Color.green);          //added some grass
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);
 
		g.setColor(Color.red);                                   //bird color is red
		g.fillRect(bird.x, bird.y, bird.width, bird.height);     //fill color in our bird rectangle

		for (Rectangle column : columns)
		{
			paintColumn(g, column);
		}

		g.setColor(Color.white);              //these 2 lines are executed when game is over
		g.setFont(new Font("Arial", 1, 100));

		if (!started)
		{
			g.drawString("Click to start!", 75, HEIGHT / 2 - 50);
		}

		if (gameOver)
		{
			g.drawString("Game Over!", 200, HEIGHT / 2 - 50);
		}

		if (!gameOver && started)
		{
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}
	}

	public static void main(String[] args)
	{
		flappyBird = new FlappyBird();       //object created,creating new instance of flappy bird
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		jump();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			jump();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e)       //all mouse related events occured due to mouse listener
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)        // all key related events are dur to keyListner
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{

	}

}