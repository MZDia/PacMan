package com.zetcode;
import java.util.*;
import javax.swing.*;
import java.awt.*;

class Ghost extends GameObject{
private Image ghost;
public Ghost(){
loadImage();
}
public Ghost(int ps){
	super(ps);
	loadImage();
}
public Ghost(int x, int y){
	super(x,y);
	loadImage();
}
  public Ghost(int x, int y, int ps){
    super(x,y,ps);
    loadImage();
  }


  	// Methods

 public void switchImage(){
	switch(super.getD()){
		case(1):
		ImageIcon iig = new ImageIcon("ghostR.png");
		ghost = iig.getImage();
		break;

		case(2):
		ImageIcon iigh = new ImageIcon("ghostL.png");
		ghost = iigh.getImage();
		break;


	}

}

public void loadImage(){

		ImageIcon iig = new ImageIcon("ghostR.png");
		ghost = iig.getImage();
}

public void draw(Graphics g, int place){
		switchImage();
		g.drawImage(ghost, Pacman_game.getXs()[place], Pacman_game.getYs()[place], this);

	}


  }


