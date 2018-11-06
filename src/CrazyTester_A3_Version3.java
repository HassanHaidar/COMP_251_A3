/*
CRAZY TESTER -- ASSIGNMENT 3 VERSION 3
(If Version 1 worked for you then you don't need to test Version 3)
(Version 2 had a small problem)

When you run this file, compare your terminal/console output with the EXPECTED OUTPUT below.
You can use text-compare.com or diffchecker.com to compare huge blocks of text.

There's a good chance that my testers/outputs have bugs,
so if you do spot one, even if you aren't sure, let me know! (jeffery.tang@mail.mcgill.ca or pm me on fb)

There are some assumptions I've made about the graph while creating this tester:
-no self-loops
-there is at least one edge in the graph (the graph is not empty)
-there are no zero-weight edges (assignment pdf confirms this)

Finally, make sure your code works on the examples given to you by the assignment, those are canon!

EXPECTED OUTPUT

+--------------------+
PART 1: FORD-FULKERSON
+--------------------+
I only tested fordFulkerson(), cuz if it works,
it probably means that your pathDFS() works too.
Just make sure your Stack array in pathDFS() is in increasing order.

This tester modifies your ff2[Student_ID].txt.

Your output doesn't have to completely match mines.
However, the first line of each paragraph (maxFlow) has to match mine.

-----------------
Test 1: Base Case
-----------------
2
0 0
2
0 1 2

-----------------
Test 2: Base Case
-----------------
0
0 0
2
0 1 0

-----------------
Test 3: Base Case
-----------------
0
0 0
2
1 0 0

-----------------
Test 4: Base Case
-----------------
5
0 0
2
1 0 5

-----------------
Test 5: Base Case
-----------------
2
0 0
2
0 1 2
1 0 0

-----------------
Test 6: Base Case
-----------------
5
0 0
2
0 1 0
1 0 5

------------------------------------
Test 7: One Path (Going All the Way)
------------------------------------
10
0 0
8
0 1 10
1 2 10
2 3 10
3 4 10
4 5 10
5 6 10
6 7 10

----------------------------------------
Test 8: One Path (Going Part of the Way)
----------------------------------------
10
0 0
8
0 1 0
1 2 10
2 3 10
3 4 10
4 5 10
5 6 10
6 7 0

-----------------------------------------------
Test 9: Easy Multiple Paths (Going All the Way)
-----------------------------------------------
50
0 0
7
0 1 10
0 2 10
0 3 10
0 4 10
0 5 10
1 6 10
2 6 10
3 6 10
4 6 10
5 6 10

----------------------------------------------------
Test 10: Easy Multiple Paths (Going Part of the Way)
----------------------------------------------------
10
0 0
7
0 1 0
0 2 0
0 3 0
0 4 0
0 5 0
1 6 10
2 6 0
3 6 0
4 6 0
5 6 0

---------------------------------------------------
Test 11: Random "Just Putting It Out There" Example
---------------------------------------------------
1000
0 0
5
0 1 1000
0 2 0
1 2 0
1 4 1000
3 4 0

----------------------------
Test 12: Wikipedia's Example
----------------------------
2000
0 0
4
0 1 1000
0 2 1000
1 2 0
1 3 1000
2 3 1000

--------------------------------------------------------------------------------------
Test 13: Tricky Example With Three Outgoing Edges From Source
Thanks to https://medium.com/100-days-of-algorithms/day-49-ford-fulkerson-e70045dafd8b
--------------------------------------------------------------------------------------
15
0 0
8
0 1 4
0 2 5
0 3 6
1 4 4
2 4 3
2 5 1
2 6 1
3 5 5
3 6 1
4 7 7
5 7 6
6 7 2

+------------------+
PART 2: BELLMAN-FORD
+------------------+
I only test shortestPath(), cuz if that works, it probably means your BellmanFord() works too.
But make sure that for unreachable vertices v, distance[v] = Integer.MAX_VALUE

If your error exception message is different from mine, that's completely ok.
What matters is the error type, e.g. BellmanFord$PathDoesNotExistException

-----------------
Test 1: Base Case
-----------------
0
0-->1

-----------------
Test 2: Base Case
-----------------
0
0-->1

---------------------------------------------
Test 3: Base Case (PathDoesNotExistException)
---------------------------------------------
0
BellmanFord$PathDoesNotExistException: A path does not exist from the source of this graph to the destination that you specified.

------------------------------------------------
Test 4: Base Case (NegativeWeightCycleException)
------------------------------------------------
BellmanFord$NegativeWeightException: There is a negative weight cycle in this graph.

-------------------------------------------------
Test 5: Small Case (NegativeWeightCycleException)
-------------------------------------------------
BellmanFord$NegativeWeightException: There is a negative weight cycle in this graph.

------------------
Test 6: Small Case
------------------
0
0-->1
0-->1-->2

--------------------------------------------------------------------------------
Test 7: Slides Example 1
https://www.cs.mcgill.ca/~jeromew/teaching/251/F2018/COMP251_Lecture11_F2018.pdf
Page 4-6
0-->1-->2-->4 is also valid.
So is 0-->1-->3-->4
And 0-->1-->2-->3-->4
--------------------------------------------------------------------------------
0-->2-->4

--------------------------------------------------------------------------------
Test 8: Slides Example 2
https://www.cs.mcgill.ca/~jeromew/teaching/251/F2018/COMP251_Lecture11_F2018.pdf
Page 36-42
--------------------------------------------------------------------------------
0-->2-->1-->3-->4

--------------------------------
Test 9: Multiple Paths Easy Case
--------------------------------
0-->5-->6

------------------------------------------------------------------------------------
Test 10: Multiple Paths Hard Case
http://www.cis.jhu.edu/education/introPatternTheory/additional/dynamic/dynamic2.html
(The website is wrong about the graph's shortest path btw, lol)
(So trust my output below) (Good work Alex Wang)
(And 0-->3-->5-->8-->9 also works) (Nice one Hsig Li)
------------------------------------------------------------------------------------
0-->3-->4-->7-->9

-----------------------------------------------------------------------
Test 11: Wikipedia's Example
This is a undirected graph, so I'm not sure if we'll be tested on this.
If it doesn't work for you, maybe there's no harm in ignoring it.
-----------------------------------------------------------------------
0-->4-->3-->5
5-->3-->4-->0

*/

