/** 
   Name : Michael Cataldo
   Class: CIS 421
   Assignment: ACO Optimization
   Due: 11/10/2016
*/
import java.util.*;
import java.io.FileNotFoundException;
import java.io.File;
import java.lang.IndexOutOfBoundsException;
import java.lang.Math;

public class ACO {

  public static ArrayList<MidEarthVertex> atlas; //list of all vertices
  public static ArrayList<MidEarthEdge> roadList; //list of all edges
  public static ArrayList<Ant> antpop = new ArrayList<Ant>(); //list of all ants
  public static final int NUM_CYCLES = 25; //number of cycles 
  public static final int POP_SIZE = 10; //how large our ant pop is
  public static final double ALPHA = 1.0; //weight of pheromone
  public static final double BETA = 0.4; //weight of heuristic
  public static final double RHO = 0.65; //trail persistence
  public static final double Q = 100; //quantity of pheromone left on trail

  public static void main(String[] args){

    try{
      File f = new File(args[0]);
      Scanner sc = new Scanner(f);
      parse(sc);
      MidEarthVertex start = getStartingNode();
      initAnts(start);
      MidEarthVertex goal = getGoal();
      runACO(start,goal);

    }catch(FileNotFoundException e){
      System.out.println(e);
      System.exit(0);
    }catch(IndexOutOfBoundsException e){
      System.out.println(e);
      System.exit(0);
    }
  }

  //Parameter: start - our starting node
  //Postcondition: initializes a POP_SIZE number of ants on the starting node,
  //               each numbered by i. Adds each ant to the antpop arraylist.
  public static void initAnts(MidEarthVertex start){
    for(int i = 0; i < POP_SIZE; i++){
      Ant s = new Ant(start,i);
      antpop.add(s);
    }
  }

  //Parameter: start - our starting node,
  //           goal - our goal node
  //Postcondition: Goes through a NUM_CYCLES number of iterations of ACO.
  //               During each iteration, each ant in our population
  //               completes its tour, has it's tour length calculated and
  //               prints it out. Then we evaporate pheromone, and disperse each
  //               ants amount of pheromone over it's chosen path. Afterwards,
  //               we clear the population of ants and reinitialize a new batch
  //               at the starting node.
  public static void runACO(MidEarthVertex start, MidEarthVertex goal){

    int iteration = 0;
    while(iteration < NUM_CYCLES){
      System.out.println("--------ITERATION: " + iteration + " -----------");
      for(Ant a : antpop){
        tour(a,goal,iteration);
        double length = calcTour(a.getEdges());
        a.setLength(length);
        System.out.print(a);
        System.out.println(" Path: " + a.getPath());
        System.out.println("Length of tour: " + length);
      }

      //evaporate pheromone
      evaporatePheromone();
      //disperse pheromone
      dispersePheromone();
 
      //evaporatePheromone();
      antpop.clear();
      initAnts(start);
      iteration++;
    }
  }

  //Postcondition: For each ant in our population, disperse its pheromone
  //               over the edges of its selected path.
  public static void dispersePheromone(){
    for(Ant a : antpop){
      ArrayList<MidEarthEdge> edges = a.getEdges();
      double amount = Q / a.getLength();
      for(MidEarthEdge e : edges){
        e.addPheromone(amount);
      }
    }
  }

  //Postcondition: Goes through all edges in our graph and evaporates a
  //               (1 - RHO) * (edges pheromone) amount of pheromone
  public static void evaporatePheromone(){
   for(MidEarthEdge e : roadList){
     e.setPheromone(e.getPheromone() * (1-RHO));        
   }
  }

  //Parameter: edges - an ArrayList of MidEarthEdges
  //Postcondition: calculates the length of an ants tour and returns it
  //Returns: length - a double containing the length of an ant's tour
  public static double calcTour(ArrayList<MidEarthEdge> edges){
    double length = 0;
    for(MidEarthEdge e : edges){
      length += e.getDistance();  
    }
    return length;
  }

