package roadgraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import geography.GeographicPoint;

public class GraphAdjList {
	private Map<GeographicPoint, List<GeographicPoint>> adjListMap;
	private int numOfEdges = 0;

	public GraphAdjList() {
		this.adjListMap = new HashMap<GeographicPoint, List<GeographicPoint>>();
	}

	public int getNumVertices() {
		return this.adjListMap.size();
	}

	public Set<GeographicPoint> getVertices() {
		return this.adjListMap.keySet();
	}

	public int getNumOfEdges() {
		return numOfEdges;
	}

	/** 
	 * Count the number of edges per node in the map and return it as list.
	 * 
	 * @return edge counts of all nodes
	 */
	public Map<GeographicPoint, Integer> getEdgeCounts() {
		Map<GeographicPoint, Integer> edgeCounts = new HashMap<>();
		for (GeographicPoint i : getVertices()) {
			List<GeographicPoint> inNeighbours = getInNeighbors(i);
			//count the in-degrees
			for (GeographicPoint in : inNeighbours) {
				updateEdgeCounts(edgeCounts, in);
			}
		}

		return edgeCounts;
	}

	private void updateEdgeCounts(Map<GeographicPoint, Integer> edgeCounts, GeographicPoint in) {
		if (edgeCounts.containsKey(in)) {
			edgeCounts.put(in, edgeCounts.get(in) + 1);
		} else {
			edgeCounts.put(in, 1);
		}
	}

	/** 
	 * Implement the abstract method for finding all 
	 * out-neighbors of a vertex.
	 * If there are multiple edges between the vertex
	 * and one of its out-neighbors, this neighbor
	 * appears once in the list for each of these edges.
	 * 
	 * @param v the index of vertex.
	 * @return List<GeographicPoint> a list of indices of vertices.  
	 */	
	public List<GeographicPoint> getNeighbors(GeographicPoint v) {
		return new ArrayList<GeographicPoint>(this.adjListMap.get(v));
	}

	/** 
	 * Implement the abstract method for finding all 
	 * in-neighbors of a vertex.
	 * If there are multiple edges from another vertex
	 * to this one, the neighbor
	 * appears once in the list for each of these edges.
	 * 
	 * @param v the index of vertex.
	 * @return List<GeographicPoint> a list of indices of vertices.  
	 */	
	public List<GeographicPoint> getInNeighbors(GeographicPoint v) {
		List<GeographicPoint> inNeighbors = new ArrayList<>();
		for (GeographicPoint u : this.adjListMap.keySet()) {
			//iterate through all edges in u's adjacency list and 
			//add u to the inNeighbor list of v whenever an edge
			//with startpoint u has endpoint v.
			for (GeographicPoint w : this.adjListMap.get(u)) {
				if (GraphUtil.isSameNode(v, w)) {
					inNeighbors.add(u);
				}
			}
		}
		return inNeighbors;
	}

	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location) {
		if (location == null || this.adjListMap.containsKey(location)) return false;
		this.adjListMap.put(location, new ArrayList<GeographicPoint>());
		return true;
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
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName, String roadType, double length) {
		if (this.adjListMap.containsKey(from) && this.adjListMap.containsKey(to)) {
			this.adjListMap.get(from).add(to);
			numOfEdges++;
		}
	}

	/**
	 * Check that a vertex is in the map.
	 * @param v
	 * @return true if exists, else false
	 */
	public boolean contains(GeographicPoint v) {
		return this.adjListMap.containsKey(v);
	}
}