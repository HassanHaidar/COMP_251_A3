import java.io.*;
import java.util.*;




/**
 * Done with consultance of TAs: Searra Chen
 * @author Hassan
 *
 */





public class FordFulkerson {

	
	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
		ArrayList<Integer> Stack = new ArrayList<Integer>();
		//=============== YOUR CODE GOES HERE===============
		
		// initialize all nodes as neither visited nor discovered
		// index of the array is the node number in the graph
		// i.e discovered[1] is the discovery time for node 2
		int[] discovered = new int[graph.getNbNodes()];
		int[] finished = new int[graph.getNbNodes()]; 
		int[] pi = new int[graph.getNbNodes()];
		
				
		ArrayList<Integer> nodesInGraph = getNodes(graph);
		
		int time = 0;
		
		for (Integer node: nodesInGraph){ 
			
			// if origin node of edge is not discovered
			if (discovered[node] == 0){	
				DFSVisit(node, graph, time, discovered, finished, pi, Stack);
			}
			
			// if depthFirst found path from source to destination, return
			if (Stack.get(Stack.size()-1) == graph.getDestination()  && Stack.get(0) == graph.getSource() ){ 
				return Stack;
			}
		}
		
		if (Stack.get(Stack.size()-1) != graph.getDestination() ){
			return new ArrayList<Integer>();
		}
		//===================================================
		return Stack;
	}
	
	/*
	 * helper method that acts as DFS-Visit(u)
	 * see Lecture 8 slide 27 for pseudo-code
	 */
	private static void DFSVisit(Integer node, WGraph graph, int time, int[] discovered,int[] finished, int[] pi, ArrayList<Integer> Stack){
		time++;
		discovered[node] = time; // discover
		Stack.add(node);
				
		for (Integer adjacentNode: getAdjacencyList(node, graph.getEdges())){
			if ((discovered[adjacentNode] == 0) && (graph.getEdge(node, adjacentNode).weight > 0)){
				pi[adjacentNode] = node;
				DFSVisit(adjacentNode, graph, time, discovered, finished, pi, Stack);
			}
		}
		time++;
		finished[node] = time;		
	}
	/*
	 * helper method to get nodes of a the graph
	 */
	private static ArrayList<Integer> getNodes(WGraph graph){
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		for (Edge edge: graph.getEdges()){
			if (!nodes.contains(edge.nodes[0])){
				nodes.add(edge.nodes[0]);
			}
			if (!nodes.contains(edge.nodes[1])){
				nodes.add(edge.nodes[1]);
			}
		}
		return nodes;
	}
	
	/*
	 * helper method that returns adjacency list of a node
	 */
	private static ArrayList<Integer> getAdjacencyList(Integer node, ArrayList<Edge> edges){
		ArrayList<Integer> adjacencyList = new ArrayList<Integer>();
		for (Edge edge: edges){
			if ((edge.nodes[0] == node)){
				adjacencyList.add(edge.nodes[1]);
			}
		}
		return adjacencyList;
	}
	
	
	public static void fordfulkerson(Integer source, Integer destination, WGraph graph, String filePath){
		String answer="";
		String myMcGillID = "260711061"; //Please initialize this variable with your McGill ID
		int maxFlow = 0;
		
		// ================== YOUR CODE GOES HERE ===================
		
		// initialize flow graph that stores flow values for edges
		WGraph graphF = new WGraph(graph); 
		
		// treat weight in flow graph flow
		// set it initially to zero
		for (Edge edge: graphF.getEdges()){
			edge.weight = 0;
		}		
		
		// =============================== RUN NAIVE ALGORITHM =====================================
		
		while (true){
			ArrayList<Integer> path = pathDFS(source,destination,graph);
			ArrayList<Edge> edgesInPath = getEdgesInPath(path, graph);
			
			
			int[] flowPerEdge = new int[edgesInPath.size()]; // initialize all flows through edges in path as 0
			if (!path.isEmpty() && isFlowLessThanCapacity(edgesInPath, flowPerEdge)){
				
				int bottleneck = getBottleneck(edgesInPath);
				
				// update flow in flow graph
				for (Edge edge: edgesInPath){
					graphF.getEdge(edge.nodes[0], edge.nodes[1]).weight += bottleneck; // get the equivalent edge in graphF and update the flow	
				}
			}
			else {
				break;
			}
		}
		
		// ===========================================================================================
		
		
		// ===============================FORD FULKERSON ALGORITHM==================================
		//  =============================== create graph ===============================
		
//		WGraph graphR = new WGraph(); // initialize residual graph as empty
//		
//		ArrayList<Edge> forwardEdges = new ArrayList<Edge>();
//		ArrayList<Edge> backwardEdges = new ArrayList<Edge>();
//		
//		for (Edge edge: graph.getEdges()){
//			int flow = graphF.getEdge(edge.nodes[0], edge.nodes[1]).weight;
//			int capacity = edge.weight;
//			if ( flow < capacity){
//				Edge newEdge = new Edge(edge.nodes[0], edge.nodes[1], capacity - flow);
//				graphR.addEdge(newEdge);
//				forwardEdges.add(newEdge);
//			}
//			
//			if (flow > 0){
//				Edge newEdge = new Edge(edge.nodes[1], edge.nodes[0],flow);
//				graphR.addEdge(newEdge);
//				backwardEdges.add(newEdge);
//				
//			}
//		}	
		// =======================================================================================
		
		
		
		while (true){
			// find path in residual graph
			
			
			WGraph graphR = new WGraph(); // initialize residual graph as empty
			
			ArrayList<Edge> forwardEdges = new ArrayList<Edge>();
			ArrayList<Edge> backwardEdges = new ArrayList<Edge>();
			
			for (Edge edge: graph.getEdges()){
				int flow = graphF.getEdge(edge.nodes[0], edge.nodes[1]).weight;
				int capacity = edge.weight;
				if ( flow < capacity){
					Edge newEdge = new Edge(edge.nodes[0], edge.nodes[1], capacity - flow);
					graphR.addEdge(newEdge);
					forwardEdges.add(newEdge);
				}
				
				if (flow > 0){
					Edge newEdge = new Edge(edge.nodes[1], edge.nodes[0],flow);
					graphR.addEdge(newEdge);
					backwardEdges.add(newEdge);
					
				}
			}	
			
			
			ArrayList<Integer> pathR = pathDFS(graphF.getSource(), graphF.getDestination(), graphR);
			ArrayList<Edge> edgesInPathR = getEdgesInPath(pathR, graphR);
			// if there is no path in the loop, break
			if (pathR.isEmpty()){
				break;
			}
			else{
				// augment path
				int bottleneck = getBottleneck(edgesInPathR);
				
				for (Edge edge : edgesInPathR){
					if (forwardEdges.contains(edge)){
						graphF.getEdge(edge.nodes[0],edge.nodes[1]).weight += bottleneck;
					}
					else{
						graphF.getEdge(edge.nodes[1],edge.nodes[0]).weight -= bottleneck;
					}
				}
			}
		}
		
		// calculate max flow
		for (Edge edge: graphF.getEdges()){
			if (edge.nodes[0] == graphF.getSource()){
				maxFlow += edge.weight;
			}
		}
		
		
		answer += maxFlow + "\n" + graph.toString();	
		writeAnswer(filePath+myMcGillID+".txt",answer);
		System.out.println(answer);
	}
	
	/*
	 * helper method that gets the edges in a path
	 */
	private static ArrayList<Edge> getEdgesInPath(ArrayList<Integer> path, WGraph graph){
		ArrayList<Edge> edgesInPath = new ArrayList<Edge>();
		for (int i = 0; i < path.size() - 1; i++){
			edgesInPath.add(graph.getEdge(path.get(i), path.get(i+1)));
		}
		return edgesInPath;
	}
	
	/*
	 * helper method that checks if every flow through the path is less than the capacity
	 */
	private static boolean isFlowLessThanCapacity(ArrayList<Edge> edgesInPath, int[] flowPerEdge){
		for (Edge edge: edgesInPath){
			if (flowPerEdge[edgesInPath.indexOf(edge)] >= edge.weight){
				return false;
			}
		}
		return true;
		
	}
	
	/*
	 * helper method that returns the bottleneck of a path
	 */
	
	private static int getBottleneck(ArrayList<Edge> edgesInPath){
		// find the bottleneck among path;
		int bottleneck = edgesInPath.get(0).weight;
		for (Edge edge: edgesInPath){
			bottleneck = Math.min(bottleneck, edge.weight);
		}
		return bottleneck;
	}
	
	
	
	public static void writeAnswer(String path, String line){
		BufferedReader br = null;
		File file = new File(path);
		// if file doesnt exists, then create it
		
		try {
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(line+"\n");	
		bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	 public static void main(String[] args){
		 String file = args[0];
		 File f = new File(file);
		 WGraph g = new WGraph(file);
		 fordfulkerson(g.getSource(),g.getDestination(),g,f.getAbsolutePath().replace(".txt",""));
	 }
}
