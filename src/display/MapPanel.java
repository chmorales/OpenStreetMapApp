package display;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/**
 * MapPanel takes a list of Ways and draws them by connecting each
 * node in a way to form a line.
 * @author cpm02_000
 *
 */
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import data.Bounds;
import data.MapData;
import data.Node;
import data.Way;

public class MapPanel extends JPanel {

    private MapData data;
    private ArrayList<Way> ways;
    private Bounds panelBounds;

    private Point dragStart;
    private Converter converter;

    private static final Color HIGH = new Color(192,197,193);
    private static final Color MED = new Color(125,132,145);
    private static final Color LOW = new Color(115,106,125);
    private static final Color LAND = new Color(63,51,77);
    private static final Color BUILD = new Color(97,88,109);
    private static final Color PATH = new Color(87,75,96);
    private static final Color DIRECT = Color.CYAN;

    private ArrayList<Way> buildings;
    private ArrayList<Way> paths;
    private ArrayList<Way> lowWays; 
    private ArrayList<Way> medWays;
    private ArrayList<Way> highWays;

    private String namedNodeString;
    private Point2D hoverPoint;
    private Node hoverNode;
    private Node locationNode;
    private Node drivableNodeNearLocation;

    private Node startNode;
    private Node endNode;
    private Node driveStartNode;
    private Node driveEndNode;
    private BufferedImage startPin;
    private BufferedImage endPin;
    private BufferedImage namedPoint;
    private List<Node> directions;

    /**
     * Creates a new empty MapPanle object
     */
    public MapPanel(Bounds bounds, MapData data, int width, int height){
	this.data = data;
	setPreferredSize(new Dimension(900, 600));
	ways = new ArrayList<Way>(data.getWays());
	panelBounds = bounds;
	converter = new Converter(panelBounds, new Dimension(width, height));

	try {
	    startPin = ImageIO.read(new File("pinStart.png"));
	    endPin = ImageIO.read(new File("pinEnd.png"));
	    namedPoint = ImageIO.read(new File("star.png"));
	} catch (IOException e) {
	    System.out.println(e.getMessage());
	}

	addComponentListener(new ComponentAdapter(){
	    public void componentResized(ComponentEvent e){
		converter.resize(new Dimension(getWidth(), getHeight()));
	    }
	});

	addMouseWheelListener(new MouseAdapter(){
	    public void mouseWheelMoved(MouseWheelEvent event){
		if(event.getWheelRotation()<0)
		    converter.zoomIn();
		else if(event.getWheelRotation()>0)
		    converter.zoomOut();
		repaint();
	    }
	});

	addMouseListener(new MouseAdapter(){
	    public void mousePressed(MouseEvent event){
		dragStart = event.getPoint();
	    }
	    public void mouseClicked(MouseEvent event){
		Point2D dirPointClicked = converter.pixToDir(event.getPoint());
		if(startNode == null){
		    startNode = data.findClosestNode(dirPointClicked);
		    driveStartNode = data.findClosestDrivableNode(startNode);
		}else if(endNode == null){
		    endNode = data.findClosestNode(dirPointClicked);
		    driveEndNode = data.findClosestDrivableNode(endNode);
		}else{
		    startNode = null;
		    endNode = null;
		    driveStartNode = null;
		    driveEndNode = null;
		}
		repaint();
	    }
	});

	addMouseMotionListener(new MouseMotionAdapter(){
	    public void mouseMoved(MouseEvent e){
		hoverPoint = converter.pixToDir(e.getPoint());
		hoverNode = data.findClosestNode(hoverPoint);
		if(data.getNamedNodes().contains(hoverNode)){
		    namedNodeString = hoverNode.getTag("name");
		}else
		    namedNodeString = null;
		repaint();
	    }

	    public void mouseDragged(MouseEvent event){
		Point draggedToPoint = event.getPoint();
		double xOffset = (draggedToPoint.getX() - dragStart.getX());
		double yOffset = (draggedToPoint.getY() - dragStart.getY());
		converter.pan(xOffset, yOffset);
		dragStart = draggedToPoint;
		repaint();
	    }
	});
    }

    /**
     * Sets a list of nodes as the directions to be displayed by the map panel
     * @param dirPath
     */
    public void setDirections(List<Node> dirPath){
	directions = dirPath;
	repaint();
    }

    /**
     * The selected start node, null if no start node selected
     * @return
     */
    public Node getStartNode(){
	return driveStartNode;
    }

    /**
     * The selected end node, null if no end node selected
     * @return 
     */
    public Node getEndNode(){
	return driveEndNode;
    }

    /**
     * Sets the node of your current location
     * @param n
     */
    public void setLocationNode(Node n){
	locationNode = n;
	drivableNodeNearLocation = data.findClosestDrivableNode(locationNode);
	repaint();
    }
    
    /**
     * Returns the drivable node closest to the last set location node
     * @return
     */
    public Node getLocationNode(){
	return drivableNodeNearLocation;
    }

