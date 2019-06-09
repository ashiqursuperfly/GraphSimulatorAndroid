package s.ashiqur.graphsimulator.graph;

import android.widget.Button;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public interface AnimatableGraph extends AnimatableGraphConstants {

    void setAlgoSimulationStepsQueue(BlockingQueue<AlgoSimulationStepData> changeNodeColorQueue);

    ArrayList<Button> getNodeButtons();

    void setNodeButtons(ArrayList<Button> nodeButtons);

}
