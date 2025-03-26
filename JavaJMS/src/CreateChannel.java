import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAlreadyExistsException;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nIllegalArgumentException;
import com.pcbsys.nirvana.client.nRealmUnreachableException;
import com.pcbsys.nirvana.client.nRequestTimedOutException;
import com.pcbsys.nirvana.client.nSecurityException;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAlreadyInitialisedException;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
import com.pcbsys.nirvana.client.nSessionNotConnectedException;
import com.pcbsys.nirvana.client.nSessionPausedException;
import com.pcbsys.nirvana.client.nUnexpectedResponseException;
import com.pcbsys.nirvana.client.nUnknownRemoteRealmException;

public class CreateChannel {
	public static void main(String s[]) throws nIllegalArgumentException, nRealmUnreachableException,
			nSecurityException, nSessionNotConnectedException, nSessionAlreadyInitialisedException, IOException,
			nUnknownRemoteRealmException, nChannelAlreadyExistsException, nSessionPausedException,
			nUnexpectedResponseException, nRequestTimedOutException {
		Path path = Paths.get("/tmp/Person.pb");
		String[] RNAME = { "nsp://10.130.215.24:9000" } ;
		nSessionAttributes nsa = new nSessionAttributes(RNAME);
		nsa.setName("my session");
		nSession mySession = nSessionFactory.create(nsa, "user1", "");
		mySession.init();

		nChannelAttributes cattrib = new nChannelAttributes();
		cattrib.setType(nChannelAttributes.MIXED_TYPE);
		

		cattrib.setName("Person1");

		byte[] bytes = Files.readAllBytes(path);
		byte[][] descriptors = new byte[1][bytes.length];
		descriptors[0] = bytes;
		cattrib.setProtobufDescriptorSets(descriptors);
		nChannel myChannel = mySession.createChannel(cattrib);

	}

}
