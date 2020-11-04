public class Node<T>{
	T data;
	Node<T> next; 
	public Node(T t, Node<T> n){
		data = t;
		next = n;
	}
}