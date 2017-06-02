/** 
   Name : Michael Cataldo
   Class: CIS 421
   Assignment: A* Algorithm
   Due: 10/24/2016
*/

public class MidEarthEdge implements Comparable<MidEarthEdge>{

  MidEarthVertex source;
  MidEarthVertex dest;
  double pheromone;
  boolean isNull;
  int distance;
  int roadQuality;
  int riskLevel;
  public double p;

  public MidEarthEdge(){
    isNull = true;
  }

  public MidEarthEdge(MidEarthVertex source, MidEarthVertex dest, 
                     int distance, int roadQuality, int riskLevel,
                     double pheromone){

    this.source = source;
    this.dest = dest;
    this.distance = distance;
    this.roadQuality = roadQuality;
    this.riskLevel = riskLevel;
    this.pheromone = pheromone;
  }

  public boolean isNull(){
    return isNull;
  }

  public void setP(double p){
    this.p = p;
  }

  public double getP(){
    return p;
  }

  public double getPheromone(){
    return pheromone;
  }

  public void addPheromone(double p){
    pheromone += p;
  }

  public void subPheromone(double p){
    pheromone -= p;
  }

  public void setPheromone(double p){
    pheromone = p;
  }

  public MidEarthVertex getSource(){
    return source;
  }

  public MidEarthVertex getDest(){
    return dest;
  }

  public int getDistance(){
    return distance;
  }

  public int getRoadQuality(){
    return roadQuality;
  }

  public int getRiskLevel(){
    return riskLevel;
  }

  public String toString(){
    return source + " -> " + dest + " : " + distance + " " + roadQuality +
           " " + riskLevel + " " + pheromone;
  }  

  public int compareTo(MidEarthEdge other){
    return Double.compare(other.p,this.p);
  }

}

