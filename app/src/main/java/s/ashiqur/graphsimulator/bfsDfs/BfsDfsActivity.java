package s.ashiqur.graphsimulator.bfsDfs;

import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import s.ashiqur.graphsimulator.graph.AlgoSimulationStepData;
import s.ashiqur.graphsimulator.graph.GraphDrawUtil;
import s.ashiqur.graphsimulator.graph.AnimatableGraphConstants;
import s.ashiqur.graphsimulator.graph.GraphNodeButtonData;
import s.ashiqur.graphsimulator.R;
import s.ashiqur.graphsimulator.Sizes;


public class BfsDfsActivity extends AppCompatActivity implements AnimatableGraphConstants {


    private static int nodeCount = 0;
    public BfsDfsGraph graph;
    public BlockingQueue<AlgoSimulationStepData> changeNodeColorQueue;
    ///XML VARIABLES
    private BfsDfsView customView;
    private RelativeLayout relativeLayoutBFS;
    private RelativeLayout relativeLayoutSettings;
    private TextView tvBFS;
    private TextView tvSearchStats;
    private View.OnDragListener handleDrag;
    private View.OnClickListener handleClick;
    private View.OnTouchListener handleTouch;
    private EditText eTnumberOfVertices;
    private EditText eTStartNode;
    private EditText eTBFSspeed;
    private CheckBox checkBoxDirection;
    private Button buttonRefresh;
    private Button buttonDone;
    private Button buttonAddEdge;
    private Button buttonBfsStart;
    private Button buttonDfsStart;
    private Button buttonShowSearchStats;
    private Button buttonHideBFSsettings;
    private Button buttonShowAlgorithm;
    int BFS = 4;
    int DFS = 5;

