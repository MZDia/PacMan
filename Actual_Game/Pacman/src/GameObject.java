package com.zetcode;

import java.util.*;
import javax.swing.*;
import java.lang.Math;

class GameObject extends JFrame{
  private int pixelSize;

  private boolean leftDirection = false;
  private boolean rightDirection = false;
  private boolean upDirection = false;
  private boolean downDirection = false;
  private int x;
  private int y;
  int direction = 1;

//Constructors // // // // // // // // // // // // // //
public GameObject(){
	this.pixelSize = 0;
}
  public GameObject(int ps){
    this.pixelSize = ps;
  }

  public GameObject(int x, int y, int ps){
    this.pixelSize = ps;
    this.x = x;
    this.y = y;
  }

  public GameObject(int x, int y){
    this.pixelSize = 10;
    this.x = x;
    this.y = y;
  }

//Methods for Wall Collision /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /  /

  public boolean wallCollisionL(int place){
  	for(int p = 0; p < Wall.allWalls.size(); p++){
      if(Wall.allWalls.get(p).getX() +10 == Pacman_game.getXs()[place] && !((Pacman_game.getYs()[place] > Wall.allWalls.get(p).getY() || (Pacman_game.getYs()[place] < Wall.allWalls.get(p).getY())))){
        return true;
      }
  	}
    return false;
  }
  public boolean wallCollisionR(int place){
  	for(int p = 0; p < Wall.allWalls.size(); p++){
    if(Wall.allWalls.get(p).getX() -10 == Pacman_game.getXs()[place] && !((Pacman_game.getYs()[place] > Wall.allWalls.get(p).getY() || (Pacman_game.getYs()[place] < Wall.allWalls.get(p).getY())))){
        return true;
    }
  	}
      return false;
  }
  public boolean wallCollisionD(int place){
  	for(int p = 0; p < Wall.allWalls.size(); p++){
      if(Wall.allWalls.get(p).getY() -10 == Pacman_game.getYs()[place]&& !((Pacman_game.getXs()[place] > Wall.allWalls.get(p).getX() || (Pacman_game.getXs()[place] < Wall.allWalls.get(p).getX())))){
        return true;
      }
  	}
      return false;
    }
  public boolean wallCollisionU(int place){
  	for(int p = 0; p < Wall.allWalls.size(); p++){
      if(Wall.allWalls.get(p).getY() +10 == Pacman_game.getYs()[place] && !((Pacman_game.getXs()[place] > Wall.allWalls.get(p).getX() || (Pacman_game.getXs()[place] < Wall.allWalls.get(p).getX())))){
        return true;
      }
  	}
      return false;
  }

public void move(int place){


  		if(wallCollisionL(place)){
  			switch((int)(Math.random()*3)){
  				case 0:
  				direction = 1;
  					break;
  				case 1:
  				direction = 2;
  					break;
  				case 2:
  				direction = 3;
  					break;
  			}

  		}
  		else if(wallCollisionR(place)){
  			switch((int)(Math.random()*3)){
  				case 0:
  					direction = 4;
  					break;
  				case 1:
  					direction = 2;
  					break;
  				case 2:
  					direction = 3;
  					break;
  			}

  		}
  		else if(wallCollisionU(place)){
  			switch((int)(Math.random()*3)){
  				case 0:
  					direction = 1;
  					break;
  				case 1:
  					direction = 4;
  					break;
  				case 2:
  					direction = 3;
  					break;
  			}

  		}
  		else if(wallCollisionD(place)){
  			switch((int)(Math.random()*3)){
  				case 0:
  					direction = 1;
  					break;
  				case 1:
  					direction = 2;
  					break;
  				case 2:
  					direction = 4;
  					break;
  			}

  			}


  		switch(direction){
  			case 1:
  				moveRight(place);
  				break;
  			case 2:
  				moveUp(place);
  				break;
  			case 3:
  				moveDown(place);
  				break;
  			case 4:
  				moveLeft(place);
  				break;
  		}

  		 setPosition(Pacman_game.getXs()[place], Pacman_game.getYs()[place]);

  	}

public void moveLeft(int place){
Pacman_game.getXs()[place] -= pixelSize;
}
public void moveRight(int place){
Pacman_game.getXs()[place] += pixelSize;
}
public void moveUp(int place){
Pacman_game.getYs()[place] -= pixelSize;
}
public void moveDown(int place){
Pacman_game.getYs()[place] += pixelSize;
}



public void checkCollision( int B_HEIGHT, int B_WIDTH, int place) {


        if (Pacman_game.getYs()[place] >= B_HEIGHT) {
             Pacman_game.getYs()[place] = 10;       }

        if (Pacman_game.getYs()[place] < 0) {
             Pacman_game.getYs()[place] =B_HEIGHT-10;    }

        if (Pacman_game.getXs()[place] >= B_WIDTH) {
    Pacman_game.getXs()[place] = 10;
    }

        if (Pacman_game.getXs()[place] < 0) {
     Pacman_game.getXs()[place] = B_WIDTH-10;
        }
	}



// methods ////////////////////////////////////////

public void setPosition(int x, int y){
	this.x = x;
	this.y = y;
}

// gets / / / / / / / / / / / / / / / / / / / / / / /
public int getD(){
	return direction;
}

  public int getPS(){
  	return pixelSize;
  }

   	  public boolean getDD(){
	 return downDirection;
	 }

	 	 public boolean getRD(){
	 	return rightDirection;
	 }

	 	 public boolean getUD(){
	 	return upDirection;
	 }

	 	 public boolean getLD(){
	 	return leftDirection;
	 }

	 public int getX(){
	 	return x;
	 }

	 public int getY(){
	 	return y;
	 }

// sets / / / / / / / / / / / / / / / / / / / / / / /
public void setD(int dire){
	direction = dire;
}
  public void setPS(int ohNo){
  	this.pixelSize = ohNo;
  }

	 public void setDD(boolean bool){
	 	downDirection = bool;
	 }

	 	 public void setRD(boolean bool){
	 	rightDirection = bool;
	 }

	 	 public void setUD(boolean bool){
	 	upDirection = bool;
	 }

	 	 public void setLD(boolean bool){
	 	leftDirection = bool;
	 }
	 public void setX(int x){
	 	this.x = x;
	 }

	 public void setY(int y){
	 	this.y = y;
	 }

}
