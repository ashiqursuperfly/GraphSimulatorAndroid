package s.ashiqur.graphsimulator;

/*
  Created by ASUS on 5/30/2018.
 */

/** All these values gets initialized In Main Menu Activity according to Device Screen Size.
 * And Gets used for the rest of the time
 */
public enum Sizes {
    NodeSize, ScreenHeight, ScreenWidth, NodeTextSize, XLimit, YLimit, XOffset, YOffset;
    public double value;
    Sizes(){}
    public void setValue(double value) {
        this.value = value;
    }
}
