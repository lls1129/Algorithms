import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.*;
import java.io.*;
import java.nio.*;
import java.nio.charset.Charset;

public class HuffmanCodings {
    
    
    public static StringBuilder readchar(String filename){
        File file = new File(filename);
        Reader reader = null;
        StringBuilder readstring = new StringBuilder();
        try {
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                if (((char) tempchar) != '\r'){
                    readstring.append((char)tempchar);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return readstring;
    }
    
    //read from file to a map with frequency of each character. 
    public static Map<String, Integer> StringFrequence(String filename){
        Map<String, Integer> map = new HashMap<String, Integer>();
        try{
            File file = new File(filename);
            if (file.isFile() && file.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");
                BufferedReader breader = new BufferedReader(read);
                String line = null;
                while((line = breader.readLine()) != null){
                    for(int i = 1; i <= line.length(); i++){
                        String temp = line.substring(i-1, i);
                        boolean k = false;
                        for(String key : map.keySet()){
                            if(key.equals(temp)){
                                map.put(key, (int)map.get(key)+1);
                                k = true;
                                break; 
                            }
                        }
                        if(!k){
                            map.put(temp, 1);
                        }
                    }
                }
                read.close();
                return map; 
            }else{
                System.out.println("No such file!");
            }
        }catch(Exception e){
            System.out.println("Error! ");
            e.printStackTrace();
        }
        return map;
    }
    
    //sort the map by frequency of each character. 
    public static Map<Character, Integer> sortmap(Map<Character, Integer> originalmap){
        if(originalmap == null || originalmap.isEmpty()){
            return null; 
        }
        Map<Character, Integer> sortedmap = new LinkedHashMap<Character, Integer>();
        List<Map.Entry<Character, Integer>> entryList = new ArrayList<Map.Entry<Character, Integer>>(originalmap.entrySet());
        Collections.sort(entryList, new MapValueComparator());
        
        Iterator<Map.Entry<Character, Integer>> iter = entryList.iterator();
        Map.Entry<Character, Integer> tmpentry = null; 
        while(iter.hasNext()){
            tmpentry = iter.next();
            sortedmap.put(tmpentry.getKey(), tmpentry.getValue());
        }
        return sortedmap; 
    }
    //Comparator. 
    public static class MapValueComparator implements Comparator<Map.Entry<Character, Integer>>{
        @Override
        public int compare(Map.Entry<Character, Integer> m1, Map.Entry<Character, Integer> m2){
            return m2.getValue().compareTo(m1.getValue());
        }
    }    

    public static Map<Character, Integer> statistics(char[] charArray) {
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		for (char c : charArray) {
			Character character = new Character(c);
			if (map.containsKey(character)) {
				map.put(character, map.get(character) + 1);
			} else {
				map.put(character, 1);
			}
		}
 
		return map;
	}




    //initial root of each tree and return the root. 
    static class Tree {
		private Node root;
 
		public Node getRoot() {
			return root;
		}
 
		public void setRoot(Node root) {
			this.root = root;
		}
	}
 
	static class Node implements Comparable<Node> {
		private String chars = "";
		private int frequence = 0;
		private Node parent;
		private Node leftNode;
		private Node rightNode;
 
		@Override
		public int compareTo(Node n) {
			return frequence - n.frequence;
		}
 
		public boolean isLeaf() {
			return chars.length() == 1;
		}
 
		public boolean isRoot() {
			return parent == null;
		}
 
		public boolean isLeftChild() {
			return parent != null && this == parent.leftNode;
		}
 
		public int getFrequence() {
			return frequence;
		}
 
		public void setFrequence(int frequence) {
			this.frequence = frequence;
		}
 
		public String getChars() {
			return chars;
		}
 
		public void setChars(String chars) {
			this.chars = chars;
		}
 
		public Node getParent() {
			return parent;
		}
 
		public void setParent(Node parent) {
			this.parent = parent;
		}
 
		public Node getLeftNode() {
			return leftNode;
		}
 
		public void setLeftNode(Node leftNode) {
			this.leftNode = leftNode;
		}
 
		public Node getRightNode() {
			return rightNode;
		}
 
		public void setRightNode(Node rightNode) {
			this.rightNode = rightNode;
		}
	}
    
    //Build a min heap. 
    private static Tree buildTree(Map<Character, Integer> statistics, List<Node> leafs) {
		Character[] keys = statistics.keySet().toArray(new Character[0]);
 
		PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();
		for (Character character : keys) {
			Node node = new Node();
			node.chars = character.toString();
			node.frequence = statistics.get(character);
			priorityQueue.add(node);
			leafs.add(node);
		}
 
		int size = priorityQueue.size();
		for (int i = 1; i <= size - 1; i++) {
			Node node1 = priorityQueue.poll();
			Node node2 = priorityQueue.poll();
 
			Node sumNode = new Node();
			sumNode.chars = node1.chars + node2.chars;
			sumNode.frequence = node1.frequence + node2.frequence;
 
			sumNode.leftNode = node1;
			sumNode.rightNode = node2;
 
			node1.parent = sumNode;
			node2.parent = sumNode;
 
			priorityQueue.add(sumNode);
		}
 
		Tree tree = new Tree();
		tree.root = priorityQueue.poll();
		return tree;
	}
    
    //Encoding process. 
    public static String encode(String originalStr, Map<Character, Integer> statistics) {
		if (originalStr == null || originalStr.equals("")) {
			return "";
		}
 
		char[] charArray = originalStr.toCharArray();
		List<Node> leafNodes = new ArrayList<Node>();
		buildTree(statistics, leafNodes);
		Map<Character, String> encodInfo = buildEncodingInfo(leafNodes);
        
        Set<Map.Entry<Character, String>> entrys = encodInfo.entrySet();
 /*When doing the test for the runtime, the io process should be temporarily ignored. */
        for (Map.Entry<Character, String> entry : entrys) {
            Character node = entry.getKey();
            String code = entry.getValue();
            System.out.println("The code of '" + node + "' is " + code);
        }
        
 
		StringBuffer buffer = new StringBuffer();
		for (char c : charArray) {
			Character character = new Character(c);
			buffer.append(encodInfo.get(character));
		}
 
		return buffer.toString();
	}
    
    
    private static Map<Character, String> buildEncodingInfo(List<Node> leafNodes) {
		Map<Character, String> codewords = new HashMap<Character, String>();
		for (Node leafNode : leafNodes) {
			Character character = new Character(leafNode.getChars().charAt(0));
			String codeword = "";
			Node currentNode = leafNode;
 
			do {
				if (currentNode.isLeftChild()) {
					codeword = "0" + codeword;
				} else {
					codeword = "1" + codeword;
				}
 
				currentNode = currentNode.parent;
			} while (currentNode.parent != null);
 
			codewords.put(character, codeword);
		}
 
		return codewords;
	}
    
    //Decoding process. 
    public static String decode(String binaryStr, Map<Character, Integer> statistics) {
		if (binaryStr == null || binaryStr.equals("")) {
			return "";
		}
 
		char[] binaryCharArray = binaryStr.toCharArray();
		LinkedList<Character> binaryList = new LinkedList<Character>();
		int size = binaryCharArray.length;
		for (int i = 0; i < size; i++) {
			binaryList.addLast(new Character(binaryCharArray[i]));
		}
 
		List<Node> leafNodes = new ArrayList<Node>();
		Tree tree = buildTree(statistics, leafNodes);
 
		StringBuffer buffer = new StringBuffer();
 
		while (binaryList.size() > 0) {
			Node node = tree.root;
 
			do {
				Character c = binaryList.removeFirst();
				if (c.charValue() == '0') {
					node = node.leftNode;
				} else {
					node = node.rightNode;
				}
			} while (!node.isLeaf());
 
			buffer.append(node.chars);
		}
 
		return buffer.toString();
	}
    
    //To get the UTF code. 
    public static String getStringOfByte(String str, Charset charset) {
		if (str == null || str.equals("")) {
			return "";
		}
 
		byte[] byteArray = str.getBytes(charset);
		int size = byteArray.length;
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < size; i++) {
			byte temp = byteArray[i];
			buffer.append(getStringOfByte(temp));
		}
 
		return buffer.toString();
	}
    public static String getStringOfByte(byte b) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 7; i >= 0; i--) {
			byte temp = (byte) ((b >> i) & 0x1);
			buffer.append(String.valueOf(temp));
		}
 
