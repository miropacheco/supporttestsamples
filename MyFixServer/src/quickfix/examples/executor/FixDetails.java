package quickfix.examples.executor;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.ole.win32.IFont;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import quickfix.*;

import quickfix.Field;
import quickfix.FieldNotFound;
import quickfix.Message;

public class FixDetails {
	private Shell ch;
	private Shell parent;
	private Composite child;
	private Message msg;
	private MyUserInterface m;

	public FixDetails(Shell parent, Message msg, MyUserInterface m) {
		ch = new Shell(parent);
		this.m = m;
		ch.setLayout(new FillLayout());
		ScrolledComposite childScroll = new ScrolledComposite(ch, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);

		ch.setSize(500, 500);
		child = new Composite(childScroll, SWT.None);
		childScroll.setContent(child);
		this.parent = parent;
		this.msg = msg;

	}

	public void show() {
		parent.getDisplay().syncExec(new Runnable() {
			HashMap<Text, Field> map;

			@Override
			public void run() {
				try {
					DataDictionary dataDict = new DataDictionary(msg
							.getHeader().getString(8).replaceAll("\\.", "")
							+ ".xml");
					Label prevLabel = null;

					child.setLayout(new FormLayout());
					Iterator<Field<?>> it = msg.getHeader().iterator();
					map = new HashMap<Text, Field>();
					FormData data;
					while (it.hasNext()) {
						Field f = it.next();
						String fieldName = dataDict.getFieldName(f.getField());
						Label label = new Label(child, SWT.NONE);
						label.setText(fieldName + "(" + f.getField() + ")");
						data = new FormData();
						data.left = new FormAttachment(5, 0);
						data.right = new FormAttachment(40, 0);
						if (prevLabel != null)
							data.top = new FormAttachment(prevLabel, +10);
						else {
							data.top = new FormAttachment(0, +5);

						}
						label.setLayoutData(data);

						Label value = new Label(child, SWT.NONE);
						value.setText(f.getObject().toString());
						data = new FormData();
						data.left = new FormAttachment(label, +5);
						if (prevLabel != null)
							data.top = new FormAttachment(prevLabel, +10);
						else
							data.top = new FormAttachment(0, +5);
						value.setLayoutData(data);
						prevLabel = label;

					}

					it = msg.iterator();

					while (it.hasNext()) {

						Field f = it.next();
						String fieldName = dataDict.getFieldName(f.getField());
						Label label = new Label(child, SWT.NONE);
						label.setText(fieldName == null ? "field not found"
								: fieldName + "(" + f.getField() + ")");
						data = new FormData();
						data.left = new FormAttachment(5, 0);
						data.right = new FormAttachment(40, 0);
						if (prevLabel != null)
							data.top = new FormAttachment(prevLabel, +10);
						else {
							data.top = new FormAttachment(0, +5);

						}

						label.setLayoutData(data);

						Text value = new Text(child, SWT.BORDER);
						value.setText(f.getObject().toString());

						data = new FormData();
						data.left = new FormAttachment(label, +5);
						if (prevLabel != null)
							data.top = new FormAttachment(prevLabel, +10);
						else
							data.top = new FormAttachment(0, +5);
						data.right = new FormAttachment(100,-30);
						value.setLayoutData(data);
						
						prevLabel = label;
						map.put(value, f);

					}
					Button btn = new Button(child, SWT.PUSH);
					btn.setText("Update values");

					data = new FormData();
					data.left = new FormAttachment(0, +50);
					if (prevLabel != null)
						data.top = new FormAttachment(prevLabel, +10);
					else
						data.top = new FormAttachment(0, +5);

					btn.setLayoutData(data);
					btn.addSelectionListener(new SelectionListener() {

						@Override
						public void widgetSelected(SelectionEvent arg0) {
							Collection<Text> c = map.keySet();
							for (Text text : c) {
								Field field = map.get(text);
								if (field instanceof StringField) {
									((StringField) field).setValue(text
											.getText());
								}
								if (field instanceof IntField) {
									((IntField) field).setValue(Integer.parseInt(text.getText()));

								} else if (field instanceof CharField) {
									((CharField) field).setValue(text.getText().toCharArray()[0]);

								} else if (field instanceof DecimalField) {
									((DecimalField) field).setValue(Double.parseDouble(text.getText()));

								} else if (field instanceof DoubleField) {
									((DoubleField) field).setValue(Double.parseDouble(text.getText()));

								} else if (field instanceof UtcDateOnlyField) {
									try {
										((UtcDateOnlyField) field).setValue(DateFormat.getDateInstance().parse(text.getText()));
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} else if (field instanceof UtcTimeOnlyField) {
									try {
										((UtcTimeOnlyField) field).setValue(DateFormat.getDateInstance().parse(text.getText()));
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} else if (field instanceof UtcTimeStampField) {
									try {
										((UtcTimeStampField) field).setValue(DateFormat.getDateInstance().parse(text.getText()));
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} else if (field instanceof BooleanField) {
									((BooleanField) field).setValue(Boolean.parseBoolean(text.getText()));

								}
								m.updateMsgs();

							}

						}

						@Override
						public void widgetDefaultSelected(SelectionEvent arg0) {
							// TODO Auto-generated method stub

						}
					});

					child.pack();
					child.redraw();

					ch.open();

				} catch (ConfigError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FieldNotFound e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}
}
