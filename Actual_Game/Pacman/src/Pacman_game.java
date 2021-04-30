package com.zetcode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.ArrayList;

public class Pacman_game extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int pixelSize = 10;
    private static final int AllPixels = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private static int[] x = new int[AllPixels];
    private static int[] y = new int[AllPixels];
	private static int level = 1;

	private int numCher = 2;
	private int numGhost = 4;
    private int numChar;
	private static int score;
    private boolean inGame = true;

    private Timer timer;




    //image declaration
	private Image wall;
	private Image coin;
    private static Image pacBoi;

    //Charcter declaration
    Pacman player;

	//Object Array Lists /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /
	public static ArrayList<Coin> allCoin = new ArrayList<Coin>();
	public static ArrayList<Cherry> allCher = new ArrayList<Cherry>();
	public static ArrayList<Ghost> allGhost = new ArrayList<Ghost>();

    public Pacman_game() {
        initBoard();
        Pacman_game.resetScore();
        Pacman_game.resetLevel();

    }

    private void initBoard() {
		addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

// initializing images
    private void loadImages() {

		ImageIcon iic = new ImageIcon("coin.png");
		coin = iic.getImage();

        ImageIcon iih = new ImageIcon("pacmanright.png");
        pacBoi = iih.getImage();

		ImageIcon iiw = new ImageIcon("Wall.jpg");
		wall = iiw.getImage();

    }

    private void initGame() {

    	//reset stuff
		Wall.resetWalls();
		Pacman_game.resetCoin();
		Pacman_game.resetCher();
		Pacman_game.resetGhost();



		//Spawn stuff
		initializeMaze(initializeMazeArrays());
		player = new Pacman(50, 50, 3);
		spawnCher();
		spawnGhost();


        numChar =1+numGhost+numCher;

// sets intial position of pacman
 for (int z = 0; z < numChar; z++) {
 	if(z == 0){
            x[z] = 40 - z * 10;
            y[z] = 40;
        }
        if(z>0 && z<(1+numGhost)){
        	x[z] = z*40 + 50;
        	y[z] = 290;
        }
        if(z>(1+numGhost)){
        	if(z % 2 == 0){
        	x[z] = z * 10;
        	y[z] = 100;
        	}
        	else{
        	x[z] =300- z * 10;
        	y[z] = 100;
        	}
        }
 }


        timer = new Timer(DELAY, this);
        timer.start();
    }

 @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

			collision();
			ghostMove();
			cherMove();
            player.move(0);
            player.checkCollision(B_HEIGHT, B_WIDTH, 0);
            inGame = !player.amIDead();
            checkC();
        }

        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);


       doDrawing(g);
    }


private void doDrawing(Graphics g) {



        if (inGame) {

                for(int h = 0; h<Wall.allWalls.size(); h++){
            	g.drawImage(wall, Wall.allWalls.get(h).getX(), Wall.allWalls.get(h).getY(), this);
            }

                            for(int c = 0; c<Pacman_game.allCoin.size(); c++){
            	g.drawImage(coin, Pacman_game.allCoin.get(c).getX(),Pacman_game.allCoin.get(c).getY(), this);
            }


				//Draws moving things
                        for (int z = 0; z < numChar; z++) {
                if (z == 0) {
                    g.drawImage(pacBoi, x[z], y[z], this);
                }

                for(int c = 1; c<Pacman_game.allCher.size()+1; c++){
                	if(z == c+(numGhost)){
                		Pacman_game.allCher.get(c-1).draw(g,z);
                	}
                }

                for(int c = 1; c<Pacman_game.allGhost.size()+1; c++){
                	if(z>0 && z<(numGhost+1)){
                		Pacman_game.allGhost.get(c-1).draw(g,z);
                	}
                }
            }

            drawText(g);



            Toolkit.getDefaultToolkit().sync();

        } else {

           gameOver(g);
        }
    }


