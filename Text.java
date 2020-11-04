public class Text implements Comparable<Text> {
	int textid;
	String statement;
	int flag;
	int ptime;
	int publisherid;
	MySet<Text> comments;
	

	public Text(String s, int u, int f, int time, int tid) {
		textid = tid;
		statement = s;
		flag = f;
		ptime = time;
		publisherid = u;
		comments = new MySet<Text>();
	}

	public int compareTo(Text text){
		if(text.ptime>this.ptime){
			return 1;
		}else if (text.ptime<this.ptime) {
			return -1;
		}else{
			return 0;
		}
	}
}