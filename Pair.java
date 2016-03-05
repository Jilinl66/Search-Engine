package cs221.project3.indexer;

public class Pair<First, Second> {
	public First first;// first member of pair
	public Second second;// second member of pair

	public Pair(First first, Second second) {
		this.first = first;
		this.second = second;
	}

	public void setFirst(First first) {
		this.first = first;
	}

	public void setSecond(Second second) {
		this.second = second;
	}

	public First getFirst() {
		return this.first;
	}

	public Second getSecond() {
		return this.second;
	}
}
