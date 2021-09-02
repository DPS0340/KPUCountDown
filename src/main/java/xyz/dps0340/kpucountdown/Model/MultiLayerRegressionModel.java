package xyz.dps0340.kpucountdown.Model;

import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings({"DuplicatedCode", "FieldCanBeLocal"})
public class MultiLayerRegressionModel {
    //Number of epochs (full passes of the data)
    private static final int nEpochs = 200;
    //Network learning rate
    private static final double learningRate = 0.01;
    private MultiLayerNetwork network = null;
    private boolean isTrained = false;
    public static final int DEFAULT_BATCH_SIZE = 50;

    public void initializeNeuralNetwork() {
        int numInput = 1;
        int numOutputs = 1;
        int nHidden = 10;

        network = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                .weightInit(WeightInit.XAVIER)
                .updater(new Nesterovs(learningRate, 0.9))
                .list()
                .layer(0, new DenseLayer.Builder().nIn(numInput).nOut(nHidden)
                        .activation(Activation.RELU)
                        .build())
                .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(nHidden).nOut(numOutputs).build())
                .build()
        );
        network.init();
        network.setListeners(new ScoreIterationListener(1));
    }

    public void train(List<Double> outputs, int batchSize) {
        if(network == null) {
            throw new IllegalStateException("Network not set. please initializeNeuralNetwork() before train.");
        }
        //Generate the training data
        DataSetIterator iterator = getTrainingData(outputs, batchSize);

        for (int i = 0; i < nEpochs; i++) {
            iterator.reset();
            network.fit(iterator);
        }
        isTrained = true;
    }

    public List<Double> inference(List<Integer> dots) {
        if(!isTrained) {
            throw new IllegalStateException("Network not trained. please train() before inference.");
        }

        final INDArray input = Nd4j.create(dots);
        INDArray out = network.output(input, false);

        List<Double> result = new ArrayList<>();
        Arrays.stream(out.toDoubleVector()).forEach(result::add);

        return result;
    }

    @SuppressWarnings("SameParameterValue")
    public static DataSetIterator getTrainingData(List<Double> outputs, int batchSize) {
        int outputLength = outputs.size();
        int[] inputArray = Stream.iterate(1, n -> n + 1).limit(outputLength).mapToInt(Integer::intValue).toArray();
        double[] outputArray = outputs.stream().mapToDouble(Double::doubleValue).toArray();

        INDArray input = Nd4j.create(inputArray);
        INDArray output = Nd4j.create(outputArray);

        DataSet dataSet = new DataSet(input, output);
        List<DataSet> listDs = dataSet.asList();
        Collections.shuffle(listDs);

        return new ListDataSetIterator<>(listDs, batchSize);

    }

    public static int getnEpochs() {
        return nEpochs;
    }

    public static double getLearningRate() {
        return learningRate;
    }

    public MultiLayerNetwork getNetwork() {
        return network;
    }

    public boolean isInitialized() {
        return network != null;
    }

    public boolean isTrained() {
        return isTrained;
    }
}
