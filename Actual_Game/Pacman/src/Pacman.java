package com.zetcode;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;


class Pacman extends GameObject {

    private static int lives;


	//Constructors // // // // // // // // // // // // // //
	public Pacman(){

		Pacman.lives = 3;
	}

	public Pacman(int x, int y, int live){
	super(x,y);
	Pacman.lives = live;

	}




	//Methods /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /

public void move(int place) {
        if (super.getLD() && !super.wallCollisionL(place)) {
            Pacman_game.getXs()[place] -= super.getPS();
        }

        else if (super.getRD() && !super.wallCollisionR(place)) {
           Pacman_game.getXs()[place] += super.getPS();
        }

        else if (super.getUD() && !super.wallCollisionU(place)) {
           Pacman_game.getYs()[place] -= super.getPS();
        }

        else if (super.getDD() && !super.wallCollisionD(place)) {
           Pacman_game.getYs()[place] += super.getPS();
        }

        super.setPosition(Pacman_game.getXs()[place], Pacman_game.getYs()[place]);

	}




	public boolean amIDead(){
		if(lives <= 0){
			return true;
		}
		return false;
	}


		//collision for anything /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /

		public void collisionWithCoin(ArrayList<Coin> obj){

		for(int oh = 0; oh<obj.size(); oh++){
			if(Pacman_game.getXs()[0] == (obj.get(oh)).getX() ){
				if(Pacman_game.getYs()[0] == (obj.get(oh)).getY() ){
					Pacman_game.allCoin.remove(oh);
					Pacman_game.setScore(Pacman_game.getScore()+10);
			}
			}
			}

	}

		public void collisionWithCher(ArrayList<Cherry> obj, int numGh){

		for(int oh = 0; oh<obj.size(); oh++){
			if(Pacman_game.getXs()[0] == (Pacman_game.getXs()[oh+(1+numGh)])){
				if(Pacman_game.getYs()[0] == Pacman_game.getYs()[oh+(1+numGh)]){
					Pacman_game.allCher.remove(oh);
					Pacman_game.setScore(Pacman_game.getScore()+200);
			}
			}
			}
	}

		public int collisionWithGhost(ArrayList<Ghost> obj){

		for(int oh = 0; oh<obj.size(); oh++){
			if(Pacman_game.getXs()[0] == (Pacman_game.getXs()[oh+1])){
				if(Pacman_game.getYs()[0] == Pacman_game.getYs()[oh+1]){
			lives -= 1;
			Pacman_game.getXs()[0] = 40;
			Pacman_game.getYs()[0] = 40;

					}
			}
			}


		return -1;
	}

	// gets / / / / / / / / / / / / / / / / / / / / / / /
	  public static int getLives(){
  		return Pacman.lives;
 	 }


	// sets / / / / / / / / / / / / / / / / / / / / / / /
 	 public static void setLives(int ohNo){
  		Pacman.lives = ohNo;
	 }


}

