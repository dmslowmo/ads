package roadgraph;

import geography.GeographicPoint;

public class GraphUtil {

	public static boolean isSameNode(GeographicPoint v, GeographicPoint w) {
		return ((v.getX() == w.getX()) && (v.getY() == w.getY()));
	}

}