public void drawText(Graphics g){
		String msg = "Score: " + Pacman_game.score;
		String msg4 = "Level: " + Pacman_game.level;
		String msg5 = "Lives: " + player.getLives();
		String msg2 = "Game Over";
		String msg3 = "You Win";
        Font small = new Font("Helvetica", Font.BOLD, 10);
        Font large = new Font("Helvetica", Font.BOLD, 30);
        FontMetrics metr = getFontMetrics(small);
        FontMetrics metr2 = getFontMetrics(large);


        if(inGame){
 		g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)), 8);
        g.drawString(msg4, 2, 8);
        g.drawString(msg5, (metr.stringWidth(msg4)+30), 8);
        }
        else if(!inGame && player.getLives() <= 0){
        	g.setColor(Color.white);
       		g.setFont(large);
        	g.drawString(msg2, B_WIDTH/2-60, B_HEIGHT/2);
        }
        else{
        	g.setColor(Color.white);
       		g.setFont(large);
        	g.drawString(msg3, B_WIDTH/2-60, B_HEIGHT/2);
        	g.drawString(msg, B_WIDTH/2-80, B_HEIGHT/2+50);
        }
}


// Movement for Ghost and cherries

public void ghostMove(){
	for(int x = 0; x<Pacman_game.allGhost.size(); x++){
		Pacman_game.allGhost.get(x).move(x+1);
		Pacman_game.allGhost.get(x).checkCollision(B_HEIGHT, B_WIDTH, x+1);
	}
}
public void cherMove(){
	for(int x = 0; x<Pacman_game.allCher.size(); x++){
		Pacman_game.allCher.get(x).move(x + (numGhost + 1));
		Pacman_game.allCher.get(x).checkCollision(B_HEIGHT, B_WIDTH, x + (numGhost + 1));
	}
}

// Spawning for ghost and cherries

public void spawnCher(){
	for(int x = 0; x<numCher; x++){
		Pacman_game.allCher.add(new Cherry(pixelSize));
	}
}
public void spawnGhost(){
	for(int x = 0; x<numGhost; x++){
		Pacman_game.allGhost.add(new Ghost(pixelSize));
	}
}



    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT)){
                player.setLD(true);
                player.setUD(false);
                player.setDD(false);
          player.setRD(false);
                   ImageIcon iih = new ImageIcon("pacmanleft.png");
        pacBoi = iih.getImage();
            }

            if ((key == KeyEvent.VK_RIGHT) ){
                player.setRD(true);
                player.setUD(false);
                player.setDD(false);
                 player.setLD(false);
                   ImageIcon iih = new ImageIcon("pacmanright.png");
        pacBoi = iih.getImage();
            }

            if ((key == KeyEvent.VK_UP)){
                player.setUD(true);
                player.setRD(false);
                player.setLD(false);
                player.setDD(false);
                   ImageIcon iih = new ImageIcon("pacmanup.png");
        pacBoi = iih.getImage();
            }

             if ((key == KeyEvent.VK_DOWN) ) {
                player.setDD(true);
                player.setRD(false);
                player.setLD(false);
                player.setUD(false);
                 ImageIcon iih = new ImageIcon("pacmandown.png");
       			 pacBoi = iih.getImage();
            }
        }
    }


	// Gets / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / /
	public int getNumG(){
		return numGhost;
	}

	public int getNumCh(){
		return numCher;
	}

	public int getNumChar(){
		return numChar;
	}

	public static int getScore(){
		return Pacman_game.score;
	}

	public static int getLevel(){
		return Pacman_game.level;
	}

	public static ArrayList<Coin> getCoinArray(){
		return Pacman_game.allCoin;
	}

	public static ArrayList<Ghost> getGhostArray(){
		return Pacman_game.allGhost;
	}

	public static ArrayList<Cherry> getCherArray(){
		return Pacman_game.allCher;
	}

	public static int[] getXs(){
		return x;
	}

	public static int[] getYs(){
		return y;
	}


	// Sets / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / /

		public void setNumG(int g){
		numGhost = g;
	}

	public void setNumCh(int ch){
		numCher = ch;
	}

	public void setNumChar(int charr){
		numChar = charr;
	}

	public static void setScore(int bee){
		Pacman_game.score = bee;
	}

	public static void setLevel(int level){
		Pacman_game.level = level;
	}

	public static void setXs( int[] arr){
		x = arr;
	}

	public static void setYs( int[] arr){
		 y = arr;
	}


	// Reset static varables / / / / / / / / / / / / /
	public static void resetCoin(){
		Pacman_game.allCoin.clear();
	}

	public static void resetCher(){
		Pacman_game.allCher.clear();
	}

	public static void resetScore(){
		Pacman_game.score =  -10;
	}

	public static void resetLevel(){
		Pacman_game.level =  1;
	}

	public static void resetGhost(){
		Pacman_game.allGhost.clear();
	}




	// Reactions from collision / / / / / / / / / / /
	public void collision(){
	player.collisionWithCoin(Pacman_game.allCoin);
	player.collisionWithCher(Pacman_game.allCher, numGhost);
	player.collisionWithGhost(Pacman_game.allGhost);
}


