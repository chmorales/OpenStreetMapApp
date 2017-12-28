package math;

import data.Node;

public class Edge {

    private Node startNode;
    private Node endNode;
    double weight;
    
    public Edge(Node n1, Node n2, double weight){
	startNode = n1;
	endNode = n2;
	this.weight = weight;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * @return the startNode
     */
    public Node getStartNode() {
        return startNode;
    }

    /**
     * @return the endNode
     */
    public Node getEndNode() {
        return endNode;
    }
    
    @Override
    public int hashCode(){
	return startNode.hashCode() + endNode.hashCode() + 
		new Double(weight).hashCode();
    }
    
    @Override 
    public boolean equals(Object other){
	if(other == null)
	    return false;
	if(!(other instanceof Edge))
	    return false;
	Edge e = (Edge) other;
	if(e.startNode.equals(startNode) && e.endNode.equals(endNode) 
		&& e.weight == weight)
	    return true;
	return false;
	}
}
