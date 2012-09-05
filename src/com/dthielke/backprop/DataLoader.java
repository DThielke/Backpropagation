package com.dthielke.backprop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataLoader {
    
    private double[][] inputs;
    private double[][] targets;
    
    public void load(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner fileScanner = new Scanner(file);
        
        int status = 0;
        int entries = 0;
        int entry = 0;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (!line.startsWith("%") && !line.trim().isEmpty()) {
                Scanner lineScanner = new Scanner(line);
                switch (status++) {
                case 0:
                    lineScanner.skip("entries:");
                    entries = lineScanner.nextInt();
                    break;
                case 1:
                    lineScanner.skip("inputs:");
                    inputs = new double[entries][lineScanner.nextInt()];
                    break;
                case 2:
                    lineScanner.skip("outputs:");
                    targets = new double[entries][lineScanner.nextInt()];
                    break;
                default:
                    lineScanner.useDelimiter(",");
                    for (int i = 0; i < inputs[0].length; ++i) {
                        inputs[entry][i] = lineScanner.nextDouble();
                    }
                    for (int i = 0; i < targets[0].length; ++i) {
                        targets[entry][i] = lineScanner.nextDouble();
                    }
                    ++entry;
                }
                lineScanner.close();
            }
        }
        fileScanner.close();
    }
    
    public double[][] getInputs() {
        return inputs;
    }
    
    public double[][] getTargets() {
        return targets;
    }
    
}
