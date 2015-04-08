import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

//Author(s): Marcus Johnsson, Tobias Nielsen
//Email:	marcjohn@student.chalmers.se , tobnie@student.chalmers.se
//Date:		25-03-2015	

public class WordLists {
	private Reader in = null;
	protected TreeMap<String, Integer> tm = new TreeMap<String, Integer>();
	public int maxWordCount = 0;
	
	public WordLists(String inputFileName) throws FileNotFoundException
	{
	    // ... define!

		in = new BufferedReader(new FileReader(inputFileName));
	}
	
	private boolean isPunctuationChar(char c) {
	    final String punctChars = ",.:;?!";
	    return punctChars.indexOf(c) != -1;
	}
	
	private String getWord() throws IOException {
		int state = 0;
		int i;
		String word = "";
		while ( true ) {
			i = in.read();
			char c = (char)i;
			switch ( state ) {
			case 0:
				if ( i == -1 )
					return null;
				if ( Character.isLetter( c ) ) {
					word += Character.toLowerCase( c );
					state = 1;
				}
				break;
			case 1:
				if ( i == -1 || isPunctuationChar( c ) || Character.isWhitespace( c ) )
					return word;
				else if ( Character.isLetter( c ) ) 
					word += Character.toLowerCase( c );
				else {
					word = "";
					state = 0;
				}
			}
		}
	}
	
	private String reverse(String s)
	{
		char[] word = s.toCharArray();
		
		StringBuffer buf = new StringBuffer(s.length());
			
		for(int i = 0; i < s.length(); i++)
		{
			buf.append(word[s.length() - 1 - i]);
		}
		
		return buf.toString(); 
	}
	
	private void computeWordFrequencies() throws IOException
	{
		FileWriter fw = new FileWriter("Alfasorted.txt");
		
		for(Map.Entry<String,Integer> e : tm.entrySet())
		{
			fw.write(e.getKey() + ":" + e.getValue() + '\n');
		}
		
		fw.flush();
		fw.close();
	}

	private void computeFrequencyMap() throws IOException
	{
		FileWriter fw = new FileWriter("frequencySorted.txt");
		
		
		Comparator<Integer> strComp = new IntComparator();
		
		TreeMap<Integer, TreeSet<String>> cfm = new TreeMap<Integer, TreeSet<String>>(strComp);
		
		for(Map.Entry<String,Integer> e : tm.entrySet())
		{
			if(cfm.containsKey(e.getValue()))
				cfm.get(e.getValue()).add(e.getKey());
			else
			{
				cfm.put(e.getValue(), new TreeSet<String>());
				cfm.get(e.getValue()).add(e.getKey());
			}
		}
		
		for(Map.Entry<Integer,TreeSet<String>> e : cfm.entrySet())
		{
			fw.write("\t" + e.getKey() + '\n');
			
			for(String s : e.getValue())
			{
				fw.write("\t\t" + s + '\n');
			}
		}
		
		fw.flush();
		fw.close();
	}

	private void computeBackwardsOrder() throws IOException
	{
		FileWriter fw = new FileWriter("backwardsSorted.txt");
		TreeMap<String, String> backwardsMap = new TreeMap<String, String>();
		
		for(Map.Entry<String,Integer> e : tm.entrySet())
		{
			backwardsMap.put(reverse(e.getKey()), e.getKey());
		}
		
		for(Map.Entry<String, String> e : backwardsMap.entrySet())
		{
			fw.write(e.getValue() + '\n');
		}
		
		fw.flush();
		fw.close();
	}
	
	public class IntComparator implements Comparator<Integer> {
		public int compare(Integer val0, Integer val1) {
			return val1 - val0;
		}
	} 

	public static void main(String[] args) throws IOException
	{
		WordLists wl = new WordLists("provtext.txt");  // arg[0] contains the input file name
		
		String word = wl.getWord();
		while(word != null)
		{
			// Computes frequency while adding words to TreeMap
			if(wl.tm.containsKey(word))
				wl.tm.put(word, wl.tm.get(word).intValue() + 1 );
			else
				wl.tm.put(word, 1);	
			
			word = wl.getWord();
		}
		
		for(Map.Entry<String,Integer> e : wl.tm.entrySet())
		{
			wl.maxWordCount = e.getValue() > wl.maxWordCount ? e.getValue() : wl.maxWordCount;
		}
		
		wl.computeWordFrequencies();
		wl.computeFrequencyMap();
		wl.computeBackwardsOrder();
		
		System.out.println("Finished!");
	}
}