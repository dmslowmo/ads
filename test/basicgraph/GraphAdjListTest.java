/**
 * 
 */
package basicgraph;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import util.GraphLoader;

/**
 * @author Daniel
 *
 */
public class GraphAdjListTest {

	public GraphAdjList graphFromFile;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		graphFromFile = new GraphAdjList();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", graphFromFile);
	}

	/**
	 * Test method for {@link basicgraph.GraphAdjList#getVertices()}.
	 */
	@Test
	public void testGetVertices() {
		System.out.println(graphFromFile);
		assertEquals(9, graphFromFile.getVertices().size());
		assertArrayEquals(new Integer[] {0,1,2,3,4,5,6,7,8}, graphFromFile.getVertices().toArray(new Integer[9]));
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
