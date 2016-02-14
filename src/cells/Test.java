package cells;

public class Test {
	
	public static void main(String args[]) {
		Test t = new Test();
		String test = "abcd";
		String newString = t.shift(test, 2);
		System.out.println(newString);
		
	}
	
	public String shift(String str, int amt) {
		String temp = "";
		int n = str.length();
		for(int i = 0; i < n; i++) {
			temp += str.charAt((i + amt) % n);
		}
		return temp;
	}

}
