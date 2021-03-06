package graphic;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.*;

import splitters.*;

/**
 * Pannello della scelta del tipo di divisione di un file specifico
 * Contiene i bottoni per scegliere il tipo di divisione, e delle JTextField
 * in cui inserire le specifiche di divisione, oltre al bottone ADD
 * 
 * @author Gamberi Elia
 *
 */
public class ChoosePanel extends JPanel implements ActionListener{
	
	private String choice;
	private LinkedList<FileLocation> fl;
	private String fn;
	
	private JFrame f;
	private JButton add;
	private FileTabMod TabMod;
	
	/**
	 * diventa true se chiudo col bottone ADD
	 */
	private boolean closeWithButton;
	
	private JTextField dim= new JTextField();
	private JTextField parts= new JTextField();
	private JTextField name = new JTextField();
	
	private JProgressBar progress;
	/**
	 * Costruttore iniziale del pannello di scelta del tipo di divisione
	 * @param l lista di oggetti "FileLocation", che diventano uno dei figli in base alle nostre scelte
	 * @param f Frame in cui e' contenuto il JPanel
	 * @param TabMod Modello della Tabella contenuto nel Frame iniziale
	 * @param fn Stringa contenente il path del file che abbiamo appena scelto
	 * @param progress JprogressBar, da passare agli splitter per aggiornarla alla fine della funzione run()
	 */
	public ChoosePanel(LinkedList<FileLocation> l,JFrame f,FileTabMod TabMod, String fn,JProgressBar progress) {
		
		setLayout(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();
		
		this.progress=progress;
		
		fl=l;
		this.f=f;
		this.TabMod=TabMod;
		
		closeWithButton=false;
		
		JRadioButton nbyte=new JRadioButton("NByte");
		JRadioButton zip=new JRadioButton("Zip");
		JRadioButton crypt=new JRadioButton("Crypt");
		JRadioButton npart=new JRadioButton("NParti");
		JRadioButton join=new JRadioButton("Join");
		
		ButtonGroup grp=new ButtonGroup();
		grp.add(nbyte);;grp.add(zip);grp.add(crypt);grp.add(npart);grp.add(join);
		
		nbyte.addActionListener(this);
		zip.addActionListener(this);
		crypt.addActionListener(this);
		npart.addActionListener(this);
		join.addActionListener(this);

		
		add = new JButton("ADD");
		add.addActionListener(new ButtonListener());
		add.setEnabled(false);
		
		dim.setEnabled(false);
		parts.setEnabled(false);
		name.setEnabled(false);
		
		cs.fill = GridBagConstraints.HORIZONTAL;
		 
		cs.gridx=0;
		cs.gridy=0;
		add(nbyte,cs);
		cs.gridx=1;
		add(new JLabel("Dim: "),cs);
		cs.gridx=2;
		add(dim,cs);
		cs.gridx=3;
		add(new JLabel(" Byte"));
		
		
		cs.gridx=0;
		cs.gridy=1;
		add(npart,cs);
		cs.gridx=1;
		add(new JLabel("N Parti: "),cs);
		cs.gridx=2;
		
		add(parts,cs);
		
		cs.gridx=0;
		cs.gridy=2;
		add(zip,cs); 
		
		cs.gridy=3;
		add(crypt,cs);
		
		cs.gridx=0;
		cs.gridy=4;
		add(join,cs);
		cs.gridx=1;
		add(new JLabel("Nome: "),cs);
		cs.ipadx=100;
		cs.gridx=2;
		add(name,cs);
		
		cs.gridy=5;
		add(add,cs);
		
		this.fn=fn;
		
	}
	
	/**
	 *  Assicura che le label si disattivino nel momento in cui
	 *  selezioniamo un'opzione per cui non serve
	 */
	public void actionPerformed(ActionEvent e)
	{
		choice=e.getActionCommand();
		add.setEnabled(true);
		
		switch(choice) {
		case "NByte":
			dim.setEnabled(true);
			parts.setEnabled(false);
			name.setEnabled(false);
			break;
		case "Zip":
			dim.setEnabled(true);
			parts.setEnabled(false);
			name.setEnabled(false);
			break;
		case "Crypt":
			dim.setEnabled(true);
			parts.setEnabled(false);
			name.setEnabled(false);
			break;
		case "NParti":
			dim.setEnabled(false);
			parts.setEnabled(true);
			name.setEnabled(false);
			break;
		case "Join":
			dim.setEnabled(false);
			parts.setEnabled(false);
			name.setEnabled(true);
			break;
		}
	}
	/**
	 * 
	 * @return false se chiudo in modi che non sono il bottone,
	 * per non salvare nella Lista elementi non desiderati
	 */
	public boolean buttonClosing()
	{
		return closeWithButton;
	}
	/**
	 * Listener del bottone ADD
	 */
	public class ButtonListener implements ActionListener
	{
		/**
		 * Nel momento in cui premiamo ADD, controlliamo una serie di elementi, per verificare
		 * che non ci siano problemi nelle opzioni che abbiamo scelto
		 * In caso di incongruenze, si stampa un messaggio di errore
		 * 
		 */
		public void actionPerformed(ActionEvent e)
		{
			
			FileLocation flmom;//oggetto FileLocation, padre di tutti gli splitter, che poi verrà definito
			
			switch(choice) {
			case "NByte":
				if(isDimOk()) {
					flmom= new NByteSplitter(fn,Integer.parseInt(dim.getText()),progress);
					flmom.setTOD('b');
					fl.add(flmom);
				}
				else
					return;
				break;
			case "Crypt":
				flmom= new CryptoSplitter(fn,Integer.parseInt(dim.getText()),progress);
				flmom.setTOD('c');
				fl.add(flmom);
				break;
			case "Zip":
				flmom= new ZipSplitter(fn,Integer.parseInt(dim.getText()),progress);
				flmom.setTOD('z');
				fl.add(flmom);
				break;
			case "NParti":
				try {
					if(Integer.parseInt(parts.getText())>1){
						flmom= new NPartsSplitter(fn,Integer.parseInt(parts.getText()),progress);
						flmom.setTOD('n');
						fl.add(flmom);
					}
					else
					{
						JOptionPane.showMessageDialog(f,"Scegliere una quantita' di parti maggiore di 1",
							"Numero di parti non valido",
						    JOptionPane.ERROR_MESSAGE);
						return;
					}}
				catch(NumberFormatException n) {
					JOptionPane.showMessageDialog(f,"Scegliere un numero di parti valido",
						"Numero di parti non valido",
					    JOptionPane.ERROR_MESSAGE);
					return;
				}
				break;
			case "Join":
				try {
				if(fn.endsWith(".zip.par"))
					flmom=  new ZipSplitter(fn,name.getText(),progress);
				else if(fn.endsWith(".crypt.par"))
					flmom= new CryptoSplitter(fn,name.getText(),progress);
				else if(fn.endsWith(".par"))
					flmom= new NByteSplitter(fn,name.getText(),progress);
				else {
					JOptionPane.showMessageDialog(f,"Scegliere un file in grado di essere joinato",
							"File Non Splittato",
						    JOptionPane.ERROR_MESSAGE);
					return;
					}
				}
				catch(NullPointerException npe)
				{
					JOptionPane.showMessageDialog(f,"Inserire un nome per il file risultante",
							"Nome non Inserito",
						    JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				if(flmom.getFinalFileNameNoExtension().equals(""))
				{
					JOptionPane.showMessageDialog(f,"Scegliere un nome per il file finale",
							"Nome Vuoto",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				flmom.setTOD('j');
				fl.add(flmom);
				break;
			}
			closeWithButton=true;
			
			progress.setValue(0);
			progress.setMaximum(fl.size());
			
			TabMod.fireTableDataChanged();
			WindowEvent close = new WindowEvent(f, WindowEvent.WINDOW_CLOSING);
			f.dispatchEvent(close);
		}
		
		/**
		 * Insieme di controlli effettuati prima di creare uno splitter basato sulla dimensione del file finale
		 */
		private boolean isDimOk()
		{
			FileInputStream fi = null;
			try {
				fi = new FileInputStream(fn);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				if(Integer.parseInt(dim.getText())<=0)
				{
					JOptionPane.showMessageDialog(f,"Scegliere una dimensione maggiore di 0",
						"Numero di Byte negativo",
						JOptionPane.ERROR_MESSAGE);
					return false;
				}
				try {
					if(Integer.parseInt(dim.getText()) > fi.available())
					{
						JOptionPane.showMessageDialog(f,"Scegliere una dimensione minore della dimensione del file",
							"Numero di Byte inserito troppo grande",
							JOptionPane.ERROR_MESSAGE);
					return false;
					}
				}catch(IOException e)
				{
					JOptionPane.showMessageDialog(f,"File Rimosso o non piu' disponibile",
							"File Rimosso",
							JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}catch(NumberFormatException n) {
				JOptionPane.showMessageDialog(f,"Scegliere una dimensione valida",
						"Numero di Byte non valido",
					    JOptionPane.ERROR_MESSAGE);
				return false;
			}
			try {
				fi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		
		
		
	}
}
