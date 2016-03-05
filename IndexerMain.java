 package cs221.project3.indexer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import cs221.project3.searcher.Searcher;

public class IndexerMain {

	public static void main(String[] args) throws IOException, SQLException {
		Indexer idx = new Indexer();
		idx.db.connection = idx.db.getConnection();
//		idx.generateIndex("url_text.txt");
		
//		create documentData
//		idx.getDocumentsDataAndIndex("data.txt");
//		try {
//			idx.db.insertDocumentFreq();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
//		create tfidf
//		idx.db.buildDocfreqMap();
//		idx.db.insertTfIdfScore();
		
		
//		System.out.println("searching");
//		Searcher s = new Searcher();
//		List<Integer> l = s.getTopURLs(10, "mondego");
//		for(int i=0;i<l.size();i++){
//			System.out.println(l.get(i));
//		}
		
		idx.db.connection.close();
//		idx.printIndex();
//		idx.toFile("testyGarden.txt");
		System.out.println("#documents: " +idx.numberOfDocument);
	}
}
