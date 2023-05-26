package com.kingdee.shr;

@SuppressWarnings("serial")
public class CryptException extends Exception
{
    private Throwable nested;

    public CryptException() {}

    public CryptException(String s)
	{
		super(s);
    }

    public CryptException(String s, Throwable ex)
	{
		super(s);
		nested = ex;
    }

    public Throwable getNested()
	{
		return nested;
    }

    public String getMessage() {
		if (nested == null)
		{
		    return super.getMessage();
		} else
		{
		    return (super.getMessage() +
					"\nnested exception is: \n\t" +
					nested.toString());
		}
    }

    public void printStackTrace(java.io.PrintStream ps)
	{
		if (nested == null)
		{
		    super.printStackTrace(ps);
		}
		else
		{
		    synchronized(ps) {
				ps.println(this);
				nested.printStackTrace(ps);
		    }
		}
    }

    public void printStackTrace()
	{
		printStackTrace(System.err);
    }

    public void printStackTrace(java.io.PrintWriter pw)
	{
		if (nested == null)
		{
		    super.printStackTrace(pw);
		}
		else
		{
		    synchronized(pw) {
				pw.println(this);
				nested.printStackTrace(pw);
		    }
		}
    }
}

