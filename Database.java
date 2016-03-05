package cs221.project3.mysqldatabase;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysql.jdbc.Connection;

import cs221.project3.indexer.Pair;
import cs221.project3.tokenizer.Tokenizer;

public class Database {

	public Connection connection = getConnection();
	public static Map<String, Integer> documentFre = new HashMap<String, Integer>();
	int numOfIndexRow;
	public static void main(String[] args) throws SQLException, IOException {
		Database database = new Database();
		List<Integer> resDocuments = new ArrayList<Integer>();
		resDocuments = database.ranking("compute");
		for (int j:resDocuments){
			System.out.println(j);
		}
		List<DocumentsData> documentsDatas = new ArrayList<DocumentsData>();
		documentsDatas = database.selectAll(resDocuments);
 //		String query = "compute science";
//		List<Integer> topList = database.ranking(query);
//		System.out.println("start");
//		for (int id: topList){
//			System.out.println(id);
//		}
//		System.out.println("end");
	}
	
	public Connection getConnection(){
		String connectionUrl = "jdbc:mysql://localhost:8889/indexer?useServerPrepStmts=false&rewriteBatchedStatements=true";
//		String connectionUrl = "jdbc:mysql://localhost:8889/indexer";
		
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = (Connection) DriverManager.getConnection(connectionUrl, "root", "root");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	public void closeConnection(Connection connection){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	// insert data into table documentData 
	public void insertData(DocumentsData documentsData){
		String sql = "INSERT INTO document_data (documentId, url, text, html, anchor, title) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
	
		Connection connection = getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, documentsData.getDocumentId());
			statement.setString(2, documentsData.getUrl().toString());
			statement.setString(3, documentsData.getText());
			statement.setString(4, documentsData.getHtml());
			statement.setString(5, documentsData.getAnchor());
			statement.setString(6, documentsData.getTitle());
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeConnection(connection);
		}
	}
	
//	//page retrieval using documentId
//	public List<DocumentsData> selectAll(List<Integer> documentIds){
//		List<DocumentsData> documentsDatas = new ArrayList<DocumentsData>();
//		String condition = "";
//	for (int i = 1; i< documentIds.size(); i++){
//		condition = condition + " OR " + documentIds.get(i);
//	}
//		String sql = "SELECT * "
//				+ "FROM document_data "
//				+ "WHERE documentId = " + documentIds.get(0) + condition;
//		
//		Connection connection = getConnection();
//		try {
//			PreparedStatement statement = connection.prepareStatement(sql);
//			ResultSet results = statement.executeQuery();
//			while(results.next()){
//				int documentId = results.getInt(1);
//				String url = results.getString(2);
//				String text = results.getString(3);
//				String html = results.getString(4);
//				String anchor = results.getString(5);
//				String title = results.getString(6);
//				DocumentsData documentsData = new DocumentsData(documentId, url, text, html, anchor, title);
//				documentsDatas.add(documentsData);
//				results.close();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally {
//			closeConnection(connection);
//		}
//		return documentsDatas;
//	}
	
