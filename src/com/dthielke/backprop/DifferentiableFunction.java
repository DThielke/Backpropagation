package com.dthielke.backprop;

public interface DifferentiableFunction {

    double eval(double x);
    
    double diff(double x);
    
}
