package s.ashiqur.graphsimulator;

import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
        import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import static android.graphics.Color.WHITE;


public class Graph implements GraphConstants {


    BlockingQueue changeNodeColorQueue;
    boolean state = false;
    public int nVertices, nEdges;
    public ArrayList<Button> nodeButtons;
    public boolean directed;
    int time = 0;
    int[][] matrix; //adjacency matrix to store the graph
    int color[], parent[], dist[], discoveryTime[], finishTime[];
    StringBuffer bfsVisitedSequence;
    StringBuffer dfsVisitedSequence;
    //define other variables required for bfs such as color, parent, and dist
    //you must use pointers and dynamic allocation


    public void setChangeNodeColorQueue(BlockingQueue changeNodeColorQueue) {
        this.changeNodeColorQueue = changeNodeColorQueue;
    }

    Graph(boolean dir, ArrayList<Button> nodeButtons) {
        nVertices = 7;
        nEdges = 0;
        directed = dir; //set direction of the graph
        this.nodeButtons = nodeButtons;
        bfsVisitedSequence = new StringBuffer();
        dfsVisitedSequence=new StringBuffer();
    }

    public void setNodeButtons(ArrayList<Button> nodeButtons) {
        this.nodeButtons = nodeButtons;
    }

    void setnVertices(int n) {
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

    void addEdge(int u, int v) {
        //write your code here
        if (u < 0 || u >= nVertices || v < 0 || v >= nVertices) return;
        matrix[u][v] = 1;
        if (!directed) matrix[v][u] = 1;

    }

    void removeEdge(int u, int v) {
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

    int getDegree(int u) {
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

    boolean hasCommonAdjacent(int u, int v) {
        //returns true if vertices u and v have common adjacent vertices
        int[] U = new int[nVertices], V = new int[nVertices];
        for (int i = 0; i < nVertices; i++) if (matrix[u][i] == 1) U[i] = 1;

        for (int i = 0; i < nVertices; i++) if (matrix[v][i] == 1) V[i] = 1;

        int i = 0;
        for (i = 0; i < nVertices; i++) if ((U[i] & V[i]) == 1) return true;

        return false;


    }

    void bfs(int source) {
        //complete this function
        //initialize BFS variables
        bfsVisitedSequence.delete(0, bfsVisitedSequence.length());
        for (int i = 0; i < nodeButtons.size(); i++) {

            color[i] = UNVISITED;

            if (nodeButtons.get(i) != null) {
                changeNodeColorQueue.add(nodeButtons.get(i));
                changeNodeColorQueue.add(UNVISITED);

            }

            //System.out.println(nodeButtons.size()+" "+i);


            parent[i] = -1;
            dist[i] = INFINITY;
        }
        Queue q = new LinkedList();
        color[source] = MARKED;
        if (nodeButtons.get(source) != null) {
            changeNodeColorQueue.add(nodeButtons.get(source));
            changeNodeColorQueue.add(MARKED);

        }

        dist[source] = 0;
        q.add(source);
        while (!q.isEmpty()) {
            //complete this part
            int visited = (int) q.remove();
            color[visited] = VISITED;
            if (nodeButtons.get(visited) != null) {
                changeNodeColorQueue.add(nodeButtons.get(visited));
                changeNodeColorQueue.add(VISITED);

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
                            changeNodeColorQueue.add(nodeButtons.get(neighbourOfV));
                            changeNodeColorQueue.add(MARKED);

                        }//nodeButtons.get(neighbourOfV).setBackgroundColor(MARKED);
                        dist[neighbourOfV] = dist[visited] + 1;
                        parent[neighbourOfV] = visited;
                    }
                }
            }

        }
        // cout+"|\n";
    }
    void dfs(int source)
    {
        changeNodeColorQueue.clear();
        dfsVisitedSequence.delete(0, dfsVisitedSequence.length());
        time = 0;
        for (int i = 0; i < nodeButtons.size(); i++) {
            color[i] = UNVISITED;
            changeNodeColorQueue.add(nodeButtons.get(i));
            changeNodeColorQueue.add(color[i]);
            parent[i] = -1;
        }

        dfs_visitM(source);
        for(int i = 0; i<nVertices ; i++){
            if(color[i] == WHITE){
                dfs_visitM(i);
            }
        }
    }




    int getDist(int u, int v) {
        //returns the shortest path distance from u to v
        //must call bfs using u as the source vertex, then use distance array to find the distance
        if (u < 0 || u >= nVertices || u >= nVertices || v >= nVertices) {
            System.out.println("Invalid Index\n");
            return 0; //vertex out of range
        }
        bfs(u);
        return dist[v];

    }

    void dfs_visitM(int vertex) {
        if (color[vertex] == VISITED) {
            return;
        }

        time++;
        discoveryTime[vertex] = time;
        System.out.println("Visit :" + vertex + "\n");
        dfsVisitedSequence.append("Visit :"+ vertex + "\n");


        color[vertex] = MARKED;
        changeNodeColorQueue.add(nodeButtons.get(vertex));
        changeNodeColorQueue.add(color[vertex]);
        for (int i = 0; i < nVertices; i++) {
            if ((matrix[vertex][i] == 1) && (color[i] == UNVISITED)) {
                parent[i] = vertex;
                dfs_visitM(i);
            }
        }
        color[vertex] = VISITED;
        changeNodeColorQueue.add(nodeButtons.get(vertex));
        changeNodeColorQueue.add(color[vertex]);

        time++;
        System.out.println("Leave :" + vertex);
       // dfsVisitedSequence.append("Leave :" + vertex);
        dfsVisitedSequence.append("Leave :"+ vertex + "\n");
        finishTime[vertex] = time;
       // dfsVisitedSequence.append("\n");
        return;
    }

