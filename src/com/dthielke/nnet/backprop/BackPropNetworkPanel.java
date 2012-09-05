package com.dthielke.nnet.backprop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BackPropNetworkPanel extends JPanel {

    private BackPropNetwork network;

    public BackPropNetworkPanel(BackPropNetwork network) {
        this.network = network;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        int border = 10;
        int wNet = getNetworkWidth();
        int hNet = getNetworkHeight();
        int hSpacing = (int) ((getWidth() - border * 2) / (wNet - 0.5));
        int vSpacing = (int) ((getHeight() - border * 2) / (hNet - 0.5));
        int size = Math.min(hSpacing / 2, vSpacing / 2);
        int lineWidth = size / 5;

        // draw the bias node first
        drawNode(g, network.getBiasNode(), border + hSpacing / 2, border, size);

        // draw the layer nodes
        List<Node>[] layers = network.getLayers();
        for (int i = 0; i < layers.length; ++i) {
            for (int j = 0; j < layers[i].size(); ++j) {
                int x = border + hSpacing * i;
                int y = border + (int) ((hNet - 1 - layers[i].size()) / 2.0 * vSpacing) + vSpacing * (j + 1);
                drawNode(g, layers[i].get(j), x, y, size);
            }
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // draw the bias weights
        if (((InputNode) network.getBiasNode()).getActivation() != 0) {
            int xBias = border + size / 2 + hSpacing / 2;
            int yBias = border + size / 2;
            for (int i = 1; i < layers.length; ++i) {
                for (int j = 0; j < layers[i].size(); ++j) {
                    int xNode = border + hSpacing * i + size / 2;
                    int yNode = border + (int) ((hNet - 1 - layers[i].size()) / 2.0 * vSpacing) + vSpacing * (j + 1) + size / 2;

                    double weight = getWeight(network.getBiasNode(), layers[i].get(j));
                    float shade = (float) Math.tanh(weight);
                    if (shade > 0) {
                        g2d.setColor(new Color(0, shade, 0));
                    } else {
                        g2d.setColor(new Color(-shade, 0, 0));
                    }
                    g2d.setStroke(new BasicStroke(Math.abs(shade) * lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2d.drawLine(xBias, yBias, xNode, yNode);
                }
            }
        }

        // draw the layer weights
        for (int i = 0; i < layers.length - 1; ++i) {
            for (int j = 0; j < layers[i].size(); j++) {
                for (int k = 0; k < layers[i + 1].size(); k++) {
                    int x1 = border + hSpacing * i + size / 2;
                    int y1 = border + (int) ((hNet - 1 - layers[i].size()) / 2.0 * vSpacing) + vSpacing * (j + 1) + size / 2;
                    int x2 = border + hSpacing * (i + 1) + size / 2;
                    int y2 = border + (int) ((hNet - 1 - layers[i + 1].size()) / 2.0 * vSpacing) + vSpacing * (k + 1) + size / 2;

                    double weight = getWeight(layers[i].get(j), layers[i + 1].get(k));
                    float shade = (float) Math.tanh(weight);
                    if (shade > 0) {
                        g2d.setColor(new Color(0, shade, 0));
                    } else {
                        g2d.setColor(new Color(-shade, 0, 0));
                    }
                    g2d.setStroke(new BasicStroke(Math.abs(shade) * lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2d.drawLine(x1, y1, x2, y2);
                }
            }
        }
    }

    private double getWeight(Node head, Node tail) {
        for (Link link : head.getLinks()) {
            if (link.getTail().equals(tail)) {
                return link.getWeight();
            }
        }
        return 0;
    }

    private void drawNode(Graphics g, Node node, int x, int y, int size) {
        float shade = (float) Math.tanh(node.getActivation());
        if (shade > 0) {
            g.setColor(new Color(0, shade, 0));
        } else {
            g.setColor(new Color(-shade, 0, 0));
        }
        g.fillRect(x, y, size, size);
    }

    private int getNetworkWidth() {
        return network.getLayers().length;
    }

    private int getNetworkHeight() {
        int max = 0;
        for (List<Node> layer : network.getLayers()) {
            if (layer.size() > max) {
                max = layer.size();
            }
        }
        return max + 1; // one added for bias
    }

}
