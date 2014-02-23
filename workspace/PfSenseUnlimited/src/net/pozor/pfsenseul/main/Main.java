package net.pozor.pfsenseul.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Main
{
	private static String[] fileList;
	private static String xmlFile;
	
	public static void main(String [] args)
	{
		Display display = new Display();
		Shell shell = new Shell(display);
		
		shell.setSize(300, 400);
		shell.setText("XML 4 pfSense");
		
		Composite inputComposite = new Composite(shell, SWT.BORDER);
		FormData formDataInput = new FormData();
		formDataInput.top = new FormAttachment(0, 5);
		formDataInput.bottom = new FormAttachment(40, -5);
		formDataInput.left = new FormAttachment(0, 5);
		formDataInput.right = new FormAttachment(100, -5);
		inputComposite.setLayoutData(formDataInput);
		inputComposite.setLayout(new FormLayout());
		
		final Text inputText = new Text(inputComposite, SWT.V_SCROLL);
		FormData formDataInputLabel = new FormData();
		formDataInputLabel.top = new FormAttachment(0, 5);
		formDataInputLabel.bottom = new FormAttachment(100, 0);
		formDataInputLabel.left = new FormAttachment(0, 5);
		formDataInputLabel.right = new FormAttachment(100, 0);
		inputText.setLayoutData(formDataInputLabel);
		inputText.setEditable(false);		
		inputText.setText("Zvleci vhodne datoteke za dodajanje v XML.");
		
		Composite outputComposite = new Composite(shell, SWT.BORDER);
		FormData formDataInput2 = new FormData();
		formDataInput2.top = new FormAttachment(inputComposite, 5, SWT.BOTTOM);
		formDataInput2.bottom = new FormAttachment(80, -5);
		formDataInput2.left = new FormAttachment(0, 5);
		formDataInput2.right = new FormAttachment(100, -5);
		outputComposite.setLayoutData(formDataInput2);
		outputComposite.setLayout(new FormLayout());
		
		final Text xmlText = new Text(outputComposite, SWT.V_SCROLL);
		FormData formDataXmlLabel = new FormData();
		formDataXmlLabel.top = new FormAttachment(0, 5);
		formDataXmlLabel.bottom = new FormAttachment(100, 0);
		formDataXmlLabel.left = new FormAttachment(0, 5);
		formDataXmlLabel.right = new FormAttachment(100, 0);
		xmlText.setLayoutData(formDataXmlLabel);
		xmlText.setText("Zvleci XML datoteko, ki jo želiš spremeniti.");
		
		xmlText.setEditable(false);
		
		DropTarget dt0 = new DropTarget(xmlText, DND.DROP_DEFAULT | DND.DROP_MOVE);
		dt0.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dt0.addDropListener(new DropTargetListener() {
			
			@Override
			public void dropAccept(DropTargetEvent arg0)
			{
				
			}
			
			@Override
			public void drop(DropTargetEvent arg0)
			{	
				xmlFile = ((String[])arg0.data)[0];
				xmlText.setText("");
				
				xmlText.append("Izbrana XML datoteka: \n\n");				
				xmlText.append(xmlFile.substring(xmlFile.lastIndexOf(File.separator) + 1, xmlFile.length()) + "\n");
			}
			
			@Override
			public void dragOver(DropTargetEvent arg0)
			{
				
			}
			
			@Override
			public void dragOperationChanged(DropTargetEvent arg0)
			{
				
			}
			
			@Override
			public void dragLeave(DropTargetEvent arg0)
			{
				
			}
			
			@Override
			public void dragEnter(DropTargetEvent arg0)
			{
				
			}
		});
		
		Composite buttonComposite = new Composite(shell, SWT.BORDER);
		FormData formDataInput3 = new FormData();
		formDataInput3.top = new FormAttachment(outputComposite, 5, SWT.BOTTOM);
		formDataInput3.bottom = new FormAttachment(100, -5);
		formDataInput3.left = new FormAttachment(0, 5);
		formDataInput3.right = new FormAttachment(100, -5);
		buttonComposite.setLayoutData(formDataInput3);
		buttonComposite.setLayout(new FormLayout());
		
		Button button = new Button(buttonComposite, SWT.PUSH);
		FormData formDataInput4 = new FormData();
		formDataInput4.top = new FormAttachment(0, 5);
		formDataInput4.bottom = new FormAttachment(100, -5);
		formDataInput4.left = new FormAttachment(0, 5);
		formDataInput4.right = new FormAttachment(100, -5);
		button.setLayoutData(formDataInput4);
		
		button.setText("Zapiši XML");
		
		button.addSelectionListener(new SelectionListener()
		{	
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{	
				ArrayList<Base64Content> contents = new ArrayList<Base64Content>();
				
				if (fileList != null)
				{	
					for (String file : fileList)
					{
						File inputFile = new File(file);
						
						try
						{
							byte[] inputBytes = loadFile(inputFile);
							
							Base64Content content = new Base64Content(inputFile.getName(),
																	  inputFile.length(),
																	  Base64.encodeBase64(inputBytes/*, true*/));
							
							contents.add(content);
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}	
					}
				}
				
				File xmlBackupFile = new File(xmlFile);
				
				readWriteXml(xmlBackupFile, contents);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0)
			{
				widgetSelected(arg0);
			}
		});

		DropTarget dt = new DropTarget(inputText, DND.DROP_DEFAULT | DND.DROP_MOVE);
		dt.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dt.addDropListener(new DropTargetListener() {
			
			@Override
			public void dropAccept(DropTargetEvent arg0)
			{	
				
			}
			
			@Override
			public void drop(DropTargetEvent arg0)
			{	
				fileList = (String[])arg0.data;
				
				inputText.setText("");
				
				inputText.append("Izbrane datoteke: \n\n");
				
				for (String file : fileList)
				{		
					inputText.append(file.substring(file.lastIndexOf(File.separator) + 1, file.length()) + "\n");
				}
			}
			
			@Override
			public void dragOver(DropTargetEvent arg0)
			{
				
			}
			
			@Override
			public void dragOperationChanged(DropTargetEvent arg0)
			{
				
			}
			
			@Override
			public void dragLeave(DropTargetEvent arg0)
			{
				
			}
			
			@Override
			public void dragEnter(DropTargetEvent arg0)
			{
				
			}
		});
		
		shell.setLayout (new FormLayout());
		shell.open();
		while (!shell.isDisposed()) 
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		display.dispose();
	}
	
	private static byte[] loadFile(File file) throws IOException
	{
	    InputStream is = new FileInputStream(file);
	    
	    long length = file.length();
	    if (length > Integer.MAX_VALUE)
	    {
	        // File is too large
	    	System.out.println("Too large");
	    }
	    byte[] bytes = new byte[(int)length];
	    
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0)
	    {
	        offset += numRead;
	    }
	    
	    is.close();
 
	    if (offset < bytes.length)
	    {
	        throw new IOException("Could not completely read file "+ file.getName());
	    }
	    return bytes;
	}
	
	public static void writeFile(byte[] bytes, String fileName) 
	{
		//String strFilePath = "C:/Users/Mito/Desktop/testmapa/" + fileName +"_encoded_.txt";
		String strFilePath = "/home/albert/Desktop/testmapa/" + fileName +"_encoded_.txt";
		
		System.out.println("Writing " + strFilePath);
		
		try
		{
			FileOutputStream fos = new FileOutputStream(strFilePath);
			fos.write(bytes);
			fos.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}  
	}
	
	private static void readWriteXml(File xmlFile2, ArrayList<Base64Content> contents)
	{	
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		String xmlEdit = xmlFile.substring(0, xmlFile.lastIndexOf(".")) + "_edit.xml";
		
		File temp = new File(xmlEdit);
		 
		try {
			
			temp.createNewFile();
			bw = new BufferedWriter(new FileWriter(temp));
 
			String line;
 
			br = new BufferedReader(new FileReader(xmlFile2));
			
			while ((line = br.readLine()) != null)
			{	
				bw.write(line);
				bw.newLine();
				
				if (line.contains("</element>"))
				{
					bw.newLine();
					for (Base64Content content : contents)
					{
						bw.write("        <element>");
						bw.newLine();
						bw.write("            <name>" + "captiveportal-" + content.getFileName() + "</name>");
						bw.newLine();
						bw.write("            <size>" + content.getFileSize() + "</size>");
						bw.newLine();
						bw.write("            <content>");
						
						// Write base64 data
						String string = new String(content.getFileBytes(), "UTF-8");
						
						bw.write(string);
						
						bw.write("</content>");
						bw.newLine();
						bw.write("        </element>");
						bw.newLine();
					}
					contents.clear();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (br != null)
				{
					br.close();
				}
				if (bw != null)
				{
					bw.close();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		
	}

}
