package data;

public class Bounds {

    private double maxLatitude;
    private double maxLongitude;
    private double minLatitude;
    private double minLongitude;

    private double cLon;
    private double cLat;

    /**
     * Creates a new Bounds object with the 
     * @param maxLatitude
     * @param minLatitude
     * @param maxLongitude
     * @param minLongitude
     */
    public Bounds(String maxLongitude, String minLongitude,
                  String maxLatitude, String minLatitude){

        this.maxLatitude = Double.parseDouble(maxLatitude);
        this.minLatitude = Double.parseDouble(minLatitude);
        this.maxLongitude = Double.parseDouble(maxLongitude);
        this.minLongitude = Double.parseDouble(minLongitude);

        cLon = (this.maxLongitude + this.minLongitude)/2;
        cLat = (this.maxLatitude + this.minLatitude)/2;
    }

    /**
     * @return the cLon
     */
    public double getcLon() {
        return cLon;
    }

    /**
     * @return the cLat
     */
    public double getcLat() {
        return cLat;
    }

    /**
     * @return the maxLatitude
     */
    public double getMaxLatitude() {
        return maxLatitude;
    }

    /**
     * @param maxLatitude the maxLatitude to set
     */
    public void setMaxLatitude(double maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    /**
     * @return the maxLongitude
     */
    public double getMaxLongitude() {
        return maxLongitude;
    }

    /**
     * @param maxLongitude the maxLongitude to set
     */
    public void setMaxLongitude(double maxLongitude) {
        this.maxLongitude = maxLongitude;
    }

    /**
     * @return the minLatitude
     */
    public double getMinLatitude() {
        return minLatitude;
    }

    /**
     * @param minLatitude the minLatitude to set
     */
    public void setMinLatitude(double minLatitude) {
        this.minLatitude = minLatitude;
    }

    /**
     * @return the minLongitude
     */
    public double getMinLongitude() {
        return minLongitude;
    }

    /**
     * @param minLongitude the minLongitude to set
     */
    public void setMinLongitude(double minLongitude) {
        this.minLongitude = minLongitude;
    }

    /**
     * Adds an offset to the bounds. Simulates the "panning" of the window
     * that the Bounds represents. 
     * @param x
     * @param y
     */
    public void addOffset(double x, double y){
        minLongitude -= x;
        maxLongitude -= x;
        minLatitude += y;
        maxLatitude += y;
        cLon = (this.maxLongitude + this.minLongitude)/2;
        cLat = (this.maxLatitude + this.minLatitude)/2;
    }
}
