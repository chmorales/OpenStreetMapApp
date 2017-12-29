package data;

import java.awt.geom.Point2D;

public class Node extends Element {

    private double latitude;
    private double longitude;

    /**
     * Creates a new Node at the specified lat and lon with
     * the specified ID
     * @param id ID of the node
     * @param lat the Latitude
     * @param lon the Longitude
     */
    public Node(String id, String lat, String lon) {
        super(id);
        latitude = Double.parseDouble(lat);
        longitude = Double.parseDouble(lon);
    }

    public Node(String id, double lat, double lon) {
        super(id);
        latitude = lat;
        longitude = lon;
    }

    /**
     * Returns the latitude of the node
     * @return
     */
    public double getLat(){
        return latitude;
    }

    /**
     * Returns the longitude of the node
     * @return
     */
    public double getLon(){
        return longitude;
    }

    /**
     * Packs the (longitude, latitude) into a Point2D object
     * @return The point
     */
    public Point2D getPoint(){
        return new Point2D.Double(longitude, latitude);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Node [longitude=" + longitude + ", latitude=" + latitude + "]";
    }

    @Override
    public int hashCode(){
        return getID().hashCode();
    }

    @Override
    public boolean equals(Object other){
        if(other == null)
            return false;
        if(!(other instanceof Node))
            return false;
        Node n = (Node) other;
        if(n.getID() == getID() && n.latitude == latitude && n.longitude == longitude){
            return true;
        }
        return false;
    }
}
