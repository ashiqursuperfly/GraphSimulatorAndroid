package s.ashiqur.graphsimulator.graph;

import android.widget.Button;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public interface AnimatableGraph extends GraphConstants {

    void setChangeNodeColorQueue(BlockingQueue<AlgoSimulationData> changeNodeColorQueue);

    void setNodeButtons(ArrayList<Button> nodeButtons);
}
