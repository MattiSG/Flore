/** Interface de lancement des jeux DeViNT : ressemble à MenuAbstrait
 * mais n'est pas une classe dérivée car il faut un scroller
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

    // pour gérer les boutons associés aux jeux
    // les noms des options
    private String[] nomOptions;  
    // les boutons associés aux options
    private JButton[] boutonOption;  
    // les répertoires associés aux options
    private String[] nomRepertoires;  
    // les fichiers associés aux répertoires
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

    // les éléments qui parlent et que l'on veut pouvoir interrompre
    SIVOXDevint voix;

    // l'option courante qui est sélectionnée
    private int optionCourante;

    // éléments graphiques
    private GridBagLayout placement; // le layout
    private GridBagConstraints regles; // les regles de placement
    private JScrollPane sc; // le scroller qui contient les boutons
    private JButton quitter;  // bouton pour quitter
 
    // le répertoire qui contient les jeux
    private String pathRepertoire = "../../"; 

    // taille de l'écran
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

	// on récupère la taille du scroll après l'avoir ajouté dans le frame
	scSize = sc.getSize();
	nbBoutonByView = (scSize.height / HBOUTON)-1;
	// on lit le message d'accueil
	voix =new SIVOXDevint();
	voix.playWav("../ressources/wav/listorAccueil.wav");
     }

   //-------------------------------------------------------
    // méthodes utilisées par le constructeur


   /** créé les attributs (couleurs, fonte, ...)
     */
    protected void creerAttributs() {
	// la couleur des textes 
	couleurTexte = Color.WHITE;
	couleurTexteSelectionne = new Color(10,0,150);
 	// mise à jour des attributs des boutons
	fonteBouton = new Font("Tahoma",1,56);
	couleurBouton=couleurTexteSelectionne;
	couleurBoutonSelectionne=couleurTexte;
    }

    /** créé le layout pour placer les composants
     */
    private void creerLayout() {
	placement = new GridBagLayout();
	regles = new GridBagConstraints();
	setLayout(placement);
	// par défaut on étire les composants horizontalement et verticalement
	regles.fill = GridBagConstraints.BOTH;
	// par défaut, tous les composants ont un poids de 1
	// on les répartit donc équitablement sur la grille
	regles.weightx = 1; 
	regles.weighty = 1;
	// espaces au bord des composants
	regles.insets = new Insets(10, 50, 10, 50);
	// pour placer en haut des zones
	regles.anchor= GridBagConstraints.NORTH;
	//pour aller à la ligne (chaque composant occupe tout le reste de la ligne)
	regles.gridwidth=GridBagConstraints.REMAINDER;
    }

    /** créé l'entête avec le nom du jeu
     */
    public void creerEntete() {

	// panel d'entete de la fenêtre
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

	// placement de l'entete en 1ère ligne, 1ère colonne
	//regles.gridx=1; regles.gridy=1;
	placement.setConstraints(entete, regles);
	add(entete);
    }

    /** creer les boutons associés aux noms d'options
     */
    private void creerOption() {
	// affectation des tableaux liés aux répertoires des jeux
	File f = new File(pathRepertoire);
	nomRepertoires = f.list();
	fileRepertoires = new File[nomRepertoires.length];

	// création des boutons
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

	// poids relatif de 3 (i.e 3 fois plus grand que l'entête
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
	// poids relatif de 3 (i.e 3 fois plus grand que l'entête
       	regles.weighty=2;
	// espace vertical avant de le placer
	regles.ipady=1000;
	// on ajuste seulement horizontalement
	regles.fill = GridBagConstraints.HORIZONTAL;
	add(quitter);
    }


    // ------------------------------
    // méthodes de gestion des boutons

    private void creerBouton(int i,String texte) {
	boutonOption[i] = new JButton();
	boutonOption[i].setText(texte);
	fileRepertoires[i] = (new File(pathRepertoire+texte)).getAbsoluteFile();
	setProperties(boutonOption[i]);
    }

    // mettre à jour les propriétés des boutons
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
    // méthodes pour réagir aux évènements clavier

    // évènements clavier

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
	// enter = sélectionner l'option
	if (e.getKeyCode()==KeyEvent.VK_ENTER){
	    lancer(optionCourante);
	}
	// se déplacer dans les options vers le bas
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
	// se déplacer dans les options vers le haut
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

    // lance le jeu associé au bouton n°i
    private void lancer(int i) {
	try{
            File repExec = new File(fileRepertoires[i], "bin");
            String command = repExec + "\\execution.bat";
	    final Process process = 
		Runtime.getRuntime().exec(command,null,repExec);
	    //consommation des flux de sortie du processus lancé
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
		   // on utilise une exception générée par le Thread
		   process.exitValue();
		   finished=true;
	       } catch (IllegalThreadStateException e) {
		   //le process n'est pas encore terminé
	       }
	   }
	   if(finished) {
	       this.requestFocus();
	       //on dit ce texte quand on sort du jeu lancé
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
    