    private void initializeWays(){
	paths = new ArrayList<Way>();
	buildings = new ArrayList<Way>();
	lowWays = new ArrayList<Way>();
	medWays = new ArrayList<Way>();
	highWays = new ArrayList<Way>();
	String hTag;
	for(Way w : ways){
	    hTag = w.getTag("highway");
	    if(hTag!=null){
		if(hTag.equals("motorway")|| hTag.equals("trunk")
			|| hTag.equals("primary"))
		    highWays.add(w);
		else if(hTag.equals("secondary") || hTag.equals("tertiary"))
		    medWays.add(w);
		else
		    lowWays.add(w);
	    }else if(w.getTag("building")!=null)
		buildings.add(w);
	    else
		paths.add(w);
	}
    }

    private void drawWays(Graphics2D g){
	if(buildings == null)
	    initializeWays();
	if(converter.getZoom() > 0.85){
	    for(Way w: buildings)
		drawPath(g, w.getNodes(), BUILD, (float)(2*converter.getZoom()/3.5));
	    for(Way w: lowWays)
		drawPath(g, w.getNodes(), LOW, (float)(3*converter.getZoom()/3.5));
	}
	for(Way w: paths)
	    drawPath(g, w.getNodes(), PATH, (float)(2*converter.getZoom()/3.5));
	for(Way w: medWays)
	    drawPath(g, w.getNodes(), MED, (float)(4*converter.getZoom()/3.5));
	for(Way w: highWays)
	    drawPath(g, w.getNodes(), HIGH, (float)(5*converter.getZoom()/3.5));
    }

    private void drawPath(Graphics2D g, List<Node> nodeList, Color c, float strokeThickness){
	g.setStroke(new BasicStroke(strokeThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	g.setColor(c);
	GeneralPath path = new GeneralPath();
	Point nPoint = converter.nodeToXY(nodeList.get(0));
	path.moveTo(nPoint.getX(), nPoint.getY());
	for(Node n : nodeList){
	    nPoint = converter.nodeToXY(n);
	    path.lineTo(nPoint.getX(), nPoint.getY());
	}
	g.draw(path);
    }

    @Override
    public void paintComponent(Graphics g){
	Graphics2D g2 = (Graphics2D) g;
	super.paintComponent(g2);
	setBackground(LAND);
	drawWays(g2);
	if(directions != null){
	    g2.setStroke(new BasicStroke(5));
	    drawPath(g2, directions, DIRECT, (float)(4*converter.getZoom()/3.5));
	}
	if(locationNode != null){
	    g2.setColor(Color.ORANGE);
	    Point nodePoint = converter.nodeToXY(locationNode);
	    g2.fillOval(((int)nodePoint.getX()-5), (int)(nodePoint.getY()-5), (int)(10), (int)(10));
	}
	if(hoverNode!=null){
	    Point p = converter.nodeToXY(hoverNode);
	    g2.setColor(Color.GREEN);
	    g2.fillOval((int)(p.getX()-5*converter.getZoom()/3.5),(int)(p.getY()-5*converter.getZoom()/3.5),(int)(10*converter.getZoom()/3.5),(int)(10*converter.getZoom()/3.5));
	}
	for(Node n : data.getNamedNodes()){
	    Point point = converter.nodeToXY(n);
	    g2.drawImage(namedPoint, (int)point.getX()-5, (int)point.getY()-6, null);
	}
	if(startNode != null){
	    Point point = converter.nodeToXY(startNode);
	    if(driveStartNode != null && !driveStartNode.equals(startNode) && directions != null && directions.get(0).equals(driveStartNode)){
		g2.setColor(DIRECT);
		g2.setStroke(new BasicStroke((float)(3*converter.getZoom()/3.5), BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0,
			new float[] {(float)(9*converter.getZoom()/3.5)}, 0));
		Point point2 = converter.nodeToXY(driveStartNode);
		g2.drawLine((int)point.getX(), (int)point.getY(), (int)point2.getX(), (int)point2.getY());
	    }
	    g2.drawImage(startPin, (int)point.getX()-12, (int)point.getY()-40, null);
	}
	if(endNode != null){
	    Point point = converter.nodeToXY(endNode);
	    if(driveEndNode != null && !driveEndNode.equals(endNode) && directions != null && directions.get(directions.size()-1).equals(driveEndNode)){
		g2.setColor(DIRECT);
		g2.setStroke(new BasicStroke((float)(3*converter.getZoom()/3.5), BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0,
			new float[] {(float)(9*converter.getZoom()/3.5)}, 0));
		Point point2 = converter.nodeToXY(driveEndNode);
		g2.drawLine((int)point.getX(), (int)point.getY(), (int)point2.getX(), (int)point2.getY());
	    }
	    g2.drawImage(endPin, (int)point.getX()-12, (int)point.getY()-40, null);
	}
	if(namedNodeString != null){
	    Point x = converter.nodeToXY(hoverNode);
	    g.setColor(Color.WHITE);
	    g.drawString(namedNodeString, (int)x.getX()+5, (int)x.getY());
	}
    }
}
