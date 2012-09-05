package com.dthielke.backprop;

import java.util.List;

public interface Node {

    double getActivation();
    
    double getDelta();
    
    void clearCachedActivation();
    
    void clearCachedDelta();
    
    void addLink(Link link);
    
    void train(double rate, double momentum, double decay);
    
    int getId();
    
    List<Link> getLinks();
    
}
