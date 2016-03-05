package cs221.project3.mysqldatabase;

public class Indexing {
	private String term;
	private int documentId;
	private String positions;
	private int termFrequency;
	public Indexing(String term, int documentId, String positions, int termFrequency) {
		super();
		this.term = term;
		this.documentId = documentId;
		this.positions = positions;
		this.termFrequency = termFrequency;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public int getDocumentId() {
		return documentId;
	}
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	public String getPositions() {
		return positions;
	}
	public void setPositions(String positions) {
		this.positions = positions;
	}
	public int getTermFrequency() {
		return termFrequency;
	}
	public void setTermFrequency(int termFrequency) {
		this.termFrequency = termFrequency;
	}
	
}
