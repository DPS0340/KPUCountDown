package xyz.dps0340.kpucountdown.Model;

import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.modelimport.keras.KerasLayer;
import org.deeplearning4j.nn.modelimport.keras.layers.core.KerasLambda;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.AdaGrad;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings({"DuplicatedCode", "FieldCanBeLocal"})
public class MultiLayerRegressionModel implements NeuralNetworkModel<Double, Double> {
    //Number of epochs (full passes of the data)
    private static final int nEpochs = 500;
    //Network learning rate
    private static final double learningRate = 0.0005;
    private MultiLayerNetwork network = null;
    private boolean isTrained = false;
    public static final int DEFAULT_BATCH_SIZE = 50;

    public void initializeNeuralNetwork() {
        int numInput = 1;
        int numOutputs = 1;
        int nHidden = 128;

        network = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                .weightInit(WeightInit.XAVIER_UNIFORM)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Adam(learningRate))
                .list()
                .layer(new DenseLayer.Builder().nIn(numInput).nOut(nHidden)
                        .activation(Activation.RELU)
                        .build())
                .layer(new DenseLayer.Builder().nIn(nHidden).nOut(nHidden)
                        .activation(Activation.SIGMOID)
                        .build())
                .layer(new DenseLayer.Builder().nIn(nHidden).nOut(nHidden)
                        .activation(Activation.CUBE)
                        .build())
                .layer(new DenseLayer.Builder().nIn(nHidden).nOut(nHidden)
                        .activation(Activation.SIGMOID)
                        .build())
                .layer(new DenseLayer.Builder().nIn(nHidden).nOut(nHidden)
                        .activation(Activation.SOFTPLUS)
                        .build())
                .layer(new DenseLayer.Builder().nIn(nHidden).nOut(nHidden)
                        .activation(Activation.CUBE)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(nHidden).nOut(numOutputs).build())
                .build()
        );
        network.init();
        network.setListeners(new ScoreIterationListener(50));
    }

    public void train(List<Double> outputs, int batchSize) {
        if(network == null) {
            throw new IllegalStateException("Network not set. please initializeNeuralNetwork() before train.");
        }
        //Generate the training data
        DataSetIterator iterator = getTrainingData(outputs, batchSize);

        for (int i = 0; i < nEpochs; i++) {
            if(i % 50 == 0 || i == nEpochs - 1) {
                System.out.println(String.format("epoch %d", i));
            }
            iterator.reset();
            network.fit(iterator);
        }
        isTrained = true;
    }

    public List<Double> inference(List<Double> inputs) {
        if(!isTrained) {
            throw new IllegalStateException("Network not trained. please train() before inference.");
        }

        int inputLength = inputs.size();
        double[] inputsArray = inputs.stream().mapToDouble(Double::doubleValue).toArray();

        final INDArray input = Nd4j.create(inputsArray, inputLength, 1);
        INDArray out = network.output(input, false);

        List<Double> result = new ArrayList<>();
        Arrays.stream(out.toDoubleVector()).forEach(result::add);

        return result;
    }

    @SuppressWarnings("SameParameterValue")
    public static DataSetIterator getTrainingData(List<Double> outputs, int batchSize) {
        int outputLength = outputs.size();
        double[] inputArray = Stream.iterate(1, n -> n + 1).limit(outputLength).map(e -> (double)e).mapToDouble(Double::doubleValue).toArray();
        double[] outputArray = outputs.stream().mapToDouble(Double::doubleValue).toArray();

        INDArray input = Nd4j.create(inputArray, outputLength, 1);
        INDArray output = Nd4j.create(outputArray, outputLength, 1);

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
