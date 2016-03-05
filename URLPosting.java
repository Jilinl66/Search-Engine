package cs221.project3.indexer;

import java.util.ArrayList;

public class URLPosting {
	
	public int urlID = 0;
	ArrayList<Integer> pos = new ArrayList<Integer>();
	
	public URLPosting(int urlID){
		this.urlID = urlID;
	}
	
	public boolean addPosition(int pos){
		return this.pos.add(pos);
	}
	
	public int getFrequency(){
		return this.pos.size();
	}
	
	public ArrayList<Integer> getPosition(){
		return this.pos;
	}
	
	public String toString(){
		String result = "";
		for(int i=0;i<pos.size();i++){
			if(i==pos.size()-1) result+= pos.get(i);
			else result+=pos.get(i)+", ";
		}
		return result;
	}
}
