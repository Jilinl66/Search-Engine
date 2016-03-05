package cs221.project3.searcher;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cs221.project3.indexer.Pair;
import cs221.project3.mysqldatabase.*;
import cs221.project3.tokenizer.*;

public class Searcher {
	private Database db = new Database();
	
	private HashMap<String,ArrayList<Integer>> termsAndURL = new HashMap<String,ArrayList<Integer>>();
	private HashMap<Integer, ArrayList<String>> urlAndTerms = new HashMap<Integer, ArrayList<String>>();
	
	private Set<Integer> urlSet = new HashSet<Integer>();
	private ArrayList<String> terms = new ArrayList<String>();
	
	private ArrayList<Integer> topURL = new ArrayList<Integer>();
	
	private String query;
	private static int maximumSearch = 10;
	
//	sum of tfidf
	private void setTopURL(){
		ArrayList<Pair<Integer,Double>> urlAndScore = new ArrayList<Pair<Integer,Double>>();
		Iterator<Integer> iter = urlSet.iterator();
		while(iter.hasNext()){
			int urlID = iter.next();
			ArrayList<String> termsOfThisURL = urlAndTerms.get(urlID);
			for(String s:termsOfThisURL)System.out.println(s);
			double score = 0;
			for(int i=0;i<termsOfThisURL.size();i++){
				try {
					score += db.getTfidf(termsOfThisURL.get(i), urlID);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("cannot find the score of url: " + urlID + ", term: "+termsOfThisURL.get(i));
				}
			}
			urlAndScore.add(new Pair<Integer,Double>(urlID,score));
		}
		Collections.sort(urlAndScore, new Comparator<Pair>() {
			@Override
			public int compare(Pair o1, Pair o2) {
				// TODO Auto-generated method stub
				double first = (double)o1.second;
				double second = (double)o2.second;
				if(first == second) return 0;
				return first<second?-1:1;
			}
	    });
		topURL.clear();
		for(int i=0; i<urlAndScore.size(); i++){
			topURL.add(urlAndScore.get(i).first);
		}
	}
	
//	intersection and frequency
	private void setTopURL2(){
		ArrayList<Pair<Integer,Double>> urlAndScore = new ArrayList<Pair<Integer,Double>>();
		Iterator<Integer> iter = urlSet.iterator();
		while(iter.hasNext()){
			int urlID = iter.next();
			ArrayList<String> termsOfThisURL = urlAndTerms.get(urlID);
			double score = 0;
			for(int i=0;i<termsOfThisURL.size();i++){
				try {
					score += db.getTfidf(termsOfThisURL.get(i), urlID);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("cannot find the score of url: " + urlID + ", term: "+termsOfThisURL.get(i));
				}
			}
			urlAndScore.add(new Pair<Integer,Double>(urlID,score));
		}
		Collections.sort(urlAndScore, new Comparator<Pair>() {
			@Override
			public int compare(Pair o1, Pair o2) {
				// TODO Auto-generated method stub
				double first = (double)o1.second;
				double second = (double)o2.second;
				if(first == second) return 0;
				return first<second?-1:1;
			}
	    });
	}
	
	private void prepare() throws SQLException, IOException{
		for(int i=0;i<terms.size();i++){	
			termsAndURL.put(terms.get(i), (ArrayList<Integer>) db.getDocList(terms.get(i)));
//			ArrayList<Integer> listOfDocuments = (ArrayList<Integer>) db.getDocList(terms.get(i));
//			for(int j=0;j<listOfDocuments.size();j++) System.out.println(listOfDocuments.get(j));
		}
		for(int i=0;i<terms.size();i++){
			ArrayList<Integer> urlIDs = termsAndURL.get(terms.get(i));
			for(int j=0;j<urlIDs.size();j++){
				urlSet.add(urlIDs.get(j));
				if(urlAndTerms.containsKey(urlIDs.get(j))){
					ArrayList<String> termsOfThisURL = urlAndTerms.get(urlIDs.get(j));
					termsOfThisURL.add(terms.get(i));
//					for(String s:termsOfThisURL){
//						System.out.println(s);
//					}
					urlAndTerms.put(urlIDs.get(j), termsOfThisURL);
				}else{
					ArrayList<String> termsOfThisURL = new ArrayList<String>();
					termsOfThisURL.add(terms.get(i));
//					for(String s:termsOfThisURL){
//						System.out.println(s);
//					}
					urlAndTerms.put(urlIDs.get(j), termsOfThisURL);
				}
			}
		}
		
		
	}
	private void setQuery(String query) throws IOException{
		this.query = query;
		terms = Tokenizer.tokenize(query);
	}
	
	public List<Integer> getTopURLs(int numberOfURL, String query) throws IOException, SQLException{
		db.connection = db.getConnection();
		maximumSearch = numberOfURL;
		setQuery(query);
		prepare();
		System.out.println("done prepare!");
		setTopURL();
		System.out.println("done set top url!");
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i=0;i<maximumSearch;i++)result.add(topURL.get(i));
		db.closeConnection(db.connection);
		return result;	
	}
	
	
}
