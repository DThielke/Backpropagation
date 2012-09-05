package com.dthielke.nnet.backprop;

public interface DifferentiableFunction {

    double eval(double x);
    
    double diff(double x);
    
}
