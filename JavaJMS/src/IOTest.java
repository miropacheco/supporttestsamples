import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IOTest {
	public static void main(String s[]) {
		try {
			RandomAccessFile file = new RandomAccessFile("./data/file.test", "rw");
			file.write("test.out".getBytes());
			file.seek(4);
			file.write("overlap.out".getBytes());
			file.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
