package ssmith.lang;

public class TimeFunctions {

	private TimeFunctions() {
	}

	public static void sleep( long pTime )
	{
		try
		{
			Thread.sleep( pTime );
		}
		catch( InterruptedException e )
		{
			//e.printStackTrace();
		}
	}

}
