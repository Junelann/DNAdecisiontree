package org.example.input;

import org.example.node.LeafNode;
import org.example.node.MiddleNode;
import org.example.node.Node;
import org.example.node.RootNode;
import org.example.process.NodeProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DataReader {
    private String filePath;

    // The number of nodes in a graph/tree.
    private int size = 0;

    NodeProcessor processor;

    int[][] matrix;

    // The table of node's in-degree.
    private final HashMap<Integer, Integer> inDegreeMap = new HashMap<>();
    // he table of node's out-degree.
    private final HashMap<Integer, Integer> outDegreeMap = new HashMap<>();
    public DataReader(String filePath, NodeProcessor processor){
        this.filePath = filePath;
        this.processor = processor;
    }

    public void readAndCreateGraph(LinkedList<Node> nodes) {
        readDataFromFile();
        processor.setAdjacentMatrix(matrix);
        size = matrix.length;
        updateDegreeOfNode(matrix);
        createNodes(nodes);
        buildParentChildRelationShip(matrix, nodes);
    }

    private void readDataFromFile() {
        List<int[]> matrixList = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] numbers = line.trim().split("\\s+");
                int[] row = new int[numbers.length];
                for (int i = 0; i < numbers.length; i++) {
                    row[i] = Integer.parseInt(numbers[i]);
                }
                matrixList.add(row);
            }
            matrix = new int[matrixList.size()][];
            for (int i = 0; i < matrix.length; i++) {
                matrix[i] = matrixList.get(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *  Convert the out-degree and in-degree relationships stored in a two-dimensional array
     *  into a map, making it convenient to calculate attribute values for each node.
     */
    private void updateDegreeOfNode(int[][] array) {
        for (Integer i = 0; i < size; ++i) {
            for(Integer j = 0; j < size; ++j) {
                if(array[i][j] > 0) {
                    outDegreeMap.put(i, outDegreeMap.getOrDefault(i, 0) + 1);
                    inDegreeMap.put(j, inDegreeMap.getOrDefault(j, 0) + 1);
                }
            }
        }
    }

    /**
     *  Create a node, and set the out-degree and in-degree information; whether it is a root node;
     *  whether it is a leaf node; node number (for debugging assistance).
     */
    private void createNodes(LinkedList<Node> nodes) {
        for (Integer i = 0; i < size; ++i) {
            Integer inDegree = inDegreeMap.getOrDefault(i, 0);
            Integer outDegree = outDegreeMap.getOrDefault(i, 0);

            Boolean isRootNode = ((inDegree == 0) && (outDegree > 0)); // Whether it is a root node.
            Boolean isLeafNode = ((inDegree > 0) && (outDegree == 0)); // Whether it is a leaf node.

            Node node = createNode(isRootNode, isLeafNode);
            node.index = i; //  Node Number
            nodes.add(node);
        }
    }

    private Node createNode(Boolean isRootNode, Boolean isLeafNode) {
        if (isRootNode) {
            return new RootNode();
        } else if (isLeafNode) {
            return new LeafNode();
        } else {
            return new MiddleNode();
        }
    }

    //  Establish parent-child relationships between nodes to prepare for subsequent sequence construction
    private void buildParentChildRelationShip(int[][] array, LinkedList<Node> nodes) {
        for (int i = 0; i < size; ++i) {
            for(int j = 0; j <size; ++j) {
                if(array[i][j] > 0) {
                    nodes.get(i).children.add(nodes.get(j));
                    nodes.get(j).parents.add(nodes.get(i));
                }
            }
        }
    }

}
