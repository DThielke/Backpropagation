package com.dthielke.backprop;

import java.util.ArrayList;
import java.util.List;

public class InputNode implements Node {
    
    private static int nextId = 1;
    private final int id;
    
    private List<Link> outputs = new ArrayList<Link>();
    private double value;
    
    public InputNode() {
        this.value = 0;
        this.id = nextId++;
    }
    
    public InputNode(double value) {
        this.value = value;
        this.id = nextId++;
    }
    
    public void setValue(double value) {
        this.value = value;
    }
    
    @Override
    public void addLink(Link link) {
        if (link.getHead().equals(this) && !outputs.contains(link)) {
            outputs.add(link);
            link.getTail().addLink(link);
        }
    }
    
    @Override
    public double getActivation() {
        return value;
    }
    
    @Override
    public double getDelta() {
        return 0;
    }
    
    @Override
    public void clearCachedActivation() {}

    @Override
    public void clearCachedDelta() {}

    @Override
    public void train(double rate, double momentum, double decay) {}

    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return "InputNode " + id;
    }

    @Override
    public List<Link> getLinks() {
        return outputs;
    }
    
}
