package cs221.project3.mysqldatabase;

public class DocumentsData {
	public int documentId;
	public String url;
	public String text;
	public String html;
	public String anchor;
	public String title;
	public DocumentsData() {
		super();
	}
	public DocumentsData(int documentId, String url, String text, String html, String anchor, String title) {
		super();
		this.documentId = documentId;
		this.url = url;
		this.text = text;
		this.html = html;
		this.anchor = anchor;
		this.title = title;
	}
	public int getDocumentId() {
		return documentId;
	}
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getAnchor() {
		return anchor;
	}
	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}