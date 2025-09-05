package org.example.node;

import java.util.ArrayList;
import static org.example.constant.DNSSequenceConstants.FIXED_SEQUENCE_LEAF_NODE_A_SEGMENT;
import static org.example.constant.DNSSequenceConstants.SUBSEQUENCE_B;
import static org.example.utils.StringUtils.getReverseComplementSequence;

public class LeafNode extends Node {

    @Override
    public void generateOriginDNASequence(int[][] matrix) {
        String sequence = generateSegmentA() + generateSegmentB();
        ArrayList<String> selfDNASequence = new ArrayList<String>();
        selfDNASequence.add(sequence);
        originDNASequence.put(-1, selfDNASequence);
    }
    @Override
    String generateSegmentA() {
        return FIXED_SEQUENCE_LEAF_NODE_A_SEGMENT;
    }

    @Override
    String generateSegmentC() {
        return "";
    }

    @Override
    String generateSegmentD(int childIndex) {
        return "";
    }

    @Override
    public void generateResultDNASequence() {
        String originString = originDNASequence.get(-1).get(0);
        String segmentB = originString.substring(originString.length() - SUBSEQUENCE_B);
        String resultSequence = buildTopSegment("", segmentB, "", "", -1) + "," + buildBTM1Segment("", segmentB, "", "", -1);
        ArrayList<String> resultArray = new ArrayList<>();
        resultArray.add(resultSequence);
        resultDNASequence.put(-1, resultArray);
    }

    @Override
    String buildTopSegment(String segmentA, String segmentB, String segmentC, String segmentD, int childIndex) {
        return getReverseComplementSequence(segmentB);
    }

    @Override
    String buildBTM1Segment(String segmentA, String segmentB, String segmentC, String segmentD, int childIndex) {
        return FIXED_SEQUENCE_LEAF_NODE_A_SEGMENT + segmentB;
    }

}