	//page retrieval using documentId
		public List<DocumentsData> selectAll(List<Integer> documentIds) throws SQLException{
			if(documentIds.size()==0)return null;
			
			List<DocumentsData> documentsDatas = new ArrayList<DocumentsData>();
			DocumentsData documentsData = new DocumentsData();
			
//			original
//			String sql = "SELECT * "
//					+ "FROM document_data "
//					+ "WHERE documentId = ?";
			
			String sql = "SELECT * "
					+ "FROM document_data "
					+ "WHERE documentId IN (";
			for(int i=0;i<documentIds.size();i++){
				if(i==documentIds.size()-1)sql+="?)";
				else sql+= "?,";
			}
			if (connection.isClosed()){
				connection = getConnection();
			}
			try {
				//
				PreparedStatement statement = connection.prepareStatement(sql);
				for(int i=1;i<=documentIds.size();i++)statement.setInt(i, documentIds.get(i));
				ResultSet result = statement.executeQuery();
				while(result.next()){
					int documentId = result.getInt(1);
					String url = result.getString(2);
					String text = result.getString(3);
					String html = result.getString(4);
					String anchor = result.getString(5);
					String title = result.getString(6);
					documentsData = new DocumentsData(documentId, url, text, html, anchor, title);
					documentsDatas.add(documentsData);
				}
					
//				original	
//				PreparedStatement statement = connection.prepareStatement(sql);
//				for (int i:documentIds){
//					statement.setInt(1, i);
//					ResultSet result = statement.executeQuery();
//					result.next();
//					int documentId = result.getInt(1);
//					String url = result.getString(2);
//					String text = result.getString(3);
//					String html = result.getString(4);
//					String anchor = result.getString(5);
//					String title = result.getString(6);
//					documentsData = new DocumentsData(documentId, url, text, html, anchor, title);
//					documentsDatas.add(documentsData);
////					System.out.println(documentsData.getTitle());
// 				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return documentsDatas;
		}
	
	//insert index into database by indexing object
	public void insertIndexing(List<Indexing> indexings) throws SQLException{
		String sql = "INSERT INTO indexing (term, documentId, position, termFrequency) "
				+ "VALUES (?, ?, ?, ?)";
		if (connection.isClosed()){
			connection = getConnection();
		}
		//insert indexing
		PreparedStatement statement = connection.prepareStatement(sql);
		int i = 0;
		for (Indexing indexing : indexings){
			statement.setString(1, indexing.getTerm());
			statement.setInt(2, indexing.getDocumentId());
			statement.setString(3, indexing.getPositions());
			statement.setInt(4, indexing.getTermFrequency());
			statement.addBatch();
			i ++;
			if(i%500 == 0 || i == indexings.size()){
				statement.executeBatch();
			}
			computeDocumentFreq(indexing);
		}	
	}
	
	//compute and put documentFreq into HashMap
	public void computeDocumentFreq(Indexing indexing) throws SQLException{
		Integer fre = documentFre.get(indexing.getTerm());
		if (fre == null){
			documentFre.put(indexing.getTerm(), 1);
		}
		else{
			documentFre.put(indexing.getTerm(), fre + 1);
		}
	}
	
	//insert documentFreq into database
	public void insertDocumentFreq() throws SQLException{
		int j = 0;
		String sql  = "INSERT INTO document_freq (term, documentFreq) "
				+ "VALUES (?, ?)";
		PreparedStatement statement = connection.prepareStatement(sql);	
		Set<String> terms = documentFre.keySet();
		Iterator<String> iter = terms.iterator();
		for(int i = 0; i < terms.size(); i ++){
			String term = iter.next();
			statement.setString(1, term);
			statement.setInt(2, documentFre.get(term));
			statement.addBatch();
			j ++;
			if(j%500 == 0 || j == terms.size()){
				statement.executeBatch();
			}
		}
	}
	
	//get tfidf values from indexing table and insert into new table
	public void insertTfIdfScore() throws SQLException{
		String sql1 = "SELECT count(*) FROM indexing";
		String sql3 = "INSERT INTO tf_idf (term, documentId, tfIdfScore) "
				+ "VALUES (?, ?, ?)";
//		String sql4 = "SELECT * FROM document_freq WHERE term = ?";
		try {
			if(connection.isClosed()){
				connection = getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Statement statement1 = connection.createStatement();
		ResultSet result1 = statement1.executeQuery(sql1);
		result1.next();
		numOfIndexRow = result1.getInt(1);
		System.out.println(numOfIndexRow);
		int numOfDoc = 114947;
		int k = 32947;
		int bound = 1000;
		int lowerK = k - bound;
		if(lowerK < 0)lowerK = 0;
		int upperK = k;
		
		while (k >= 0 ){
			String sql2 = "SELECT * FROM indexing WHERE documentId >= " + lowerK
					+ " AND documentId < " + upperK;
			System.out.println("inserting: "+ lowerK + " to " + (upperK-1));
			k-=bound;
			lowerK-=bound;
			if(lowerK<0)lowerK=0;
			upperK-=bound;
			
			Statement statement2 = connection.createStatement();
			ResultSet result2 = statement2.executeQuery(sql2);
			double tfidf = 0;
			ArrayList<Indexing> temp = new ArrayList<Indexing>();
			while(result2.next())temp.add(new Indexing(result2.getString(1), result2.getInt(2), "", result2.getInt(4)));
			
			PreparedStatement statement3 = connection.prepareStatement(sql3);
			for(int i=0;i<temp.size();i++){
//				System.out.println(temp.get(i).getTerm());
//				System.out.println(temp.get(i).getDocumentId());
//				System.out.println(temp.get(i).getTermFrequency());
				int documentFreq = documentFre.get(temp.get(i).getTerm());
			    
				//compute tdidf
				tfidf = Math.log10(1 + temp.get(i).getTermFrequency()) * Math.log10(numOfDoc/documentFreq);
				statement3.setString(1, temp.get(i).getTerm());
				statement3.setInt(2, temp.get(i).getDocumentId());
				statement3.setDouble(3, tfidf);
				statement3.addBatch();
//				System.out.println(temp.get(i).getTerm() + " " + temp.get(i).getDocumentId() + " " + tfidf);
			}
			try{
				statement3.executeBatch();
			} catch (Exception e){
				System.out.println(e);
				System.out.println("trying half batch size");
				
				for(int i=0;i<temp.size();i++){
					int documentFreq = documentFre.get(temp.get(i).getTerm());
					tfidf = Math.log10(1 + temp.get(i).getTermFrequency()) * Math.log10(numOfDoc/documentFreq);
					statement3.setString(1, temp.get(i).getTerm());
					statement3.setInt(2, temp.get(i).getDocumentId());
					statement3.setDouble(3, tfidf);
					statement3.addBatch();
					if(i==temp.size()/2)statement3.executeBatch();
					if(i==temp.size()-1)statement3.executeBatch();
				}
			}
			temp.clear();
		}
		System.out.println("TFIDF is DONE!!");
	}
	
//	original
//	public void insertTfIdfScore() throws SQLException{
//		String sql1 = "SELECT count(*) FROM indexing";
//		String sql3 = "INSERT INTO tf_idf (term, documentId, tfIdfScore) "
//				+ "VALUES (?, ?, ?)";
////		String sql4 = "SELECT * FROM document_freq WHERE term = ?";
//		try {
//			if(connection.isClosed()){
//				connection = getConnection();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		Statement statement1 = connection.createStatement();
//		ResultSet result1 = statement1.executeQuery(sql1);
//		result1.next();
//		numOfIndexRow = result1.getInt(1);
//		System.out.println(numOfIndexRow);
//		int numOfDoc = 114947;
//		int k = numOfDoc;
//		while (k >= 0 ){
//		String sql2 = "SELECT * FROM indexing WHERE documentId = " + k;
////		PreparedStatement statement2 = connection.prepareStatement(sql2);
////		statement2.setInt(1, numOfDoc);
//		Statement statement2 = connection.createStatement();
//  		ResultSet result2 = statement2.executeQuery(sql2);
//		PreparedStatement statement3 = connection.prepareStatement(sql3);
//		double tfidf = 0;
////		PreparedStatement statement4 = connection.prepareStatement(sql4);
//		while(result2.next()){
//			String term = result2.getString(1);
//			int  documentId = result2.getInt(2);
//			int termFrequency = result2.getInt(4);
////			statement4.setString(1, term);
////			ResultSet result4 = statement4.executeQuery();
////			result4.next();
////			int documentFreq = result4.getInt(2);
//			int documentFreq = documentFre.get(term);
//		    
//			//compute tdidf
//			tfidf = Math.log10(1 + termFrequency) * Math.log10(numOfDoc/documentFreq); 
//			statement3.setString(1, term);
//			statement3.setInt(2, documentId);
//			statement3.setDouble(3, tfidf);
//			System.out.println(term + " " + documentId + " " + tfidf);
//			statement3.execute();
////			statement3.addBatch();
////			i++;
////			if (i%500 == 0 || i == numOfIndexRow){
////				statement3.executeBatch();
////			}
//		}
// 		k--;
//		}
//	}
	
	//get tfidf value for each term and document
	public Double getTfidf(String term, int documentId) throws SQLException{
		String sql = "SELECT * "
				+ "FROM tf_idf "
				+ "WHERE term = ? " + "AND documentId = ?";
		try {
			if(connection.isClosed()){
				connection = getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, term);
		statement.setInt(2, documentId);;
		ResultSet result = statement.executeQuery();
		result.next();
		return result.getDouble(3);
	}
	
	//get all documents contain a specific term
  	public List<Integer> getDocList(String term) throws SQLException{
  		List<Integer> docIdList = new ArrayList<Integer>();
		String sql = "SELECT * "
				+ "FROM tf_idf "
				+ "WHERE term = ?";
		try {
			if(connection.isClosed()){
				connection = getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, term);
		ResultSet results = statement.executeQuery();
		while(results.next()){
			docIdList.add(results.getInt(2));
		}
		return docIdList;
	}
	
  	//get all documents which contain all terms in query
	public List<Integer> getAllDocList(String query) throws SQLException, IOException{
		List<Integer> docList = new ArrayList<Integer>();
		List<Integer> docList1 = new ArrayList<Integer>();
		List<Integer> docList2 = new ArrayList<Integer>();
		List<String> termlist = Tokenizer.myTokenizeFile(query.toLowerCase());
		docList1 = getDocList(termlist.get(0));
		for (String term : termlist){
			docList.clear();
			docList2.clear();	
			docList2 = this.getDocList(term);
			for (int Id : docList2){
				if (docList1.contains(Id)){
					docList.add(Id);
				}
			}
			docList1.clear();
			docList1 = docList;
		}
		for (int i:docList){
			System.out.println(i);
		}
		return docList;
	}
	
	//score and get top5 documents
	public List<Integer> ranking(String query) throws SQLException, IOException{
		List<String> termlist = Tokenizer.myTokenizeFile(query.toLowerCase());
		List<Integer> docList = this.getDocList(query);
		
//		List<Integer> docList = this.getAllDocList(query);
//		Map<Integer, Double> docScores = new HashMap<Integer, Double>();
//		for (int Id: docList){
//			Double score = (double) 0;
//			for (String term: termlist){
//				score += getTfidf(term, Id);
//			}
//			docScores.put(Id, score);
//		}
		ArrayList<Pair<Integer,Double>> docScores = new ArrayList<Pair<Integer,Double>>();
		for(int Id:docList){
			Double score = 0.0;
			for(String term:termlist){
				score+= getTfidf(term, Id);
			}
			docScores.add(new Pair<Integer,Double>(Id,score));
		}
		Collections.sort(docScores, new Comparator<Pair>() {
			@Override
			public int compare(Pair o1, Pair o2) {
				// TODO Auto-generated method stub
				double first = (double)o1.second;
				double second = (double)o2.second;
				if(first == second) return 0;
				return first<second?-1:1;
			}
	    });
		ArrayList<Integer> topList = new ArrayList<Integer>();
		for(int i=0;i<5;i++)topList.add(docScores.get(i).first);
		return topList;
// 		docScores = sortByComparator(docScores, true);
//		@SuppressWarnings("rawtypes")
//		Iterator it = docScores.entrySet().iterator();
//		ArrayList<Integer> topList = new ArrayList<Integer>();
//		for(int i = 0; i<5; i++){
//			if(it.hasNext()){
//				@SuppressWarnings("rawtypes")
//				Map.Entry pair = (Map.Entry) it.next();
//				topList.add((Integer)pair.getKey());
//			}
//			else break;
//		}
//		return topList;
	}
	
	//sort hashmap by value(document score)
	private static Map<Integer, Double> sortByComparator(Map<Integer, Double> unsortMap, final boolean order){
        List<Map.Entry<Integer, Double>> list = new LinkedList<Map.Entry<Integer, Double>>(unsortMap.entrySet());
        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>(){
            public int compare(Map.Entry<Integer, Double> o1,
            	Map.Entry<Integer, Double> o2){
                if (order){
                    return o1.getValue().compareTo(o2.getValue());
                }
                else{
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> entry : list){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
	
	public void buildDocfreqMap() throws SQLException{
		String sql1 = "SELECT * FROM document_freq";
		Statement statement1 = connection.createStatement();
		ResultSet result1 = statement1.executeQuery(sql1);
		while(result1.next()){
			documentFre.put(result1.getString(1), result1.getInt(2));
		}
	}
	
	public List<String> getRanking(int numberOfRanking, String term) throws SQLException{
		List<String> tokens = Tokenizer.tokenize(term);
		List<String> urls = new ArrayList<String>();
		
		String sql = "SELECT * "
				+ "FROM tf_idf "
				+ "WHERE term in (";
		for(int i = 0; i<tokens.size();i++){
			System.out.println("i: "+ i+", token: "+tokens.get(i));
			if(i==tokens.size()-1)sql+= " ? )";
			else sql+= " ?,";
		}
		System.out.println("sql: " + sql);
		try {
			if(connection.isClosed()){
				connection = getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PreparedStatement statement = connection.prepareStatement(sql);
		for(int i=0;i<tokens.size();i++)statement.setString(i+1, tokens.get(i));
		
		ResultSet results = statement.executeQuery();
		ArrayList<Integer> listOfURLIds = new ArrayList<Integer>();
		while(results.next()){
			listOfURLIds.add(results.getInt(2));
		}
		
		sql = "SELECT * "
			+ "FROM document_data "
			+ "WHERE documentId IN (";
		for(int i=1;i<=listOfURLIds.size();i++){
			if(i==listOfURLIds.size())sql+=" ? )";
			else sql+= " ? ,";
		}
		if (connection.isClosed()){
			connection = getConnection();
		}
			//
		statement = connection.prepareStatement(sql);
		System.out.println("number of found urls: "+listOfURLIds.size());
		for(int i=1;i<=listOfURLIds.size();i++)statement.setInt(i, listOfURLIds.get(i-1));
		ResultSet result = statement.executeQuery();
		while(result.next()){
			int documentId = result.getInt(1);
			String url = result.getString(2);
			String text = result.getString(3);
			String html = result.getString(4);
			String anchor = result.getString(5);
			String title = result.getString(6);
			DocumentsData documentsData = new DocumentsData(documentId, url, text, html, anchor, title);
			urls.add(documentsData.getUrl());
		}
		if(urls.size()>=5) return urls.subList(0, numberOfRanking); 
		else return urls.subList(0, urls.size());
	}
	
	
	
	
	
	
	
	
	
	
}