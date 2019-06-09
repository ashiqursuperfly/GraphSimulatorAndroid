package s.ashiqur.graphsimulator.bfsDfs;

import android.widget.Button;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import s.ashiqur.graphsimulator.graph.AlgoSimulationData;
import s.ashiqur.graphsimulator.graph.AnimatableGraph;

import static android.graphics.Color.WHITE;


public class BfsDfsGraph implements AnimatableGraph {

    private int nVertices, nEdges;
    private ArrayList<Button> nodeButtons;
    private boolean directed;
    private BlockingQueue<AlgoSimulationData> changeNodeColorQueue;
    private int time = 0;
    private int[][] matrix; //adjacency matrix to store the graph
    private int [] color, parent, dist, discoveryTime, finishTime;
    StringBuffer bfsVisitedSequence;
    private StringBuffer dfsVisitedSequence;


    BfsDfsGraph(boolean dir, ArrayList<Button> nodeButtons) {
        nVertices = 7;
        nEdges = 0;
        directed = dir; //set direction of the graph
        this.nodeButtons = nodeButtons;
        bfsVisitedSequence = new StringBuffer();
        dfsVisitedSequence = new StringBuffer();
    }

    @Override
    public void setChangeNodeColorQueue(BlockingQueue<AlgoSimulationData> animationChangeQueue) {
        this.changeNodeColorQueue = animationChangeQueue;
    }

    @Override
    public void setNodeButtons(ArrayList<Button> nodeButtons) {
        this.nodeButtons = nodeButtons;
    }

    public void setnVertices(int n) {
        this.nVertices = n;

        //allocate space for the matrix
        matrix = new int[nVertices][nVertices];
        color = new int[nVertices];
        parent = new int[nVertices];
        dist = new int[nVertices];
        discoveryTime = new int[nVertices];
        finishTime = new int[nVertices];
        for (int i = 0; i < nVertices; i++) {
            matrix[i] = new int[nVertices];
            for (int j = 0; j < nVertices; j++)
                matrix[i][j] = 0; //initialize the matrix cells to 0
        }

    }

    public void addEdge(int u, int v) {
        //write your code here
        if (u < 0 || u >= nVertices || v < 0 || v >= nVertices) return;
        matrix[u][v] = 1;
        if (!directed) matrix[v][u] = 1;

    }

    public void removeEdge(int u, int v) {
        //write this function
        if (u < 0 || u >= nVertices || v < 0 || v >= nVertices) return;
        if (directed) matrix[u][v] = 0;
        else {
            matrix[u][v] = 0;
            matrix[v][u] = 0;
        }
    }

    public boolean isEdge(int u, int v) {
        //returns true if (u,v) is an edge, otherwise should return false
        if (u < 0 || u >= nVertices || v < 0 || v >= nVertices) {
            System.out.println("INVALID INDEX\n");
            return false;
        }
        if (directed) return matrix[u][v] == 1;
        else return matrix[u][v] == 1 && matrix[v][u] == 1;
    }

    public int getDegree(int u) {
        //returns the degree of vertex u
        if (u < 0 || u >= nVertices) return NULL_VALUE;

        boolean isLoop = matrix[u][u] == 1;
        int degree = 0;
        for (int i = 0; i < nVertices; degree += matrix[u][i], i++) ;
        if (!directed) {
            if (isLoop) return degree + 1;
            else return degree;
        } else {
            for (int i = 0; i < nVertices; i++) {
                if (i == u) continue;
                if (matrix[i][u] == 1) degree++;
            }
            if (isLoop) return degree + 1;
            else return degree;
        }
    }

    public boolean hasCommonAdjacent(int u, int v) {
        //returns true if vertices u and v have common adjacent vertices
        int[] U = new int[nVertices], V = new int[nVertices];
        for (int i = 0; i < nVertices; i++) if (matrix[u][i] == 1) U[i] = 1;

        for (int i = 0; i < nVertices; i++) if (matrix[v][i] == 1) V[i] = 1;

        int i = 0;
        for (i = 0; i < nVertices; i++) if ((U[i] & V[i]) == 1) return true;

        return false;


    }

    public void bfs(int source) {
        //complete this function
        //initialize BFS variables
        bfsVisitedSequence.delete(0, bfsVisitedSequence.length());
        for (int i = 0; i < nodeButtons.size(); i++) {

            color[i] = UNVISITED;

            if (nodeButtons.get(i) != null) {
                changeNodeColorQueue.add(new AlgoSimulationData(nodeButtons.get(i),UNVISITED));
            }

            //System.out.println(nodeButtons.size()+" "+i);


            parent[i] = -1;
            dist[i] = INFINITY;
        }
        Queue q = new LinkedList();
        color[source] = MARKED;
        if (nodeButtons.get(source) != null) {
            changeNodeColorQueue.add(new AlgoSimulationData(nodeButtons.get(source),MARKED));
        }

        dist[source] = 0;
        q.add(source);
        while (!q.isEmpty()) {
            //complete this part
            int visited = (int) q.remove();
            color[visited] = VISITED;
            if (nodeButtons.get(visited) != null) {
                changeNodeColorQueue.add(new AlgoSimulationData(nodeButtons.get(visited),VISITED));
            }
            //nodeButtons.get(visited).setBackgroundColor(VISITED);
            bfsVisitedSequence.append(visited + "--");
            System.out.println(visited + "->");

            for (int i = 0; i < nVertices; i++) {
                int neighbourOfV;
                if (matrix[visited][i] != 0) {
                    neighbourOfV = i;
                    //int neighbourOfV=[visited].getItem(i);
                    if (color[neighbourOfV] == UNVISITED) {
                        q.add(neighbourOfV);
                        color[neighbourOfV] = MARKED;
                        if (nodeButtons.get(neighbourOfV) != null) {
                            changeNodeColorQueue.add(new AlgoSimulationData(nodeButtons.get(neighbourOfV),MARKED));
                        }//nodeButtons.get(neighbourOfV).setBackgroundColor(MARKED);
                        dist[neighbourOfV] = dist[visited] + 1;
                        parent[neighbourOfV] = visited;
                    }
                }
            }

        }
        // cout+"|\n";
    }

