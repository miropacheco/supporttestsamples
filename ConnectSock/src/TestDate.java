import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TestDate {
	public static void main(String s[]) {
		try {
			Date d = new SimpleDateFormat(("MM/dd/yyyy, HH:mm:ss z")).parse("12/31/2023, 17:44:49 EST");
			System.out.println(d);
			
			//System.out.println(ZonedDateTime.ofInstant(d.toInstant(), ZoneId.of("UTC")).format(DateTimeFormatter.BASIC_ISO_DATE));
			SimpleDateFormat sd1 = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.sss'Z'");
			SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
			SimpleDateFormat format=new SimpleDateFormat();
			System.out.println(format.toLocalizedPattern());
			System.out.println(format.toPattern());
			//sd.setTimeZone(TimeZone.getTimeZone("UTC"));
			System.out.println(sd1.format(d));
			System.out.println(sd2.format(d));
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		

}
