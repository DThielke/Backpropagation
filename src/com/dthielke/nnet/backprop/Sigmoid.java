package com.dthielke.nnet.backprop;

public class Sigmoid implements DifferentiableFunction {

    private static Sigmoid instance;
    
    private Sigmoid() {}
    
    public static Sigmoid getInstance() {
        if (instance == null) {
            instance = new Sigmoid();
        }
        
        return instance;
    }
    
    @Override
    public double eval(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }
    
    @Override
    public double diff(double x) {
        double o = eval(x);
        return o * (1 - o);
    }

}
