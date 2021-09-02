package xyz.dps0340.kpucountdown.Model;

import java.util.List;

public interface NeuralNetworkModel<inputType, outputType> {
    void initializeNeuralNetwork();
    void train(List<outputType> outputs, int batchSize);
    List<outputType> inference(List<inputType> inputs);
}
