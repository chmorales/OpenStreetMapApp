package data;

import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Line2D;

/**
 * Handles the functionality of checking if a user if "off course," or is straying too
 * far from the specified route. The route is specified by the path parameter in the 
 * constructor or in the "setRoute." 
 * @author cpm02_000
 *
 */
public class RouteHandler {

	private List<Node> path;
	private ArrayList<Line2D> segments;

	/**
	 * Creates a new empty RouteHandler
	 */
	public RouteHandler(){
		segments = new ArrayList<Line2D>();
	}

	/**
	 * Creates a new RouteHandler with the specified path
	 * @param path The path to be handled by this RouteHandler
	 */
	public RouteHandler(List<Node> path){
		this();
		setRoute(path);
	}

	/**
	 * Set the path of RouteHandler
	 * @param path
	 */
	public void setRoute(List<Node> path){
		this.path = path;
		if(this.path == null){
			segments = new ArrayList<Line2D>();
			return;
		}
		Node n1 = null;
		for(Node n2 : path){
			if(n1 != null){
				Line2D line = new Line2D.Double(n1.getPoint(), n2.getPoint());
				segments.add(line);
			}
			n1 = n2;
		}
	}

	/**
	 * Returns true if the route is set, false if otherwise
	 * @return
	 */
	public boolean routeSet(){
		if(path == null)
			return false;
		else
			return true;
	}

	/**
	 * Returns true if the specified location node is too far off course, false
	 * if otherwise.
	 * @param location
	 * @return
	 */
	public boolean isOffCourse(Node location){
		double minDist = segments.get(0).ptLineDist(location.getPoint())*69.0;
		double newDist;
		for(Line2D line : segments){
			newDist = line.ptLineDist(location.getPoint())*69.0;
			if(newDist < minDist)
				minDist = newDist;
		}
		System.out.println("Distance: "+minDist);
		if(minDist > 0.005)
			return true;
		else
			return false;
	}
}