  //Parameters: a - an Ant selected to perform its tour
  //            goal - our goal node
  //            iteration - our iteration number
  //Postcondition: Ant will traverse edges and nodes until it reaches the goal
  //               If this is our first iteration, we choose edges randomly
  //               Else we choose an edge based on probability
  //               Ant will backtrack if stuck.
  //               Continually add edges to ants list of edges for path and mark
  //               the node it selects to travel to as visited. 
  public static void tour(Ant a,MidEarthVertex goal,int iteration){
    MidEarthVertex start = antpop.get(0).getCurrent();
    Random rand = new Random();
    
    while(!a.getCurrent().equals(goal)){
      ArrayList<MidEarthEdge> neighbours = getNeighbours(a.getCurrent(),0);
      MidEarthEdge selected;
      if(iteration == 0)
        selected = ranChoice(a,neighbours);
      else
        selected = chooseEdge(a,neighbours);

      if(selected.isNull()){
        a.backtrack();
        a.subEdge();
        continue;
      }

      a.addEdge(selected);
      a.setCurrent(selected.getDest());
      a.markVisited(selected.getDest());
    }
  }

  //Parameters: a - an Ant chosen to select its next edge.
  //            neighbours - an arraylist of edges our ant can see
  //Postcondition: This method is called if this is our first iteration in our
  //               runACO method. All edges have the same chance of being
  //               selected. Randomly return an edge that the ant can see.
  //Returns: an edge that our ant can see.
  public static MidEarthEdge ranChoice(Ant a,
                       ArrayList<MidEarthEdge> neighbours){

    ArrayList<MidEarthVertex> visited = a.getVisited();
    MidEarthEdge selected = new MidEarthEdge();
    ArrayList<MidEarthEdge> candidates = new ArrayList<MidEarthEdge>();
    Random rand = new Random();    

    for(MidEarthEdge e : neighbours){
      if(visited.contains(e.getDest()))
        continue;
      candidates.add(e);
    }
   
    if(candidates.size() < 1)
      return selected;
    int r = rand.nextInt(candidates.size());
    return candidates.get(r); 
  }
  
  //Parameters: a - an ant that needs to select its next edge
  //            neighbours - an ArrayList of edges that our ant can see
  //Postcondition: Runs through list of neighbouring edges (skipping
  //               visited ones) and calculates total amount of pheromone
  //               observable along those edges. Then reiterates through
  //               list of neighbouring edges and sets their probability to
  //               their calculated probability. Then we probabilistically 
  //               choose an edge to return.
  //Returns: bestEdge - the selected edge based on probability
  public static MidEarthEdge chooseEdge(Ant a, 
                       ArrayList<MidEarthEdge> neighbours){
    
    double total = 0;
    double p = 0;
    ArrayList<MidEarthVertex> visited = a.getVisited();
    MidEarthEdge bestEdge = new MidEarthEdge();
    for(MidEarthEdge e : neighbours){
      if(visited.contains(e.getDest()))
        continue; 
      double t = Math.pow(e.getPheromone(), ALPHA);
      double n = Math.pow(e.getDistance(), BETA);
      double s = t * n;
      total += s;
    }
    
    for(MidEarthEdge e : neighbours){
      if(visited.contains(e.getDest()))
        continue;
      double tempP = calculateP(e,total);
      e.setP(tempP);
      if(tempP > p){
        p = tempP;
        bestEdge = e;
      }
    } 
   
    Random rand = new Random();
    double r = rand.nextDouble();
    double cumulative = 0.0;
    for(MidEarthEdge e : neighbours){
      if(visited.contains(e.getDest()))
        continue;
      cumulative += e.getP();
      if(r <= cumulative){
        return e;
      }
    } 

    return bestEdge;
  }

  //Parameters: edge - an edge that needs its probability calculated
  //            total - a double containing the total pheromone
  //Postcondition: Calculates the probability of selecting this edge from
  //               the list of observable edges
  //Returns: p - a double contianing the probability of selecting this edge.
  public static double calculateP(MidEarthEdge edge, double total){
    double t = Math.pow(edge.getPheromone(), ALPHA);
    double n = Math.pow(edge.getDistance(), BETA);
    double p = (t*n)/total;
    return p;
  }

