package s.ashiqur.graphsimulator.graph;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.concurrent.BlockingQueue;

import s.ashiqur.graphsimulator.R;
import s.ashiqur.graphsimulator.Sizes;

import static s.ashiqur.graphsimulator.graph.GraphConstants.NODETEXTCOLOR;
/** We Are using a Button as a Node and using customView DrawLine to draw edges**/
public class GraphDrawUtil {

    private static final String TAG = "DrawUtil";

    public static void drawUndirectedEdge(Canvas canvas, Button srcNode, Button destNode, Paint paintLine){
        canvas.drawLine((srcNode.getX() + (int) Sizes.NodeSize.value / 2),
                 (srcNode.getY() + (int) Sizes.NodeSize.value / 2),
                 (destNode.getX() + (int) Sizes.NodeSize.value / 2),
                 (destNode.getY() + (int) Sizes.NodeSize.value / 2), paintLine);
    }

    public static void drawDirectedEdge(Canvas canvas, Button t1, Button t2, Paint paintLine, Paint paintArrow) {
        GraphDrawUtil.drawUndirectedEdge(canvas,t1,t2,paintLine);
        GraphDrawUtil.drawArrow(canvas,t1,t2,paintArrow);
    }

    public static void constrainXY(int[] x, int[] y) {
        if (x[0] >= Sizes.XLimit.value) x[0] = (int) (Sizes.XLimit.value);
        if (x[0] <= Sizes.XOffset.value) x[0] = (int) Sizes.XOffset.value;

        if (y[0] >= Sizes.YLimit.value) y[0] = (int) (Sizes.YLimit.value);
        if (y[0] <= Sizes.YOffset.value) y[0] = (int) Sizes.YOffset.value;

    }

    public static Button createNodeButton(RelativeLayout layout, Context c, GraphNodeButtonData nodeButtonData) {
        //RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);


        Button newButton = new Button(c);

        // Add the Buttons to the View
        GraphDrawUtil.setButtonProperties(layout, newButton, nodeButtonData.getBtnText(),
                (int) nodeButtonData.getX(), (int) nodeButtonData.getY(), nodeButtonData.getResID()
                , nodeButtonData.getOnTouchListener(), nodeButtonData.getOnDragListener(),
                nodeButtonData.getOnClickListener());
        //These Lines MUST be after addView();

        return newButton;

    }

    public static void animateSearch(final Activity a, final Runnable onUiAnimatorStop, final float speedPerMove, final BlockingQueue<AlgoSimulationData> changeQueue) {
        final Button[] temp = new Button[1];
        final int[] color = new int[1];

        final Runnable uiAnimationInEachStep = new Runnable() {

            @Override
            public void run() {

                // Stuff that updates the UI
                try {
                    AlgoSimulationData algoSimulationData = changeQueue.take();
                    temp[0] = algoSimulationData.getNodeButton();
                    color[0] = algoSimulationData.getColor();
                    temp[0].setBackgroundResource(color[0]);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    while (!changeQueue.isEmpty()) {
                        long start = System.currentTimeMillis();
                        a.runOnUiThread(uiAnimationInEachStep);
                        while (System.currentTimeMillis() - start < speedPerMove * 1000) {
                            if (changeQueue.isEmpty()) a.runOnUiThread(onUiAnimatorStop);
                        }
                    }
                    a.runOnUiThread(onUiAnimatorStop);
                }


            }

        };
        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }

    private static void setButtonProperties(RelativeLayout relativeLayout, Button b, String text, int x, int y, int resID, View.OnTouchListener handleTouch, View.OnDragListener handleDrag, View.OnClickListener handleClick) {
        int[] ax = {x};
        int[] ay = {y};
        constrainXY(ax, ay);

        String s = "DispH :" + Sizes.ScreenHeight.value + "DispW :" + Sizes.ScreenWidth.value + "(" + x + "," + y + ")";
        Log.d(TAG, "setButtonProperties() returned: " + s);
        LayoutAddButton(b, RelativeLayout.ALIGN_PARENT_START, ax[0], ay[0], 0, 0);

        relativeLayout.removeView(b);
        relativeLayout.addView(b);
        ViewGroup.LayoutParams params = b.getLayoutParams();
        b.setText(text);
        b.setTextSize((float) Sizes.NodeTextSize.value);
        params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        params.width = (int) Sizes.NodeSize.value;
        b.setLayoutParams(params);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // only for MArshMellow and newer versions
            System.out.println("Android Version :" + android.os.Build.VERSION.SDK_INT);
            b.setTextAppearance(R.style.TextAppearance_AppCompat_Small);
        }
        b.setBackgroundResource(R.drawable.round_button_deselected);
        b.setTextColor(NODETEXTCOLOR);

        b.setOnDragListener(handleDrag);
        b.setOnTouchListener(handleTouch);
        b.setOnClickListener(handleClick);
        b.setId(resID);

    }

    private static void LayoutAddButton(Button button, int centerInParent, int marginLeft, int marginTop, int marginRight, int marginBottom) {

        // Defining the layout parameters of the Button
        RelativeLayout.LayoutParams buttonLayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        // Add Margin to the LayoutParameters
        buttonLayoutParameters.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        // Add Rule to Layout
        buttonLayoutParameters.addRule(centerInParent);
        // Setting the parameters on the Button
        button.setLayoutParams(buttonLayoutParameters);

    }

    private static void drawArrow(Canvas canvas,Button src,Button dest, Paint paintArrow) {
        double node1X = src.getX()+ (int) Sizes.NodeSize.value / 2;
        double node1Y = src.getY()+ (int) Sizes.NodeSize.value / 2;
        double node2X = dest.getX()+ (int) Sizes.NodeSize.value / 2;
        double node2Y = dest.getY()+ (int) Sizes.NodeSize.value / 2;

        double arrowAngle = Math.toRadians(10.0);
        double arrowLength = 60.0;
        double midx = ((node1X + node2X) * 0.5);
        double midy = ((node1Y + node2Y) * 0.5);
        double dx = node1X - midx;
        double dy = node1Y - midy;
        double angle = Math.atan2(dy, dx);
        double x1 = Math.cos(angle + arrowAngle) * arrowLength + midx;
        double y1 = Math.sin(angle + arrowAngle) * arrowLength + midy;

        double x2 = Math.cos(angle - arrowAngle) * arrowLength + midx;
        double y2 = Math.sin(angle - arrowAngle) * arrowLength + midy;


        canvas.drawLine((float) midx, (float) midy, (float) x1, (float) y1, paintArrow);
        canvas.drawLine((float) midx, (float) midy, (float) x2, (float) y2, paintArrow);
    }


}
