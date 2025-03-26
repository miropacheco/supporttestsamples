import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class EncodingTest {
	public static void main(String s1[]) {

		try {
			FileInputStream fs = new FileInputStream(new File("c:/tmp/test.txt"));
			String s = new BufferedReader(
				      new InputStreamReader(fs, StandardCharsets.ISO_8859_1))
				        .lines()
				        .collect(Collectors.joining("\n"));

			System.out.println(s);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	
		
	}

}
