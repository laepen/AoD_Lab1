import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;


// Author(s): Marcus Johnsson, Tobias Nielsen
// Email:	marcjohn@student.chalmers.se , tobnie@student.chalmers.se
// Date:	25-03-2015


public class AngloTrainer
{
	
	private TreeSet<String> dictionary; 
	private Scanner scanner; 
	public int longestWord;
	private Random randomGenerator;

	public AngloTrainer(String dictionaryFile) throws IOException {
		randomGenerator = new Random();

		dictionary = new TreeSet<String>();
		
		loadDictionary(dictionaryFile);
	}

	// use this to verify loadDictionary
	private void dumpDict()
	{
		for(String s : dictionary)
		{
			System.out.println(s);
		}
	}

	private void loadDictionary( String fileName )
	{
		int words = 0;
		longestWord = 0;

		try {
			scanner = new Scanner(new BufferedReader(new FileReader(fileName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while(scanner.hasNextLine())
		{
			String word = scanner.nextLine();

			longestWord = longestWord < word.length() ? word.length() : longestWord; 

			dictionary.add(word);
			words++;
		}
		System.out.println(words + " words loaded from " + fileName);
	}

	private String randomLetters( int length ) {
		// this makes vovels a little more likely
		String letters = "aabcdeefghiijklmnoopqrstuuvwxyyz";  
		StringBuffer buf = new StringBuffer(length);
		for ( int i = 0; i < length; i++ ) 
			buf.append( letters.charAt(randomGenerator.nextInt(letters.length())));

		return buf.toString();
	}


	/* Def. includes	
	 * Let #(x,s) = the number of occurrences of the charcter x in the string s.
	 * includes(a,b) holds iff for every character x in b, #(x,b) <= #(x,a)
	 * 
	 * A neccessary precondition for includes is that both strings are sorted
	 * in ascending order.
	 */
	private boolean includes( String a, String b ) {
		if ( b == null || b.length() == 0 )
			return true;
		else if ( a == null || a.length() == 0 )
			return false;

		//precondition: a.length() > 0 && b.length() > 0
		int i = 0, j = 0;
		while ( j < b.length() ) {
			if (i >= a.length() || b.charAt(j) < a.charAt(i))
				return false;
			else if (b.charAt(j) == a.charAt(i)) {
				i++; j++;
			} else if (b.charAt(j) > a.charAt(i))
				i++;
		}
		//postcondition: j == b.length()
		return true;
	}

	public static void main(String[] args) throws IOException
	{
		AngloTrainer trainer = null;
		String input;
		char[] test1, test2;
		TreeSet<String> inputLetters = new TreeSet<String>(); 
		TreeSet<String> sortedRandomLetters = new TreeSet<String>(); 
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		trainer = new AngloTrainer("dictionary.txt");
		
		//trainer.longestWord is length of longest word
		String randomLetters = trainer.randomLetters(5);

		System.out.println("The random letters are: " + randomLetters);
		System.out.println("Try to form as many word as possible\n");
	
		while(true){
			input = br.readLine();
			test1 = input.toCharArray();
			for(Character x : test1){
				inputLetters.add(x.toString());
			}
			test2 = randomLetters.toCharArray();
			for(Character x : test2){
				sortedRandomLetters.add(x.toString());
			}
			boolean exists = false; 
			exists = (trainer.dictionary.contains(input) && (inputLetters.contains(sortedRandomLetters.)))? true:false;
			if(exists)
				System.out.print("ok!");
			else{
				System.out.println("Your suggestion was not found in the dictionary.");
				System.out.println("I found: \n");
				break;
			}			
		}

//		while(true)
//		{
//			input = br.readLine();
//
//			boolean exists = false;
//
//			for(String word : trainer.dictionary)
//			{
//				exists = input.equals(word) ? true : false;
//				if(exists) break; 
//			}
//
//			if(exists)
//			{
//				System.out.println("ok!");
//			}
//			else
//			{
//				System.out.println("Your suggestion was not found in the dictionary.");
//				System.out.println("I found: \n");
//				break;
//			}
//		}
		
		char [] newWord = randomLetters.toCharArray();
		Arrays.sort(newWord);
		
		for(String w : trainer.dictionary)
		{
			char [] newDic = w.toCharArray();
			Arrays.sort(newDic);
			
			if(trainer.includes(new String(newWord), new String(newDic)))
			{
				System.out.println(w);
			}
		}
	}
}