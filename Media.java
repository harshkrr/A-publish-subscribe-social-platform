import java.util.*;
import java.io.*;


public class Media {
	MySet<User> Users = new MySet<User>();
	MySet<Text> Posts = new MySet<Text>();

	public Media() {

	}

	public User UserFromId(int uid) {
		int i = 0;
		while(i<Users.size()){
			if (Users.get(i).userid==uid) {
				break;
			} 
			i++;
		} 
		if (i == Users.size()){
			return null;
		} 
		return Users.get(i);
	}

	public Text TextWithId(int tid){
		int i = 0;
		while(i<Posts.size()){
			if(Posts.get(i).textid==tid){
				break;
			} i++;
		} 
		if(i == Posts.size()){
			return null;
		} 
		return Posts.get(i);
	}

	public void Publish(int t, int uid, String s, int textid){
		Text newPost = new Text(s, uid ,0 ,t, textid);
		if (this.UserFromId(uid)==null){
			User u=new User(uid);
			Users.Insert(u);
		}
		User u=UserFromId(uid);
		u.Timeline.Insert(newPost);
		Posts.Insert(newPost);
	}

	public void Repost(int t, int uid, int tid,int textid) {
		if(this.TextWithId(tid)==null){
			System.out.println("no text exist with tid for Repost "+tid);
			return;
		}
		if(this.TextWithId(textid)!=null) {
			System.out.println("Can't publish with "+textid);
		}
		String s = TextWithId(tid).statement;
		Text newRepost = new Text(s,uid,1,t,textid);
		if (this.UserFromId(uid)==null){
			User u=new User(uid);
			Users.Insert(u);
		}
		User u=UserFromId(uid);
		u.Timeline.Insert(newRepost);
		Posts.Insert(newRepost);

	}

	public void Reply(int t, int uid, String s, int tid,int replyid) {
		if(this.TextWithId(tid)==null){
			System.out.println("no text exist with tid for Reply "+tid);
			return;
		}
		Text newReply = new Text(s,uid,2,t,replyid);
		if (this.UserFromId(uid)==null){
			User u=new User(uid);
			Users.Insert(u);
		}
		User u=UserFromId(uid);
		u.Timeline.Insert(newReply);
		Posts.Insert(newReply);
		Text post = TextWithId(tid);
		post.comments.Insert(newReply);
	}

	public ArrayList<Text> Read(int uid, int t) {
		if (this.UserFromId(uid)==null){
			User u=new User(uid);
			Users.Insert(u);
		}
		User u=UserFromId(uid);
		ArrayList<Text> arr = new ArrayList<Text>();
		int i = 0;
		Subscription sub;
		while(i<u.FollowingSet.size()){
			sub = u.FollowingSet.get(i);
			int subtime = sub.Time;
			for(int j=0; j<UserFromId(sub.name).Timeline.size(); j++)
			{
				int new_ptime = UserFromId(sub.name).Timeline.get(j).ptime;
				if(new_ptime >= subtime && new_ptime >= u.LastOnlineTime && new_ptime<=t)
				{
					arr.add(UserFromId(sub.name).Timeline.get(j));
				}
			} i++;

		} 
		for (int k=0; k<u.Timeline.size(); k++){
			Text com = u.Timeline.get(k);
			for(int n = 0; n<com.comments.size(); n++){
				int comtime = com.comments.get(n).ptime;
				if (comtime>= u.LastOnlineTime && comtime<=t) {
					arr.add(com.comments.get(n));
				}
			}
		} 
		u.LastOnlineTime = t;
		Collections.sort(arr);
		return arr;
	}


	public void performAction(String actionMessage){
		String[] s = actionMessage.split(",");
		String action = s[0];
		if(action.equals("SUBSCRIBE")){
			int time=Integer.parseInt(s[1]);
			int uid=Integer.parseInt(s[2]);
			int pid=Integer.parseInt(s[3]);
			if(this.UserFromId(uid)==null){
				User u = new User(uid);
				Users.Insert(u);
			}
			if(this.UserFromId(pid)==null){
				User p = new User(pid);
				Users.Insert(p);
			}
			User u = UserFromId(uid);
			User p = UserFromId(pid);
			Subscription sub1 = new Subscription(uid,time);
			Subscription sub2 = new Subscription(pid, time);
			u.FollowingSet.Insert(sub2);
			p.FollowersSet.Insert(sub1);
		}
		else if(action.equals("UNSUBSCRIBE")){
			int time=Integer.parseInt(s[1]);
			int uid=Integer.parseInt(s[2]);
			int pid=Integer.parseInt(s[3]);
			try {
				User u = UserFromId(uid);
				try{
					User p = UserFromId(pid);
					if(u.isSubscribed(p)){
						for(int i=0;i<u.FollowingSet.size();i++){
							if(u.FollowingSet.get(i).name==pid){
								u.FollowingSet.Delete(u.FollowingSet.get(i));
							}
						}
						for(int j=0;j<p.FollowersSet.size();j++){
							if(p.FollowersSet.get(j).name==uid){
								p.FollowersSet.Delete(p.FollowersSet.get(j));
							}
					}
				 }
				// else
				// {
				// 	System.out.println("User "+uid+" is not subscribed1 to "+pid+" publisher");
				// }
			}
			catch(NullPointerException e){
				System.out.println("No user with id "+pid+" exists");
			}
		}catch(NullPointerException e){
				System.out.println("No user with id "+uid+" exists");
			}
		}
		else if(action.equals("READ")){
			int t = Integer.parseInt(s[1]);
			int uid = Integer.parseInt(s[2]);
			ArrayList<Text> new_arr = new ArrayList<Text>();
			new_arr = this.Read(uid,t);
			for(int i=0; i<new_arr.size();i++) {
				System.out.print(new_arr.get(i).statement+",");
			}
			if(new_arr.size()==0) {
				System.out.println("no text avilable for uid "+uid);
				return;
			}
		}
		else if (action.equals("PUBLISH")) {
			actionMessage = actionMessage.replaceAll("[^A-Za-z0-9]"," ");
			actionMessage = actionMessage.replaceAll("  "," ");
			String[] produce = actionMessage.split(" ");
			if (produce[3].equals("NEW")){
				this.Publish(Integer.parseInt(produce[1]),Integer.parseInt(produce[2]),produce[4],Integer.parseInt(produce[5]));
			}
			else if (produce[3].equals("REPOST")){
				int reposttime=Integer.parseInt(produce[1]);
				int id = Integer.parseInt(produce[2]);
				int preid= Integer.parseInt(produce[4]);
				int repostid = Integer.parseInt(produce[5]);
				this.Repost(reposttime,id,preid,repostid);
			}
			else if(produce[3].equals("REPLY")){
				int replytime = Integer.parseInt(produce[1]);
				int id = Integer.parseInt(produce[2]);
				int preid = Integer.parseInt(produce[4]);
				int replyid = Integer.parseInt(produce[6]);
				this.Reply(replytime,id,produce[5],preid,replyid);
			}
		}
	}
}
