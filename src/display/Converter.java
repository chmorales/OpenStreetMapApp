package display;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

import data.Bounds;
import data.Node;

/**
 * Used for conversions between a latitude, longitude coordinate
 * system to a X, Y coordinate system with the origin at the top
 * left of the plane.
 * @author cpm02_000
 */
public class Converter {
    
    private static final double SCALE_FACTOR = 6000.0;
    
    private double zoomFactor;
    private Dimension dim;
    private double cLon;
    private double cLat;
    
    /**
     * Creates an object that will convert points from a directional
     * bounds format to (x,y) coordinates with their origin at the
     * top left of their plane.
     * @param bounds The directional bounds
     * @param dim The initial dimensions of the (x,y) plane
     */
    public Converter(Bounds bounds, Dimension dim){
	this.dim = dim;
	zoomFactor = 1.0;
	cLon = bounds.getcLon();
	cLat = bounds.getcLat();
    }
    
    /**
     * Takes a Node object and turns it into an XY Point in relation
     * to the current bounds.
     * @param n The node
     * @return The XY Point
     */
    public Point nodeToXY(Node n) {
	int centX = (int)(dim.getWidth()/2);
	int centY = (int)(dim.getHeight()/2);
	Point2D point = nodeToDirPoint(n);
	int xCoord = (int)(centX + (point.getX() * SCALE_FACTOR));
	int yCoord = (int)(centY - (point.getY() * SCALE_FACTOR));
	return new Point(xCoord, yCoord);
    }
    
    private Point2D nodeToDirPoint(Node n) {
	double rLon = n.getLon() - cLon;
	double rLat = n.getLat() - cLat;
	return new Point2D.Double(rLon*zoomFactor, rLat*zoomFactor);
    }
    
    /**
     * Takes a point from the XY plane and converts it to a
     * directional point in relation to the current bounds.
     * @param pixPoint
     * @return
     */
    public Point2D pixToDir(Point pixPoint){
	int pixX = (int)(pixPoint.getX() - dim.getWidth()/2);
	int pixY = (int)(dim.getHeight()/2 - pixPoint.getY());
	double nLon = pixX/(zoomFactor*SCALE_FACTOR)+cLon;
	double nLat = pixY/(zoomFactor*SCALE_FACTOR)+cLat;
	return new Point2D.Double(nLon, nLat);
    }
    
    /**
     * Manipulates the bounds of the directional plane
     * in order to pan by the given offsets.
     * @param xOffset
     * @param yOffset
     */
    public void pan(double xOffset, double yOffset){
	double dirXOffset = xOffset/(zoomFactor*SCALE_FACTOR);
	double dirYOffset = yOffset/(zoomFactor*SCALE_FACTOR);
	cLon -= dirXOffset;
	cLat += dirYOffset;
    }
    
    /**
     * Resizes dimensions of the XY frame.
     * @param dim
     */
    public void resize(Dimension dim){
	this.dim = dim;
    }
    
    /**
     * Returns the current zoom factor
     * @return
     */
    public double getZoom(){
	return zoomFactor;
    }

    /**
     * Zooms into the directional plane
     * @param zoomFactor
     */
    public void zoomIn(){
	zoomFactor *= 1.05;
    }
    
    public void zoomOut(){
	zoomFactor /= 1.05;
    }
}
