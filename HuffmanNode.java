/**
 * HuffmanNode class representing the character and frequency of a letter
 * with helper variables/methods that will record leave-count and height of tree
 * @author Eddie Xu
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
  // Letter of the node
  private Character inChar;
  // Frequency of the letter of the node 
  private int frequency;
  // Left and Right child nodes of node
  private HuffmanNode left, right;
  // Variable to help with part c--> Finding the number of leaves of a node
  private int numLeaves;
  // Variable to help with part c--> Finding the height of the tree
  private int height;
  
  // Constructor with all of the variables in it
  public HuffmanNode(Character inChar, int frequency, HuffmanNode left, HuffmanNode right, int numLeaves, int height) {
    this.inChar = inChar;
    this.frequency = frequency;
    this.left = left;
    this.right = right;
    this.numLeaves = numLeaves;
    this.height = height;
  }
  
  // Method to get the frequency of a character 
  public int getFrequency() {
    return this.frequency;
  }
    
  // Method to get the left child of a node
  public HuffmanNode getLeft() {
    return this.left;
  }
  
  // Method to get the character of a node
  public Character getCharacter() {
    return this.inChar;
  }
  
  // Method to get the rigth child of a node
  public HuffmanNode getRight() {
    return this.right;
  }
  
  // Method to get the number of leaves of a node
  public int getNumLeaves() {
    return this.numLeaves;
  }
  
  // Method to get the height of a node
  public int getHeight() {
    return this.height;
  }
  
  /**
   * Helper method to merge two nodes by Huffman Encoding
   * @param two nodes a, b, representing the children of the mergedNode
   * @return HuffmanNode, with no character, the sum of the frequencies of the children node
   * the parameter nodes as the children of the node, the sum of the number of leaves of each node
   * (as merging two nodes (1 leaf) will result in that node having 2 leafs), and the farthest distance
   * from the root to a leaf
   */
  public static HuffmanNode mergeNode(HuffmanNode a, HuffmanNode b) {
    return new HuffmanNode(null, a.getFrequency() + b.getFrequency(), a, b, 
                           a.getNumLeaves() + b.getNumLeaves(), Math.max(a.getHeight()+1, b.getHeight()+1));
  }
  
  // Method to compare the frequencies of two nodes, returning the smaller one
  @Override
  public int compareTo(HuffmanNode node) {
    return Integer.compare(this.getFrequency(), node.getFrequency());
  }
}
