   

  import javax.jms.JMSException;
  
  import javax.jms.Message;
  import javax.jms.MessageListener;
  import javax.jms.TextMessage;
  import com.sun.genericra.inbound.ActivationSpec;
  public class SpringMdp implements MessageListener {

     public void onMessage(Message message) {
        try {
          
        	Thread.currentThread().sleep(50);
          
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
  }