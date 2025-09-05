package org.example.node;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

import static org.example.constant.DNSSequenceConstants.*;
import static org.example.utils.StringUtils.*;

abstract public class Node {

    public Integer index = -1;
    public ArrayList<Node> children = new ArrayList<>(); //  Child node list
    public ArrayList<Node> parents = new ArrayList<>(); //  Parent node list

    /**
     *  Key: Child node number, Value: Sequence corresponding to the edge pointing to the child node (multiple may exist).
     */
    public Map<Integer, ArrayList<String>> originDNASequence = new HashMap<>();

    /**
     * Key: Child node number, Value: Sequence corresponding to the edge pointing to the child node (multiple may exist).
     * The leaf node outputs 2 sequences; the number of sequences output by the remaining nodes equals the outdegree * 4 (each edge corresponds to 4 sequences, which are top, btm1, btm2, input).
     */
    public Map<Integer, ArrayList<String>> resultDNASequence = new HashMap<>();

    // The generated original sequence.
    public void generateOriginDNASequence(int[][] matrix) {
        String segmentA = generateSegmentA();
        String segmentB = generateSegmentB();
        for (Node child : children) {
            int sideNum = matrix[index][child.index];
            ArrayList<String> childDNASequence = new ArrayList<String>();
            while (sideNum > 0) {
                String sequence = segmentA + segmentB + generateSegmentC() + generateSegmentD(child.index);
                childDNASequence.add(sequence);
                --sideNum;
            }
            originDNASequence.put(child.index, childDNASequence);
        }
    }

    // Generate result sequence
    public void generateResultDNASequence() {
        for (Map.Entry<Integer, ArrayList<String>> entry : originDNASequence.entrySet()) {
            Integer childIndex = entry.getKey();
            ArrayList<String> sequences = entry.getValue();
            ArrayList<String> resultSequences = new ArrayList<>();
            for (String sequence : sequences) {
                String segmentA = getSegmentA(sequence);
                String segmentB = getSegmentB(sequence);
                String segmentC = getSegmentC(sequence);
                String segmentD = getSegmentD(sequence);
                String resultString = buildTopSegment(segmentA, segmentB, segmentC, segmentD, childIndex) + ","
                        + buildBTM1Segment(segmentA, segmentB, segmentC, segmentD, childIndex) + ","
                        + buildBTM2Segment(segmentA, segmentB, segmentC, segmentD, childIndex) + ","
                        + buildInputSegment(segmentA, segmentB, segmentC, segmentD, childIndex);
                resultSequences.add(resultString);
            }
            resultDNASequence.put(childIndex, resultSequences);
        }
    }

    String buildTopSegment(String segmentA, String segmentB, String segmentC, String segmentD, int childIndex) {
        return "";
    }

    String buildBTM1Segment(String segmentA, String segmentB, String segmentC, String segmentD, int childIndex) {
        return "";
    }

    String buildBTM2Segment(String segmentA, String segmentB, String segmentC, String segmentD, int childIndex) {
        return "";
    }

    String buildInputSegment(String segmentA, String segmentB, String segmentC, String segmentD, int childIndex) {
        String reverseFixedSequence = getReverseComplementSequence(FIXED_SEQUENCE_MIDDLE_NODE_A_SEGMENT);
        String reverseSegmentC = getReverseComplementSequence(segmentC);
        String subSegmentB = segmentB.substring(segmentB.length() - 13);
        String reverseSubSegmentB = getReverseComplementSequence(subSegmentB);
        return reverseFixedSequence + reverseSegmentC + reverseSubSegmentB;
    }

    String generateSegmentA() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; ++i) {
            sb.append(letters[random.nextInt(letters.length)]);
        }
        return sb.toString();
    }

    String generateSegmentB() {
        Random random = new Random();
        double ratioG = random.nextDouble() * 0.2 + 0.4;
        int numG = (int) (ratioG * SUBSEQUENCE_B);
        char[] str = getFullATSequence(SUBSEQUENCE_B);
        for (int i = 0; i < numG; ++i) {
            int position = random.nextInt(SUBSEQUENCE_B);
            while (str[position] == 'G' || checkContinuousGLength(str, position)) {
                position = random.nextInt(SUBSEQUENCE_B);
            }
            str[position] = 'G';
        }
        return new String(str);
    }

    String generateSegmentC() {
        Random random = new Random();
        double ratioG = random.nextDouble() * 0.2 + 0.4;
        int numG = (int) (ratioG * SUBSEQUENCE_C);
        char[] str = getFullATSequence(SUBSEQUENCE_C);
        //  In the first 5 bases, G=3.
        for (int i = 0; i < 3; ++i) {
            int position = random.nextInt(5);
            while (str[position] == 'G') {
                position = random.nextInt(5);
            }
            str[position] = 'G';
        }
        for (int i = 0; i < numG - 3; ++i) {
            int lowBound = 5;
            int position = lowBound + random.nextInt(SUBSEQUENCE_C - lowBound);
            while (str[position] == 'G' || checkContinuousGLength(str, position)) {
                position = random.nextInt(SUBSEQUENCE_C);
            }
            str[position] = 'G';
        }
        return new String(str);
    }

    String generateSegmentD(int childIndex) {
        Random random = new Random();
        double ratioG = random.nextDouble() * 0.2 + 0.4;
        int numG = (int) (ratioG * SUBSEQUENCE_D);
        char[] str = getFullATSequence(SUBSEQUENCE_D);
        //  In the first 3 bases, G=1.
        int index = random.nextInt(3);
        str[index] = 'G';
        for (int i = 0; i < numG - 1; ++i) {
            int lowBound = 1;
            int position = lowBound + random.nextInt(SUBSEQUENCE_D - lowBound);
            while (str[position] == 'G' || checkContinuousGLength(str, position)) {
                position = random.nextInt(SUBSEQUENCE_D);
            }
            str[position] = 'G';
        }
        return new String(str);
    }

    //  Print the original sequence
    public void printOriginDNASequence() {

    }

    //  Print the result sequence
    public void printResultDNASequence(BufferedWriter writer) throws IOException {
        for (Map.Entry<Integer, ArrayList<String>> entry : resultDNASequence.entrySet()) {
            Integer key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            for (String sequence : value) {
                writer.write("selfIndex:" + index + "   " + "childIndex" + key + "  " + "sequence:" + sequence);
                writer.newLine();
            }
        }
        writer.newLine(); //  Write a newline character
    }

}

