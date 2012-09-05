package com.dthielke.backprop;

import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Driver {

    private static BackPropNetwork network;
    private static BackPropNetworkPanel networkPanel;

    private static List<Double> errorBuffer = new LinkedList<Double>();
    private static XYSeries errors = new XYSeries("RMS Error");

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        int[] structure = { 2, 12, 1 };
        network = new BackPropNetwork(structure, 1.0, 0.4, 0.6);
        createGUI();
        
        DataLoader loader = new DataLoader();
        loader.load("data/catheter.data");
        double[][] inputs = loader.getInputs();
        double[][] targets = loader.getTargets();

        int draw = 0;
        while (true) {
            int dataset = (int) (Math.random() * inputs.length);
            network.train(inputs[dataset], targets[dataset], 0.1, 0.0, 0.0);
            if (++draw == 50) {
                draw = 0;
                networkPanel.repaint();
            }
            synchronized (errorBuffer) {
                errorBuffer.add(network.getError());
            }
            Thread.sleep(1);
        }
    }

    public static void createGUI() {
        networkPanel = new BackPropNetworkPanel(network);

        XYSeriesCollection data = new XYSeriesCollection(errors);
        JFreeChart chart = ChartFactory.createXYLineChart("", "Epoch", "Error", data, PlotOrientation.VERTICAL, false, false, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chart.setAntiAlias(true);
        chart.setTextAntiAlias(true);

        JFrame frame = new JFrame("Backpropagation Neural Network");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(1, 2));
        frame.getContentPane().add(networkPanel);
        frame.getContentPane().add(chartPanel);
        frame.setSize(800, 600);
        frame.pack();
        frame.setVisible(true);

        new Thread(new GraphUpdater()).start();
    }

    static class GraphUpdater implements Runnable {

        private int epoch = 0;

        @Override
        public void run() {
            while (true) {
                synchronized (errorBuffer) {
                    errors.setNotify(false);
                    for (double error : errorBuffer) {
                        errors.add(++epoch, error);
                    }
                    errorBuffer.clear();
                    errors.setNotify(true);
                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            }
        }

    }

}
