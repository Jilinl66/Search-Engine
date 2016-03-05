package cs221.project3.mysqldatabase;

public class TFIDFScore {
	private String term;
	private int documentId;
	private double tfIdfScore;
	public TFIDFScore(String term, int documentId, double tfIdfScore) {
		super();
		this.term = term;
		this.documentId = documentId;
		this.tfIdfScore = tfIdfScore;
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
	public double getTfIdfScore() {
		return tfIdfScore;
	}
	public void setTfIdfScore(double tfIdfScore) {
		this.tfIdfScore = tfIdfScore;
	}
}
