package quickfix.examples.executor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.soap.MessageFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import quickfix.DataDictionary;
import quickfix.FieldNotFound;
import quickfix.InvalidMessage;
import quickfix.Message;
import quickfix.MessageUtils;
import quickfix.Session;
import quickfix.SessionID;

public class ConnUserInterface extends MyUserInterface {

	private Shell frame;
	private Message msg;
	private SessionID sessionID;
	Button sendPause;

	public ConnUserInterface(String msgType, SessionID sessionID, Message msg,
			Application parent, Shell frame, float orderQty, float cumQty) {

		super(msgType, sessionID, msg, parent, frame, orderQty, cumQty);
		this.frame = frame;
		this.msg = msg;
		this.sessionID = sessionID;

	}

	private Text fileName;
	private Text message;
	private String fixMessage;
	protected Button headLess;
	private Text script;

	private int checkSum(String s) {
		final int offset = s.lastIndexOf("\00110=");
		int sum = 0;
		for (int i = 0; i < offset; i++) {
			sum += s.charAt(i);
		}
		return (sum + 1) % 256;
	}

	@Override
	public void createUserInterface() throws FieldNotFound {
		frame.getDisplay().syncExec(new Runnable() {

			private TabItem tab;
			private Text error;
			private Text offSet;

			@Override
			public void run() {

				TabFolder folder = (TabFolder) frame.getChildren()[0];
				tab = new TabItem(folder, SWT.NULL);

				tab.setText("Connection");

				SashForm form = new SashForm(folder, SWT.HORIZONTAL);
				Composite c = new Composite(form, SWT.None);

				c.setLayout(new FormLayout());

				Label lFileName = new Label(c, SWT.NORMAL);
				fileName = new Text(c, SWT.BORDER);
				lFileName.setText("Fix file to  push back");

				Label lString = new Label(c, SWT.NORMAL);
				lString.setText("Fix string to push back");
				message = new Text(c, SWT.BORDER);

				Label lScript = new Label(c, SWT.NORMAL);
				lScript.setText("Fix script to run");
				script = new Text(c, SWT.BORDER);

				Button sendIt = new Button(c, SWT.NORMAL);
				sendIt.setText("Send this file");
				Button closeIt = new Button(c, SWT.NORMAL);
				closeIt.setText("Close");

				Label lOffset = new Label(c, SWT.NORMAL);
				offSet = new Text(c, SWT.BORDER);
				lOffset.setText("Offset of the fix message in the file");
				offSet.setText("24");

				sendPause = new Button(c, SWT.CHECK);
				sendPause.setText("Pause 1 second on each message");

				headLess = new Button(c, SWT.CHECK);
				headLess.setText("Add header to messages");

				/* label FILE */
				FormData data = new FormData();
				data.top = new FormAttachment(10);
				data.left = new FormAttachment(10);
				lFileName.setLayoutData(data);

				/* file name */
				data = new FormData();
				data.top = new FormAttachment(10);
				data.left = new FormAttachment(lFileName, +10);
				data.right = new FormAttachment(60, 100, 0);
				fileName.setLayoutData(data);

				/* label offset */
				data = new FormData();
				data.top = new FormAttachment(fileName, +10);
				data.left = new FormAttachment(10, 0);
				lOffset.setLayoutData(data);

				/* offset */
				data = new FormData();
				data.top = new FormAttachment(fileName, +10);
				data.left = new FormAttachment(lOffset, +0);
				offSet.setLayoutData(data);

				// pause
				data = new FormData();
				data.top = new FormAttachment(fileName, +10);
				data.left = new FormAttachment(offSet, +0);
				sendPause.setLayoutData(data);
				// headless
				data = new FormData();
				data.top = new FormAttachment(fileName, +10);
				data.left = new FormAttachment(sendPause, +0);
				headLess.setLayoutData(data);

				// text for the fix message

				data = new FormData();
				data.top = new FormAttachment(sendPause, +10);
				data.left = new FormAttachment(10);
				lString.setLayoutData(data);

				data = new FormData();
				data.top = new FormAttachment(sendPause, +10);
				data.left = new FormAttachment(lString, 0);
				data.right = new FormAttachment(60, 100, 0);
				message.setLayoutData(data);

				data = new FormData();
				data.top = new FormAttachment(lString, +10);
				data.left = new FormAttachment(+10);
				lScript.setLayoutData(data);

				data = new FormData();
				data.top = new FormAttachment(lString, +10);
				data.left = new FormAttachment(lScript, +5);
				data.right = new FormAttachment(60, 100, 0);
				script.setLayoutData(data);

				/* button send it */
				data = new FormData();
				data.top = new FormAttachment(lScript, +10);
				data.right = new FormAttachment(30, 100, 0);
				sendIt.setLayoutData(data);

				sendIt.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						frame.getDisplay().syncExec(new Runnable() {

							@Override
							public void run() {
								// script is informed, it's the first one
								// processed

								if (message.getText() != null
										&& message.getText().length() != 0) {
									// if the message is informed, gets
									// precedence over the file
									boolean lerror = false;
									String line = message.getText().replaceAll(
											"^\\s+", "");
									try {
										if (line.indexOf(sessionID
												.getBeginString()) == -1
												&& !headLess.getSelection()) {
											MessageBox alert = new MessageBox(
													frame);
											alert.setMessage("Message is incompatible with Begin string. This session only handles:"
													+ sessionID
															.getBeginString());
											alert.setText("Error in message");
											alert.open();
											lerror = true;

										}
										if (!lerror)
											this.sendLine(line);
									} catch (InvalidMessage e) {
										e.printStackTrace();
									}

								} else if (fileName.getText() != null && fileName.getText().length()!=0) {

									try {
										BufferedReader reader = new BufferedReader(
												new FileReader(fileName
														.getText()));
										System.out.println(sendPause
												.getSelection() == true);
										int i = 0;

										do {
											try {
												i++;
												String line = reader.readLine();
												if (line == null) {
													reader.close();
													break;
												} else if (line != null) {
													line = line.replaceAll(
															"^\\s+", "");
													line = new String(
															line.substring(Integer
																	.parseInt(offSet
																			.getText())));
													if (line.indexOf(sessionID
															.getBeginString()) == -1
															&& !headLess
																	.getSelection()) {
														MessageBox alert = new MessageBox(
																frame);
														alert.setMessage("Message is incompatible with Begin string. This session only handles:"
																+ sessionID
																		.getBeginString());
														alert.setText("Error in message");
														alert.open();
														reader.close();
														break;

													}
													int checkSum = checkSum(line);
													sendLine(line);

												}

											} catch (FileNotFoundException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											} catch (InvalidMessage e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

											catch (IOException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
											if (sendPause.getSelection()) {
												if (i % 500 == 0)

													Thread.sleep(500);
											}
										} while (true);

									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}

								}
								else if (script.getText()!=null && script.getText().length()!=0){
									// process the script. 
									try {
										BufferedReader reader = new BufferedReader(
												new FileReader(script
														.getText()));
										System.out.println(sendPause
												.getSelection() == true);
										parent.setScript(true);
										ScriptRunner script = new ScriptRunner(reader,parent,sessionID);
										script.run();
										
									
									}
									catch(IOException e) {
										System.out.println("Error opening file:" + e.getMessage());
									}
									
										
									
								}
							}

							private void sendLine(String line)
									throws InvalidMessage {
								if (headLess.getSelection()) {
									line = "8=" + sessionID.getBeginString()
											+ Character.toString((char) 1)
											+ "9="
											+ Integer.toString(line.length())
											+ Character.toString((char) 1)
											+ line + "10=";
									line = line + checkSum(line)
											+ Character.toString((char) 1);

								}
								if (sessionID.getBeginString().contains("4.2")) {

									quickfix.fix42.Message msg = new quickfix.fix42.Message();

									msg = (quickfix.fix42.Message) MessageUtils
											.parse(Session
													.lookupSession(sessionID),
													line);

									Session.lookupSession(sessionID).send(msg);
								} else if (sessionID.getBeginString().contains(
										"4.4")) {
									quickfix.fix44.Message msg = new quickfix.fix44.Message();

									msg = (quickfix.fix44.Message) MessageUtils
											.parse(Session
													.lookupSession(sessionID),
													line);
									/*msg = (quickfix.fix44.Message) MessageUtils
											.parse(new quickfix.fix44.MessageFactory(),
													Session.lookupSession(
															sessionID)
															.getDataDictionary(),
													line); */

									Session.lookupSession(sessionID).send(msg);

								}
							}
						});

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub

					}
				});
				// button send it

				data = new FormData();
				data.top = new FormAttachment(lScript, +10);
				data.right = new FormAttachment(50, 100, 0);
				closeIt.setLayoutData(data);

				closeIt.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						tab.dispose();
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub

					}
				});

				tab.setControl(form);

				frame.redraw();

			}
		});
	}
}