package org.example.node;

import java.util.ArrayList;

import static org.example.constant.DNSSequenceConstants.*;
import static org.example.utils.StringUtils.getReverseComplementSequence;

public class MiddleNode extends Node {
    @Override
    public void generateOriginDNASequence(int[][] matrix) {
        //  Prioritize generating the original sequence for the parent node, because segment B is completely equal to segment D of the parent node.
        for (Node node : parents) {
            if (node.originDNASequence.isEmpty()) {
                node.generateOriginDNASequence(matrix);
            }
        }
        syncParentsSegmentD();
        //  Prioritize generating the original sequence for leaf child nodes, because the D segment depends on the leaf node sequence.
        for (Node node : children) {
            if ((node instanceof LeafNode) && node.originDNASequence.isEmpty()) {
                node.generateOriginDNASequence(matrix);
            }
        }
        super.generateOriginDNASequence(matrix);
    }

    /**
     *  The D segment of the sequence corresponding to the edges pointing to the same node is the same.
     */
    private void syncParentsSegmentD() {
        String segment = null;
        for (Node node : parents) {
            if (segment == null) {
                String str = node.originDNASequence.get(index).get(0);
                segment = str.substring(str.length() - SUBSEQUENCE_D);
            }
            ArrayList<String> array = node.originDNASequence.get(index);
            ArrayList<String> result = new ArrayList<>();
            for (String sequence : array) {
                String replace = sequence.substring(0, sequence.length() - SUBSEQUENCE_D) + segment;
                result.add(replace);
            }
            node.originDNASequence.put(index, result);
        }
    }

    @Override
    String generateSegmentA() {
        return FIXED_SEQUENCE_MIDDLE_NODE_A_SEGMENT;
    }

    @Override
    String generateSegmentB() {
        String str = parents.get(0).originDNASequence.get(index).get(0);
        return str.substring(str.length() - SUBSEQUENCE_D);
    }

    /**
     * 1.  If the child node is a leaf node: The D segment equals the leaf node sequence.
     * 2.  If the child node is a non-leaf node: The D segment is generated in the default way.
     */
    @Override
    String generateSegmentD(int childIndex) {
        Node childNode = null;
        for (Node node : children) {
            if (node.index == childIndex) {
                childNode = node;
                break;
            }
        }
        if (childNode instanceof LeafNode) {
            return childNode.originDNASequence.get(-1).get(0);
        } else {
            return super.generateSegmentD(childIndex);
        }
    }

    @Override
    String buildTopSegment(String segmentA, String segmentB, String segmentC, String segmentD, int childIndex) {
        Node childNode = null;
        for (Node node : children) {
            if (node.index == childIndex) {
                childNode = node;
                break;
            }
        }
        String result = "";
        String commonString = FIXED_SEQUENCE_MIDDLE_NODE_A_SEGMENT + segmentB + segmentC;
        if (childNode instanceof LeafNode) {
            // 叶子序列B段的前2个碱基
            String childNodeSequence = childNode.originDNASequence.get(-1).get(0);
            String childNodeSegmentB = childNodeSequence.substring(childNodeSequence.length() - SUBSEQUENCE_B);
            result = commonString + FIXED_SEQUENCE_LEAF_NODE_A_SEGMENT + childNodeSegmentB.substring(0, 2);
        } else {
            result = commonString + FIXED_SEQUENCE_MIDDLE_NODE_A_SEGMENT + segmentD.substring(0, 6);
        }
        return result;
    }

    @Override
    String buildBTM1Segment(String segmentA, String segmentB, String segmentC, String segmentD, int childIndex) {
        //  The reverse complementary sequence of the first 5 bases in the C segment.
        String subSegmentC = segmentC.substring(0, 5);
        String reverseSubSegmentC = getReverseComplementSequence(subSegmentC);

        //  The reverse complementary sequence of the 15 bases after segment B.
        String subSegmentB = segmentB.substring(segmentB.length() - 15);
        String reverseSubSegmentB = getReverseComplementSequence(subSegmentB);
        return reverseSubSegmentC + reverseSubSegmentB;
    }

    @Override
    String buildBTM2Segment(String segmentA, String segmentB, String segmentC, String segmentD, int childIndex) {
        Node childNode = null;
        for (Node node : children) {
            if (node.index == childIndex) {
                childNode = node;
                break;
            }
        }
        String result = "";
        String subsegmentC = segmentC.substring(segmentC.length() - 20);
        String commonString = getReverseComplementSequence(subsegmentC);
        if (childNode instanceof LeafNode) {
            result = getReverseComplementSequence(segmentD) + getReverseComplementSequence(FIXED_SEQUENCE_MIDDLE_NODE_A_SEGMENT) + commonString;
        } else {
            String childNodeSequence = childNode.originDNASequence.get(-1).get(0);
            String childNodeSegmentB  = childNodeSequence.substring(childNodeSequence.length() - SUBSEQUENCE_B);
            String reverseSegmentB = getReverseComplementSequence(childNodeSegmentB);
            result = reverseSegmentB + getReverseComplementSequence(FIXED_SEQUENCE_LEAF_NODE_A_SEGMENT) + commonString;
        }
        return result;
    }

}
