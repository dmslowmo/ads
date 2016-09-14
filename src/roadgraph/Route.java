/**
 * 
 */
package roadgraph;

import geography.GeographicPoint;

/**
 * This class represents the route from the origin to current location.
 * 
 * @author Daniel
 *
 */
public class Route implements Comparable<Route> {

	private GeographicPoint origin;
	private GeographicPoint location;
	private double distanceFromOrigin = Double.POSITIVE_INFINITY;

	public Route(GeographicPoint orig, GeographicPoint curr) {
		origin = orig;
		location = curr;
	}

	public Route(GeographicPoint orig, GeographicPoint curr, double dist) {
		origin = orig;
		location = curr;
		distanceFromOrigin = dist;
	}

	public GeographicPoint getOrigin() {
		return origin;
	}

	public GeographicPoint getLocation() {
		return location;
	}

	public double getDistanceFromOrigin() {
		return distanceFromOrigin;
	}

	public void setDistanceFromOrigin(double d) {
		distanceFromOrigin = d;
	}

	@Override
	public int compareTo(Route route) {
		// for ascending order
		return (int)(distanceFromOrigin - route.getDistanceFromOrigin());
	}
}
