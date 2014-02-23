package net.pozor.pfsenseul.main;

public class Base64Content
{
	private String fileName;
	private long fileSize;
	private byte[] fileBytes;
	
	public Base64Content(String fileName,
						 long fileSize,
						 byte[] fileBytes)
	{
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.fileBytes = fileBytes;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public long getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(long fileSize)
	{
		this.fileSize = fileSize;
	}

	public byte[] getFileBytes()
	{
		return fileBytes;
	}

	public void setFileBytes(byte[] fileBytes)
	{
		this.fileBytes = fileBytes;
	}
	
}
