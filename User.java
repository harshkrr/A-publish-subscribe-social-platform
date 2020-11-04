public class User {
	int userid;
	MySet<Text> Timeline;
	MySet<Subscription> FollowersSet; // the users who follows him
	MySet<Subscription> FollowingSet; // the users whom he follows
	int LastOnlineTime;

	public User(int uid) {
		LastOnlineTime = 0;
		userid = uid;
		Timeline = new MySet<Text>();
		FollowersSet = new MySet<Subscription>();
		FollowingSet = new MySet<Subscription>();
	}


	public Boolean isSubscribed(User t){
		int i = 0;
		while(i<FollowingSet.size()){
			if(FollowingSet.get(i).name==t.userid){
				return true;
			} 
			i++;
		} 
		return false;
	}
} 