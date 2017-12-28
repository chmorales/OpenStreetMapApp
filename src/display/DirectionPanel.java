package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;

//import com.starkeffect.highway.GPSDevice;
//import com.starkeffect.highway.GPSEvent;
//import com.starkeffect.highway.GPSListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

import data.MapData;
import data.Node;
import data.RouteHandler;
import math.Graph;

/**
 * A DirectionPanel can hold one direction and displays information based off it
 * @author cpm02_000
 *
 */
public class DirectionPanel extends JPanel {
// 'implements GPSListener removed'
    private MapPanel mapPanel;
    private RouteHandler route;
    private Graph graph;
    private Node location;
    private DefaultListModel<String> listModel;
    private JScrollPane directPane;

    /**
     * Creates a new DirectionPanel
     * @param map The Map Panel to be paired with the Direction Panel
     * @param graph The Graph object used to find shortest paths
     */
    public DirectionPanel(MapPanel map, Graph graph){
    // 'GPSDevice gps' arg removed
	this.mapPanel = map;
	this.graph = graph;
	route = new RouteHandler();
	setLayout(new BorderLayout());
	JPanel buttonBar = new JPanel();
	buttonBar.setPreferredSize(new Dimension(350, 50));
	buttonBar.setLayout(new GridLayout(1,0));
	setPreferredSize(new Dimension(350,900));
	setBorder(BorderFactory.createLineBorder(Color.BLACK));
	listModel = new DefaultListModel<String>();

	JButton directButton = new JButton("Get Directions");
	directButton.addActionListener(new ActionListener(){
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if(map.getStartNode() == null || map.getEndNode() == null){
		    JOptionPane.showMessageDialog(mapPanel, "Please select a start (green) and end (red) point");
		    return;
		}
		List<Node> path = graph.shortestPath(map.getStartNode(), map.getEndNode());
		if(path == null){
		    JOptionPane.showMessageDialog(mapPanel, "No direcitons found!");
		    return;
		}
		listModel.clear();
		DecimalFormat df = new DecimalFormat("#.##");
		listModel.addElement("Total Distance: "+df.format(graph.getLastDistance())+" miles");
		for(String s : getDirectStrings(path))
		    listModel.addElement(s);
		route.setRoute(path);
		mapPanel.setDirections(path);
	    }
	});

	JButton clearDirections = new JButton("Clear Directions");
	clearDirections.addActionListener(new ActionListener(){
	    @Override
	    public void actionPerformed(ActionEvent e) {
		route.setRoute(null);
		mapPanel.setDirections(null);
		listModel.clear();
	    }
	});
	
	directPane = new JScrollPane(new JList<String>(listModel));
	buttonBar.add(directButton);
	buttonBar.add(clearDirections);
	add(buttonBar, BorderLayout.NORTH);
	add(directPane, BorderLayout.CENTER);
//	gps.addGPSListener(this);
    }

    private String[] getDirectStrings(List<Node> path){
	if(path == null)
	    return new String[0];
	String[] dirArray = new String[path.size()-1];
	for(int i = 1; i < path.size(); i++){
	    String dirString = "Segment "+i+": Node "+path.get(i-1).getID()+" to Node "+path.get(i).getID();
	    dirArray[i-1] = dirString;
	}
	return dirArray;
    }

//    @Override
//    public void processEvent(GPSEvent e) {
//	location = new Node("location", e.getLatitude(), e.getLongitude());
//	if(route.routeSet()){
//	    mapPanel.setLocationNode(location);
//	    if(route.isOffCourse(location)){
//		List<Node> path = graph.shortestPath(mapPanel.getLocationNode(), mapPanel.getEndNode());
//		if(path == null){
//		    JOptionPane.showMessageDialog(mapPanel, "No directions found!");
//		}
//		route.setRoute(path);
//		mapPanel.setDirections(path);
//	    }
//	}else
//	    mapPanel.setLocationNode(location);
//    }
}
