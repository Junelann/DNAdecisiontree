package org.example.process;

import org.example.node.Node;
import org.example.input.DataReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class NodeProcessor {
    LinkedList<Node> nodes = new LinkedList<>(); //All nodes

    int[][] adjacentMatrix;

    public void setAdjacentMatrix(int[][] matrix) {
        adjacentMatrix = matrix;
    }

    // Read all input information, generate nodes and store them.
    public void readExcelAndGenerateNodes(String inputFilePath) {
        DataReader dataReader = new DataReader(inputFilePath, this);
        dataReader.readAndCreateGraph(nodes);
    }

    //  Generate original & result DNA sequence information.
    public void generateDNASequence() {
        generateOriginDNASequence();
        generateResultDNASequence();
    }

    // Print the original DNA sequence information.
    public void printDNAOriginSequence() {
        for (Node node: nodes) {
            node.printOriginDNASequence();
        }
    }

    //  Print DNA sequence results information.
    public void printResultDNASequence(String outputFilePath) {
        BufferedWriter writer = null;
        try {
            // Create BufferedWriter
            writer = new BufferedWriter(new FileWriter(outputFilePath));
            for (Node node : nodes) {
                node.printResultDNASequence(writer);
            }
        } catch (IOException e) {
            //  Handle possible IO exceptions
            e.printStackTrace();
        } finally {
            //  Finally, make sure the BufferedWriter is closed.
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //  Generate original DNA sequence information.
    private void generateOriginDNASequence() {
        for (Node node: nodes) {
            node.generateOriginDNASequence(adjacentMatrix);
        }
    }

    //  Generate the result of DNA sequence information.
    private void generateResultDNASequence() {
        for (Node node: nodes) {
            node.generateResultDNASequence();
        }
    }
}