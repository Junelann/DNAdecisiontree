package org.example.utils;

import java.util.Random;

import static org.example.constant.DNSSequenceConstants.*;

public class StringUtils {

    public static Boolean checkContinuousGLength(char[] str, int position) {
        int lenGLeft = 0;
        int lenGRight = 0;
        int index = position - 1;
        while (index >= 0 && str[index] == 'G') {
            ++lenGLeft;
            --index;
        }
        index = position + 1;
        while (index < str.length && str[index] == 'G') {
            ++lenGRight;
            ++index;
        }
        return lenGLeft + lenGRight + 1 >= MAX_CONSECUTIVE_COUNT;
    }

    public static String getReverseComplementSequence(String dnaSequence) {
        StringBuilder stringBuilder = new StringBuilder(dnaSequence).reverse();
        StringBuilder complement = new StringBuilder();
        for (int i = 0; i < stringBuilder.length(); i++) {
            char currentChar = stringBuilder.charAt(i);
            switch (currentChar) {
                case 'A':
                    complement.append('T');
                    break;
                case 'T':
                    complement.append('A');
                    break;
                case 'C':
                    complement.append('G');
                    break;
                case 'G':
                    complement.append('C');
                    break;
                default:
                    throw new IllegalArgumentException("Invalid DNA base: " + currentChar);
            }
        }
        return complement.toString();
    }

    public static char[] getFullATSequence(int length) {
        char[] str = new char[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            Boolean selectA = random.nextBoolean();
            if (selectA) {
                str[i] = 'A';
            } else {
                str[i] = 'T';
            }
        }
        for (int j = 0; j < length - 3; ++j) {
            if (str[j] == str[j + 1] && str[j] == str[j + 2] && str[j] == str[j + 3]) {
                return getFullATSequence(length);
            }
        }
        return str;
    }

    public static String getSegmentA(String dnaSequence) {
        return dnaSequence.substring(0, SUBSEQUENCE_A);
    }

    public static String getSegmentB(String dnaSequence) {
        return dnaSequence.substring(SUBSEQUENCE_A, SUBSEQUENCE_A + SUBSEQUENCE_B);
    }

    public static String getSegmentC(String dnaSequence) {
        return dnaSequence.substring(SUBSEQUENCE_A + SUBSEQUENCE_B, SUBSEQUENCE_A + SUBSEQUENCE_B + SUBSEQUENCE_C);
    }

    public static String getSegmentD(String dnaSequence) {
        return dnaSequence.substring(dnaSequence.length() - SUBSEQUENCE_D);
    }

}