    ///JAVA VARIABLES
    private GestureDetector gestureDetector;
    private ArrayList<Button> nodeButtons;
    private ArrayList<Button> selectedButtons;
    private int currentDraggedNode;
    private float speedPerMove;
    private int startNode;
    private int nVertices;
    private int lastSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfs);


        initializeXmlVariables();
        initializeJavaVariables();
        refreshGraph();

        addOnClickListeners();
        addDragListeners();
        addTouchListeners();


    }

    private void initializeJavaVariables() {

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        changeNodeColorQueue = new ArrayBlockingQueue<>(1000);
        selectedButtons = new ArrayList<>();
        nodeButtons = new ArrayList<>();
        graph = new BfsDfsGraph(false, nodeButtons);
        startNode = 0;
        speedPerMove = 1;
        nVertices = 7;
        lastSearch = 0;
        customView.setGraph(graph);

        graph.setnVertices(7);//Default value set to 7


    }

    private void initializeXmlVariables() {

        //Views and Layouts
        customView = findViewById(R.id.customView);
        relativeLayoutBFS = findViewById(R.id.relativeLayoutBFS);
        relativeLayoutSettings = findViewById(R.id.relativeLayoutSettings);
        //TExtView
        tvBFS = findViewById(R.id.tvBfsDEBUG);
        tvSearchStats = findViewById(R.id.textViewSearchStat);
        tvSearchStats.setMovementMethod(new ScrollingMovementMethod());
        //Buttons
        buttonDone = findViewById(R.id.buttonDone);
        buttonRefresh = findViewById(R.id.buttonRefresh);
        buttonAddEdge = findViewById(R.id.buttonAddEdge);
        buttonAddEdge.setVisibility(View.VISIBLE);
        buttonBfsStart = findViewById(R.id.buttonBFSstart);
        buttonDfsStart = findViewById(R.id.buttonDFSstart);
        buttonShowSearchStats = findViewById(R.id.buttonSearchStats);
        buttonHideBFSsettings = findViewById(R.id.buttonHideBFSSettings);
        //TextFields

        eTStartNode = findViewById(R.id.eTstartNode);
        eTnumberOfVertices = findViewById(R.id.editTextNvertices);
        eTBFSspeed = findViewById(R.id.eTBFSspeed);
        checkBoxDirection = findViewById(R.id.checkBoxDirection);


    }

    private void addDragListeners() {
        ///THIS LISTENER is for the receiving the drops
        relativeLayoutBFS.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                final int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // tvBFS.setText("DRAGGED : Node" + (v.getId() - NODE_RES_ID_START) + " Degree :" + graph.getDegree(v.getId() - NODE_RES_ID_START));
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED: {
                        break;
                    }
                    case DragEvent.ACTION_DROP: {
                        tvBFS.setText("Drop Co-Ordinates :" + event.getX() + " " + event.getY());
                        int[] x = {(int) event.getX()};
                        int[] y = {(int) event.getY()};
                        GraphDrawUtil.constrainXY(x, y);

                        nodeButtons.get(currentDraggedNode).setX((float) x[0]);
                        nodeButtons.get(currentDraggedNode).setY((float) y[0]);
                        customView.setGraph(graph);
                        customView.invalidate();

                        return (true);
                    }

                    case DragEvent.ACTION_DRAG_LOCATION:
                        break;

                }

                return true;
            }
        });

        handleDrag = new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {


                final int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DROP: {
                        //do Nothing if dropped on itself
                        return false;
                    }

                }


                return true;
            }
        };
    }

    private void addTouchListeners() {
        handleTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int selected = v.getId() - NODE_RES_ID_START;

                if (gestureDetector.onTouchEvent(event)) {
                    // single tap
                    tvBFS.setText("Node" + (v.getId() - NODE_RES_ID_START) + " Degree :" + graph.getDegree(v.getId() - NODE_RES_ID_START));

                    try {
                        selectOrDeselectButton(nodeButtons.get(selected));

                    } catch (Exception e) {
                        tvBFS.setText(e.toString());
                    }

                    return true;
                } else {
                    // your code for move and drag
                    currentDraggedNode = selected;

                    tvBFS.setText("Selected for Drag: node[" + selected + "]");
                    System.out.println("Selected for Drag: node[" + selected + "]");
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadow = new View.DragShadowBuilder(nodeButtons.get(selected));
                    v.startDrag(data, shadow, null, 0);


                }
                return false;

            }

        };
        ///TODO :Add Drag and Drop For Node and Vertices
        relativeLayoutBFS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (customView.getVisibility() == View.INVISIBLE) {
                    Toast.makeText(getApplication(), "Hide Settings To Add Nodes", Toast.LENGTH_SHORT).show();
                    return false;
                }
                float x = event.getX();
                float y = event.getY();
                createNode((int) x, (int) y);
                //if(MenuActivity.DEBUG)tvBFS.setText("Coordinates("+x+","+y+")" +graph.directed+" "+graph.nVertices);

                return false;
            }
        });

    }

    private void addOnClickListeners() {

        handleClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.buttonDone: {

                        System.out.println("Clicked");
                        try {
                            String temp = eTStartNode.getText() + "";
                            String temp2 = eTBFSspeed.getText() + "";
                            String temp3 = eTnumberOfVertices.getText() + "";

//                            if (temp.equalsIgnoreCase("")) throw new Exception();
//                            if (temp2.equalsIgnoreCase("")) throw new Exception();
                            //if(temp3.equalsIgnoreCase(""))throw new Exception();

                            int prevNvertices = nVertices;
                            boolean prevDir = graph.isDirected();
                            if (!temp.equalsIgnoreCase("")) startNode = Integer.parseInt(temp);
                            if (!temp2.equalsIgnoreCase("")) speedPerMove = Float.parseFloat(temp2);
                            if (!temp3.equalsIgnoreCase("")) nVertices = Integer.parseInt(temp3);

                            if (nVertices <= 0 || nVertices > MAX_VERTICES) throw new Exception();
                            if (speedPerMove <= 0 || speedPerMove > MAX_SPEED || startNode < 0 || startNode >= nVertices)
                                throw new Exception();
                            if ((prevNvertices != nVertices) || (prevDir != checkBoxDirection.isChecked()))
                                refreshGraph();
                            Toast.makeText(getApplicationContext(), "Applied !", Toast.LENGTH_SHORT).show();
                            break;
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "\"PLEASE Enter VALID NUMBER OF VERTICES(Max :" + MAX_VERTICES + "+Please Enter Valid Start Node and Speed per Move(Max :" + MAX_SPEED + ")", Toast.LENGTH_SHORT).show();
                            tvBFS.setText("Error :" + e.toString() + " n=" + nVertices + " " + " " + "Start Node :" + startNode + "Bfs Speed :" + speedPerMove);
                            return;
                        }
                    }

                    case R.id.buttonHideBFSSettings: {
                        if ((buttonHideBFSsettings.getText() + "").equalsIgnoreCase("HideAll")) {

                            setBFSsettingsInvisible(false);
                            System.out.println("HideNow");

                        } else {
                            System.out.println(buttonHideBFSsettings.getText());
                            setBFSsettingsInvisible(true);
                            System.out.println("!HideNow");
                        }
                        break;
                    }
                    case R.id.buttonRefresh:
                        System.out.println(view.getX());
                        refreshGraph();
                        Toast.makeText(getApplicationContext(), "Refreshed !", Toast.LENGTH_SHORT).show();
                        customView.invalidate();
                        break;
                    case R.id.buttonAddEdge: {
                        System.out.println(view.getX());

                        if (customView.getVisibility() == View.INVISIBLE) {
                            Toast.makeText(getApplicationContext(), "Hide Settings to add/remove edges", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (selectedButtons.size() < 2) {
                            Toast.makeText(getApplicationContext(), "Please Select 2 Nodes First", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        int u = selectedButtons.get(0).getId() - NODE_RES_ID_START;
                        int v = selectedButtons.get(1).getId() - NODE_RES_ID_START;
                        if (graph.isEdge(u, v)) {
                            //Toast.makeText(getApplicationContext(), "Already An Edge", Toast.LENGTH_SHORT).show();
                            buttonAddEdge.setEnabled(true);
                            graph.removeEdge(u, v);
                            graph.printGraph();
                            customView.invalidate();
                            break;
                        }
                        graph.addEdge(u, v);
                        customView.setGraph(graph);
                        customView.invalidate();
                        graph.printGraph();

                        break;
                    }
                    case R.id.buttonDFSstart: {
                        System.out.println(view.getX());

                        if (customView.getVisibility() == View.INVISIBLE) {
                            Toast.makeText(getApplicationContext(), "Hide Settings to add/remove edges", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        graph.setAlgoSimulationStepsQueue(changeNodeColorQueue);
                        if (startNode >= nodeButtons.size()) {
                            Toast.makeText(getApplicationContext(), "Start Node Doesnt Exist", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        lastSearch = DFS;
                        graph.dfs(startNode);

                        animateSearch();
                        Iterator<Button> iterator = selectedButtons.iterator();

                        while (iterator.hasNext()) {
                            Button b = iterator.next();

                            if (b != null)
                                iterator.remove();
                        }
                        //tvBFS.setText(graph.bfsVisitedSequence);
                        break;
                    }
                    case R.id.buttonBFSstart: {
                        System.out.println(view.getX());


                        if (customView.getVisibility() == View.INVISIBLE) {
                            Toast.makeText(getApplicationContext(), "Hide Settings to add/remove edges", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        graph.setAlgoSimulationStepsQueue(changeNodeColorQueue);
                        if (startNode >= nodeButtons.size()) {
                            Toast.makeText(getApplicationContext(), "Start Node Doesnt Exist", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        lastSearch = BFS;
                        graph.bfs(startNode);

                        animateSearch();
                        Iterator<Button> iter = selectedButtons.iterator();

                        while (iter.hasNext()) {
                            Button b = iter.next();

                            if (b != null)
                                iter.remove();
                        }
                        tvBFS.setText(graph.bfsVisitedSequence);
                        break;
                    }
                    case R.id.buttonSearchStats: {
                        System.out.println(view.getX());


                        if (lastSearch == 0) {
                            tvSearchStats.setText("No Search Performed YET :( \n ");
                        } else tvSearchStats.setText(graph.printStat(lastSearch));

                        if (tvSearchStats.getVisibility() == View.INVISIBLE) {
                            buttonHideBFSsettings.setVisibility(View.INVISIBLE);
                            tvSearchStats.setVisibility(View.VISIBLE);
                            for (Button b :
                                    nodeButtons) {
                                b.setVisibility(View.INVISIBLE);
                                b.setEnabled(false);
                            }
                            customView.setVisibility(View.INVISIBLE);
                            relativeLayoutSettings.setVisibility(View.INVISIBLE);
                            buttonHideBFSsettings.setY((float) (Sizes.YOffset.value / 7));
                            if ((buttonHideBFSsettings.getText() + "").equalsIgnoreCase("HideAll"))
                                buttonHideBFSsettings.setText("ShowAll");
                        } else {
                            tvSearchStats.setVisibility(View.INVISIBLE);
                            buttonHideBFSsettings.setVisibility(View.VISIBLE);

                            for (Button b :
                                    nodeButtons) {
                                b.setVisibility(View.VISIBLE);
                                b.setEnabled(true);
                            }
                            if ((buttonHideBFSsettings.getText() + "").equalsIgnoreCase("ShowAll")) {
                                customView.setVisibility(View.VISIBLE);
                                //relativeLayoutSettings.setVisibility(View.VISIBLE);
                                //buttonHideBFSsettings.setY((float) (Size.YOffset.value*1.8));
                                //buttonHideBFSsettings.setText("HideAll");
                            }
                        }
                        break;
                    }


                }
            }
        };
        buttonDfsStart.setOnClickListener(handleClick);
        buttonAddEdge.setOnClickListener(handleClick);
        buttonRefresh.setOnClickListener(handleClick);
        buttonBfsStart.setOnClickListener(handleClick);
        buttonShowSearchStats.setOnClickListener(handleClick);
        buttonHideBFSsettings.setOnClickListener(handleClick);
        buttonDone.setOnClickListener(handleClick);


    }

    private void setBFSsettingsInvisible(boolean isHidden) {

        if (tvSearchStats.getVisibility() == View.VISIBLE)
            tvSearchStats.setVisibility(View.INVISIBLE);
        if (!isHidden) {

            customView.setVisibility(View.VISIBLE);
            for (Button b :
                    nodeButtons) {
                b.setVisibility(View.VISIBLE);
            }
            relativeLayoutSettings.setVisibility(View.INVISIBLE);

            buttonHideBFSsettings.setText("ShowAll");
            buttonHideBFSsettings.setY((float) (Sizes.YOffset.value / 7));

        } else {

            for (Button b :
                    nodeButtons) {
                b.setVisibility(View.INVISIBLE);
            }
            customView.setVisibility(View.INVISIBLE);
            relativeLayoutSettings.setVisibility(View.VISIBLE);

            buttonHideBFSsettings.setText("HideAll");
            buttonHideBFSsettings.setY((float) (Sizes.YOffset.value * 4.2));
        }

    }

    private void refreshGraph() {
        selectedButtons.clear();
        changeNodeColorQueue.clear();
        //  tvBFS.setText(graph.nVertices+"");
        graph = new BfsDfsGraph(checkBoxDirection.isChecked(), nodeButtons);
        graph.setnVertices(nVertices);
        nodeCount = 0;
        for (Button b : nodeButtons) {
            relativeLayoutBFS.removeView(b);

        }

        customView.setGraph(graph);
        nodeButtons.clear();

    }

    private void createNode(int x, int y) {
        //RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);


        if (nodeCount >= graph.getnVertices()) {
            Toast.makeText(getApplicationContext(), "Cant add more Nodes!" + "Already Added " + nVertices + " nodes", Toast.LENGTH_LONG).show();
            return;
        }

        // Add the Buttons to the View

        Button btn = GraphDrawUtil.createNodeButton(relativeLayoutBFS, this, new GraphNodeButtonData(x, y, nodeCount + "", NODE_RES_ID_START + nodeCount, handleDrag, handleTouch, null));
        //These Lines MUST be after addView();

        nodeButtons.add(btn);
        graph.setNodeButtons(nodeButtons);
        nodeCount++;

    }

    private void selectOrDeselectButton(Button b) {
        if (selectedButtons.size() <= 2) {

            if (!selectedButtons.contains(b)) {

                if (selectedButtons.size() == 2) {
//                System.out.println(selectedButtons.size());
                    // Toast.makeText(getApplicationContext(),"Already Selected 2 Nodes",Toast.LENGTH_SHORT).show();
                    Button temp = selectedButtons.remove(0);
                    temp.setBackgroundResource(DESELECTED);
                    selectedButtons.add(b);
                    b.setBackgroundResource(SELECTED);

                } else if (selectedButtons.size() != 2) {
                    b.setBackgroundResource(SELECTED);
                    selectedButtons.add(b);
                }

            } else if (selectedButtons.contains(b)) {
                b.setBackgroundResource(DESELECTED);
                selectedButtons.remove(b);
            }
        }

        if (selectedButtons.size() == 2) {
            buttonAddEdge.setVisibility(View.VISIBLE);
            buttonAddEdge.setEnabled(true);
        }
    }

    private void animateSearch() {
        buttonBfsStart.setEnabled(false);
        buttonDfsStart.setEnabled(false);
        buttonAddEdge.setEnabled(false);


        final Button[] temp = new Button[1];
        final int[] color = new int[1];
        final Runnable onUiAnimatorStop = new Runnable() {
            @Override
            public void run() {
                buttonBfsStart.setEnabled(true);
                buttonDfsStart.setEnabled(true);
                buttonAddEdge.setEnabled(true);

            }
        };
        GraphDrawUtil.animateSearch(BfsDfsActivity.this, onUiAnimatorStop, speedPerMove, changeNodeColorQueue);
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }


}
