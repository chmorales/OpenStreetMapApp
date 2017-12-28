package math;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import data.MapData;
import data.Node;
import data.Way;

/**
 * A mathematical graph constructed with the specified MapData
 * object. Used to find the shortest path from one node to
 * another.
 * @author cpm02_000
 *
 */
public class Graph {

    private HashMap<Node, ArrayList<Edge>> adjMap;
    private double lastDistance;

    /**
     * Creates a new Graph out of the specified MapData object.
     * @param data
     */
    public Graph(MapData data){
	adjMap = new HashMap<Node, ArrayList<Edge>>();
	for(Way w : data.getDrivableWays()){
	    Node n1 = null;
	    for(Node n2: w.getNodes()){
		if(adjMap.get(n2) == null)
		    adjMap.put(n2, new ArrayList<Edge>());
		if(n1 != null){
		    double distance = n1.getPoint().distance(n2.getPoint())*69;
		    Edge e = new Edge(n1, n2, distance);
		    addEdgeToNode(n1, e);
		    if(w.getTag("oneway") == null || w.getTag("oneway").equals("no")){
			e = new Edge(n2, n1, distance);
			addEdgeToNode(n2, e);
		    }
		}
		n1 = n2;
	    }
	}
	lastDistance = 0.0;
    }

    private void addEdgeToNode(Node n, Edge e){
	if(adjMap.get(n) == null)
	    adjMap.put(n, new ArrayList<Edge>());
	adjMap.get(n).add(e);
    }
    
    /**
     * Returns the total distance of the last path calculated.
     * @return
     */
    public double getLastDistance(){
	return lastDistance;
    }
    
    public LinkedList<Node> shortestPath(Node source, Node end){
	HashMap<Node, Node> predecessors = new HashMap<Node, Node>();
	HashMap<Node, Double> distances = new HashMap<Node, Double>(adjMap.size());
	MyPriorityQueue<Node> queue = new MyPriorityQueue<Node>(adjMap.size());
	queue.insert(source, 0.0);
	distances.put(source, 0.0);
	for(Node n : adjMap.keySet()){
	    if(!n.equals(source)){
		queue.insert(n, Double.POSITIVE_INFINITY);
		distances.put(n, Double.POSITIVE_INFINITY);
	    }
	}
	Node minNode;
	Double minDist;
	double newDist;
	while(!queue.isEmpty()){
	    minNode = queue.poll();
	    if(minNode == null)
		return null;
	    if(minNode.equals(end))
		break;
	    minDist = distances.get(minNode);
	    for(Edge e : adjMap.get(minNode)){
		newDist = minDist + e.getWeight();
		if(newDist < distances.get(e.getEndNode())){
		    queue.updateKey(e.getEndNode(), newDist);
		    distances.put(e.getEndNode(), newDist);
		    predecessors.put(e.getEndNode(), minNode);
		}
	    }
	}
	LinkedList<Node> path = new LinkedList<Node>();
	Node temp = end;
	while(temp != null){
	    path.addFirst(temp);
	    temp = predecessors.get(temp);
	}
	if(!path.get(0).equals(source) || !path.get(path.size()-1).equals(end)){
	    return null;
	}
	lastDistance = distances.get(end);
	return path;
    }

}