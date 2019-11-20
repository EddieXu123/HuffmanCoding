import java.util.*;
import java.io.*;

/**
 * Class that contains the huffmanEncoder method, 
 * a method to combine two HuffmanNodes, and a main method
 * @author Eddie Xu
 */
public class HuffmanCompressor {
  // A hashMap that will contain the key of the alphabetical letter and the value of frequency
  private static HashMap<Character, Integer> table = new HashMap<Character, Integer>();
  // Temp HashTable to record the character and their binary representation
  private static HashMap<Character, String> tempTable = new HashMap<Character, String>();
  // A PriorityQueue that will have huffman nodes ordered into it (as a minHeap)
  private static PriorityQueue<HuffmanNode> heap = new PriorityQueue<HuffmanNode>();
  // A temporary LinkedList that will be used to sort the frequencies of the characters of type HmanNode
  private static LinkedList<HuffmanNode> list = new LinkedList<HuffmanNode>();
  // Helper variable to record the amount of space saved
  private static int counter = 0;
  // HuffmanNode variable to represent the tree
  private static HuffmanNode rootNode;
  
  /**
   * Method to parse through a file, map the characters to a Hashmap, and then build a Huffman encoding BST
   * @param String inputFileName, outputFileName, the files that will be parsed through to build the new file
   * @return String of a file that will change the characters of the file to its huffman encoding
   */
  public static String huffmanCoder(String inputFileName, String outputFileName) {
    
    try {
      // Assigning the input file of type File a name called "file"
      File file = new File(inputFileName);
      // Inputting this file into a scanner, used later to parse through input file
      Scanner scanner = new Scanner(file);
      
      // While there is text in the file
      while (scanner.hasNextLine()) {
        // Store the line in a permanent String
        String s = scanner.nextLine();
        // Parsing through the string
        for (int i = 0; i < s.length(); i++) {
          // If the character in the line is a letter
          if (Character.toLowerCase(s.charAt(i)) >= 'a' && Character.toLowerCase(s.charAt(i)) <= 'z') {
            // If the character does not exist in the hash table yet
            if (table.get(Character.toLowerCase(s.charAt(i))) == null) {
              // Add the character to the hash table and set the frequency to 1 (To avoid null pointers)
              table.put(Character.toLowerCase(s.charAt(i)), 1);
            }
            // Else (if the character already exists)
            else {
              // Update the frequency of the key (character)
              table.put(Character.toLowerCase(s.charAt(i)), table.get(Character.toLowerCase(s.charAt(i))) + 1);
            }
          }
        }
      }
      
      // Creating a set of characters containing the keys of the hashmap
      Set<Character> characterSet = table.keySet();
      // For-each loop to parse through this set
      for (Character character : characterSet) {
        // Each time we reach a character in the set, we set the variable frequency to the frequency 
        // of that character in the hash map
        int freq = table.get(character);
        // Using our HuffmanNode class, create a new node for that character. This will be a leaf node
        HuffmanNode node = new HuffmanNode(character, freq, null, null, 1, 0);
        // Add the node to our list which will later be sorted
        list.add(node);
      }
      
      // Sorting the linkedList
      for (int i = 0; i < list.size() - 1; i++) {
        // Setting the first value of the list equal to a variable called min (we will assume this is the smallest)
        HuffmanNode min = list.get(i);
        // If the frequency of the next node is less than the minimum node's frequency, the next node is our new min node
        if (list.get(i+1).getFrequency() < min.getFrequency()) {
          // Swap the items in the list
          list.set(i, list.get(i+1));
          list.set(i+1, min);
        }
      }
      
      // Now, add the elements of the list to a priority queue
      for (int i = 0; i < list.size(); i++) {
        heap.add(list.get(i));
      }

      // Calling the helper method makeTree on rootnode to make a Huffman Tree with our heap
      rootNode = makeTree(heap);
      // Printing the characters, frequency, and binary representation using our printCode helper method
      printCode(rootNode, "");
      // The number of leaves is the number of leaves of our rootNode (using our HuffmanNode class definition)
      System.out.println("Number of Leaves: " + rootNode.getNumLeaves());
      // The height of the tree is the height of the rootNode (using our HuffmanNode class definition)
      System.out.println("Height of Tree: " + rootNode.getHeight());
      // Helper method to write our input file as a binary representation
      printStream(inputFileName, outputFileName);
      table.clear();
      heap.clear();
      list.clear();
      System.out.println("OK!");
    }
    
    // If the file is not found
    catch (FileNotFoundException e) {
      // Print out that your file is not in the folder
      System.err.println("Input File Error!");
    }
    // If everything runs smoothly print out OK! :D
    return "OK!";
  }
  
