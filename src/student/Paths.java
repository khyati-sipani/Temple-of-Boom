package student;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import game.Edge;
import game.Node;




/** This class contains Dijkstra's shortest-path algorithm and some other methods. */
public class Paths {

    /** Return a list of the nodes on the shortest path from start to 
     * end, or the empty list if a path does not exist.
     * Note: The empty list is NOT "null"; it is a list with 0 elements. */
    public static List<Node> shortestPath(Node start, Node end) {
        /* TODO Implement Dijkstras's shortest-path algorithm presented
         in the slides titled "Final Algorithm" (slide 37 or so) in the slides
         for lecture 20.
         In particular, a min-heap (as implemented in assignment A6) should be
         used for the frontier set. We provide a declaration of the frontier.
         We will make our solution to min-heap available as soon as the deadline
         for submission has passed.

         Maintaining information about shortest paths will require maintaining
         for each node in the settled and frontier sets the backpointer for
         that node, as described in the A7 handout, along with the length of the
         shortest path (thus far, for nodes in the frontier set). For this
         purpose, we provide static class SFdata. You have to declare
         the HashMap local variable for it and describe carefully what it
         means. WRITE A PRECISE DEFINITION OF IT.

         Note 1: Read the notes on pages 2..3 of the handout carefully.
         Note 2: The method MUST return as soon as the shortest path to node
                 end has been calculated. In that case, do NOT continue to find
                 shortest paths. You will lost 15 points if you do not do this.
         Note 3: the only graph methods you may call are these:

            1. n.getExits():  Return a List<Edge> of edges that leave Node n.
            2. e.getOther(n): n must be one of the Nodes of Edge e.
                              Return the other Node.
            3. e.length():    Return the length of Edge e.

         Method pathDistance uses one more: n1.getEdge(n2)
         */

    	 Heap<Node> F= new Heap<Node>();

         // Each node in the Settled and Frontier sets has an entry in map,
         // which gives its shortest distance from node start and the backpointer
         // of the node on a shortest path from node start.
         HashMap<Node, SFdata> map= new HashMap<Node, SFdata>();

         F.add(start, 0);
         map.put(start, new SFdata(0, null));

         // invariant: As presented in notes for Lecture 20
         while (F.size() > 0) {
             Node f= F.poll();
             if (f == end) return constructPath(f, map);

             SFdata fData= map.get(f);

             for (Edge edge : f.getExits()) {
                 Node w= edge.getOther(f);
                 int distToW= fData.distance + edge.length;
                 SFdata wData= map.get(w);
                 if (wData == null) {  // if w is in the far-out set
                     F.add(w, distToW);
                     map.put(w, new SFdata(distToW, f));
                 }
                 else // w in settled or frontier set 
                     if (distToW < wData.distance) {
                         F.changePriority(w, distToW);
                         wData.distance= distToW;
                         wData.backPointer= f;
                     }
             }
         }

         return new LinkedList<Node>(); // no path found
    }
    
    public static List<Node> shortestPath2(Node start, Node end){
    	 Heap<Node> F= new Heap<Node>();

         // Each node in the Settled and Frontier sets has an entry in map,
         // which gives its shortest distance from node start and the backpointer
         // of the node on a shortest path from node start.
         HashMap<Node, SFdata> map= new HashMap<Node, SFdata>();

         F.add(start, 0);
         map.put(start, new SFdata(0, null));

         // invariant: As presented in notes for Lecture 20
         while (F.size() > 0) {
             Node f= F.poll();
             if (f == end) return constructPath(f, map);

             SFdata fData= map.get(f);

             for (Edge edge : f.getExits()) {
                 Node w= edge.getOther(f);
                 int distToW= fData.distance + edge.length;
                 SFdata wData= map.get(w);
                 if (wData == null) {  // if w is in the far-out set
                     F.add(w, distToW);
                     map.put(w, new SFdata(distToW, f));
                 }
                 else // w in settled or frontier set 
                     if (distToW < wData.distance) {
                         F.changePriority(w, distToW);
                         wData.distance= distToW;
                         wData.backPointer= f;
                     }
             }
         }

         return new LinkedList<Node>();
    }

    /** Return the path from the start node to node end.
     *  Precondition: nData contains all the necessary information about
     *  the path. */
    public static List<Node> constructPath(Node end, HashMap<Node, SFdata> nData) {
        LinkedList<Node> path= new LinkedList<Node>();
        Node p= end;
        // invariant: All the nodes from p's successor to the end are in
        //            path, in reverse order.
        while (p != null) {
            path.addFirst(p);
            p= nData.get(p).backPointer;
        }
        return path;
    }

    /** Return the sum of the weights of the edges on path path. */
    public static int pathDistance(List<Node> path) {
        if (path.size() == 0) return 0;
        synchronized(path) {
            Iterator<Node> iter= path.iterator();
            Node p= iter.next();  // First node on path
            int s= 0;
            // invariant: s = sum of weights of edges from start to p
            while (iter.hasNext()) {
                Node q= iter.next();
                s= s + p.getEdge(q).length;
                p= q;
            }
            return s;
        }
    }

    /** An instance contains information about a node: the previous node
     *  on a shortest path from the start node to this node and the distance
     *  of this node from the start node. */
    private static class SFdata {
        private Node backPointer; // backpointer on path from start node to this one
        private int distance; // distance from start node to this one

        /** Constructor: an instance with distance d from the start node and
         *  backpointer p.*/
        private SFdata(int d, Node p) {
            distance= d;     // Distance from start node to this one.
            backPointer= p;  // Backpointer on the path (null if start node)
        }

        /** return a representation of this instance. */
        public String toString() {
            return "dist " + distance + ", bckptr " + backPointer;
        }
    }
}
