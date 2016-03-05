package cs221.project3.indexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs221.project3.mysqldatabase.*;
import cs221.project3.tokenizer.Tokenizer;

public class Indexer {
	
	public static final String startString = "********";
	public HashMap<String,Posting> invertIndex = new HashMap<String,Posting>();
	public double numberOfDocument = 0;
	public Database db = new Database();
	
	/**
	 * Insert an index into invertIndex object. 
	 * 
	 * @param  page a page consists url and text
	 * @return      true when the insert is completed, false if something go wrong         
	 */
	public boolean getIndex(Page page){
		ArrayList<String> tokens;
		tokens = Tokenizer.tokenize(page.text);
		HashMap<String, URLPosting> count = new HashMap<String, URLPosting>();
		for(int i=0;i<tokens.size();i++){
			String term = tokens.get(i);
			if(count.containsKey(term)){
				count.get(term).addPosition(i);
			}else{
				URLPosting urlPosting = new URLPosting(0);
				urlPosting.addPosition(i);
				count.put(term, urlPosting);
			}
		}
		Set<String> keySet = count.keySet();
		Iterator<String> iter = keySet.iterator();
		List<Indexing> indexList = new ArrayList<Indexing>();
		for(int i=0; i< keySet.size();i++){
			String key = iter.next();
			String position = count.get(key).pos.toString();
//			System.out.println(key + position + ": " + numberOfDocument);
			
			// normalize by document length
			Indexing idx = new Indexing(key, (int)numberOfDocument, position, count.get(key).pos.size()/tokens.size());
			// normalize by document length
//			Indexing idx = new Indexing(key, (int)numberOfDocument, position, count.get(key).pos.size());
			indexList.add(idx);
		}
		try {
			db.insertIndexing(indexList);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean getPageRank(Page page){
		
		
		
		
		return true;
	}
	
	/**
	 * Insert an indices into invertIndex object using fileName.
	 * the content must start with ******** http://www.example_url.com ******** 
	 * and continue with the content.
	 * 
	 * @param  fileName a name of file with the format as above
	 * @return      true when the insert is completed, false if something go wrong         
	 */
	public boolean generateIndex(String fileName) throws IOException{
		BufferedReader br;
		String thisLine="";
		try {
			br = new BufferedReader(new FileReader(fileName), 500);
			thisLine = br.readLine();
			if(thisLine == null){
				System.out.println("Read empty file.");
				br.close();
				return false;
			}
			String url="";
			String text="";
			while(thisLine!=null){
				if(thisLine.startsWith(startString)){
					url = thisLine;
					url = url.replace("*", "");
					url = url.trim();
				}else{
					System.out.println("the file is in wrong format.");
					br.close();
					return false;
				}
				while((thisLine = br.readLine())!=null){
					if(thisLine.startsWith(startString)) break;
					text+=thisLine+"\n";
				}
				Page page = new Page(url,text);
//				System.out.println(page.url);
//				System.out.println(page.text);
				text = "";
				this.getIndex(page);
				if(thisLine==null)break;
			}			
			br.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//untested
	public List<String> getListOfAnchor(String html){
		ArrayList<String> list = new ArrayList<String>();
		final Pattern pattern = Pattern.compile("<a href=(.+?)>");
		final Matcher matcher = pattern.matcher(html.toLowerCase());
		if(matcher.find()){
			int numberOfAnchor = matcher.groupCount();
			for(int i=0;i<numberOfAnchor;i++)list.add(matcher.group(i));
			return list;
		}else return null;
	}
	
	public String getTitle(String html){
		final Pattern pattern = Pattern.compile("<title>(.+?)</title>");
		final Matcher matcher = pattern.matcher(html.toLowerCase());
		if(matcher.find()){
			return matcher.group(1);
		}else return "No Title";
	}
	
	public boolean getDocumentsDataAndIndex(String fileName) throws IOException{
		BufferedReader br;
		
//		int num = 1;
		String thisLine = "";
		boolean isFirstLine = true;
		br = new BufferedReader(new FileReader(fileName), 500);
		thisLine = br.readLine();
		if(thisLine == null){
			System.out.println("Read empty file.");
			br.close();
			return false;
		}
		String url;
		String text="";
		String html="";
		String anchor = "";
		String title = "";
		while(thisLine!=null){
//				if (num <= 20){
			if(thisLine.equals("******** URL ********")){
				thisLine = br.readLine();
				url = thisLine;
//					url = url.replace("*", "");
//					url = url.trim();
			}else{
				System.out.println(thisLine + " should be ******** URL ********");
				System.out.println("the file is in wrong format.");
				br.close();
				return false;
			}
			//reading ******** TEXT ********
			thisLine = br.readLine();
			if(thisLine.equals("******** TEXT ********")){
				thisLine = br.readLine();
				while(!thisLine.equals("******** HTML ********")){
					text += thisLine+"\n";
					thisLine = br.readLine();
				}
			}else{
				System.out.println(thisLine + " should be ******** TEXT ********");
				System.out.println("the file is in wrong format.");
				br.close();
				return false;
			}
			
			//reading ******** HTML ********
			if(thisLine.equals("******** HTML ********")){
				thisLine = br.readLine();
				while(!thisLine.equals("******** URL ********")){
					html += thisLine+"\n";
					thisLine = br.readLine();
					if(thisLine == null) break;
				}
			}else{
				System.out.println(thisLine + " should be ******** HTML ********");
				System.out.println("the file is in wrong format.");
				br.close();
				return false;
			}
			
			title = getTitle(html);
			text = text.replace("  ", " ");
			html = html.replace("  ", " ");
			DocumentsData data = new DocumentsData((int)numberOfDocument, url, text, html, anchor, title);
			System.out.println("#documents: " + (int)numberOfDocument);
			System.out.println("url       : " + url);
//			System.out.println("text      : " + text);
//			System.out.println("html      : " + html);
			System.out.println("title     : " + title);
			db.insertData(data);
			Page page = new Page(url,text);
			getIndex(page);
			getPageRank(page);
			
//			url;
			text = "";
			html = "";
			title = "";
			numberOfDocument++;
//			num ++;
			if(thisLine==null)break;
//			}
//				else thisLine = null;
		}
		System.out.println("#total: " + numberOfDocument);
		numberOfDocument = 0;
		br.close();
		return true;
	}
	
}
