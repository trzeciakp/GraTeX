package pl.edu.agh.gratex.gui;

import java.awt.EventQueue;

public class Application
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
					{
						if ("Nimbus".equals(info.getName()))
						{
							javax.swing.UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				}
				catch (ClassNotFoundException ex)
				{
					java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
				}
				catch (InstantiationException ex)
				{
					java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
				}
				catch (IllegalAccessException ex)
				{
					java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
				}
				catch (javax.swing.UnsupportedLookAndFeelException ex)
				{
					java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
				}

				try
				{
					new MainWindow();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					ControlManager.reportError("The application has encountered an error:\n" + e.toString());
				}
			}
		});
	}
}
