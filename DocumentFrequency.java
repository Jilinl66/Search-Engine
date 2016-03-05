package cs221.project3.mysqldatabase;

public class DocumentFrequency {
	private String term;
	private int documentFreq;
	public DocumentFrequency(String term, int documentFreq) {
		super();
		this.term = term;
		this.documentFreq = documentFreq;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public int getDocumentFreq() {
		return documentFreq;
	}
	public void setDocumentFreq(int documentFreq) {
		this.documentFreq = documentFreq;
	}
	
}
