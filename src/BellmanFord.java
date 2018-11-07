import java.util.ArrayList;

public class BellmanFord {

	/**
	 * Utility class. Don't use.
	 */
	public class BellmanFordException extends Exception {
		private static final long serialVersionUID = -4302041380938489291L;

		public BellmanFordException() {
			super();
		}

		public BellmanFordException(String message) {
			super(message);
		}
	}

	/**
	 * Custom exception class for BellmanFord algorithm
	 * 
	 * Use this to specify a negative cycle has been found
	 */
	public class NegativeWeightException extends BellmanFordException {
		private static final long serialVersionUID = -7144618211100573822L;

		public NegativeWeightException() {
			super();
		}

		public NegativeWeightException(String message) {
			super(message);
		}
	}

	/**
	 * Custom exception class for BellmanFord algorithm
	 *
	 * Use this to specify that a path does not exist
	 */
	public class PathDoesNotExistException extends BellmanFordException {
		private static final long serialVersionUID = 547323414762935276L;

		public PathDoesNotExistException() {
			super();
		}

		public PathDoesNotExistException(String message) {
			super(message);
		}
	}

	private int[] distances = null;
	private int[] predecessors = null;
	private int source;

	BellmanFord(WGraph g, int source) throws BellmanFordException {
		/*
		 * Constructor, input a graph and a source Computes the Bellman Ford
		 * algorithm to populate the attributes distances - at position "n" the
		 * distance of node "n" to the source is kept predecessors - at position
		 * "n" the predecessor of node "n" on the path to the source is kept
		 * source - the source node
		 *
		 * If the node is not reachable from the source, the distance value must
		 * be Integer.MAX_VALUE
		 * 
		 * When throwing an exception, choose an appropriate one from the ones
		 * given above
		 */

		/* YOUR CODE GOES HERE */

		// initialize distances as infinity and predecessors of nodes as nil
		this.distances = new int[g.getNbNodes()];
		this.predecessors = new int[g.getNbNodes()];
		this.source = source;
		for (int i = 0; i < g.getNbNodes(); i++) {
			this.distances[i] = Integer.MAX_VALUE;
			this.predecessors[i] = -1; // should i just do -1?
		}


		// main loop
		this.distances[source] = 0; // distance from source to source is 0
		this.predecessors[source] = 0; // source should not have a predecessor

		for (int i = 0; i < g.getNbNodes() - 1; i++) {
			for (Integer node : getNodes(g)) {
				ArrayList<Integer> adjacentList = getAdjacencyList(node, g.getEdges());
				for (Integer adjacentNode : adjacentList) {
					Edge edge = g.getEdge(node, adjacentNode);
					// ========= UPDATE ===========
					// check if i know the distance of the node i am at
					int previousDistance = this.distances[adjacentNode];
					if (this.distances[node] < Integer.MAX_VALUE) {
						this.distances[adjacentNode] = Math.min(this.distances[adjacentNode],
								this.distances[node] + edge.weight);

						// we do not want to change the predecessor of the source
						// we only want to set the node as the predecessor if it gives new shortest path
						if ((adjacentNode != source) && (previousDistance != this.distances[adjacentNode])) {
							this.predecessors[adjacentNode] = node;
						}
					}
				}
			}
		}
		
		// CHECK IF THERE ARE NEGATIVE WEIGHT CYCLES
		for (Edge edge: g.getEdges()){
			int u = edge.nodes[0];
			int v = edge.nodes[1];
			int weight = edge.weight;
			if (this.distances[u] != Integer.MAX_VALUE && (this.distances[u]) + weight < this.distances[v]){
				throw new NegativeWeightException();
			}
		}
	}

	/*
	 * helper method that returns an arraylist of the nodes in a graph similar
	 * to the one in Ford Fulkerson
	 */
	private static ArrayList<Integer> getNodes(WGraph graph) {
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		for (Edge edge : graph.getEdges()) {
			if (!nodes.contains(edge.nodes[0])) {
				nodes.add(edge.nodes[0]);
			}
			if (!nodes.contains(edge.nodes[1])) {
				nodes.add(edge.nodes[1]);
			}
		}
		return nodes;
	}

	/*
	 * helper method that returns the adjacency list of a node similar to the
	 * one in Ford Fulkerson
	 */
	private static ArrayList<Integer> getAdjacencyList(Integer node, ArrayList<Edge> edges) {
		ArrayList<Integer> adjacencyList = new ArrayList<Integer>();
		for (Edge edge : edges) {
			if ((edge.nodes[0] == node)) {
				adjacencyList.add(edge.nodes[1]);
			}
		}
		return adjacencyList;
	}

	public int[] shortestPath(int destination) throws BellmanFordException {
		/*
		 * Returns the list of nodes along the shortest path from the object
		 * source to the input destination If not path exists an Exception is
		 * thrown Choose appropriate Exception from the ones given
		 */

		/* YOUR CODE GOES HERE (update the return statement as well!) */
		ArrayList<Integer> stack = new ArrayList<Integer>();
		stack = stack(stack, destination, this.predecessors, this.source);

		// if path is found

		if (stack.isEmpty()) {
			throw new PathDoesNotExistException();
		}
		else if ((stack.get(0) == destination) && (stack.get(stack.size() - 1) == this.source)) {
			int[] shortestPath = new int[stack.size()];
			for (int i = 0; i < stack.size(); i++) {
				shortestPath[i] = stack.get(stack.size() - 1 - i);
			}
			return shortestPath;
		}

		return null;
	}

	/*
	 * helper method that returns shortest path as an arraylist starting from
	 * destination
	 */
	private static ArrayList<Integer> stack(ArrayList<Integer> stack, int destination, int[] predecessors, int source)
			throws PathDoesNotExistException {

		stack.add(destination);

		// if there is no path from source to destination
		// return an empty stack
		if ((predecessors[destination] == -1) && (destination != source)) {
			return new ArrayList<Integer>();
		}

		// if i reach destination
		else if (destination == source) {
			return stack;
		} else {
			stack(stack, predecessors[destination], predecessors, source);
		}
		return stack;
	}

	public void printPath(int destination) {
		/*
		 * Print the path in the format s->n1->n2->destination if the path
		 * exists, else catch the Error and prints it
		 */
		try {
			int[] path = this.shortestPath(destination);
			for (int i = 0; i < path.length; i++) {
				int next = path[i];
				if (next == destination) {
					System.out.println(destination);
				} else {
					System.out.print(next + "-->");
				}
			}
		} catch (BellmanFordException e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {

		String file = args[0];
		WGraph g = new WGraph(file);
		try {
			BellmanFord bf = new BellmanFord(g, g.getSource());
			bf.printPath(g.getDestination());
		} catch (BellmanFordException e) {
			System.out.println(e);
		}

	}
}
