package t2s.test.analyse;

import java.util.*;
import java.io.*;
import t2s.exception.*;
import t2s.prosodie.*;
import t2s.test.prosodie.*;
import t2s.traitement.*;
import t2s.util.*;

public class Analyser {

	private String texte;
	private int prosodie;
	
	public Analyser (String t, int p)
	{
		texte = t;
		prosodie = p;
	}
	
	public Vector<Phoneme> analyserGroupes ()
	{
	    Vector<Phrase> phrases = new Vector<Phrase>();
	    String chainePhonemes;
	    Pretraitement pt = new Pretraitement(texte);        
	    try {
		Arbre a = new Arbre("");
		try {
		    Phrase p = pt.nouvellePhrase();
		    while (!"".equals(p.getPhrase()))
		    {
		    	String s[] = a.trouverPhoneme(p.getPhrase()).trim().replaceAll("  ", " ").split("%");
		    	for (int i = 0; i < s.length; ++i)
		    	{
		    		if (!"".equals(s[i]))
		    			if (i == s.length-1)
		    				phrases.add(new Phrase(s[i].trim(), p.getProsodie()));
		    			else
		    				phrases.add(new Phrase(s[i].trim(), 3));
		    	}
		    	if (prosodie == 1)
		    		chainePhonemes= this.afficher(new Prosodie1(phrases).prosodier());
		    	else if (prosodie == 2)
		    		chainePhonemes=this.afficher(new Prosodie2(phrases).prosodier());
		    	else
		    		chainePhonemes=this.afficher(new Prosodie3(phrases).prosodier());
		    	try {
		    		FileWriter fw = new FileWriter(ConfigFile.rechercher("REPERTOIRE_PHO_WAV")+"/"+ConfigFile.rechercher("FICHIER_PHO_WAV")+".pho");
		    		fw.write(chainePhonemes);
		    		fw.close();
		    	} catch (Exception e) {}
		    	p = pt.nouvellePhrase();
		    }
			} catch (PlusDePhraseException e) {/*erreur ecriture*/}
	    } catch (AnalyseException aa) {/*erreur analyse*/}
	    if (prosodie == 1)
	    	return new Prosodie1(phrases).prosodier();
	    else if (prosodie == 2)
	    	return new Prosodie2(phrases).prosodier();
	    else
	    	return new Prosodie3(phrases).prosodier();
	}
	
	//methode qui retourne un string comportant les phonemes
	public String afficher (Vector<Phoneme> l)
	{
		String s = new String();
		for (int i = 0; i < l.size(); ++i)
			s += l.get(i).toString() + "\n";
		return s;
	}
}
