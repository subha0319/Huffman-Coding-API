package com.github.subha0319.huffman_api;

/**
 * Represents a node in the Huffman Tree.
 * Each node can be either a leaf node (containing a character) or an internal node.
 * The class implements Comparable to allow sorting in a PriorityQueue based on frequency.
 */
public class HuffmanNode implements Comparable<HuffmanNode> {

    // The character for this node (only for leaf nodes)
    char character;
    
    // The frequency of the character or sum of frequencies of children
    int frequency;

    // Left and right children in the tree
    HuffmanNode leftChild;
    HuffmanNode rightChild;

    // Constructor for leaf nodes
    public HuffmanNode(char character, int frequency, HuffmanNode leftChild, HuffmanNode rightChild) {
        this.character = character;
        this.frequency = frequency;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    /**
     * Helper method to easily check if a node is a leaf.
     * A node is a leaf if it has no children.
     * @return true if the node is a leaf, false otherwise.
     */
    public boolean isLeaf() {
        return this.leftChild == null && this.rightChild == null;
    }

    /**
     * Compares this node to another node based on frequency.
     * This is essential for the PriorityQueue to function as a min-heap,
     * ensuring that nodes with lower frequencies have higher priority.
     * @param other The other HuffmanNode to compare against.
     * @return a negative integer, zero, or a positive integer as this node's
     * frequency is less than, equal to, or greater than the other node's frequency.
     */
    @Override
    public int compareTo(HuffmanNode other) {
        return this.frequency - other.frequency;
    }
}
