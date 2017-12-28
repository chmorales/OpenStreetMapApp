package data;
import java.util.ArrayList;

public class Way extends Element {
    
    private ArrayList<Node> nodes;

    /**
     * Creates a new empty Way, can be filled with nodes
     * using the "addNode()" method.
     * @param id The ID of the Way
     */
    public Way(String id) {
	super(id);
	nodes = new ArrayList<Node>();
    }
    
    /**
     * Creates a new Way out of the nodes given. The order
     * of the nodes will be in the order that the ArrayList
     * is iterated over naturally.
     * @param id The ID of the way
     * @param nodes The List of Nodes to make up the Way
     */
    public Way(String id, ArrayList<Node> nodes){
	this(id);
	this.nodes.addAll(nodes);
    }
    
    /**
     * Adds a Node to the Way
     * @param n
     */
    public void addNode(Node n){
	nodes.add(n);
    }
    
    /**
     * Returns an ArrayList of nodes contained in this Way
     * @return
     */
    public ArrayList<Node> getNodes(){
	return new ArrayList<Node>(nodes);
    }
    
    public boolean containsNode(Node n){
	return nodes.contains(n);
    }
    
    public Node getStartNode(){
	return nodes.get(0);
    }
    
    public Node getEndNode(){
	return nodes.get(nodes.size()-1);
    }
}
