package display;
import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;

import javax.swing.JFrame;

//import com.starkeffect.highway.GPSDevice;

import data.Bounds;
import data.MapData;
import data.OSMParser;
import math.Graph;

/**
 * GPS Application that allows a user to track their position and find directions
 * from one point to another.
 * @author cpm02_000
 */
public class App {
    
    private MapData data;
    private Bounds masterBounds;
    
    private JFrame frame;
    private MapPanel mapPanel;
    private DirectionPanel directPanel;
//    private GPSDevice gps;
    
    /**
     * Creates a new instance of the OSM app
     * @param f
     * @throws Exception
     */
    public App(File f) throws Exception{
	frame = new JFrame("Open Street Map");
	Container content = frame.getContentPane();
	content.setLayout(new BorderLayout());
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	OSMParser parser = new OSMParser();
	data = parser.parse(f);
//	gps = new GPSDevice(f.getName());
	
	Graph graph = new Graph(data);
	masterBounds = data.getBounds();
	mapPanel = new MapPanel(masterBounds, data, 900, 900);
	directPanel = new DirectionPanel(mapPanel, graph);
	content.add(mapPanel, BorderLayout.CENTER);
	content.add(directPanel, BorderLayout.WEST);
	
	frame.pack();
	frame.setVisible(true);
    }
    
    /**
     * Driver method
     * @param args Program arguments
     */
    public static void main(String[] args) throws Exception{
	App demo = new App(new File(args[0]));
    }
}
