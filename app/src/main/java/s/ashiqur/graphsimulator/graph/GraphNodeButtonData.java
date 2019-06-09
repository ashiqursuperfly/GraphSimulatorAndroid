package s.ashiqur.graphsimulator.graph;

import android.view.View;

public class GraphNodeButtonData {
    int resID;
    private float x, y;
    private String btnText;
    private View.OnDragListener onDragListener;
    private View.OnTouchListener onTouchListener;
    private View.OnClickListener onClickListener;

    public GraphNodeButtonData(float x, float y, String btnText, int resID, View.OnDragListener onDragListener, View.OnTouchListener onTouchListener, View.OnClickListener onClickListener) {
        this.x = x;
        this.y = y;
        this.btnText = btnText;
        this.resID = resID;
        this.onDragListener = onDragListener;
        this.onTouchListener = onTouchListener;
        this.onClickListener = onClickListener;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getBtnText() {
        return btnText;
    }

    public int getResID() {
        return resID;
    }

    public View.OnDragListener getOnDragListener() {
        return onDragListener;
    }

    public View.OnTouchListener getOnTouchListener() {
        return onTouchListener;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }
}
