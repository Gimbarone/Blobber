package graphic;

import javax.swing.JFrame;
/**
 * Classe che implementa il Frame in cui effettuiamo la
 * scelta del tipo di divisione di un file specifico
 * @author Gamberi Elia
 *
 */
public class ChooseFrame extends JFrame{
	
	/**
	 * Frame per quando scegliamo che tipo di divisione/join vogliamo eseguire
	 * 
	 */
	public ChooseFrame()
	{
		super("Choose");
		setBounds(450,200,400,200);
		setResizable(false);
	}
}