    String printStat(int type)

    {


        StringBuffer result = new StringBuffer();

        if(type==BFS) {
            System.out.println("BFS Stats\n");
            result.append("BFS Stats\n\n");
            for (int i = 0; i < nVertices; i++) {
                System.out.println("NODE: " + i);
                result.append("NODE: " + i + "\n");
                System.out.println(" Parent :" + parent[i] + " Distance :" + dist[i] + "\n");
                result.append("Parent :" + parent[i] + " Distance :" + dist[i] + "\n");


            }
            result.append("\n BFS TRAVERSAL OF THE GRAPH : \n" + bfsVisitedSequence);
        }
        else if(type==DFS)
        {
            System.out.println("DFS Stats\n");
            result.append("DFS Stats\n\n");
        for (int i = 0; i < nVertices; i++) {

            System.out.println("NODE:" + i);
            result.append("NODE:" + i+"\n");
            System.out.println("  Parent :" + parent[i] + " DiscoveryTime :" + discoveryTime[i]
                    + " FinishTime :" + finishTime[i]);
            result.append("  Parent :" + parent[i] + " DiscoveryTime :" + discoveryTime[i] + " FinishTime :" + finishTime[i]+"\n");

        }
            result.append("\nDFS Traversal Of The Graph :\n");
            result.append(dfsVisitedSequence);

        }
        return result.toString();
    }


    void printGraph() {
        System.out.printf("\nNumber of vertices: %d, Number of edges: %d\n", nVertices, nEdges);
        for (int i = 0; i < nVertices; i++) {
            System.out.printf("%d:", i);
            for (int j = 0; j < nVertices; j++) {
                if (matrix[i][j] == 1)
                    System.out.printf(" %d", j);
            }
            System.out.printf("\n");
        }
    }


    public void setDirection(boolean b) {
        directed=b;
    }
}
//class Main2 implements GraphConstants{
//
//    public static void main(String[] args)  {
//        int n;
//
//        Graph g=new Graph(false);
//        g.setnVertices(8);
//
//        g.addEdge(0,1);
//        g.addEdge(1,3);
//        g.addEdge(3,2);
//        g.addEdge(3,5);
//        g.addEdge(1,5);
//        g.addEdge(4,5);
//        g.addEdge(6,5);
//        g.addEdge(5,7);
//        g.addEdge(7,1);
//
//        Scanner sc=new Scanner(System.in);
//
//        while(true)
//        {
//            System.out.printf("1. Add edge. \n");
//            //System.out.printf("2. RemoveEdge(u , v). 3.isEdge(u , v) 4.getDegree(u) 5. Print Graph \n"+"6. CheckCommonAdjacent(int u,int v) 7.getDist() 8.BFS 9.DFS 10. Exit.\n");
//
//            int ch;
//            ch=sc.nextInt();
//            System.out.println("Your Choice :"+ch);
//            if(ch==1)
//            {
//                int u, v;
//                u=sc.nextInt();
//                v=sc.nextInt();
//                g.addEdge(u, v);
//            }
//            else if(ch==2)
//            {
//                int s=0,e=0;
//                //scanf("%d%d",&s,&e);
//
//                s=sc.nextInt();
//                e=sc.nextInt();
//
//                g.removeEdge(s,e);
//                g.printGraph();
//            }
//            else if(ch==3)
//            {
//                int u=0,v=0;
//                u=sc.nextInt();
//                v=sc.nextInt();
//                if(g.isEdge(u,v))System.out.println("Edge Exists");
//                else System.out.println("Edge Not Found ");
//
//            }
//            else if(ch==4)
//            {
//                int u=0;
//                u=sc.nextInt();
//
//                System.out.println("Degree of :"+u+" is:"+g.getDegree(u));
//            }
//            else if(ch==5)
//            {
//                g.printGraph();
//            }
//            else if(ch==6)
//            {
//                int u=0,v=0;
//                u=sc.nextInt();
//                v=sc.nextInt();
//                if(g.hasCommonAdjacent(u,v))System.out.println("Common Vertex Exists");
//                else System.out.println("Doesnot Exist");
//            }
//            else if(ch==7)
//            {
//                int u=0,v=0;
//                u=sc.nextInt();
//                v=sc.nextInt();
//                System.out.println("Distance From:"+u+"to"+v+"="+g.getDist(u,v));
//
//            }
//            else if(ch==8)
//            {
//                int u=0;
//                u=sc.nextInt();
//
//                g.bfs(u);
//                g.printStat(BFS);
//            }
//            else if(ch==9)
//            {
//                g.dfs();
//                g.printStat(DFS);
//            }
//            else if(ch==10)
//            {
//                break;
//            }
//        }
//
//    }
//}