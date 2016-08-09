package basicgraph;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import util.GraphLoader;

public class GraphAdjMatrixTest {

	public GraphAdjMatrix graphFromFile;

	@Before
	public void setUp() throws Exception {
		graphFromFile = new GraphAdjMatrix();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", graphFromFile);
	}

	@Test
	public void testGetVertices() {
		System.out.println(graphFromFile);
		assertEquals(10, graphFromFile.getVertices().size()); // extra row and col for????
		System.out.println(graphFromFile.getVertices());
	}

	@Test
	public void testGetDistance2() {
		for (Integer v : graphFromFile.getVertices()) {
			System.out.println(graphFromFile.getDistance2(v));
		}
		/*
		  	[2, 3, 6, 7, 0]
			[1, 4, 6, 1, 8, 1, 3, 5, 1, 1]
			[2, 3, 6, 7, 0]
			[5, 3, 8, 1, 3, 2, 3, 6, 7, 0]
			[4, 7, 8, 4, 6, 1]
			[5, 3, 5, 1, 5, 6]
			[5, 6, 2, 3, 6, 7, 0, 4, 6, 1]
			[4, 7, 8, 2, 3, 6, 7, 0]
			[4, 7, 8, 8, 1, 3]
		 */
	}
}
