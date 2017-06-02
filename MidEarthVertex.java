/** 
   Name : Michael Cataldo
   Class: CIS 421 
   Assignment: A* Algorithm
   Due: 10/24/2016
*/
import java.util.ArrayList;

public class MidEarthVertex implements Comparable<MidEarthVertex>{

  int toGoal;        //dist from current node to goal;
  public double f;
  public double g;
  public double h;
  boolean isStart;
  String name;
  MidEarthVertex parent;
  ArrayList<MidEarthEdge> neighbours;

  public MidEarthVertex(){
  
  }

  public MidEarthVertex(String name){
    this.name = name;
  }

  public MidEarthVertex(String name,int toGoal){
    this.name = name;
    this.toGoal = toGoal;
    h = toGoal;
  }

  public String toString(){
    return name;
  }

  public void setParent(MidEarthVertex parent){
    this.parent = parent;
  } 

  public MidEarthVertex getParent(){
    return parent;
  }

  public void setNeighbours(ArrayList<MidEarthEdge> neighbours){
    this.neighbours = neighbours;
  }

  public ArrayList<MidEarthEdge> getNeighbours(){
    return neighbours;
  }

  public void setF(double f){
    this.f = f;
  }

  public double getF(){
    return f;
  }

  public void setStart(){
    isStart = true;
    g = 0;
    parent = null;
    h = toGoal;
    f = h;
  }

  public boolean isStart(){
    return isStart;
  }

  public String getName(){
    return name;
  }

  public void setToGoal(int toGoal){
    this.toGoal = toGoal;
  }

  public int distToGoal(){
    return toGoal;
  }

  public void setG(double g){
    this.g = g;
  }

  public double getG(){
    return g;
  }

  public int compareTo(MidEarthVertex other){
    return Double.compare(other.f,this.f);
  }

}

