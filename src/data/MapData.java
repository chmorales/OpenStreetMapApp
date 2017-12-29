package data;
import java.awt.geom.Point2D;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * MapData stores objects of type "Element" and organizes the data in a useful manner.
 * Elements can be looked up by their ID, and a List of each type of Element in this
 * MapData object can be requested. 
 * @author cpm02_000
 *
 */
public class MapData {

	private HashMap<String, Node> nodeMap;
	private HashMap<String, Way> wayMap;
	private HashMap<String, Relation> relatMap;
	private HashMap<String, Element> miscMap;
	private Bounds bounds;

	private ArrayList<Node> namedNodes;
	private ArrayList<Way> drivableWays;


	/**
	 * Creates a new empty MapData object. MapData
	 */
	public MapData(){
		nodeMap = new HashMap<String, Node>();
		wayMap = new HashMap<String, Way>();
		relatMap = new HashMap<String, Relation>();
		miscMap = new HashMap<String, Element>();
	}

	/**
	 * Created a new MapData object with the specified elements
	 * @param elements The elements to be added to the MapData object
	 */
	public MapData(Collection<Element> elements){
		this();
		for(Element e : elements){
			addElement(e);
		}
	}

	/**
	 * Adds the Element to the MapData Object
	 * @param e
	 */
	public void addElement(Element e){

		if(e instanceof Relation){
			Relation r = (Relation) e;
			relatMap.put(r.getID(), r);
		}else if(e instanceof Way){
			Way w = (Way) e;
			wayMap.put(w.getID(), w);
		}else if(e instanceof Node){
			Node n = (Node) e;
			nodeMap.put(n.getID(), n);
		}else
			miscMap.put(e.getID(), e);
	}

	/**
	 * Sets the bounds
	 * @param b new Bounds
	 */
	public void setBounds(Bounds b){
		bounds = b;
	}

	/**
	 * Returns the bounds
	 * @return the bounds
	 */
	public Bounds getBounds(){
		return bounds;
	}

	/**
	 * Gets the Node object with the specified ID
	 * @param nodeID
	 * @return The Node; null if no such nodeID exists
	 */
	public Node getNode(String nodeID){
		return nodeMap.get(nodeID);
	}

	/**
	 * Gets the Way object with the specified ID
	 * @param wayID
	 * @return The Way; null if no such wayID exists
	 */
	public Way getWay(String wayID){
		return wayMap.get(wayID);
	}

	public ArrayList<Way> getWays(){
		return new ArrayList<Way>(wayMap.values());
	}

	public ArrayList<Way> getDrivableWays(){
		if(drivableWays == null){
			drivableWays = new ArrayList<Way>();
			for(Way w: wayMap.values()){
				if(w.getTag("highway") != null)
					drivableWays.add(w);
			}
		}
		return drivableWays;
	}

	/**
	 * Gets the Relation object with the specified ID
	 * @param relatID
	 * @return The Relation; null if no such relatID exists
	 */
	public Relation getRelation(String relatID){
		return relatMap.get(relatID);
	}

	public ArrayList<Node> getNamedNodes(){
		if(namedNodes!=null){
			return namedNodes;
		}
		namedNodes = new ArrayList<Node>();
		for(Node node: nodeMap.values()){
			if(node.getTag("name") != null)
				namedNodes.add(node);
		}
		return namedNodes;
	}

	/**
	 * Given a Point2D in (Longitude, Latitude) format, find the
	 * closest node to that point
	 * @param point The point
	 * @return The closest Node
	 */
	public Node findClosestNode(Point2D point){
		Node closestNode = null;
		for(Node n : nodeMap.values()){
			if(closestNode == null)
				closestNode = n;
			if(point.distance(closestNode.getPoint()) > point.distance(n.getPoint())){
				closestNode = n;
			}
		}
		return closestNode;
	}

	public Node findClosestDrivableNode(Node node){
		Node closestNode = null;
		for(Way w : getDrivableWays()){
			for(Node n : w.getNodes()){
				if(closestNode == null)
					closestNode = n;
				if(node.getPoint().distance(closestNode.getPoint()) >
						node.getPoint().distance(n.getPoint())){
					closestNode = n;
				}
			}
		}
		return closestNode;
	}
}