  //Postcondition: gets the goal node
  //Returns: the goal node
  public static MidEarthVertex getGoal(){
    MidEarthVertex goal = new MidEarthVertex();
    for(MidEarthVertex v : atlas){
      if(v.getName().equals("Iron_Hills")){
        goal = v;
      }
    }
    return goal;
  }

  //Postcondition: Gathers the starting node from the user. Errors out if node
  //               provided is not in the txt document
  //Returns: the starting node, current.
  public static MidEarthVertex getStartingNode(){

    System.out.print("Entered your desired starting node: ");
    Scanner sc = new Scanner(System.in);
    String name = sc.next();
    boolean contains = false;
    MidEarthVertex current = new MidEarthVertex();

    for(MidEarthVertex v: atlas){
      if(v.getName().equals(name)){
        contains = true;
        current = v;
        current.setStart();
        break;
      }
    }
    if(!contains){
      System.out.println("No node with such a name: " + name);
      System.out.println("Exiting...");
      System.exit(0);
    }
    
    return current;
  }
 
  //Parameter: MidEarthVertex current - the current node we are looking at
  //           int flag - used to differentiate getNeighbours methods
  //Postcondition: Gathers neighbouring edges for current node, adds them
  //               to an arraylist and returns it.
  //Returns: neighbours - an Arraylist of neighbouring edges
  public static ArrayList<MidEarthEdge> getNeighbours(MidEarthVertex current, 
                                                                    int flag){
    ArrayList<MidEarthEdge> neighbours = new ArrayList<MidEarthEdge>();
    for(MidEarthEdge e : roadList){
      if(current.getName().equals(e.getSource().getName()))
        neighbours.add(e);
    }
    return neighbours;
  }

  //Parameter: sc - a Scanner open on the File provided from stdin
  //Postcondition: Parses the input file into Vertices and Edges connected
  //               together. Adds vertices to ArrayList atlas, adds edges to
  //               ArrayList roadList.
  public static void parse(Scanner sc){
    atlas = new ArrayList<MidEarthVertex>();
    roadList = new ArrayList<MidEarthEdge>();
    double startPheromone = 0.01;

    for(int i = 0; i < 25; i++){
      String in = sc.nextLine();
      String[] line = in.split(",");
      MidEarthVertex locale;
      try{
        ArrayList<MidEarthEdge> temp = new ArrayList<MidEarthEdge>();
        String name = line[0];
        int toGoal = Integer.parseInt(line[1]);
        locale = new MidEarthVertex(name,toGoal);
        atlas.add(locale);
      }catch(NumberFormatException e){
        System.out.println("Received Number Format Exception from line:");
        System.out.println(Arrays.toString(line));
      }
    }

    for(int i = 0; i < 25; i++){
      String in = sc.nextLine();
      String[] line = in.split(",");
    
      for(int j = 0; j < line.length; j++){
        ArrayList<MidEarthEdge> temp = new ArrayList<MidEarthEdge>();
        try{
          MidEarthVertex dest = new MidEarthVertex();
          for(int k = 0; k < 25; k++){
            if(atlas.get(k).toString().equals(line[j]))
              dest = atlas.get(k);
          }
          int distance = Integer.parseInt(line[j+1]);
          int roadQuality = Integer.parseInt(line[j+2]);
          int riskLevel = Integer.parseInt(line[j+3]);

          MidEarthEdge road = new MidEarthEdge(atlas.get(i),dest,distance,
                                      roadQuality,riskLevel,startPheromone);
          temp.add(road);
          j+=3;
        }catch(NumberFormatException e){
          System.out.println("Received Number Format Exception from line:");
          System.out.println(Arrays.toString(line));
        }
        roadList.addAll(temp);
      }
    }
  }
}