    public void dfs(int source) {
        changeNodeColorQueue.clear();
        dfsVisitedSequence.delete(0, dfsVisitedSequence.length());
        time = 0;
        for (int i = 0; i < nodeButtons.size(); i++) {
            color[i] = UNVISITED;
            changeNodeColorQueue.add(new AlgoSimulationData(nodeButtons.get(i),color[i]));
            parent[i] = -1;
        }

        dfs_visitM(source);
        for (int i = 0; i < nVertices; i++) {
            if (color[i] == WHITE) {
                dfs_visitM(i);
            }
        }
    }

    public int getDist(int u, int v) {
        //returns the shortest path distance from u to v
        //must call bfs using u as the source vertex, then use distance array to find the distance
        if (u < 0 || u >= nVertices || u >= nVertices || v >= nVertices) {
            System.out.println("Invalid Index\n");
            return 0; //vertex out of range
        }
        bfs(u);
        return dist[v];

    }

    public String printStat(int type) {

        StringBuilder result = new StringBuilder();

        if (type == BFS) {
            System.out.println("BFS Stats\n");
            result.append("BFS Stats\n\n");
            for (int i = 0; i < nVertices; i++) {
                System.out.println("NODE: " + i);
                result.append("NODE: ").append(i).append("\n");
                System.out.println(" Parent :" + parent[i] + " Distance :" + dist[i] + "\n");
                result.append("Parent :").append(parent[i]).append(" Distance :").append(dist[i]).append("\n");


            }
            result.append("\n BFS TRAVERSAL OF THE GRAPH : \n").append(bfsVisitedSequence);
        } else if (type == DFS) {
            System.out.println("DFS Stats\n");
            result.append("DFS Stats\n\n");
            for (int i = 0; i < nVertices; i++) {

                System.out.println("NODE:" + i);
                result.append("NODE:").append(i).append("\n");
                System.out.println("  Parent :" + parent[i] + " DiscoveryTime :" + discoveryTime[i]
                        + " FinishTime :" + finishTime[i]);
                result.append("  Parent :").
                        append(parent[i]).append(" DiscoveryTime :").append(discoveryTime[i]).append(" FinishTime :").append(finishTime[i]).append("\n");

            }
            result.append("\nDFS Traversal Of The Graph :\n");
            result.append(dfsVisitedSequence);

        }
        return result.toString();
    }

    public void printGraph() {
        System.out.printf("\nNumber of vertices: %d, Number of edges: %d\n", nVertices, nEdges);
        for (int i = 0; i < nVertices; i++) {
            System.out.printf("%d:", i);
            for (int j = 0; j < nVertices; j++) {
                if (matrix[i][j] == 1)
                    System.out.printf(" %d", j);
            }
            System.out.print("\n");
        }
    }

    private void dfs_visitM(int vertex) {
        if (color[vertex] == VISITED) {
            return;
        }

        time++;
        discoveryTime[vertex] = time;
        System.out.println("Visit :" + vertex + "\n");
        dfsVisitedSequence.append("Visit :" + vertex + "\n");


        color[vertex] = MARKED;
        changeNodeColorQueue.add(new AlgoSimulationData(nodeButtons.get(vertex),color[vertex]));
        for (int i = 0; i < nVertices; i++) {
            if ((matrix[vertex][i] == 1) && (color[i] == UNVISITED)) {
                parent[i] = vertex;
                dfs_visitM(i);
            }
        }
        color[vertex] = VISITED;
        changeNodeColorQueue.add(new AlgoSimulationData(nodeButtons.get(vertex),color[vertex]));

        time++;
        System.out.println("Leave :" + vertex);
        // dfsVisitedSequence.append("Leave :" + vertex);
        dfsVisitedSequence.append("Leave :" + vertex + "\n");
        finishTime[vertex] = time;
        // dfsVisitedSequence.append("\n");
        return;
    }

    public int getnVertices() {
        return nVertices;
    }

    public int getnEdges() {
        return nEdges;
    }

    public ArrayList<Button> getNodeButtons() {
        return nodeButtons;
    }

    public boolean isDirected() {
        return directed;
    }
}
