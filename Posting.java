package cs221.project3.indexer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class Posting {

	public HashMap<Integer, URLPosting> listOfURL = new HashMap<Integer, URLPosting>();
	
	public static int getHashID(String url){
		return url.hashCode();
	}
	
	public int getNumberOfPage(){
		return listOfURL.size();
	}
	
	public boolean insertPosting(String url, int pos){
		int urlID = getHashID(url);
		if(listOfURL.containsKey(urlID))return listOfURL.get(urlID).addPosition(pos);
		else {
			URLPosting urlPosting = new URLPosting(urlID);
			listOfURL.put(urlID, urlPosting);
			return listOfURL.get(urlID).addPosition(pos);
		}
	}
	
	public URLPosting getURLPosting(String url){
		int urlID = getHashID(url);
		return listOfURL.get(urlID);
	}
	
	public String toString(){
		Set<Integer> keyList = listOfURL.keySet();
		Iterator<Integer> iter = keyList.iterator();
		String result = "";
		for(int i=0;i<listOfURL.size();i++){
			int key = (int) iter.next();
			if(i==listOfURL.size()-1) result += key + "=[" + listOfURL.get(key).toString()+ "]";
			else result += key + "=[" + listOfURL.get(key).toString()+ "], ";
		}
		return result;
		
	}
	
//	public List<Pair<Integer,URLPosting>> getAllURLPosting(){
//		ArrayList<Pair<Integer,URLPosting>> list = new ArrayList<Pair<Integer,URLPosting>>();
//		for(Entry<Integer, URLPosting> entry : listOfURL.entrySet()) {
//			list.add(new Pair<Integer,URLPosting>(entry.getKey(),entry.getValue()));
//		}
//		return list;
//	}
	
}

