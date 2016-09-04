/**
 * 
 */
package roadgraph;

import java.util.ArrayList;
import java.util.List;

import geography.GeographicPoint;

/**
 * @author Daniel
 *
 */
public class MapNode {

	private GeographicPoint location;
	private List<MapEdge> neighbours;

	public MapNode(GeographicPoint gp) {
		this.location = gp;
		this.neighbours = new ArrayList<MapEdge>();
	}

	public GeographicPoint getLocation() {
		return location;
	}

	public List<MapEdge> getEdges() {
		return new ArrayList<MapEdge>(this.neighbours);
	}

	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName, String roadType, double length) {
		this.neighbours.add(new MapEdge(from, to, roadName, roadType, length));
	}
}
