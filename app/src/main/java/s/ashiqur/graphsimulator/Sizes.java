package s.ashiqur.graphsimulator;

/**
 * Created by ASUS on 5/30/2018.
 */

public enum Sizes {
    NodeSize, ScreenHeight, ScreenWidth, NodeTextSize, XLimit, YLimit, XOffset, YOffset;
    public double value;
    Sizes(){}
    public void setValue(double value) {
        this.value = value;
    }
}
