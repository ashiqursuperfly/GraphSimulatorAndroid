package s.ashiqur.graphsimulator.graph;

import android.graphics.Color;

import s.ashiqur.graphsimulator.R;


public interface GraphConstants {

    int MARKED = R.drawable.round_button_marked ;
    int SELECTED =R.drawable.round_button_selected;
    int UNVISITED =R.drawable.round_button_unmarked;
    int VISITED =R.drawable.round_button_visited;
    int BFS=4;
    int DFS=5;
    int NULL_VALUE=(-999999999);
    int INFINITY=(99999999);
    int MAX_VERTICES=30;
    float MAX_SPEED=5;
    int NODE_RES_ID_START =999000;

     //(int)Long.parseLong("FFC640FF",16);///PURPLE
    //(int)Long.parseLong("48E1D7",16);//Lavender
    int YELLOW=Color.YELLOW;
    int DESELECTED = R.drawable.round_button_deselected;
    int EDGECOLOR=Color.BLACK;
    int NODETEXTCOLOR=Color.rgb(14, 113, 206);




}
