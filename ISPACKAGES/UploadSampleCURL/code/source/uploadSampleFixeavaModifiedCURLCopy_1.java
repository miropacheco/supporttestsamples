

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class uploadSampleFixeavaModifiedCURLCopy_1

{
	// ---( internal utility methods )---

	final static uploadSampleFixeavaModifiedCURLCopy_1 _instance = new uploadSampleFixeavaModifiedCURLCopy_1();

	static uploadSampleFixeavaModifiedCURLCopy_1 _newInstance() { return new uploadSampleFixeavaModifiedCURLCopy_1(); }

	static uploadSampleFixeavaModifiedCURLCopy_1 _cast(Object o) { return (uploadSampleFixeavaModifiedCURLCopy_1)o; }

	// ---( server methods )---




	public static final void FixContentType (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(FixContentType)>> ---
		// @sigtype java 3.5
		// [i] field:0:required MimeString
		// [i] object:0:required streamBytes
		// [o] field:0:required Boundary
		IDataCursor pipelineCursor = pipeline.getCursor();
		Object obj = (IDataUtil.get(pipelineCursor, "streamBytes"));;
		byte[] b = (byte []) obj;
		String str = new String(b,0,300);
		String mime= str;
		System.out.println("my class:" + obj.getClass().getName());
		 
		//System.out.println(mime);
		int boundaryPos = mime.indexOf("boundary=")+10;
		//System.out.println(boundaryPos);
		String boundary = mime.substring( boundaryPos , mime.indexOf('"',boundaryPos +1));
		pipeline.getCursor().insertAfter("Boundary", boundary);
		
		
			
		// --- <<IS-END>> ---

                
	}
}

