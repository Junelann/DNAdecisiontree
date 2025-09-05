package org.example.node;

import static org.example.constant.DNSSequenceConstants.*;
import static org.example.utils.StringUtils.*;

// Root node
public class RootNode extends Node {

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
        String commonString = segmentA + segmentB + segmentC;
        if (childNode instanceof LeafNode) {
            result = commonString + childNode.originDNASequence.get(-1).get(0).substring(0, 8);

        } else {
            result = commonString + FIXED_SEQUENCE_MIDDLE_NODE_A_SEGMENT + segmentD.substring(0, 6);
        }
        return result;
    }

    @Override
    String buildBTM1Segment(String segmentA, String segmentB, String segmentC, String segmentD, int childIndex) {
        return getReverseComplementSequence(segmentB) + getReverseComplementSequence(segmentA);
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
        String substring1 = segmentC.substring(segmentC.length() - 18);
        if(childNode instanceof LeafNode) {
            //  The reverse complementary sequence of leaf sequence B.
            String childNodeSegment = childNode.originDNASequence.get(-1).get(0);
            String childNodeSegmentB = childNodeSegment.substring(childNodeSegment.length() - SUBSEQUENCE_B);
            String reverseChildNodeSegmentB = getReverseComplementSequence(childNodeSegmentB);

            String reverseFixedSequence = getReverseComplementSequence(FIXED_SEQUENCE_LEAF_NODE_A_SEGMENT);

            //  The reverse complementary sequence of the 18 bases after the C segment.
            String reverseSubSegmentC = getReverseComplementSequence(substring1);
            result = reverseChildNodeSegmentB + reverseFixedSequence + reverseSubSegmentC;
        } else {
            String reverseSegmentD = getReverseComplementSequence(segmentD);
            String reverseFixedSequence = getReverseComplementSequence(FIXED_SEQUENCE_MIDDLE_NODE_A_SEGMENT);

            // The reverse complementary sequence of the 18 bases after the C segment.
            String reverseSubSegmentC = getReverseComplementSequence(substring1);
            result = reverseSegmentD + reverseFixedSequence + reverseSubSegmentC;
        }
        return result;
    }

}
