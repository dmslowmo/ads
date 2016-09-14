/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	private int numEdges = 0;
	private List<MapNode> nodes;
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		nodes = new ArrayList<MapNode>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		return nodes.size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		Set<GeographicPoint> vertices = new HashSet<>();
		for (MapNode n : nodes) {
			vertices.add(n.getLocation());
		}
		return vertices;
	}

	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		return numEdges;
	}

	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		for (MapNode n : nodes) {
			if(isSameLocation(location, n.getLocation())) return false;
		}
		MapNode node = new MapNode(location);
		nodes.add(node);
		return true;
	}

	private boolean isSameLocation(GeographicPoint a, GeographicPoint b) {
		return (a.getX() == b.getX()) && (a.getY() == b.getY());
	}

	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {
		boolean added = false;
		added = addEdgeFrom(from, to, roadName, roadType, length, added);
		if (!added) {
			added = addEdgeTo(from, to, roadName, roadType, length, added);
			if (!added) return; // should not happen, but just in case
		}
		++numEdges;
	}

	private boolean addEdgeTo(GeographicPoint from, GeographicPoint to, String roadName, String roadType, double length,
			boolean added) {
		for (MapNode node : nodes) {
			if (isSameLocation(to, node.getLocation())) {
				node.addEdge(from, to, roadName, roadType, length);
				added = true;
			}
		}
		return added;
	}

	private boolean addEdgeFrom(GeographicPoint from, GeographicPoint to, String roadName, String roadType,
			double length, boolean added) {
		for (MapNode node : nodes) {
			if (isSameLocation(from, node.getLocation())) {
				node.addEdge(from, to, roadName, roadType, length);
				added = true;
			}
		}
		return added;
	}
	

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal)
	{
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		boolean found = false;
		for (MapNode node : nodes) {
			if (isSameLocation(node.getLocation(), start)) {
				found = true;
				break;
			}
		}
		if (!found) return null;

		Queue<GeographicPoint> next = new ArrayDeque<>();
		Set<GeographicPoint> visited = new HashSet<>();
		Map<GeographicPoint, GeographicPoint> parentMap = new HashMap<>();

		next.add(start);
		visited.add(start);
		while (!next.isEmpty()) {
			GeographicPoint current = next.remove();
			if (isSameLocation(current, goal)) {
				return reconstructPath(start, current, parentMap);
			}
			//get neighbours of current node
			List<MapEdge> edges = getEdges(current);
			for (MapEdge edge : edges) {
				if (visited.contains(edge.getTo())) {
					continue;
				}
				visited.add(edge.getTo());
				parentMap.put(edge.getTo(), current);
				next.add(edge.getTo());
			}
		}

		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
		return null;
	}

	private List<MapEdge> getEdges(GeographicPoint current) {
		MapNode currNode = null;
		for (MapNode node : nodes) {
			if (isSameLocation(node.getLocation(), current)) {
				currNode = node;
				break;
			}
		}
		List<MapEdge> edges = currNode.getEdges();
		return edges;
	}

	/**
	 * Searching the starting node, beginning from the current node, and return the path.
	 * @param start
	 * @param current
	 * @param parentMap
	 * @return path
	 */
	private List<GeographicPoint> reconstructPath(GeographicPoint start, GeographicPoint current,
			Map<GeographicPoint, GeographicPoint> parentMap) {
		List<GeographicPoint> path = new ArrayList<>();
		GeographicPoint currv = current;
		while (!isSameLocation(currv, start)) {
			path.add(currv);
			currv = parentMap.get(currv);
		}
		path.add(start);
		Collections.reverse(path);
		return path;
	}

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 3
		Queue<Route> pq = new PriorityQueue<>();
		Set<GeographicPoint> visited = new HashSet<>();
		Map<GeographicPoint, GeographicPoint> parentMap = new HashMap<>();

		Map<GeographicPoint, Route> routeMap = new HashMap<>();
		routeMap.put(start, new Route(start, start, 0.0));
		pq.add(routeMap.get(start));

		Set<GeographicPoint> vertices = getVertices();
		for (GeographicPoint v : vertices) {
			routeMap.put(v, new Route(start, v));
		}

		while (!pq.isEmpty()) {
			GeographicPoint curr = pq.remove().getLocation();

			if (visited.contains(curr)) continue;

			visited.add(curr);
			if (GraphUtil.isSameNode(curr, goal)) return reconstructPath(start, curr, parentMap);
			//get neighbours of current node
			List<MapEdge> edges = getEdges(curr);
			for (MapEdge edge : edges) {
				if (visited.contains(edge.getTo())) {
					continue;
				}
				Route n = routeMap.get(edge.getTo());
				double distance = routeMap.get(curr).getDistanceFromOrigin() + edge.getLength();
				if (distance < n.getDistanceFromOrigin()) {
					n.setDistanceFromOrigin(distance);
					routeMap.put(n.getLocation(), n);
				}
				parentMap.put(n.getLocation(), curr);
				pq.add(n);
			}
		}

		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
		
		return null;
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 3
		
		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
		
		return null;
	}

	
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		System.out.println("DONE.");
		
		// You can use this method for testing.  
		
		
		/* Here are some test cases you should try before you attempt 
		 * the Week 3 End of Week Quiz, EVEN IF you score 100% on the 
		 * programming assignment.
		 */
		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		System.out.println("Dijkstra path: " + testroute);
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		/* Use this code in Week 3 End of Week Quiz */
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		
	}
	
}
