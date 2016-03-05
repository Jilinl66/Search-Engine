package cs221.project3.tokenizer;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

 
public class Tokenizer {
	
	static ArrayList<String> stopWordsList = new ArrayList<String>();
	public static ArrayList<String> tokenList =  new ArrayList<String>();
	static File file1 = new File("tokens.txt");
	static File file2 = new File("threeGrams.txt");
	static File file3 = new File("twoGrams.txt");
	static File file4 = new File("threeGramsFreq.txt");
	static File file5 = new File("twoGramsFreq.txt");
	static File file6 = new File("tokenFreq.txt");

	// Tokenizer on input file
	public static ArrayList<String> tokenize(String text) {
		int count = 1;
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(text.toLowerCase(), " ,!*^/");
		String s;
		Stemmer stemmer = new Stemmer();
		
		while (tokenizer.hasMoreTokens()) {
			 if (isAlphaNum(s = tokenizer.nextToken())) {
//		        System.out.println("Find the " + count + " token: "+ s);
//				accept only length less than 50
//				stemmer = Porter's algorithm
				 
				stemmer.add(s.toCharArray(), s.length());
				stemmer.stem();
				s = stemmer.toString();
				if(s.length()<=50){
			        list.add(s);
			     	count = count + 1;
				} else ;
			 }
		}
//		while (tokenizer.hasMoreTokens()) {
//			 if (isAlphaNum(s = tokenizer.nextToken())) {
////		        System.out.println("Find the " + count + " token: "+ s);
//		        list.add(s);
//		     	count = count + 1;
//			 }
//		}
		return list;
	}
	
	
	public static ArrayList<String> myTokenizeFile(String document) throws IOException {
		int count = 1;
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(document.toLowerCase(), " ,!*^/");
		FileWriter fw1 = new FileWriter(file1);
		String s;
		while (tokenizer.hasMoreTokens()) {
			 if (isAlphaNum(s = tokenizer.nextToken())) {
//		        System.out.println("Find the " + count + " token: "+ s);
		        list.add(s);
		        fw1.write(s + '\n');
		     	count = count + 1;
			 }
		}
		fw1.close();
		return list;
	}

	@SuppressWarnings("rawtypes")
	private static class Compare implements Comparator { 
		public int compare (Object aa, Object bb) {
			TokenObject a = (TokenObject)aa;
			TokenObject b = (TokenObject)bb;
			if (a.freq < b.freq) {
				return 1;
			}
			else if (a.freq > b.freq) {
				return -1;
			}
			else {
				return a.token.compareTo(b.token);
			}
			}
		}
	
//	Compute token frequencies
	public static void computeWordFrequencies() throws IOException {
		Map<String,Integer> freq = new HashMap<String,Integer>(); 
		@SuppressWarnings("resource")
		Scanner input = new Scanner(file1);
		while (input.hasNext()) {
			String word = input.next().toLowerCase();
			Integer f = freq.get(word);
			if (f == null){
				freq.put(word, 1);
			}
			else {
				freq.put(word, f + 1);
			}
		}
		// Sort frequency of tokens
		@SuppressWarnings("unchecked")
		SortedSet<TokenObject> list = new TreeSet<TokenObject>(new Compare()); 
		for (String word : freq.keySet()) {
			TokenObject tb = new TokenObject();
			tb.token = word;
			tb.freq = freq.get(word); 
			list.add(tb);
		}
		
		//print
		int i= 500;
		int num = 1;
		FileWriter fw6 = new FileWriter(file6);
		System.out.println("No.                      Token                    Frequencies");
		System.out.println("---------------------------------------------------------------");
		for (TokenObject tb : list) {
			System.out.println(Integer.toString(num) + "               " + tb.token + "          " + tb.freq + "\n");
			fw6.write(Integer.toString(num) + ". Token: " + tb.token + ",  Freuency: " + tb.freq + "\n");
			i --;
			num ++;
			if( i<=0 ){
				break;
			}
		}
		fw6.close();
	}
	
