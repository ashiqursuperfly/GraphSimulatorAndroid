package s.ashiqur.graphsimulator.graph;

import android.widget.Button;

public class AlgoSimulationData {

    private Button nodeButton;
    private int color;

    public AlgoSimulationData(Button nodeButton, int color) {
        this.nodeButton = nodeButton;
        this.color = color;
    }

    public Button getNodeButton() {
        return nodeButton;
    }

    public void setNodeButton(Button nodeButton) {
        this.nodeButton = nodeButton;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
