package flappyBird;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Renderer extends JPanel
{

	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g)  //paintCom then shift+space and we get this
	{
		super.paintComponent(g);

		FlappyBird.flappyBird.repaint(g);
	}
	
}