package com.dthielke.nnet.backprop;

import java.util.ArrayList;
import java.util.List;

public class BackPropNetwork {

    private List<Node>[] layers;
    private Node bias;
    
    public BackPropNetwork(int[] structure, double bias, double minWeight, double maxWeight) {
        minWeight = Math.abs(minWeight);
        maxWeight = Math.abs(maxWeight);
        
        this.bias = new InputNode(bias);
        this.layers = new ArrayList[structure.length];

        // create the nodes
        for (int i = 0; i < structure.length; ++i) {
            layers[i] = new ArrayList<Node>();
            for (int j = 0; j < structure[i]; ++j) {
                if (i == 0) {
                    layers[i].add(new InputNode());
                } else if (i == structure.length - 1) {
                    layers[i].add(new OutputNode(Sigmoid.getInstance()));
                } else {
                    layers[i].add(new HiddenNode(Sigmoid.getInstance()));
                }
            }
        }
        
        // link the layer nodes
        for (int i = 0; i < structure.length - 1; ++i) {
            for (int j = 0; j < structure[i]; ++j) {
                for (int k = 0; k < structure[i + 1]; ++k) {
                    double weight = (Math.random() > 0.5 ? 1 : -1) * (minWeight + (Math.random() * (maxWeight - minWeight)));
                    Link.create(layers[i].get(j), layers[i + 1].get(k), weight);
                }
            }
        }
        
        // link the bias node
        for (int i = 1; i < structure.length; ++i) {
            for (int j = 0; j < structure[i]; ++j) {
                double weight = (Math.random() > 0.5 ? 1 : -1) * (minWeight + (Math.random() * (maxWeight - minWeight)));
                Link.create(this.bias, layers[i].get(j), weight);
            }
        }
    }
    
    public double[] run(double[] inputs) {
        if (inputs.length != layers[0].size()) {
            throw new IllegalArgumentException("Input count does not match network dimensions");
        }
        
        // update input nodes with the training data
        for (int i = 0; i < inputs.length; ++i) {
            ((InputNode) layers[0].get(i)).setValue(inputs[i]);
        }
        
        // clear cached values from the prior training period
        clearCaches();
        
        // calculate the new outputs
        List<Node> outputLayer = layers[layers.length - 1];
        double[] outputs = new double[outputLayer.size()];
        for (int i = 0; i < outputs.length; ++i) {
            outputs[i] = outputLayer.get(i).getActivation();
        }
        
        return outputs;
    }
    
    public double getError() {
        // calculate SSE of output nodes compared to training data expectations
        double error = 0;
        for (Node node : layers[layers.length - 1]) {
            double e = node.getActivation() - ((OutputNode)node).getTarget();
            error += e * e;
        }
        return Math.sqrt(error / layers[layers.length - 1].size());
    }
    
    private void clearCaches() {
        for (int i = 0; i < layers.length; ++i) {
            for (int j = 0; j < layers[i].size(); ++j) {
                layers[i].get(j).clearCachedActivation();
                layers[i].get(j).clearCachedDelta();
            }
        }
    }
    
    public void train(double[] inputs, double[] targets, double rate, double momentum, double decay) {
        if (inputs.length != layers[0].size()) {
            throw new IllegalArgumentException("Input count does not match network dimensions");
        }
        
        if (targets.length != layers[layers.length - 1].size()) {
            throw new IllegalArgumentException("Target count does not match network dimensions");
        }
        
        // run the network forwards
        run(inputs);

        // update weights starting at the output layer
        for (int i = layers.length - 1; i > 0; --i) {
            for (int j = 0; j < layers[i].size(); ++j) {
                if (i == layers.length - 1) {
                    ((OutputNode) layers[i].get(j)).setTarget(targets[j]);
                }
                layers[i].get(j).train(rate, momentum, decay);
            }
        }
    }
    
    public List<Node>[] getLayers() {
        return layers;
    }
    
    public Node getBiasNode() {
        return bias;
    }
    
}
