package com.dthielke.nnet.backprop;

public class HyperbolicTangent implements DifferentiableFunction {
    
    private static HyperbolicTangent instance;
    
    private HyperbolicTangent() {}
    
    public static HyperbolicTangent getInstance() {
        if (instance == null) {
            instance = new HyperbolicTangent();
        }
        
        return instance;
    }

    @Override
    public double eval(double x) {
        return Math.tanh(x);
    }

    @Override
    public double diff(double x) {
        double o = 1.0 / Math.cosh(x);
        return o * o;
    }

}
