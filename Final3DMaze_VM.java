//Vaani Menon
/*add ons:
	1) music in background
	2) gif
*/
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.Color;
import java.awt.Font;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import java.awt.Polygon;
import java.util.ArrayList;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Final3DMaze_VM extends JPanel implements KeyListener,MouseListener{
	JFrame frame; JFrame frame1; JPanel jPanel;
	//declare an array to store the maze
	String[][] maze = new String[15][40];
	private String[] r = null;
	String end = "";
	ArrayList<String[]> rowMaze = new ArrayList<String[]>();
	int begX = 0; int begY = 0; int endX = 0;int endY = 0; 	int row = begY, col = begX; int startNum = -1;
	int scale = 14; int count=0; int rotation = 1; static int secPass = 0;
	boolean mazeComplete = false; static boolean checkFrame;
	Polygon ceiling1; Polygon floor;
	Polygon backWall1, backWall2, backWall3, backWall4, backWall5, backWall6, backWall7, backWall8;
	Polygon polyWalls1, polyWalls2, polyWalls3, polyWalls4;
	Polygon wall1, wall2, wall21, wall22, wall31, wall32, wall41, wall42;
	ArrayList<Polygon> drawWallBack = new ArrayList<>(); ArrayList<Polygon> polyWalls = new ArrayList<>(); ArrayList<Polygon> drawWalls = new ArrayList<>();
	ArrayList<Boolean> checkDrawWall = new ArrayList<>();
	Color color;
	private Font f; private Font f2;
	static Thread thread = new Thread();

	//int x=100,y=100;
	public Final3DMaze_VM(){
		setBoard();
		frame=new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,800);
		frame.setVisible(true);
		frame.addKeyListener(this);
		color = new Color(255,255,255,1);
		f=new Font("Times New Roman",Font.PLAIN,40);
		f2 = new Font("Times New Roman", Font.PLAIN, 12);

		frame1 = new JFrame("Maze Opening Screen");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jPanel = new JPanel();
		jPanel.setBackground(Color.BLACK);
		 jPanel.setPreferredSize(new Dimension(1000,900));
		ImageIcon image = new ImageIcon("begin.gif");
		JLabel label = new JLabel(image);

		jPanel.add(label);
		frame1.getContentPane().add(jPanel);
		frame1.pack();
		frame1.setVisible(true);
		frame1.addKeyListener(this);
		checkFrame = true;
	}
	public void paintComponent(Graphics g)  {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g.setColor(Color.WHITE);	//this will set the backWallground color
		g.fillRect(0,0,1000,800);
		//draw drawWalls
		g.setColor(Color.BLACK);
		g.fillPolygon(ceiling1);
		g.setColor(Color.BLACK);
		g.fillPolygon(floor);
		double[] d1X={0,300, 990,300, 990,750, 0, 750};
		double[] floor1Y={300,300, 750, 750};

		g.setColor(Color.DARK_GRAY);

			g.setColor(Color.LIGHT_GRAY);
			for(Polygon backWall : drawWallBack)
				g.fillPolygon(backWall);
			g.setColor(Color.DARK_GRAY);
			for(int i =0; i<checkDrawWall.size(); i++){
				if(checkDrawWall.get(i)){
					g.fillPolygon(drawWalls.get(i));
				}
			}


			g.setColor(Color.LIGHT_GRAY);
			//g.fillPolygon(polyWalls4);
			if(startNum>0){
				g.fillPolygon(polyWalls.get(startNum-1));
			}
		// 2d drawBoard here!

		g.setColor(Color.WHITE);

	for(int x=0; x<maze.length; x++){
		for(int y=0; y<maze[0].length; y++){
			if(maze[x][y].equals("*")){
				g.setColor(color);
				g.fillRect(scale*y,scale*x,scale,scale);
				g.drawRect(scale*y,scale*x,scale,scale);

			}
			if(maze[x][y].equals(" ")){
				g.setColor(Color.WHITE);
				g.fillRect(scale*y,scale*x,scale,scale);
				g.drawRect(scale*y,scale*x,scale,scale);

			}
		}
	}
		//character
		g.setColor(Color.RED);
		g.drawOval(7+row*scale,2+col*scale,scale-10,scale-10);
		g.fillOval(7+row*scale,2+col*scale,scale-10,scale-10);
		//not done yet
		if(row==36 && col == 9){
			g.setColor(Color.RED);
			g.setFont(f);
			g.drawString("Great job!", 400, 400);
		}
		g.setColor(Color.YELLOW);
		g.setFont(f2);
		g.drawString("Direction:"+rotation, 15, 450);
		g.drawString("0-UP", 15, 460); g.drawString("1-RIGHT", 15, 470);
		g.drawString("2-DOWN", 15, 480);  g.drawString("3-LEFT", 15, 490);

		g.setFont(f);
		g.drawString("Count:"+count,15,420);
	}
	public void setBoard(){
		//choose your maze design
		//pre-fill maze array here
		File name = new File("mazeLayout.txt");
		try{
			BufferedReader input = new BufferedReader(new FileReader(name));
			String text;
			while( (text=input.readLine())!= null){
				r = text.split("");
				rowMaze.add(r);
				//your code goes in here to chop up the maze design
				//fill maze array with actual maze stored in text file
			}
			maze = new String[rowMaze.size()][r.length];
			for(int x=0; x<maze.length; x++){
				for(int y=0; y<maze[0].length; y++){
					maze[x][y] =  rowMaze.get(x)[y];
					col=1; row=1;
				}
			}
		}
		catch (IOException io){
			System.err.println("File error");
		}
		setWalls();
		checkPath();
	}
	public void setWalls(){
		//when you're ready for the 3D part

		int[] ceiling1X={10,1000,1000,10}; int[] ceiling1Y={10,10, 200, 200};
		ceiling1=new Polygon(ceiling1X,ceiling1Y,4);  //needs to be set as a global!

		int[] floor1X={10,1000,1000,10}; int[] floor1Y={310,310, 760, 760};
		floor = new Polygon(floor1X, floor1Y, 4);

		int[] w1X={10, 160, 160, 10}; int[] w1Y={10, 60, 610, 760}; int[] w2X={1000, 850, 850, 1000}; int[] w2Y={10, 60, 610, 760};
		int[] w21X={160, 310, 310, 160}; int[] w21Y={60, 110, 460, 610}; int[] w22X={850, 700, 700, 850}; int[] w22Y={60, 110, 460, 610};
		int[] w31X={310, 385, 385, 310}; int[] w31Y={110, 135, 385, 460}; int[] w32X={700, 625, 625, 700}; int[] w32Y={110, 135, 385, 460};
		int[] w41X={385, 460, 460, 385}; int[] w41Y={135, 160, 310, 385}; int[] w42X={625, 550, 550, 625}; int[] w42Y={135, 160, 310, 385};
		wall1 = new Polygon(w1X, w1Y,4); wall2 = new Polygon(w2X, w2Y,4);
		wall21 = new Polygon(w21X, w21Y, 4);wall22 = new Polygon(w22X, w22Y,4);
		wall31 = new Polygon(w31X, w31Y, 4); wall32 = new Polygon(w32X, w32Y,4);
		wall41 = new Polygon(w41X, w41Y, 4); wall42 = new Polygon(w42X, w42Y,4);

		int[] polyWalls1X={160,850,850,160}; int[] polyWalls1Y={60,60,610,610};
		int[] polyWalls2X={310,700,700,310}; int[] polyWalls2Y={110,110,460,460};
		int[] polyWalls3X={385,625,625,385}; int[] polyWalls3Y={135,135,385,385};
		int[] polyWalls4X={460,550,550,460}; int[] polyWalls4Y={160,160,310,310};
		polyWalls1 = new Polygon(polyWalls1X, polyWalls1Y, 4); polyWalls2 = new Polygon(polyWalls2X, polyWalls2Y, 4);
		polyWalls3 = new Polygon(polyWalls3X, polyWalls3Y, 4); polyWalls4 = new Polygon(polyWalls4X, polyWalls4Y, 4);

		int[] b1X={10,160,160,10}; int[] b1Y={10,60,610,610}; int[] b2X={850,1000,1000,850}; int[] b2Y={60,60, 610, 610};
		int[] b21X={10,310,310,10}; int[] b21Y={110,110,460,460}; int[] b22X={700,1000,1000,700}; int[] b22Y={110,110, 460, 460};
		int[] b31X={10, 385, 385, 10}; int[] b31Y={135, 135, 385, 385}; int[] b32X={625, 1000, 1000, 625}; int[] b32Y={135, 135, 385, 385};
		int[] b41X={10, 460, 460, 10}; int[] b41Y={160, 160, 310, 310}; int[] b42X={550,1000, 1000, 550}; int[] b42Y={160, 160, 310, 310};
		backWall1 = new Polygon(b1X, b1Y, 4); backWall2 = new Polygon(b2X, b2Y, 4);
		backWall3 = new Polygon(b21X, b21Y, 4); backWall4 = new Polygon(b22X, b22Y, 4);
		backWall5 = new Polygon(b31X, b31Y, 4);	backWall6 = new Polygon(b32X, b32Y, 4);
		backWall7 = new Polygon(b41X, b41Y, 4); backWall8 = new Polygon(b42X, b42Y, 4);

		drawWallBack.add(backWall1); drawWallBack.add(backWall2); drawWallBack.add(backWall3);
		drawWallBack.add(backWall4); drawWallBack.add(backWall5); drawWallBack.add(backWall6);
		drawWallBack.add(backWall7); drawWallBack.add(backWall8);

		drawWalls.add(wall1); drawWalls.add(wall2); drawWalls.add(wall21); drawWalls.add(wall22);
		drawWalls.add(wall31); drawWalls.add(wall32); drawWalls.add(wall41); drawWalls.add(wall42);

		polyWalls.add(polyWalls1); polyWalls.add(polyWalls2); polyWalls.add(polyWalls3); polyWalls.add(polyWalls4);
	}

	public void checkPath(){
		checkDrawWall = new ArrayList<>();
		startNum = -1;
		if(rotation==0){ //up
			for(int i = 0; i < 4; i++){
				if(maze[col-i][row-1].equals("*")){
					checkDrawWall.add(true);
				}
				if(maze[col-i][row+1].equals("*")){
					checkDrawWall.add(true);
				}else{
					checkDrawWall.add(false);
				}
				if(maze[col-i][row].equals("*")){
					startNum = i;
					break;
				}
			}
				if(startNum<0 && maze[col-4][col].equals("*"))
					startNum=4;
		}
		if(rotation==1){ //right
			for(int i = 0; i < 4; i++){
				if(maze[col-1][row+i].equals("*")){
					checkDrawWall.add(true);
				}
				if(maze[col+1][row+i].equals("*")){
					checkDrawWall.add(true);
				}else{
					checkDrawWall.add(false);
				}
				if(maze[col][col+i].equals("*")){
					startNum = i;
					break;
				}
			}
			if(startNum<0 && maze[col][row+4].equals("*")){
				startNum=4;
			}
			while(checkDrawWall.size()<8){
				checkDrawWall.add(true);
			}
		}
		if(rotation==2){ //down
			for(int i = 0; i < 4; i++){
				if(maze[col+i][row+1].equals("*")){
					checkDrawWall.add(true);
				}
				if(maze[col+i][row-1].equals("*")){
					checkDrawWall.add(true);
				}else{
					checkDrawWall.add(false);
				}
				if(maze[col+i][row].equals("*")){
					startNum = i;
					break;
				}
			}
				if(startNum<0 && maze[col+4][row].equals("*"))
					startNum=4;
			while(checkDrawWall.size()<8){
				checkDrawWall.add(true);
			}
		}
		if(rotation==3){ //left
			for(int i = 0; i < 4; i++){
				if(maze[col+1][row-i].equals("*")){
					checkDrawWall.add(true);
				}
				if(maze[col-1][row-i].equals("*")){
					checkDrawWall.add(true);
				}else{
					checkDrawWall.add(false);
				}
				if(maze[col][row-i].equals("*")){
					startNum = i;
					break;
				}
			}
			if(startNum<0 && maze[col][row-4].equals("*")){
				startNum=4;
			}
			while(checkDrawWall.size()<8){
				checkDrawWall.add(true);
			}
		}
	}
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode()==37){
			if(rotation==0){
				rotation = 3;
			}
			else{
				rotation--;
			}
		}
		if(e.getKeyCode()==39){
			if(rotation ==3){
				rotation = 0;
			}
			else{
				rotation++;
			}
		}
		if(e.getKeyCode()==38){
			if(rotation==0 && maze[col-1][row].equals(" ")){
				col--;
				count++;
			}
			if(rotation==1 && maze[col][row+1].equals(" ")){
				row++;
				count++;
			}
			if(rotation==3 && maze[col][row-1].equals(" ")){
				row--;
				count++;
			}
			if(rotation==2 && maze[col+1][row].equals(" ")){
				col++;
				count++;
			}
		}
		if(e.getKeyCode()==32){
			row = 1;
			col = 1;
			count = 0;
			checkFrame = false;
			if(!checkFrame){
				frame1.setVisible(false);
			}

		}
		checkPath();
		repaint();
	}
	public void keyReleased(KeyEvent e){ }
	public void keyTyped(KeyEvent e){ }
	public void mouseClicked(MouseEvent e){ }
	public void mousePressed(MouseEvent e){ }
	public void mouseReleased(MouseEvent e){ }
	public void mouseEntered(MouseEvent e){ }
	public void mouseExited(MouseEvent e){ }
	public static void main(String args[]){
		Final3DMaze_VM app = new Final3DMaze_VM();
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("bensound-inspire.wav").getAbsoluteFile());
		    Clip clip = AudioSystem.getClip();
		    clip.open(audioInputStream);
		    clip.start();
		} catch(Exception ex) {
		    ex.printStackTrace();
    	}
	}
}


