package org.example;

import org.example.process.NodeProcessor;

public class ProgramEntryPoint {
    public static void main(String[] args){
        NodeProcessor nodeProcessor = new NodeProcessor();
        nodeProcessor.readExcelAndGenerateNodes("input.txt");
        nodeProcessor.generateDNASequence();
        nodeProcessor.printDNAOriginSequence();
        nodeProcessor.printResultDNASequence("output.txt");
    }
}