	public static void computeThreeGramFrequencies() throws IOException {
		Map<String,Integer> freq_thrGram = new HashMap<String,Integer>(); 
		int count = 1;
		String threeGram = null;
		Scanner scanner1 = new Scanner(new File("tokens.txt"));
		Scanner scanner2 = new Scanner(new File("tokens.txt"));
		Scanner scanner3 = new Scanner(new File("tokens.txt"));
		scanner2.next();
		scanner3.next();
		scanner3.next();

		FileWriter fw2 = new FileWriter(file2);
		while (scanner3.hasNext()){
			threeGram = scanner1.next() + " " + scanner2.next() + " " + scanner3.next();
			//Write 3-grams into file
			fw2.write(String.valueOf(count) + ": " + threeGram + '\n');
			threeGram = threeGram.toLowerCase();
			System.out.print("Find the " + count + " 3-gram: " + threeGram + "\n");
			Integer f = freq_thrGram.get(threeGram);
			if (f == null){
				freq_thrGram.put(threeGram, 1);
			}
			else {
				freq_thrGram.put(threeGram, f + 1);
			}
			count ++;
		}
		scanner1.close();
		scanner2.close();
		scanner3.close();
		fw2.close();
		
		// Sort frequency of 3-grams
		@SuppressWarnings("unchecked")
		SortedSet<TokenObject> list = new TreeSet<TokenObject>(new Compare()); 
		for (String word : freq_thrGram.keySet()) {
			TokenObject tb = new TokenObject();
			tb.token = word;
			tb.freq = freq_thrGram.get(word); 
			list.add(tb);
		}
		
		// Print
		FileWriter fw4 = new FileWriter(file4);
		int i= 20;
		int num = 1;
		System.out.println("No.                 ThreeGrams                                Frequencies");
		System.out.println("-------------------------------------------------------------------------");
		for (TokenObject tb : list) {
			System.out.println(Integer.toString(num) + "                  " + tb.token + "                    " + tb.freq + "\n");
			fw4.write(Integer.toString(num) + " 3-gram: " + tb.token + ",  Frequency: " + tb.freq + "\n");
			i --;
			num ++;
			if( i<=0 ){
				break;
			}
		}
		fw4.close();
	}
	
	public static void computeTwoGramFrequencies() throws IOException {
		Map<String,Integer> freq_twoGram = new HashMap<String,Integer>(); 
		int count = 1;
		String twoGram = null;
		Scanner scanner1 = new Scanner(new File("tokens.txt"));
		Scanner scanner2 = new Scanner(new File("tokens.txt"));
		scanner2.next();

		FileWriter fw3 = new FileWriter(file3);
		while (scanner2.hasNext()){
			String word1 = scanner1.next();
			String word2 = scanner2.next();
			//Throw the 2-grams if it contains stop words
			if (!(isStopWords(word1)) && !(isStopWords(word2))){
				twoGram = word1 + " " + word2;
				
				//Write 2-grams into file
				fw3.write(String.valueOf(count) + ": " + twoGram + '\n');
				twoGram = twoGram.toLowerCase();
				System.out.print("Find the " + count + " 2-grams: " + twoGram + "\n");
				Integer f = freq_twoGram.get(twoGram);
				if (f == null){
					freq_twoGram.put(twoGram, 1);
				}
				else {
					freq_twoGram.put(twoGram, f + 1);
				}
				count ++;
			}
		}
		scanner1.close();
		scanner2.close();
		fw3.close();
		
		// Sort frequency of 2-grams
		@SuppressWarnings("unchecked")
		SortedSet<TokenObject> list = new TreeSet<TokenObject>(new Compare()); 
		for (String word : freq_twoGram.keySet()) {
			TokenObject tb = new TokenObject();
			tb.token = word;
			tb.freq = freq_twoGram.get(word); 
			list.add(tb);
		}
		
		//Print
		FileWriter fw5 = new FileWriter(file5);
		int i= 20;
		int num = 1;
		System.out.println("No.                 TwoGrams                                Frequencies");
		System.out.println("-------------------------------------------------------------------------");
		for (TokenObject tb : list) {
			System.out.println(Integer.toString(num) + "                  " + tb.token + "                    " + tb.freq + "\n");
			fw5.write(Integer.toString(num) + " 2-gram: " + tb.token + ",  Frequency: " + tb.freq + "\n");
			i --;
			num ++;
			if( i<=0 ){
				break;
			}
		}
		fw5.close();
	}
	                                     
	public static boolean isStopWords (String word) throws IOException{
		if (stopWordsList.contains(word))
			return true;
		else 
			return false;
}

	public static void seperateStopWords(String filename) throws IOException {
		FileReader fr = new FileReader(filename);
		StreamTokenizer st = new StreamTokenizer(fr);
 		while (st.nextToken()!=StreamTokenizer.TT_EOF) {
			stopWordsList.add(st.sval);
 		}
	}
	
	public static boolean isAlphaNum(String name) {
	    return name.matches("[a-zA-Z0-9]+");
	}
}