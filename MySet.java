public class MySet<T>{
	Node<T> head = null;

	
	public Boolean isEmpty(){
		if(head == null)
			return true;
		else
			return false;
	}
	
	public Boolean isMember(T t){
		Node<T> temp = head;
		int x = 0;
		while(temp != null) {
			if(temp.data == t){
				x++;
			}
			temp = temp.next;	
		}
		if(x>0){
			return true;
		}else{
			return false;
		}
	}
	 
	public void Insert(T t){
		if(!(this.isMember(t))){
	        Node<T> temp = head;
			Node<T> temp1 = new Node<T>(t,null);
			if(head == null){
				head = temp1;
			}else{
				while(temp.next!=null){
					temp = temp.next;
				}
				temp.next = temp1;
			}
		}		  
	}
	 
	public void Delete(Object o) {
		Node<T> curr = head;
		Node<T> prev = null;
		while( curr !=null && curr.data != o ){
			prev = curr;
			curr = curr.next;	
		}
		if(curr!=null){
			prev.next = curr.next;
		}
	}
	 
	 
	public MySet<T> union(MySet<T> a){ 
		MySet<T> c = new MySet<T>();
	    Node<T> temp = this.head;
		Node<T> temp1 = a.head;
		while(temp != null) { 
	    	c.Insert(temp.data);
	    	temp=temp.next;
		}
		while(temp1 != null){
			c.Insert(temp1.data);
			temp1 = temp1.next;
		}
		return c;
	}
		 
	public MySet<T> intersection(MySet<T> a) {
		MySet<T> d = new MySet<T>();
		Node<T> temp = a.head;
		while(temp != null){
			if((this.isMember(temp.data))){
				d.Insert(temp.data);
			}
			temp=temp.next;
		}
		return d;
	}

	public int size(){
		Node<T> temp = head;
		int i = 0;
		while(temp!= null){
			i++;
			temp = temp.next;
		} 
		return i;
	}	
	
	public T get(int i){
		Node<T> temp = head;
		for(int k=0;k<i;k++){
			if(temp==null)
				break;
			temp=temp.next;
		}
		return temp.data;
	}
}
	
 