// Maze creation and randimization
	public int[][] initializeMazeArrays(){


// #s > -1 are walls, -1s are coins, -2s are blank spaces)

if(level == 1){

switch((int)(Math.random()*4)){
case 0:
 int[][] maze1 =
{{0,10,20,30,40,50,60,70,80,90,100,110,120,130,-2,-2,-2,-2,180,190,200,210,220,230,240,250,260,270,280,290,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,30,40,50,60,70,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,240,250,260,270,280,-1,300},
{0,-1,-1,30,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,280,-1,300},
{0,-1,-1,30,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,280,-1,300},
{0,-1,-1,30,-1,-1,-1,-1,-1,-1,-1,110,120,130,140,150,160,170,180,190,200,-1,-1,-1,-1,-1,-1,-1,280,-1,300},
{0,-1,-1,30,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,280,-1,300},
{0,-1,-1,30,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,280,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,140,150,160,170,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,100,-1,-1,-1,140,150,160,170,-1,-1,200,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,50,60,-1,-1,-1,100,-1,-1,-1,140,150,160,170,-1,-1,200,-1,-1,-1,240,250,-1,-1,-1,-1,300},//y=110
{0,-1,-1,-1,-1,-1,60,-1,-1,-1,-1,-1,-1,-1,140,150,160,170,-1,-1,-1,-1,-1,-1,240,-1,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,-1,60,-1,-1,-1,-1,-1,-1,-1,140,150,160,170,-1,-1,-1,-1,-1,-1,240,-1,-1,-1,-1,-1,300},
{-2,-1,-1,-1,-1,-1,60,-1,-1,-1,100,110,120,130,140,150,160,170,180,190,200,210,-1,-1,240,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,60,-1,-1,-1,100,110,120,130,140,150,160,170,180,190,200,210,-1,-1,240,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,60,-1,-1,-1,100,110,120,130,140,150,160,170,180,190,200,210,-1,-1,240,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,60,-1,-1,-1,-1,-1,-1,-1,140,150,160,170,-1,-1,-1,-1,-1,-1,240,-1,-1,-1,-1,-1,-2},
{0,-1,-1,-1,-1,-1,60,-1,-1,-1,-1,-1,-1,-1,140,150,160,170,-1,-1,-1,-1,-1,-1,240,-1,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,50,60,-1,-1,-1,100,-1,-1,-1,140,150,160,170,-1,-1,200,-1,-1,-1,240,250,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,100,-1,-1,-1,140,150,160,170,-1,-1,200,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,140,150,160,170,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,30,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,280,-1,300},
{0,-1,-1,30,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,280,-1,300},
{0,-1,-1,30,-1,-1,-1,-1,-1,-1,-1,110,120,130,140,150,160,170,180,190,200,-1,-1,-1,-1,-1,-1,-1,280,-1,300},
{0,-1,-1,30,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,280,-1,300},
{0,-1,-1,30,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,280,-1,300},
{0,-1,-1,30,40,50,60,70,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,240,250,260,270,280,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,10,20,30,40,50,60,70,80,90,100,110,120,130,-2,-2,-2,-2,180,190,200,210,220,230,240,250,260,270,280,290,300}};

return maze1;

case 1:
 int[][] maze2 =
{{0,10,20,30,40,50,60,70,80,90,100,110,120,130,-2,-2,-2,-2,180,190,200,210,220,230,240,250,260,270,280,290,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,30,40,50,60,70,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,240,250,260,270,280,-1,300},
{0,-1,-1,30,-1,-1,-1,70,-1,-1,-1,110,120,130,140,150,160,170,180,190,200,-1,-1,-1,240,-1,-1,-1,280,-1,300},
{0,-1,-1,30,-1,-1,-1,-1,-1,-1,-1,110,-1,-1,-1,-1,-1,-1,-1,-1,200,-1,-1,-1,-1,-1,-1,-1,280,-1,300},
{0,-1,-1,30,-1,-1,-1,-1,-1,-1,-1,110,-1,-1,-1,-1,-1,-1,-1,-1,200,-1,-1,-1,-1,-1,-1,-1,280,-1,300},
{0,-1,-1,30,-1,-1,-1,70,-1,-1,-1,110,-1,-1,140,150,160,170,-1,-1,200,-1,-1,-1,240,-1,-1,-1,280,-1,300},
{0,-1,-1,30,40,50,60,70,-1,-1,-1,110,-1,-1,140,-1,-1,170,-1,-1,200,-1,-1,-1,240,250,260,270,280,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,110,-1,-1,140,-1,-1,170,-1,-1,200,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,140,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,140,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,140,-1,-1,170,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,30,40,50,60,-1,-1,-1,-1,-1,-1,-1,140,-1,-1,170,-1,-1,-1,-1,-1,-1,240,250,260,270,-1,-1,300},//y=110
{0,-1,-1,30,-1,-1,60,-1,-1,-1,-1,-1,-1,-1,140,150,160,170,-1,-1,-1,-1,-1,-1,240,-1,-1,270,-1,-1,300},
{-2,-1,-1,-1,-1,-1,60,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,240,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,60,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,240,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,60,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,240,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,30,-1,-1,60,-1,-1,-1,-1,-1,-1,-1,140,150,160,170,-1,-1,-1,-1,-1,-1,240,-1,-1,270,-1,-1,-2},
{0,-1,-1,30,40,50,60,-1,-1,-1,-1,-1,-1,-1,140,-1,-1,170,-1,-1,-1,-1,-1,-1,240,250,260,270,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,140,-1,-1,170,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,170,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,170,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,110,-1,-1,140,-1,-1,170,-1,-1,200,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,30,40,50,60,70,-1,-1,-1,110,-1,-1,140,-1,-1,170,-1,-1,200,-1,-1,-1,240,250,260,270,280,-1,300},
{0,-1,-1,30,-1,-1,-1,70,-1,-1,-1,110,-1,-1,140,150,160,170,-1,-1,200,-1,-1,-1,240,-1,-1,-1,280,-1,300},
{0,-1,-1,-1,-1,-1,-1,70,-1,-1,-1,110,-1,-1,-1,-1,-1,-1,-1,-1,200,-1,-1,-1,240,-1,-1,-1,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,70,-1,-1,-1,110,-1,-1,-1,-1,-1,-1,-1,-1,200,-1,-1,-1,240,-1,-1,-1,-1,-1,300},
{0,-1,-1,30,-1,-1,-1,70,-1,-1,-1,110,120,130,140,150,160,170,180,190,200,-1,-1,-1,240,-1,-1,-1,280,-1,300},
{0,-1,-1,30,40,50,60,70,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,240,250,260,270,280,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,10,20,30,40,50,60,70,80,90,100,110,120,130,-2,-2,-2,-2,180,190,200,210,220,230,240,250,260,270,280,290,300}};

return maze2;

case 2:
int[][] maze4 =
{
//A  regular snake by Mia
{-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,130,140,150,160,170,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
{-2,-1,-1,-1,-1, 50, 60,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,240,250,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1, 70, 80, 90,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,210,220,230,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1, 80, 90,100,-1,-1,-1,-1,-1,-1,-1,-1,-1,200,210,220,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,110,-1,-1,-1,-1,-1,-1,-1,190,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,130,140,150,160,170,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,100,110,120,130,140,150,160,170,180,190,200,-1,220,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1, 90,100,110,120,130,-1,-1,-1,170,180,190,200,210,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1, 60,-1, 80, 90,100,-1,-1,-1,-1,-1,-1,-1,-1,-1,200,210,220,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1, 40,-1,-1, 70, 80, 90,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,210,220,230,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1, 50,-1, 70, 80,-1,-1,-1,-1,-1,-1,150,-1,-1,-1,-1,-1,-1,220,230,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1, 60, 70, 80,-1,-1,-1,-1,-1,140,150,160,-1,-1,-1,-1,-1,220,230,-1,-1,-1,-1,-1,-1,-2},
{ 0,-1,-1,-1,-1, 50, 60, 70, 80,-1,-1,-1,-1,130,140,150,160,170,-1,-1,-1,-1,220,230,240,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1, 50, 60,-1, 80,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,230,240,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1, 60,-1,-1,-1,-1,-1,120,-1,-1,-1,-1,-1,180,-1,-1,-1,-1,230,240,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,110,120,130,-1,-1,-1,170,180,190,-1,-1,-1,230,240,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,100,110,120,130,140,-1,160,170,180,190,200,-1,220,230,240,-1,-1,-1,-1,-1,300},
{-2,-1,-1,-1,-1,-1,-1,-1, 80,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,220,230,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1, 80,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,220,230,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1, 80,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,210,220,230,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1, 90,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,200,210,220,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1, 90,100,110,120,130,-1,-1,-1,170,180,190,200,210,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,100,110,120,130,140,150,160,170,180,190,200,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,130,140,150,160,170,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,130,140,150,160,170,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2}
};

return maze4;

case 3:
int[][] maze5 =
{
	//oh eye see by mia
{-2,-2,-2,-2,-2, 50, 60, 70, 80, 90,100,110,120,130,140,150,160,170,180,190,200,210,220,230,240,250,-2,-2,-2,-2,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,50,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,250,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,50,-1,-1,-1,-1,-1,-1,-1,-1,-1,150,-1,-1,-1,-1,-1,-1,-1,-1,-1,250,-1,-1,-1,-1,-2},
{ 0,-1,-1, 30, 40,50, 60, 70,-1,-1,-1,-1,-1,-1,-1,150,-1,-1,-1,-1,-1,-1,-1,230,240,250,260,270,-1,-1,300},
{ 0,-1,-1,-1,-1,50,-1,-1,-1,-1,-1,-1,-1,-1,-1,150,-1,-1,-1,-1,-1,-1,-1,-1,-1,250,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,50,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,250,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,140,150,160,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,120,130,-1,-1,-1,170,180,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,100,110,-1,-1,-1,-1,-1,-1,-1,190,200,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1,-1, 90,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,210,-1,-1,-1,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1, 80,-1,-1,-1,-1,-1,-1,150,-1,-1,-1,-1,-1,-1,220,-1,-1,-1,-1,-1,-1,-1,300},
{ 0,-1,-1, 30, 40,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,150,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,260,270,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1, 80,-1,-1,-1,-1,-1,-1,150,-1,-1,-1,-1,-1,-1,220,-1,-1,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1,-1, 90,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,210,-1,-1,-1,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,100,110,-1,-1,-1,-1,-1,-1,-1,190,200,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,120,130,-1,-1,-1,170,180,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,140,150,160,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,50,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,250,-1,-1,-1,-1,300},
{ 0,-1,-1,-1,-1,50,-1,-1,-1,-1,-1,-1,-1,-1,-1,150,-1,-1,-1,-1,-1,-1,-1,-1,-1,250,-1,-1,-1,-1,300},
{ 0,-1,-1, 30, 40,50, 60, 70,-1,-1,-1,-1,-1,-1,-1,150,-1,-1,-1,-1,-1,-1,-1,230,240,250,260,270,-1,-1,300},
{-2,-1,-1,-1,-1,50,-1,-1,-1,-1,-1,-1,-1,-1,-1,150,-1,-1,-1,-1,-1,-1,-1,-1,-1,250,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,50,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,250,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2},
{-2,-2,-2,-2,-2, 50, 60, 70, 80, 90,100,110,120,130,140,150,160,170,180,190,200,210,220,230,240,250,-2,-2,-2,-2,-2}
};

return maze5;



default:
int[][] mazeD = new int[31][31];
return mazeD;
}
}
else{
	 int[][] maze3 =
{{0,10,20,30,40,50,60,70,80,90,100,110,120,130,-2,-2,-2,-2,180,190,200,210,220,230,240,250,260,270,280,290,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,30,40,50,60,70,-1,-1,-1,110,120,130,140,150,-1,-1,-1,190,-1,210,220,230,-1,-1,260,270,-1,-1,300},
{0,-1,-1,30,-1,-1,-1,70,-1,-1,-1,110,-1,-1,-1,150,-1,-1,-1,190,-1,-1,-1,230,-1,-1,260,270,-1,-1,300},
{0,-1,-1,30,-1,-1,-1,70,-1,-1,-1,110,-1,-1,-1,150,-1,-1,-1,190,-1,-1,-1,230,-1,-1,260,270,-1,-1,300},
{0,-1,-1,30,40,-1,60,70,-1,-1,-1,110,-1,-1,-1,150,-1,-1,-1,190,200,-1,220,230,-1,-1,260,270,-1,-1,300},
{0,-1,-1,30,-1,-1,-1,70,-1,-1,-1,110,-1,-1,-1,150,-1,-1,-1,190,-1,-1,-1,230,-1,-1,260,270,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,70,-1,-1,-1,110,-1,-1,-1,150,-1,-1,-1,190,-1,-1,-1,230,-1,-1,260,270,-1,-1,300},
{0,-1,-1,30,40,50,60,70,-1,-1,-1,110,-1,130,140,150,-1,-1,-1,190,200,210,220,230,-1,-1,260,270,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,260,270,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,140,150,160,170,180,-1,-1,-1,-1,-1,-1,-1,260,270,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,130,-1,-1,-1,-1,-1,190,-1,-1,-1,-1,-1,-1,260,270,-1,-1,300},//y=110
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,120,-1,-1,-1,-1,-1,-1,-1,200,-1,-1,-1,-1,-1,260,270,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,110,-1,-1,-1,-1,-1,-1,-1,-1,-1,210,-1,-1,-1,-1,260,270,-1,-1,300},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,110,-1,-1,-1,-1,-1,-1,-1,-1,-1,210,-1,-1,-1,-1,260,270,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,120,130,140,150,-1,170,180,190,200,-1,-1,-1,-1,-1,260,270,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,130,-1,-1,-1,-1,-1,190,-1,-1,-1,-1,-1,-1,260,270,-1,-1,-2},
{-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,130,-1,150,-1,170,-1,190,-1,-1,-1,-1,-1,-1,260,270,-1,-1,-2},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,130,-1,-1,160,-1,-1,190,-1,-1,-1,-1,-1,-1,260,270,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,190,-1,-1,-1,-1,-1,-1,260,270,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,130,140,150,160,170,180,190,-1,-1,-1,-1,-1,-1,260,270,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,260,270,-1,-1,300},
{0,-1,-1,30,40,50,60,70,-1,-1,100,110,120,130,-1,-1,160,170,180,-1,-1,210,220,230,-1,-1,260,270,-1,-1,300},
{0,-1,-1,30,-1,-1,-1,70,-1,-1,100,-1,-1,130,-1,-1,160,-1,-1,-1,-1,210,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,30,-1,-1,-1,70,-1,-1,100,-1,-1,130,-1,-1,160,-1,-1,-1,-1,210,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,30,-1,50,60,70,-1,-1,100,-1,-1,130,-1,-1,160,170,180,-1,-1,210,220,230,-1,-1,-1,-1,-1,-1,300},
{0,-1,-1,30,-1,-1,-1,70,-1,-1,100,-1,-1,130,-1,-1,-1,-1,180,-1,-1,-1,-1,230,-1,-1,260,270,-1,-1,300},
{0,-1,-1,30,-1,-1,-1,70,-1,-1,100,-1,-1,130,-1,-1,-1,-1,180,-1,-1,-1,-1,230,-1,-1,260,270,-1,-1,300},
{0,-1,-1,30,-1,-1,-1,70,-1,-1,100,110,-1,130,-1,-1,160,170,180,-1,-1,210,220,230,-1,-1,260,270,-1,-1,300},
{0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,300},
{0,10,20,30,40,50,60,70,80,90,100,110,120,130,-2,-2,-2,-2,180,190,200,210,220,230,240,250,260,270,280,290,300}};

return maze3;

}


	}
	public void initializeMaze(int[][] maze){
		int pixelY = 0;
		int pixelX = 0;
		for(int y = 0; y< 31; y++){
			for(int x = 0; x<31; x++){
				if(maze[y][x] == pixelX){
					Wall.allWalls.add(new Wall(pixelX, pixelY));

				}
				else if(maze[y][x] == -1){
					Pacman_game.allCoin.add(new Coin(pixelX,pixelY));
				}

				pixelX += 10;
				}
			pixelX = 0;
			pixelY += 10;
		}
	}

	public void gameOver(Graphics G){
		drawText(G);
	}

	public void checkC(){
		if(Pacman_game.allCoin.size() == 0){
			if(score > 12000){
			inGame = false;
		}else{
			Pacman_game.level += 1;
			initBoard();
		}
	}
	}


}



