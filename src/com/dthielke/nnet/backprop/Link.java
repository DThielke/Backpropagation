package com.dthielke.nnet.backprop;

public class Link {

    private Node head;
    private Node tail;
    private double weight;
    private double previousWeight;
    
    public static void create(Node head, Node tail, double weight) {
        new Link(head, tail, weight);
    }
    
    public Link(Node head, Node tail, double weight) {
        this.head = head;
        this.tail = tail;
        this.weight = weight;
        this.previousWeight = weight;
        head.addLink(this);
        tail.addLink(this);
    }
    
    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public void incrementWeight(double dw) {
        this.previousWeight = weight;
        this.weight += dw;
    }
    
    public double getWeightChange() {
        return weight - previousWeight;
    }
    
    public double getPreviousWeight() {
        return previousWeight;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((head == null) ? 0 : head.hashCode());
        result = prime * result + ((tail == null) ? 0 : tail.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Link other = (Link) obj;
        if (head == null) {
            if (other.head != null)
                return false;
        } else if (!head.equals(other.head))
            return false;
        if (tail == null) {
            if (other.tail != null)
                return false;
        } else if (!tail.equals(other.tail))
            return false;
        return true;
    }
    
}
