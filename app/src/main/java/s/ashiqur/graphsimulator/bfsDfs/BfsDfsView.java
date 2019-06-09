package s.ashiqur.graphsimulator.bfsDfs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import s.ashiqur.graphsimulator.graph.GraphDrawUtil;
import s.ashiqur.graphsimulator.graph.AnimatableGraph;
import s.ashiqur.graphsimulator.graph.GraphView;
import s.ashiqur.graphsimulator.R;

import static s.ashiqur.graphsimulator.graph.AnimatableGraphConstants.EDGECOLOR;
import static s.ashiqur.graphsimulator.graph.AnimatableGraphConstants.YELLOW;


public class BfsDfsView extends View implements GraphView {

    private int STROKEWIDTH = getResources().getDimensionPixelSize(R.dimen.edgeWidth);
    private AnimatableGraph graph;

    private Paint paintLine;
    private Paint paintCircle;
    private Paint paintArrow;

    public BfsDfsView(Context context) {
        super(context);

        init(null);
    }

    public BfsDfsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }


    public BfsDfsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    @SuppressLint("NewApi")
    public BfsDfsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    @Override
    public void setGraph(AnimatableGraph graph) {
        this.graph = graph;
    }

    private void init(@Nullable AttributeSet set) {


        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintArrow = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (set == null)
            return;


        paintLine.setColor(EDGECOLOR);
        paintLine.setStrokeWidth(STROKEWIDTH);
        paintLine.setStyle(Paint.Style.STROKE);
        paintCircle.setColor(YELLOW);
        paintCircle.setStrokeWidth(STROKEWIDTH);
        paintCircle.setStyle(Paint.Style.FILL);
        paintArrow.setColor(Color.RED);
        paintArrow.setStyle(Paint.Style.FILL);
        paintArrow.setStrokeWidth(STROKEWIDTH);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        // NEVER D0 THIS->this.canvas=canvas;
        BfsDfsGraph bfsDfsGraph = (BfsDfsGraph) graph;
        super.onDraw(canvas);

        if (!bfsDfsGraph.isDirected()) {
            for (int i = 0; i < bfsDfsGraph.getnVertices(); i++) {
                for (int j = 0; j < bfsDfsGraph.getnVertices(); j++) {
                    if (bfsDfsGraph.isEdge(i, j)) {
                        Button t1 = bfsDfsGraph.getNodeButtons().get(i);
                        Button t2 = bfsDfsGraph.getNodeButtons().get(j);
                        GraphDrawUtil.drawUndirectedEdge(canvas, t1, t2, paintLine);
                        //System.out.println("Edge drawn :"+i+" "+j);
                    }
                }
            }
        }
        if (bfsDfsGraph.isDirected()) {
            for (int i = 0; i < bfsDfsGraph.getnVertices(); i++) {
                for (int j = 0; j < bfsDfsGraph.getnVertices(); j++) {
                    if (bfsDfsGraph.isEdge(i, j)) {
                        Button t1 = bfsDfsGraph.getNodeButtons().get(i);
                        Button t2 = bfsDfsGraph.getNodeButtons().get(j);

                        if (!bfsDfsGraph.isEdge(j, i)) {
                            //canvas.drawLine(t1.getX() + (int) Sizes.NodeSize.value / 2, t1.getY() + (int) Sizes.NodeSize.value / 2, t2.getX() + (int) Sizes.NodeSize.value / 2, t2.getY() + (int) Sizes.NodeSize.value / 2, paintLine);
                            GraphDrawUtil.drawDirectedEdge(canvas, t1, t2, paintLine, paintArrow);
                        } else {
                            //canvas.drawLine(t1.getX() + (int) Sizes.NodeSize.value / 2, t1.getY() + (int) Sizes.NodeSize.value / 2, t2.getX() + (int) Sizes.NodeSize.value / 2, t2.getY() + (int) Sizes.NodeSize.value / 2, paintLine);
                            GraphDrawUtil.drawDirectedEdge(canvas, t1, t2, paintLine, paintArrow);
                        }

                    }
                }
            }
        }

    }

}
