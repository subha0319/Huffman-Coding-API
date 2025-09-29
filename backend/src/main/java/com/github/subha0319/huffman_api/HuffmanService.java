package com.github.subha0319.huffman_api;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Service class containing the core logic for Huffman coding.
 * This class is marked as a Spring @Service so it can be injected into controllers.
 */
@Service
public class HuffmanService {

    /**
     * Main method to compress a given text.
     * It orchestrates the entire Huffman encoding process.
     * @param text The original text to compress.
     * @return An object containing the compressed data and the code table.
     */
    public CompressionResult compress(String text) {
        if (text == null || text.isEmpty()) {
            return new CompressionResult("", new HashMap<>());
        }

        Map<Character, Integer> frequencyMap = buildFrequencyMap(text);
        HuffmanNode root = buildHuffmanTree(frequencyMap);
        Map<Character, String> codeTable = generateCodes(root);
        String encodedText = encodeText(text, codeTable);

        return new CompressionResult(encodedText, codeTable);
    }

    /**
     * Main method to decompress a given Huffman-encoded string.
     * @param encodedText The binary string of compressed data.
     * @param codeTable The map of characters to their binary codes.
     * @return The original, decompressed text.
     */
    public String decompress(String encodedText, Map<Character, String> codeTable) {
        if (encodedText == null || encodedText.isEmpty() || codeTable == null || codeTable.isEmpty()) {
            return "";
        }

        // It's more efficient to decode using an inverted map (code -> character)
        Map<String, Character> invertedCodeTable = new HashMap<>();
        for (Map.Entry<Character, String> entry : codeTable.entrySet()) {
            invertedCodeTable.put(entry.getValue(), entry.getKey());
        }

        StringBuilder decodedText = new StringBuilder();
        StringBuilder currentCode = new StringBuilder();
        for (char bit : encodedText.toCharArray()) {
            currentCode.append(bit);
            if (invertedCodeTable.containsKey(currentCode.toString())) {
                decodedText.append(invertedCodeTable.get(currentCode.toString()));
                currentCode.setLength(0); // Reset the current code
            }
        }
        return decodedText.toString();
    }


    // --- Private Helper Methods for the Algorithm ---

    /**
     * Step 1: Count the frequency of each character in the text.
     * @param text The input string.
     * @return A map where keys are characters and values are their frequencies.
     */
    private Map<Character, Integer> buildFrequencyMap(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char character : text.toCharArray()) {
            frequencyMap.put(character, frequencyMap.getOrDefault(character, 0) + 1);
        }
        return frequencyMap;
    }

    /**
     * Step 2: Build the Huffman Tree from the frequency map.
     * Uses a PriorityQueue to greedily combine the nodes with the lowest frequencies.
     * @param frequencyMap The map of character frequencies.
     * @return The root node of the completed Huffman Tree.
     */
    private HuffmanNode buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();

        // Create a leaf node for each character and add it to the priority queue
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.add(new HuffmanNode(entry.getKey(), entry.getValue(), null, null));
        }

        // Handle edge case of text with only one unique character
        if (priorityQueue.size() == 1) {
            HuffmanNode singleNode = priorityQueue.poll();
            return new HuffmanNode('\0', singleNode.frequency, singleNode, null);
        }

        // Combine nodes until only one (the root) remains in the queue
        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();

            int sumFrequency = left.frequency + right.frequency;
            // Internal nodes use a null character '\0' as a placeholder
            HuffmanNode parent = new HuffmanNode('\0', sumFrequency, left, right);
            priorityQueue.add(parent);
        }
        return priorityQueue.poll();
    }

    /**
     * Step 3: Generate the binary codes for each character by traversing the tree.
     * @param root The root of the Huffman Tree.
     * @return A map where keys are characters and values are their binary string codes.
     */
    private Map<Character, String> generateCodes(HuffmanNode root) {
        Map<Character, String> codeTable = new HashMap<>();
        generateCodesRecursive(root, "", codeTable);
        return codeTable;
    }

    private void generateCodesRecursive(HuffmanNode node, String code, Map<Character, String> codeTable) {
        if (node == null) {
            return;
        }

        // If it's a leaf node, we've found a character's code
        if (node.isLeaf()) {
            // Handle the case of a single character tree, give it a default code '0'
            if(code.isEmpty()){
                codeTable.put(node.character, "0");
            } else {
                codeTable.put(node.character, code);
            }
        }

        // Traverse left (append '0') and right (append '1')
        generateCodesRecursive(node.leftChild, code + "0", codeTable);
        generateCodesRecursive(node.rightChild, code + "1", codeTable);
    }

    /**
     * Step 4: Encode the original text using the generated code table.
     * @param text The original text.
     * @param codeTable The map of character codes.
     * @return The compressed binary string.
     */
    private String encodeText(String text, Map<Character, String> codeTable) {
        StringBuilder encodedText = new StringBuilder();
        for (char character : text.toCharArray()) {
            encodedText.append(codeTable.get(character));
        }
        return encodedText.toString();
    }
    
    /**
     * A simple record to hold the result of the compression process.
     * This makes it easy to return multiple values from the compress method.
     */
    public record CompressionResult(String encodedText, Map<Character, String> codeTable) {}
}
