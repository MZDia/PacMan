package com.zetcode;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
class Cherry extends PickUp{

private	Image cherry;

	public Cherry(){
		loadImage();
		}
	public Cherry(int ps){
		super(ps);
		loadImage();
	}
	public Cherry(int x, int y){
		super(x,y);
		loadImage();
	}


	// Methods

public void loadImage(){

		ImageIcon iich = new ImageIcon("cherryR.png");
		cherry = iich.getImage();
}

public void switchImage(){
	switch(super.getD()){
		case(1):
		ImageIcon iich = new ImageIcon("cherryR.png");
		cherry = iich.getImage();
		break;

		case(2):
		ImageIcon iiche = new ImageIcon("cherryL.png");
		cherry = iiche.getImage();
		break;


	}

}

public void draw(Graphics g, int place){
	switchImage();
		g.drawImage(cherry, Pacman_game.getXs()[place], Pacman_game.getYs()[place], this);

	}
}