  /**
   * Helper method to recursively print out the huffmanNode
   * @param root, s the root of the huffman node and a String that will have "0/1"
   */
  public static void printCode(HuffmanNode root, String s) {
    
    // Base: if the left and right are null, it is leaf and print the code s generate by traversing tree
    
    if (root.getLeft() == null && root.getRight() == null) {    
      // In our new table which will be used to rewrite the file, add the character and its binary representation
      tempTable.put(root.getCharacter(), s);
      // Print out the character, the frequency, and its binary representation
      System.out.println(root.getCharacter() + ":" + root.getFrequency() + ":" + s); 
      return; 
    }
    
    // Recursive calls for left and right sub-tree of the generated tree:
    // if we go to left then add "0" to the code. 
    printCode(root.getLeft(), s + "0"); 
    // if we go to the right add"1" to the code. 
    printCode(root.getRight(), s + "1"); 
  }
  
  /**
   * Helper method to recursively make a tree inputting a queue
   */
  public static HuffmanNode makeTree(PriorityQueue<HuffmanNode> inQueue) {
    // If the queue is only a node or empty, return
    if (inQueue.size() <= 1) {
      return inQueue.remove();
    }
    else {
      // Else recursively make parent nodes by merging the least frequent two nodes
      inQueue.add(HuffmanNode.mergeNode(inQueue.remove(), inQueue.remove()));
      return makeTree(inQueue);
    }
  }
  
  /**
   * Helper method to read through my input file and output the binary file
   * @param inputFileName, outputFileName names of the inputFile and outputFile
   */
  public static void printStream(String inputFileName, String outputFileName) throws FileNotFoundException{
    // Counter to count how many binary numbers are in the new file
    int binarySaved = 0;
    // variable of type File to represent the inputFile
    File inputFile = new File(inputFileName);
    // Scanner to scan through the file
    Scanner scann = new Scanner(inputFile);
    // name of file used to write my output File
    File outputFile = new File(outputFileName);
    // PrintWriter to write my new file of binary numbers
    PrintWriter outFile = new PrintWriter(outputFile);
    
    // Parsing through the input file
    while (scann.hasNextLine()) {
      // Permanent variable to hold the text of the file
      String s = scann.nextLine();
      // Again, inputting the characters from our hashmap<character,String> into a set 
      Set<Character> set = tempTable.keySet();
      // Parsing through each line of our input file
      for (int i = 0; i < s.length(); i++) {
        // If the set contains that letter in our file
        if (set.contains(Character.toLowerCase(s.charAt(i)))) {
          // Add the binary representation to our new file
          outFile.print(tempTable.get(Character.toLowerCase(s.charAt(i))));
          // Update our counter by the length of # of binary numbers we are outputting
          binarySaved += tempTable.get(Character.toLowerCase(s.charAt(i))).length();
        }
      }
    }
    // We must close the file b/c JAVA! :D
    outFile.close();
    // The amount of space saved is the number of characters in our original file * 16
    // (b/c a char is 16 bits in Java) subtracted by the number of binary numbers in our output file
    System.out.println("Space saved: " + ((rootNode.getFrequency()*8) - binarySaved) + " bits");    
  }
  
  // Main method to run the code, parameters of input and output file names
  public static void main(String[] args) throws FileNotFoundException {
    HuffmanCompressor.huffmanCoder(args[0], args[1]);
  }
}
