package s.ashiqur.graphsimulator.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import s.ashiqur.graphsimulator.Graph;
import s.ashiqur.graphsimulator.GraphConstants;
import s.ashiqur.graphsimulator.R;


public class CustomView extends View implements GraphConstants {


    private Graph graph;

    int STROKEWIDTH=getResources().getDimensionPixelSize(R.dimen.edgeWidth);;
    private Paint paintLine;
    private Paint paintCircle;
    private Paint paintArrow;

    public void setGraph(Graph graph) {
        this.graph = graph;
    }


    public CustomView(Context context) {
        super(context);

        init(null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    @SuppressLint("NewApi")
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {

        
        
        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle=new Paint(Paint.ANTI_ALIAS_FLAG);
        paintArrow=new Paint(Paint.ANTI_ALIAS_FLAG);
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
    private void drawArrow(Canvas canvas,double node1X, double node1Y, double node2X, double node2Y) {

        double arrowAngle = Math.toRadians(10.0);
        double arrowLength = 60.0;
        double midx=  ((node1X+node2X)*0.5);
        double midy=((node1Y+node2Y)*0.5) ;
        double dx = node1X - midx;
        double dy = node1Y - midy;
        double angle = Math.atan2(dy, dx);
        double x1 = Math.cos(angle + arrowAngle) * arrowLength + midx;
        double y1 = Math.sin(angle + arrowAngle) * arrowLength + midy;

        double x2 = Math.cos(angle - arrowAngle) * arrowLength + midx;
        double y2 = Math.sin(angle - arrowAngle) * arrowLength + midy;


        canvas.drawLine((float) midx,(float)midy,(float) x1,(float) y1,paintArrow);
        canvas.drawLine((float)midx, (float)midy, (float)x2, (float)y2,paintArrow);
    }

    @Override
    protected void onDraw(Canvas canvas) {
       // NEVER D0 THIS->this.canvas=canvas;
        super.onDraw(canvas);

        if(!graph.directed){
            for(int i=0;i<graph.nVertices;i++)
            {
                for(int j=0;j<graph.nVertices;j++){
                    if(graph.isEdge(i,j))
                    {
                        Button t1=graph.nodeButtons.get(i);
                        Button t2=graph.nodeButtons.get(j);

                        canvas.drawLine(t1.getX()+(int) Size.NODESIZE.value/2,t1.getY()+(int) Size.NODESIZE.value/2,t2.getX()+(int) Size.NODESIZE.value/2,t2.getY()+(int) Size.NODESIZE.value/2, paintLine);
                        //System.out.println("Edge drawn :"+i+" "+j);
                    }
                }
            }
        }
        if(graph.directed){
            for(int i=0;i<graph.nVertices;i++)
            {
                for(int j=0;j<graph.nVertices;j++){
                    if(graph.isEdge(i,j))
                    {
                        Button t1=graph.nodeButtons.get(i);
                        Button t2=graph.nodeButtons.get(j);


                        if(!graph.isEdge(j,i)) {

                            canvas.drawLine(t1.getX()+ (int) Size.NODESIZE.value/2, t1.getY()+(int) Size.NODESIZE.value/2, t2.getX()+(int) Size.NODESIZE.value/2, t2.getY()+(int) Size.NODESIZE.value/2, paintLine);
                            drawArrow(canvas,t1.getX()+(int) Size.NODESIZE.value/2,t1.getY()+(int) Size.NODESIZE.value/2,t2.getX()+(int) Size.NODESIZE.value/2,t2.getY()+(int) Size.NODESIZE.value/2);
                        }
                        else
                            {



                                canvas.drawLine(t1.getX()+(int) Size.NODESIZE.value/2, t1.getY()+(int) Size.NODESIZE.value/2, t2.getX()+(int) Size.NODESIZE.value/2, t2.getY()+(int) Size.NODESIZE.value/2, paintLine);
                                drawArrow(canvas,t1.getX()+(int) Size.NODESIZE.value/2,t1.getY()+(int) Size.NODESIZE.value/2,t2.getX()+(int) Size.NODESIZE.value/2,t2.getY()+(int) Size.NODESIZE.value/2);


                            }

                    }
                }
            }
        }

    }



}
