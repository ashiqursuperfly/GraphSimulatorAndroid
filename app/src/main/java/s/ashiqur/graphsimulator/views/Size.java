package s.ashiqur.graphsimulator.views;

import android.view.animation.Animation;

/**
 * Created by ASUS on 5/30/2018.
 */

public enum Size {
    NODESIZE,HEIGHT,WIDTH,NODETEXTSIZE,XLIMIT,YLIMIT,XOFFSET,YOFFSET;
    public double value;
    Size(){}
    Size(double value) {
        this.value = value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