		return buffer.toString();
    }

    
    public static void main(String[] args) {
        
        
/*        Map<String, Integer> map = new HashMap<String, Integer>();
        map = StringFrequence("testchar.txt");
        map = sortmap(map);
        //Integer[] frequency = map.keySet(); 
        //System.out.println(frequency);
        Object[] temp = map.values().toArray();
        Object[] temp2 = map.keySet().toArray();
        Integer[] frequency = new Integer[temp.length];
        String[] str = new String[temp.length];
//        for (int i = 0; i < temp.length; i++){
//            System.out.println(temp[i]);
//            System.out.println(temp2[i]);
//        }
        for (int i = 0; i < temp.length; i++){
            frequency[i] = (Integer)temp[i];
            str[i] = (String)temp2[i];
        }
        for (int i = 0; i < temp.length; i++){
            System.out.println(frequency[i]+" "+str[i]);
        }*/
        
        
        
        StringBuilder oriStrtemp = readchar("testchar.txt");
        String oriStr = oriStrtemp.toString();
//        System.out.println(oriStr);
        Map<Character, Integer> statistics = statistics(oriStr.toCharArray());
        statistics = sortmap(statistics);
 
        //Show the frequency of each character in the txt file. 
        Set<Map.Entry<Character, Integer>> entrys = statistics.entrySet();
        for (Map.Entry<Character, Integer> entry : entrys) {
            Character character = entry.getKey();
            Integer frequency = entry.getValue();
            System.out.println("The frequency of character '" + character + "' is : " + frequency);
        }
        
        long startTime = System.nanoTime();
        String encodedBinariStr = encode(oriStr, statistics);
        long endTime = System.nanoTime();
		String decodedStr = decode(encodedBinariStr, statistics);
        long endTime2 = System.nanoTime();
        System.out.println("For length of " + oriStr.length() + " input, the runtime of encoding is " + (endTime - startTime) + ". the run time of decoding is " + (endTime2 - endTime) + ". "); 
//      System.out.println("Original string: " + oriStr); //Show the original string if needed. 
//		System.out.println("Huffman encode binary string: " + encodedBinariStr);//show the huffman code if needed. 
//		System.out.println("Decoded string: " + decodedStr);//decode the huffmancode to see if it works fine.  
//      System.out.println("binary string of UTF-8: " + getStringOfByte(oriStr, Charset.forName("UTF-8")));//the UTF-8 binary string. 
        System.out.println("The decoding String is identical to the original string? The answer is " + (oriStr.equals(decodedStr)));
        System.out.println("length of Huffman encode binary string is " + encodedBinariStr.length());
        System.out.println("length of UTF-8 binary string is " + getStringOfByte(oriStr, Charset.forName("UTF-8")).length());
        

	}
    
}

