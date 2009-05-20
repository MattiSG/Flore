/** Interface de lancement des jeux DeViNT : ressemble � MenuAbstrait
 * mais n'est pas une classe d�riv�e car il faut un scroller
 */

import t2s.SIVOXDevint; // pour lire les textes et les wav

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.util.*;


public class ListorInterface extends JFrame 
    implements KeyListener, ActionListener{

    // classe pour la banniere-logo
    public class Logo extends JPanel{
            BufferedImage bi = null;

        public void paintComponent(Graphics g){
            this.setBounds(5, 5, 120, 60);

            if (bi != null){
                Graphics2D g2 = (Graphics2D)g;
                g2.drawImage(bi,null, 0, 0);
            }
        }

        public void loadLogo(){
            File l = new File("../ressources/img/logoDeViNT.gif");

            try{
                bi = ImageIO.read(l);
                repaint();
            }
            catch(IOException e){
                System.out.println("/!\\ Error while opening file logo\n");
            }
        }
    }

   //-------------------------------------------------------
    // les attributs

    // le nom du jeu
    private String nomJeu;

    // pour g�rer les boutons associ�s aux jeux
    // les noms des options
    private String[] nomOptions;  
    // les boutons associ�s aux options
    private JButton[] boutonOption;  
    // les r�pertoires associ�s aux options
    private String[] nomRepertoires;  
    // les fichiers associ�s aux r�pertoires
    private File[] fileRepertoires;  

    // le nombre d'options
    private  int nbOptions;
    

    // attributs des textes et des boutons
    static final int HBOUTON = 120;   // hauteur des boutons 
    protected Font fonteBouton ;
    protected Color couleurBouton;
    protected Color couleurBoutonSelectionne;
    protected Color couleurTexte;
    protected Color couleurTexteSelectionne;

    // les �l�ments qui parlent et que l'on veut pouvoir interrompre
    SIVOXDevint voix;

    // l'option courante qui est s�lectionn�e
    private int optionCourante;

    // �l�ments graphiques
    private GridBagLayout placement; // le layout
    private GridBagConstraints regles; // les regles de placement
    private JScrollPane sc; // le scroller qui contient les boutons
    private JButton quitter;  // bouton pour quitter
 
    // le r�pertoire qui contient les jeux
    private String pathRepertoire = "../../"; 

    // taille de l'�cran
    private int largeur = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int hauteur = Toolkit.getDefaultToolkit().getScreenSize().height;


    // pour le scroll
    private static final boolean UP = true;
    private static final boolean DOWN = false;
    Dimension scSize ;
    // l'indice de la vue visible
    int placeView = 0;
    // nb de boutons visibles pour 1 vue
    int nbBoutonByView = 0; 


    public ListorInterface(String title) {
	super(title);
	nomJeu = title;
	optionCourante = -1;
	creerAttributs();
	creerLayout();
	creerEntete();
	creerOption();
	creerQuitter();
	setExtendedState(JFrame.MAXIMIZED_BOTH);
	setVisible(true);
	requestFocus();
	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	addKeyListener(this);

	// on r�cup�re la taille du scroll apr�s l'avoir ajout� dans le frame
	scSize = sc.getSize();
	nbBoutonByView = (scSize.height / HBOUTON)-1;
	// on lit le message d'accueil
	voix =new SIVOXDevint();
	voix.playWav("../ressources/wav/listorAccueil.wav");
     }

   //-------------------------------------------------------
    // m�thodes utilis�es par le constructeur


   /** cr�� les attributs (couleurs, fonte, ...)
     */
    protected void creerAttributs() {
	// la couleur des textes 
	couleurTexte = Color.WHITE;
	couleurTexteSelectionne = new Color(10,0,150);
 	// mise � jour des attributs des boutons
	fonteBouton = new Font("Tahoma",1,56);
	couleurBouton=couleurTexteSelectionne;
	couleurBoutonSelectionne=couleurTexte;
    }

    /** cr�� le layout pour placer les composants
     */
    private void creerLayout() {
	placement = new GridBagLayout();
	regles = new GridBagConstraints();
	setLayout(placement);
	// par d�faut on �tire les composants horizontalement et verticalement
	regles.fill = GridBagConstraints.BOTH;
	// par d�faut, tous les composants ont un poids de 1
	// on les r�partit donc �quitablement sur la grille
	regles.weightx = 1; 
	regles.weighty = 1;
	// espaces au bord des composants
	regles.insets = new Insets(10, 50, 10, 50);
	// pour placer en haut des zones
	regles.anchor= GridBagConstraints.NORTH;
	//pour aller � la ligne (chaque composant occupe tout le reste de la ligne)
	regles.gridwidth=GridBagConstraints.REMAINDER;
    }

    /** cr�� l'ent�te avec le nom du jeu
     */
    public void creerEntete() {

	// panel d'entete de la fen�tre
	JPanel entete=new JPanel();
	FlowLayout enteteLayout = new FlowLayout();
	enteteLayout.setAlignment(FlowLayout.CENTER);
	entete.setLayout(enteteLayout);
	entete.setBorder(new LineBorder(Color.GRAY,8));

	// le label
	JLabel lb1 = new JLabel(nomJeu);
	lb1.setFont(new Font("Georgia",1,96));
	lb1.setForeground(couleurTexteSelectionne);
	lb1.setBackground(couleurBoutonSelectionne);
	entete.add(lb1);

	// le logo
	Logo logo = new Logo();
	entete.add(logo);
	logo.loadLogo();

	// placement de l'entete en 1�re ligne, 1�re colonne
	//regles.gridx=1; regles.gridy=1;
	placement.setConstraints(entete, regles);
	add(entete);
    }

    /** creer les boutons associ�s aux noms d'options
     */
    private void creerOption() {
	// affectation des tableaux li�s aux r�pertoires des jeux
	File f = new File(pathRepertoire);
	nomRepertoires = f.list();
	fileRepertoires = new File[nomRepertoires.length];

	// cr�ation des boutons
	// panel des boutons
	JPanel boutons = new JPanel();
	boutons.setLayout(new GridLayout(nomRepertoires.length, 1));
	// les boutons
	boutonOption = new JButton[nomRepertoires.length];

	for(int i =0; i < nomRepertoires.length; i++) {
	    // n'affiche que les repertoires
	    if (new File(pathRepertoire + "" + nomRepertoires[i]).isDirectory() 
		&& !nomRepertoires[i].equals("Listor") 
		&& !nomRepertoires[i].equals("aide")) {
		creerBouton(nbOptions,nomRepertoires[i]);
		boutons.add(boutonOption[nbOptions]);
		nbOptions++;
	    }
	}

	// le scoll qui contient les boutons
	sc = new JScrollPane(boutons);

	// poids relatif de 3 (i.e 3 fois plus grand que l'ent�te
       	regles.weighty=6;
	// on ajuste verticalement et horizontalement
	regles.fill = GridBagConstraints.BOTH;
	placement.setConstraints(sc, regles);
	add(sc);
    }


    private void creerQuitter() {
	// bouton pour quitter
	quitter = new JButton("Quitter");
	quitter.setBackground(Color.YELLOW);
	quitter.setFont(fonteBouton);
	quitter.setBorder(new LineBorder(Color.BLACK,5));
	quitter.setPreferredSize(new Dimension(40,100));
	quitter.addActionListener(this);
	// poids relatif de 3 (i.e 3 fois plus grand que l'ent�te
       	regles.weighty=2;
	// espace vertical avant de le placer
	regles.ipady=1000;
	// on ajuste seulement horizontalement
	regles.fill = GridBagConstraints.HORIZONTAL;
	add(quitter);
    }


    // ------------------------------
    // m�thodes de gestion des boutons

    private void creerBouton(int i,String texte) {
	boutonOption[i] = new JButton();
	boutonOption[i].setText(texte);
	fileRepertoires[i] = (new File(pathRepertoire+texte)).getAbsoluteFile();
	setProperties(boutonOption[i]);
    }

    // mettre � jour les propri�t�s des boutons
    private void setProperties(JButton b) {
	b.setFocusable(false);
	b.setBackground(couleurBouton);	
	b.setForeground(couleurTexte);
	b.setFont(fonteBouton);
	b.setBorder(new LineBorder(Color.BLACK,5));
	b.setPreferredSize(new Dimension(largeur,120));
	b.addActionListener(this);
    }

    // mettre le focus sur une option
    private void setFocusedButton(int i){
	voix.playShortText(boutonOption[i].getText());	
	boutonOption[i].setBackground(couleurBoutonSelectionne);
	boutonOption[i].setForeground(couleurTexteSelectionne);
// 	b.setBackground(Color.LIGHT_GRAY);
// 	playWord(b.getText());	
    }
	
    private void unFocusedButton(int i){
// 	b.setBackground(couleurFondBouton);
	boutonOption[i].setBackground(couleurBouton);
	boutonOption[i].setForeground(couleurTexte);
    }


    //-------------------------------------------------------
    // m�thodes pour r�agir aux �v�nements clavier

    // �v�nements clavier

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e){}

    public void keyPressed(KeyEvent e) {
	voix.stop();
	// escape = sortir
	if (e.getKeyCode()==KeyEvent.VK_ESCAPE){
	    System.exit(0);
	}
	// F1 = aide
	if (e.getKeyCode()==KeyEvent.VK_F1){
	    voix.playWav("../ressources/wav/aide.wav");
	}
	// enter = s�lectionner l'option
	if (e.getKeyCode()==KeyEvent.VK_ENTER){
	    lancer(optionCourante);
	}
	// se d�placer dans les options vers le bas
	if (e.getKeyCode() == KeyEvent.VK_DOWN){
	    if (optionCourante==-1) {
		optionCourante = 0;
		setFocusedButton(optionCourante);
	    } 
	    else {
		placeView++;
		unFocusedButton(optionCourante);	    
		optionCourante = (optionCourante+1)%nbOptions;
		scrolle(DOWN,optionCourante);
		setFocusedButton(optionCourante);
	    }
	}	 
	// se d�placer dans les options vers le haut
	if (e.getKeyCode() == KeyEvent.VK_UP){
	    if (optionCourante==-1) {
		optionCourante = 0;
		setFocusedButton(optionCourante);
	    } 
	    else {
		placeView--;
		unFocusedButton(optionCourante);	     
		optionCourante = optionCourante-1;
		if (optionCourante==-1) optionCourante = nbOptions-1;
		scrolle(UP,optionCourante);
		setFocusedButton(optionCourante);
	    }
	}
    }

    // pour scroller pour voir toutes les options
    private void scrolle(boolean sens, int optionCourante) {
	JViewport p = sc.getViewport();
	int viewX = (int)p.getViewPosition().getX();
	if (optionCourante==0) {
	    p.setViewPosition(new Point(viewX,0));
	}else 
	    if ( UP &&  placeView < 0) {
		int nvllePlace = (optionCourante-nbBoutonByView)*HBOUTON ;
		p.setViewPosition(new Point(viewX,nvllePlace)); 
		placeView = nbBoutonByView;
	    }
	    else if (placeView > nbBoutonByView) {
		int nvllePlace = (optionCourante-1)*(HBOUTON) ;
		p.setViewPosition(new Point(viewX,nvllePlace)); 
		placeView = 0;
	    }
    }

    // lance le jeu associ� au bouton n�i
    private void lancer(int i) {
	try{
            File repExec = new File(fileRepertoires[i], "bin");
            String command = repExec + "\\execution.bat";
	    final Process process = 
		Runtime.getRuntime().exec(command,null,repExec);
	    //consommation des flux de sortie du processus lanc�
	    new Thread() {
		public void run() {
		    try {
			BufferedReader reader =
			    new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			try {
			    while ((line=reader.readLine()) != null) {
			    }
			} finally {
			    reader.close();
			}
		    } catch(IOException ioe) {
			ioe.printStackTrace();
		    }
		}
	   }.start();
	   new Thread() {
		public void run() {
		    try {
			BufferedReader reader =
			    new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String line = "";
			try {
			    while ((line=reader.readLine()) != null) {
			    }
			} finally {
			    reader.close();
			}
		    } catch(IOException ioe) {
			ioe.printStackTrace();
		    }
		}
	   }.start();

	   boolean finished= false;
	   while(!finished) {
	       try {
		   Thread.sleep(10);
		   // on utilise une exception g�n�r�e par le Thread
		   process.exitValue();
		   finished=true;
	       } catch (IllegalThreadStateException e) {
		   //le process n'est pas encore termin�
	       }
	   }
	   if(finished) {
	       this.requestFocus();
	       //on dit ce texte quand on sort du jeu lanc�
	       voix.playWav("../ressources/wav/listorRetour.wav");
	   }
        }
        catch(Exception e) {
	    e.printStackTrace();
        }
    }

   /* activer les menus si clic sur le bouton */
    public void actionPerformed(ActionEvent ae){
	voix.stop();
	Object source = ae.getSource();
	
	if (source == quitter){
	    // quitte l'application
	    System.exit(0);
	}
	else 
	    for(int i=0; i<nbOptions; i++){
		if (source == boutonOption[i]){
		    if (optionCourante!=-1) unFocusedButton(optionCourante);
		    optionCourante=i;
		    setFocusedButton(optionCourante);
		    lancer(i);
		}
	    }
    }
}
    