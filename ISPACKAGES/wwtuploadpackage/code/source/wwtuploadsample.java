

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class wwtuploadsample

{
	// ---( internal utility methods )---

	final static wwtuploadsample _instance = new wwtuploadsample();

	static wwtuploadsample _newInstance() { return new wwtuploadsample(); }

	static wwtuploadsample _cast(Object o) { return (wwtuploadsample)o; }

	// ---( server methods )---




	public static final void FixContentType (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(FixContentType)>> ---
		// @sigtype java 3.5
		// [i] field:0:required MimeString
		// [i] object:0:required streamString
		// [o] field:0:required Boundary
		IDataCursor pipelineCursor = pipeline.getCursor();
		Object obj = (IDataUtil.get(pipelineCursor, "streamString"));;
		//byte[] b = (byte []) obj;
		//String mime = new String(b);
		String mime= (String) obj;
		pipeline.getCursor().insertAfter("incomingValue",mime);
		//System.out.println("mime:" + mime);
		 
		//System.out.println(mime);
		int boundaryPos = mime.indexOf("boundary=")+10;
		//System.out.println(boundaryPos);
		String boundary = mime.substring( boundaryPos , mime.indexOf('"',boundaryPos +1));
		pipeline.getCursor().insertAfter("Boundary", boundary);
		
		
		
			
		// --- <<IS-END>> ---

                
	}
}

