/** 
   Name : Michael Cataldo
   Class: CIS 421
   Assignment: ACO Optimization
   Due: 11/10/2016
*/
import java.util.ArrayList;

public class Ant {

  public MidEarthVertex current;
  public MidEarthEdge lastEdge;
  public int num;
  public double length = 0;
  public ArrayList<MidEarthVertex> visited = new ArrayList<MidEarthVertex>();
  public ArrayList<MidEarthVertex> path = new ArrayList<MidEarthVertex>();
  public ArrayList<MidEarthEdge> edges = new ArrayList<MidEarthEdge>();

  public Ant(MidEarthVertex current, int num){
    this.current = current;
    this.num = num;
    visited.add(this.current);
    path.add(this.current);
  }

  public void addEdge(MidEarthEdge e){
    edges.add(e);
  }

  public void subEdge(){
   edges.remove(edges.size()-1);
  }

  public ArrayList<MidEarthEdge> getEdges(){
    return edges; 
  }

  public void setLastEdge(MidEarthEdge last){
   lastEdge = last;
  }

  public void setLength(double l){
    length = l;
  }

  public double getLength(){
   return length;
  }

  public void addLength(double update){
    length += update;
  }

  public void subLength(double update){
    length -= update;
  }

  public void backtrack(){
    path.remove(current);
    current = path.get(path.size()-1);
  }

  public void markVisited(MidEarthVertex v){
    visited.add(v);
  }

  public ArrayList<MidEarthVertex> getVisited(){
    return visited;
  }

  public ArrayList<MidEarthVertex> getPath(){
    return path;
  }

  public void setCurrent(MidEarthVertex v){
    current = v;
    path.add(v);
  }

  public MidEarthVertex getCurrent(){
    return current;
  }

  public String toString(){
    return "Ant#: " + num + " Current node: " + current.toString();
  }
}

