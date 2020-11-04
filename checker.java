import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class checker
{
	public static void main ( String args [])
	{
		BufferedReader br = null;
		Media r = new Media();

		try {
			String actionString;
			br = new BufferedReader(new FileReader("actions1.txt"));

			while ((actionString = br.readLine()) != null) {

				r.performAction(actionString);
				System.out.println(actionString);
							}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
}