import java.io.*;

public class CrazyTester_A3_Version3 {
  static WGraph graph = new WGraph();

  public static void P(String txt) {
    System.out.println(txt);
  }

  public static void H1(String header) {
    String border = "+";
    for (int i = 0; i < header.length() - 2; i++) {
      border += "-";
    }
    border += "+";   
    P(border);
    P(header);
    P(border);
  }

  public static void H2(String header) {
    String border = "";
    for (int i = 0; i < header.length(); i++) {
      border += "-";
    }   
    P(border);
    P(header);
    P(border);
  }


  public static void FF(int s, int t) {
    FordFulkerson.fordfulkerson(s, t, graph, new File("ff2.txt").getAbsolutePath().replace(".txt",""));
    P("");
  }

  public static void main(String[] args) {
    P("");
    H1("PART 1: FORD-FULKERSON");
    P("I only tested fordFulkerson(), cuz if it works,");
    P("it probably means that your pathDFS() works too.");
    P("Just make sure your Stack array in pathDFS() is in increasing order.\n");
    P("This tester modifies your ff2[Student_ID].txt.\n");
    P("Your output doesn't have to completely match mines.");
    P("However, the first line of each paragraph (maxFlow) has to match mine.\n");

    H2("Test 1: Base Case");
    graph.addEdge(new Edge(0, 1, 2));
    FF(0, 1);

    H2("Test 2: Base Case");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 2));
    FF(1, 0);
    
    H2("Test 3: Base Case");
    graph = new WGraph();
    graph.addEdge(new Edge(1, 0, 5));
    FF(0, 1);

    H2("Test 4: Base Case");
    graph = new WGraph();
    graph.addEdge(new Edge(1, 0, 5));
    FF(1, 0);
    
    H2("Test 5: Base Case");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 2));
    graph.addEdge(new Edge(1, 0, 5));
    FF(0, 1);

    H2("Test 6: Base Case");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 2));
    graph.addEdge(new Edge(1, 0, 5));
    FF(1, 0);

    H2("Test 7: One Path (Going All the Way)");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 10));
    graph.addEdge(new Edge(1, 2, 10));
    graph.addEdge(new Edge(2, 3, 10));
    graph.addEdge(new Edge(3, 4, 10));
    graph.addEdge(new Edge(4, 5, 10));
    graph.addEdge(new Edge(5, 6, 10));
    graph.addEdge(new Edge(6, 7, 10));
    FF(0, 7);
    
    H2("Test 8: One Path (Going Part of the Way)");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 10));
    graph.addEdge(new Edge(1, 2, 10));
    graph.addEdge(new Edge(2, 3, 10));
    graph.addEdge(new Edge(3, 4, 10));
    graph.addEdge(new Edge(4, 5, 10));
    graph.addEdge(new Edge(5, 6, 10));
    graph.addEdge(new Edge(6, 7, 10));
    FF(1, 6);

    H2("Test 9: Easy Multiple Paths (Going All the Way)");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 10));
    graph.addEdge(new Edge(0, 2, 10));
    graph.addEdge(new Edge(0, 3, 10));
    graph.addEdge(new Edge(0, 4, 10));
    graph.addEdge(new Edge(0, 5, 10));
    graph.addEdge(new Edge(1, 6, 10));
    graph.addEdge(new Edge(2, 6, 10));
    graph.addEdge(new Edge(3, 6, 10));
    graph.addEdge(new Edge(4, 6, 10));
    graph.addEdge(new Edge(5, 6, 10));
    FF(0, 6);

    H2("Test 10: Easy Multiple Paths (Going Part of the Way)");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 10));
    graph.addEdge(new Edge(0, 2, 10));
    graph.addEdge(new Edge(0, 3, 10));
    graph.addEdge(new Edge(0, 4, 10));
    graph.addEdge(new Edge(0, 5, 10));
    graph.addEdge(new Edge(1, 6, 10));
    graph.addEdge(new Edge(2, 6, 10));
    graph.addEdge(new Edge(3, 6, 10));
    graph.addEdge(new Edge(4, 6, 10));
    graph.addEdge(new Edge(5, 6, 10));
    FF(1, 6);

    H2("Test 11: Random \"Just Putting It Out There\" Example");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 1000));
    graph.addEdge(new Edge(0, 2, 1000));
    graph.addEdge(new Edge(1, 2, 1));
    graph.addEdge(new Edge(1, 4, 1000));
    graph.addEdge(new Edge(3, 4, 1000));
    FF(0, 4);

    H2("Test 12: Wikipedia's Example");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 1000));
    graph.addEdge(new Edge(0, 2, 1000));
    graph.addEdge(new Edge(1, 2, 1));
    graph.addEdge(new Edge(1, 3, 1000));
    graph.addEdge(new Edge(2, 3, 1000));
    FF(0, 3);
    
    P("--------------------------------------------------------------------------------------");
    P("Test 13: Tricky Example With Three Outgoing Edges From Source");
    P("Thanks to https://medium.com/100-days-of-algorithms/day-49-ford-fulkerson-e70045dafd8b");
    P("--------------------------------------------------------------------------------------");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 4));
    graph.addEdge(new Edge(0, 2, 5));
    graph.addEdge(new Edge(0, 3, 7));
    graph.addEdge(new Edge(1, 4, 7));
    graph.addEdge(new Edge(2, 4, 6));
    graph.addEdge(new Edge(2, 5, 5));
    graph.addEdge(new Edge(2, 6, 1));
    graph.addEdge(new Edge(3, 5, 8));
    graph.addEdge(new Edge(3, 6, 1));
    graph.addEdge(new Edge(4, 7, 7));
    graph.addEdge(new Edge(5, 7, 6));
    graph.addEdge(new Edge(6, 7, 4));
    FF(0, 7);

    H1("PART 2: BELLMAN-FORD");
    P("I only test shortestPath(), cuz if that works, it probably means your BellmanFord() works too.");
    P("But make sure that for unreachable vertices v, distance[v] = Integer.MAX_VALUE\n");
    P("If your error exception message is different from mine, that's completely ok.");
    P("What matters is the error type, e.g. BellmanFord$PathDoesNotExistException \n");

    H2("Test 1: Base Case");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 4));
    try {
       BellmanFord bf = new BellmanFord(graph, 0);
       bf.printPath(0);
       bf.printPath(1);
    }
    catch (BellmanFord.BellmanFordException e){
      System.out.println(e);
    }

    P("");
    H2("Test 2: Base Case");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, -4));
    try {
       BellmanFord bf = new BellmanFord(graph, 0);
       bf.printPath(0);
       bf.printPath(1);
    }
    catch (BellmanFord.BellmanFordException e){
      System.out.println(e);
    }

    P("");
    H2("Test 3: Base Case (PathDoesNotExistException)");
    graph = new WGraph();
    graph.addEdge(new Edge(1, 0, 4));
    try {
       BellmanFord bf = new BellmanFord(graph, 0);
       bf.printPath(0);
       bf.printPath(1);
    }
    catch (BellmanFord.BellmanFordException e){
      System.out.println(e);
    }

    P("");
    H2("Test 4: Base Case (NegativeWeightCycleException)");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 3));
    graph.addEdge(new Edge(1, 0, -5));
    try {
      BellmanFord bf = new BellmanFord(graph, 0);
    }
    catch (BellmanFord.BellmanFordException e){
      System.out.println(e);
    }
    
    P("");
    H2("Test 5: Small Case (NegativeWeightCycleException)");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 3));
    graph.addEdge(new Edge(1, 2, -5));
    graph.addEdge(new Edge(2, 0, 1));
    try {
      BellmanFord bf = new BellmanFord(graph, 0);
    }
    catch (BellmanFord.BellmanFordException e){
      System.out.println(e);
    }

    P("");
    H2("Test 6: Small Case");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 4));
    graph.addEdge(new Edge(1, 2, 6));
    graph.addEdge(new Edge(2, 3, 8));
    try {
       BellmanFord bf = new BellmanFord(graph, 0);
       bf.printPath(0);
       bf.printPath(1);
       bf.printPath(2);
    }
    catch (BellmanFord.BellmanFordException e){
      System.out.println(e);
    }

    P("");
    P("--------------------------------------------------------------------------------");
    P("Test 7: Slides Example 1");
    P("https://www.cs.mcgill.ca/~jeromew/teaching/251/F2018/COMP251_Lecture11_F2018.pdf");
    P("Page 4-6");
    P("0-->1-->2-->4 is also valid.");
    P("So is 0-->1-->3-->4");
    P("And 0-->1-->2-->3-->4");
    P("--------------------------------------------------------------------------------");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 3));
    graph.addEdge(new Edge(0, 2, 5));
    graph.addEdge(new Edge(1, 2, 2));
    graph.addEdge(new Edge(1, 3, 6));
    graph.addEdge(new Edge(2, 1, 1));
    graph.addEdge(new Edge(2, 3, 4));
    graph.addEdge(new Edge(2, 4, 6));
    graph.addEdge(new Edge(3, 4, 2));
    graph.addEdge(new Edge(4, 3, 7));
    graph.addEdge(new Edge(4, 0, 3));
    try {
       BellmanFord bf = new BellmanFord(graph, 0);
       bf.printPath(4);
    }
    catch (BellmanFord.BellmanFordException e){
      System.out.println(e);
    }

    P("");
    P("--------------------------------------------------------------------------------");
    P("Test 8: Slides Example 2");
    P("https://www.cs.mcgill.ca/~jeromew/teaching/251/F2018/COMP251_Lecture11_F2018.pdf");
    P("Page 36-42");
    P("--------------------------------------------------------------------------------");

    // Slides Example 2
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 10));
    graph.addEdge(new Edge(0, 2, 5));
    graph.addEdge(new Edge(1, 2, 2));
    graph.addEdge(new Edge(1, 3, 2));
    graph.addEdge(new Edge(2, 1, 1));
    graph.addEdge(new Edge(2, 3, 4));
    graph.addEdge(new Edge(2, 4, 6));
    graph.addEdge(new Edge(3, 4, 2));
    graph.addEdge(new Edge(4, 3, 7));
    graph.addEdge(new Edge(4, 0, 3));
    try {
       BellmanFord bf = new BellmanFord(graph, 0);
       bf.printPath(4);
    }
    catch (BellmanFord.BellmanFordException e){
      System.out.println(e);
    }

    P("");
    H2("Test 9: Multiple Paths Easy Case");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 10));
    graph.addEdge(new Edge(0, 2, 9));
    graph.addEdge(new Edge(0, 3, 8));
    graph.addEdge(new Edge(0, 4, 7));
    graph.addEdge(new Edge(0, 5, 6));
    graph.addEdge(new Edge(1, 6, 5));
    graph.addEdge(new Edge(2, 6, 4));
    graph.addEdge(new Edge(3, 6, 3));
    graph.addEdge(new Edge(4, 6, 2));
    graph.addEdge(new Edge(5, 6, 1));
    try {
       BellmanFord bf = new BellmanFord(graph, 0);
       bf.printPath(6);
    }
    catch (BellmanFord.BellmanFordException e){
      System.out.println(e);
    }

    P("");
    P("------------------------------------------------------------------------------------");
    P("Test 10: Multiple Paths Hard Case");
    P("http://www.cis.jhu.edu/education/introPatternTheory/additional/dynamic/dynamic2.html");
    P("(The website is wrong about the graph's shortest path btw, lol)");
    P("(So trust my output below) (Good work Alex Wang)");
    P("(And 0-->3-->5-->8-->9 also works) (Nice one Hsig Li)");
    P("------------------------------------------------------------------------------------");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 1, 2));
    graph.addEdge(new Edge(0, 2, 4));
    graph.addEdge(new Edge(0, 3, 2));
    graph.addEdge(new Edge(1, 4, 7));
    graph.addEdge(new Edge(1, 5, 4));
    graph.addEdge(new Edge(1, 6, 6));
    graph.addEdge(new Edge(2, 4, 3));
    graph.addEdge(new Edge(2, 5, 2));
    graph.addEdge(new Edge(2, 6, 4));
    graph.addEdge(new Edge(3, 4, 4));
    graph.addEdge(new Edge(3, 5, 1));
    graph.addEdge(new Edge(3, 6, 5));
    graph.addEdge(new Edge(4, 7, 1));
    graph.addEdge(new Edge(4, 8, 4));
    graph.addEdge(new Edge(5, 7, 6));
    graph.addEdge(new Edge(5, 8, 3));
    graph.addEdge(new Edge(6, 7, 3));
    graph.addEdge(new Edge(6, 8, 3));
    graph.addEdge(new Edge(7, 9, 3));
    graph.addEdge(new Edge(8, 9, 4));
    try {
       BellmanFord bf = new BellmanFord(graph, 0);
       bf.printPath(9);
    }
    catch (BellmanFord.BellmanFordException e){
      System.out.println(e);
    }

    P("");
    P("-----------------------------------------------------------------------");
    P("Test 11: Wikipedia's Example");
    P("This is a undirected graph, so I'm not sure if we'll be tested on this.");
    P("If it doesn't work for you, maybe there's no harm in ignoring it.");
    P("-----------------------------------------------------------------------");
    graph = new WGraph();
    graph.addEdge(new Edge(0, 4, 1));
    graph.addEdge(new Edge(4, 0, 1));
    graph.addEdge(new Edge(0, 1, 1));
    graph.addEdge(new Edge(1, 0, 1));
    graph.addEdge(new Edge(4, 3, 1));
    graph.addEdge(new Edge(3, 4, 1));
    graph.addEdge(new Edge(4, 1, 1));
    graph.addEdge(new Edge(1, 4, 1));
    graph.addEdge(new Edge(3, 5, 1));
    graph.addEdge(new Edge(5, 3, 1));
    graph.addEdge(new Edge(3, 2, 1));
    graph.addEdge(new Edge(2, 3, 1));
    graph.addEdge(new Edge(2, 1, 1));
    graph.addEdge(new Edge(1, 2, 1));
    try {
       BellmanFord bf = new BellmanFord(graph, 0);
       bf.printPath(5);
       bf = new BellmanFord(graph, 5);
       bf.printPath(0);
    }
    catch (BellmanFord.BellmanFordException e){
      System.out.println(e);
    }

    // FINISHED!
    P("");
  } 
}