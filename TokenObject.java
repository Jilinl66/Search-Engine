package cs221.project3.tokenizer;

//Define token object
class TokenObject {
	public String token = "";
	public int freq = 0;
	
	public TokenObject(){}
	
	public TokenObject (String s, int i) {
		this.token = s;
		this.freq = i;
	}
}