package NEWSalesLoad.utils;
import java.io.File;
import org.apache.oro.io.GlobFilenameFilter;


class FilePatternFilter extends GlobFilenameFilter {
long fileAge = -1L;

String pattern = null;

public void setPattern(String paramString) {
if (paramString != null && paramString.trim().length() > 0)
this.pattern = paramString.trim();
setFilterExpression(this.pattern);
}

public void setFileAge(long paramLong) {
if (paramLong > 0L)
this.fileAge = paramLong;
}

public boolean accept(File paramFile, String paramString) {
File file = new File(paramFile, paramString);
if (file.isFile()) {
if (this.fileAge > 0L) {
long l = System.currentTimeMillis();
if (l - file.lastModified() < this.fileAge)
return false;
}
if (this.pattern == null)
return true;
return super.accept(paramFile, paramString);
}
return false;
}

public static void main(String s[]) {
FilePatternFilter fpf = new FilePatternFilter ();
}
}