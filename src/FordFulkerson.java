import java.io.*;
import java.util.*;




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
			if (Stack.get(Stack.size()-1) == graph.getDestination() ){ 
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
			if (discovered[adjacentNode] == 0){
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
		
		
		
		
		
		//===========================================================
		
		
		ArrayList path = pathDFS(source,destination,graph);
		
		answer += maxFlow + "\n" + graph.toString();	
		writeAnswer(filePath+myMcGillID+".txt",answer);
		System.out.println(answer);
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
