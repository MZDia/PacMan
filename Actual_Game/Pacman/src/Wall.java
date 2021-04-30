package com.zetcode;

import javax.swing.*;
import java.util.*;

class Wall {

  public static ArrayList<Wall> allWalls = new ArrayList<Wall>();

  private int x;
  private int y;
  private String shape;
  private int size;


  public Wall(int x, int y){
    this.x = x;
    this.y = y;
    shape = "square";
    size = 5;
  }

  public Wall(int x, int y, String shape, int size){
    this.x = x;
    this.y = y;
    this.shape = shape;
    this.size = size;
  }

  public int getX(){
    return x;
  }
  public int getY(){
    return y;
  }
  public String getShape(){
    return shape;
  }
  public int getSize(){
    return size;
  }

  public void setX(int x){
    this.x = x;
  }
  public void setY(int y){
    this.y = y;
  }
  public void setShape(String shape){
    this.shape = shape;
  }
  public void setSize(int size){
    this.size = size;
  }


  public static ArrayList<Wall> getWall(){
  	return allWalls;
  }

  public static void resetWalls(){
  	Wall.allWalls.clear();
  }


}



