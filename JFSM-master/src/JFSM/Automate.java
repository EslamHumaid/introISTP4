/**
 * 
 * Copyright (C) 2017 Emmanuel DESMONTILS
 * 
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 * 
 * 
 * 
 * E-mail:
 * Emmanuel.Desmontils@univ-nantes.fr
 * 
 * 
 **/


/**
 * Automate.java
 * 
 *
 * Created: 2017-08-25
 *
 * @author Emmanuel Desmontils
 * @version 1.0
 */

package JFSM;

import java.util.Set;
import java.util.HashSet;

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;

import java.util.Map;
import java.util.HashMap;

import java.util.Iterator;

import java.util.Stack;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import javax.xml.parsers.SAXParserFactory; 
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.File;

public class Automate implements Cloneable {
	public Map<String,Etat> Q;
	public Set<String> F, I;
	public Set<String> A;
	public Stack<Transition> histo;
	public Set<Transition> mu;
	protected String current;

	/** 
	* Constructeur de l'automate {A,Q,I,F,mu}
	* @param A l'alphabet de l'automate (toute chaîne de caratères non vide et différente de \u03b5)
	* @param Q l'ensemble des états de l'automate
	* @param I l'ensemble des états initiaux de l'automate
	* @param F l'ensemble des états finaux de l'automate
	* @param mu la fonction de transition de l'automate
	* @exception JFSMException Exception si un état qui n'existe pas est ajouté comme état initial ou final
	*/
	public Automate(Set<String> A, Set<Etat> Q, Set<String> I, Set<String> F, Set<Transition> mu) throws JFSMException {
		// Ajout de l'alphabet
		assert A.size()>0 : "A ne peut pas être vide" ;
		for(String a : A) {
			if ((a=="")||(a=="\u03b5")) throw new JFSMException("Un symbole ne peut pas être vide ou \u03b5");
		}
		this.A = A;
		this.mu = new HashSet<Transition>();

		// Ajout des états
		assert Q.size()>0 : "Q ne peut pas être vide" ;
		this.Q = new HashMap<String,Etat>();

		for (Etat e : Q)
			if (this.Q.containsKey(e.name)) System.out.println("Etat dupliqué ! Seule une version sera conservée.");
			else this.Q.put(e.name,e); 
		
		// Création de l'historique (chemin)
		this.histo = new Stack<Transition>();

		// Ajout des transitions
		this.mu.addAll(mu);

		// On collecte les états initiaux, on les positionne comme tel. S'il n'existe pas, il est oublié.
		// assert I.size()>0 : "I ne peut pas être vide" ;
		this.I = new HashSet<String>();
		for (String i : I) setInitial(i);

		// On collecte les états finaux, on les positionne comme tel. S'il n'existe pas, il est oublié.
		this.F = new HashSet<String>();
		for(String f : F) setFinal(f);
	}

	public Object clone() {
		Automate o = null;
		try {
			o = (Automate)super.clone();
			// o.Q = (Map<String,Etat>) ((HashMap<String,Etat>)Q).clone() ;
			o.Q = new HashMap<String,Etat>();
			for(Etat e : this.Q.values()) {
				o.addEtat((Etat)e.clone());
			}
			o.F = (Set<String>)  ((HashSet<String>)F).clone();
			o.I = (Set<String>)  ((HashSet<String>)I).clone();
			o.A = (Set<String>)  ((HashSet<String>)A).clone();
			o.histo = (Stack<Transition>) ((Stack<Transition>)histo).clone();
			//o.mu = (Set<Transition>) ((HashSet<Transition>)mu).clone();
			o.mu = new HashSet<Transition>();
			for(Transition t : this.mu) {
				o.addTransition((Transition)t.clone());
			}
		} catch(CloneNotSupportedException cnse) {
			cnse.printStackTrace(System.err);
		}
		return o;
	}

	public String toString() {
		String s = "{ A={ ";
		for(String a : A ) s = s + a + " ";
		s = s + "} Q={ ";
		for(Etat q : Q.values() ) s = s + q + " ";
		s = s + "} I={ " ;
		for(String q : I ) s = s + q + " ";
		s = s + "} F={ " ;
		for(String q : F ) s = s + q + " ";
		s = s + "} \n   mu={ \n" ;
		for(Transition t : mu ) s = s + "\t"+ t + "\n";
		s = s + "   }\n}" ;

		return s ;
	}

	/** 
	* Ajoute une transition à mu.  
	* @param t transition à ajouter
	*/
	public void addTransition(Transition t) {
		mu.add(t);
	}

	/** 
	* Ajoute un état à Q.  
	* @param e L'état
	*/
	public void addEtat(Etat e){
		if (!Q.containsKey(e.name))
			Q.put(e.name,e);
	}

	/** 
	* Retrouve un état par son nom.  
	* @param n Le nom de l'état 
	* @return l'état retrouvé, null sinon
	*/
	public Etat getEtat(String n) {
		if (Q.containsKey(n))
			return Q.get(n);
		else return null;
	}

	/** 
	* Fixe le vocabulaire de l'automate.  
	* @param A la vocabulaire 
	*/
	public void setA(Set<String> A){
		this.A = A;
	}

	/** 
	* Indique qu'un état (par son nom) est un état initial.  
	* @param e Le nom de l'état
	* @exception JFSMException Si l'état est absent
	*/
	public void setInitial(String e) throws JFSMException {	
		if (Q.containsKey(e)) {
			I.add(e);
		} else throw new JFSMException("Etat absent:"+e);
	}

	/** 
	* Indique qu'un état est un état initial.  
	* @param e L'état
	* @exception JFSMException Si l'état est absent
	*/
	public void setInitial(Etat e) throws JFSMException {	
		setInitial(e.name);
	}

	/** 
	* Indique qu'un état (par son nom) est un état final.  
	* @param e Le nom de l'état
	* @exception JFSMException Si l'état est absent
	*/
	public void setFinal(String e) throws JFSMException {	
		if (Q.containsKey(e)) {
			F.add(e);
		} else throw new JFSMException("Etat absent:"+e);
	}

	/** 
	* Indique qu'un état est un état final.  
	* @param e L'état
	* @exception JFSMException Si l'état est absent
	*/
	public void setFinal(Etat e) throws JFSMException {	
		setFinal(e.name);
	}

	/** 
	* Détermine si un état (par son nom) est un état initial.  
	* @param e Le nom de l'état
	* @return vrai si initial, faux sinon
	*/
	public boolean isInitial(String e){
		assert Q.containsKey(e) : "isInitial : l'état doit être un état de l'automate." ;
		return I.contains(e);
	}

	/** 
	* Détermine si un état est un état initial.  
	* @param e L'état
	* @return vrai si initial, faux sinon
	*/
	public boolean isInitial(Etat e){
		return isInitial(e.name);
	}

	/** 
	* Détermine si un état (par son nom) est un état final.  
	* @param e Le nom de l'état
	* @return vrai si final, faux sinon
	*/
	public boolean isFinal(String e){
		assert Q.containsKey(e) : "isFinal : l'état doit être un état de l'automate." ;
		return F.contains(e);
	}

	/** 
	* Détermine si un état est un état final.  
	* @param e L'état
	* @return vrai si final, faux sinon
	*/
	public boolean isFinal(Etat e){
		return isFinal(e.name);
	}

	/** 
	* Initialise l'exécution de l'automate.  
	*/
	public void init() {
		histo.clear();
	}

	/** 
	* Indique si l'automate est dans un état final.  
	* @return vrai si final, faux sinon
	*/
	public boolean accepte(){return isFinal(current);}

	/** 
	* Indique si l'automate est epsilon-libre.  
	* @return vrai si e-libre, faux sinon
	*/
	public boolean epsilonLibre(){
		boolean ok = true ;
		for(Transition t : mu) {
			if (t instanceof EpsilonTransition) {
				ok = false;
				break;
			}
		}
		return ok;
	}

	/** 
	* Indique si l'etat est Accessible.
	* @param String :  name (le nom de l'etat)  
	* @return vrai si Accessible, faux sinon
	*/
	public boolean estAccessible(String name) {
		if(isInitial(name)) {
			return true;
		}else {
			ArrayList<String> etatsAccessible = new ArrayList<String>();
			etatsAccessible.addAll(this.I);
			
			for(int i = 0; i < etatsAccessible.size(); i++ ) {
				for(Transition trans : this.mu) {
					
					if(etatsAccessible.contains(trans.source) && (!etatsAccessible.contains(trans.cible))){
						etatsAccessible.add(trans.cible);
					}
				}
			}
		
			
			return etatsAccessible.contains(name);
		}
		
		
	}
	
	/** 
	* Indique si l'etat est co accessible.
	* @param String :  name (le nom de l'etat)  
	* @return vrai si co accessible, faux sinon
	*/
	public boolean estCoAccessible(String name) {
		if(isInitial(name)) {
			return true;
		}else {
			ArrayList<String> etatsCoAccessible = new ArrayList<String>();
			etatsCoAccessible.addAll(this.F);
			
			for(int i = 0; i < etatsCoAccessible.size(); i++ ) {
				for(Transition trans : this.mu) {
					
					if(etatsCoAccessible.contains(trans.cible) && (!etatsCoAccessible.contains(trans.source))){
						etatsCoAccessible.add(trans.source);
					}
				}
			}
		
			
			return etatsCoAccessible.contains(name);
		}
		
		
	}
	
	
	/** 
	* Supprime les états qui ne sont pas utiles (accessible et co-accessible)  
	* @return un automate équivalent utile (tous les états sont utiles)
	*/
	public Automate emonder() {
		
		Automate afn = (Automate) this.clone();
		ArrayList<Etat> etats = new ArrayList<Etat>(afn.Q.values());

		for(Etat et : etats) {
			if((!afn.estAccessible(et.name)) || (!afn.estCoAccessible(et.name))) {
				
				if(afn.isInitial(et.name)) {
					afn.I.remove(et.name);
					
				}
				
				if(afn.isFinal(et.name)) {
					afn.F.remove(et.name);
					
				}
				
				
				
				for(Transition trans: afn.mu) {
					if((trans.cible == et.name) || (trans.source == et.name)) {
						afn.mu.remove(trans);
					}
					
					
				}
				
				afn.Q.remove(et.name);
			}
		}

		return afn;
	}

	/** 
	* Détermine si l'automate est utile  
	* @return booléen
	*/
	public boolean estUtile() {
	
		boolean ok = true;

		ArrayList<Etat> etats = new ArrayList<Etat>(Q.values());
		
		for(Etat et : etats) {
			if((!this.estAccessible(et.name)) || (!this.estCoAccessible(et.name))) {
				ok = false;
			}
		}
		

		return ok;
	}

	/** 
	* Permet de transformer l'automate en un automate standard  
	* @return un automate équivalent standard
	*/
	public Automate standardiser() throws JFSMException {
		
		if(this.estStandard()) { return this;}  //si l'automate est deja standard
		
		Automate afn = (Automate) this.clone();
		
		
		
        afn.I.clear();
        afn.I.add("I");  //le nouveau etat initial
        
        if(!afn.epsilonLibre()) {             //pour les automates non epsilon-libre
        	for(String etat : this.I) {
        		afn.mu.add(new EpsilonTransition("I", etat));     //ajouter des epsilon-transitions de I vers les etats initiaux
        	}
        	
        	return afn;
        	
		}
        
        
        
        for(String etat : this.I) {          //si un etat initial est aussi final, supprimer cet etat des etats finaux
        	if(this.F.contains(etat)) {      
        		afn.F.remove(etat);
        		afn.F.add("I");            //I devient un etat final
        	}
        }
        
        
        
        
        for(Transition trans : this.mu ) {
        	if(this.I.contains(trans.source)) {            //si le source d'une transition est un etat initial
        	afn.mu.add(new Transition("I",trans.symbol,trans.cible)); //ajouter une transition de I vers cet etat
        	}
        }
        
        afn = afn.emonder();  //supprimer les etats inutiles
        
		

		return afn;
	}

	/** 
	* Détermine si l'automate est standard  
	* @return booléen
	*/
	public boolean estStandard() {
		
		boolean ok = false;

		
		if(this.I.size() == 1) {   //s'il n'existe qu'un seul etat initial
			ok = true;
			for(Transition trans : this.mu) {
				if(this.I.contains(trans.cible)) {  //s'il existe des transition vers l'etat initial
					ok = false;
					
				}
			}
				
		}
		
		return ok;
	}

	/** 
	* Permet de transformer l'automate en un automate normalisé  
	* @return un automate équivalent normalisé
	*/
	public Automate normaliser() throws JFSMException {
		
		  Automate afn = (Automate) this.clone();
		  afn = afn.standardiser();
		  if(this.estNormalise()) { return afn;}  //si l'automate est deja normalise
		  
		  afn.F.clear();
	      afn.F.add("F");  //le nouveau etat final
	      
	      if(!afn.epsilonLibre()) {             //pour les automates non epsilon-libre
	        
	    	for(String etat : this.F) {
	        	afn.mu.add(new EpsilonTransition(etat, "F"));     //ajouter des epsilon-transitions des etats finaux ver F
	        }
	        	
	        return afn;
	        	
		  }
	        
	      for(Transition trans : this.mu ) {
	        if(this.F.contains(trans.cible)) {                                 //si la cible d'une transition est un etat final
	           afn.mu.add(new Transition(trans.source,trans.symbol,"F"));     //ajouter une transition de cet etat vers F
	        }
	      }
	        
			

			return afn;
	}

	/** 
	* Détermine si l'automate est normalisé  
	* @return booléen
	*/
	public boolean estNormalise() {
		boolean ok = false;

		
		if(this.F.size() == 1) {    //s'il n'existe qu'un seul etat final 
			ok = true;
			for(Transition trans : this.mu) {
				if(this.F.contains(trans.source)) {   //s'il existe des transition de l'etat final
					ok = false;
					
				}
			}
				
		}
		
		return ok && this.estStandard();
		

		
		
		
	}

	/** 
	* Construit un automate reconnaissant le produit du langage de l'automate avec celui de "a" : L(this)xL(a)
	* @param a un Automate
	* @return un automate reconnaissant le produit
	*/
	public Automate produit(Automate a) {
		System.out.println("produit() : méthode non implémentée");
		return a;
	}

	/** 
	* Construit un automate reconnaissant le langage de l'automate à l'étoile : L(this)*
	* @return un automate reconnaissant la mise à l'étoile
	*/
	public Automate etoile() {
		System.out.println("etoile() : méthode non implémentée");
		Automate afn = (Automate) this.clone();

		// A compléter

		return afn;
	}

	/** 
	* Construit un automate reconnaissant l'union du langage de l'automate avec celui de "a" : L(this) U L(a)
	* @param a un Automate
	* @return un automate reconnaissant l'union
	*/
	public Automate union(Automate a) {
		System.out.println("union() : méthode non implémentée");
		return a;
	}

	/** 
	* Construit un automate reconnaissant l'intersection du langage de l'automate avec celui de "a" 
	* @param a un Automate
	* @return un automate reconnaissant l'intersection
	*/
	public Automate intersection(Automate a) {
		System.out.println("intersection() : méthode non implémentée");
		return a;
	}

	/** 
	* Construit un automate reconnaissant le complémentaire du langage 
	* @return un automate reconnaissant le complémentaire
	*/
	public Automate complementaire() {
		System.out.println("complémentaire() : méthode non implémentée");
		return this;
	}

	/** 
	* Construit un automate complet
	* @return l'automate complet
	*/
	public Automate complet() {
		System.out.println("complet() : méthode non implémentée");
		return this;
	}

	/** 
	* Teste si un automate est complet
	* @return booléen
	*/
	public boolean estComplet() {
		System.out.println("estComplet() : méthode non implémentée");
		return true;
	}

	/** 
	* Construit un automate reconnaissant le langage transposé
	* @return l'automate complet
	*/
	public Automate transpose() {
		System.out.println("transpose() : méthode non implémentée");
		return this;
	}

	/** 
	* Détermine des transitions possibles que peut emprunter l'automate en fonction de l'état courant et du symbole courant
	* @param symbol le symbole
	* @exception JFSMException Exception levée si la méthode n'est pas implémentée
	* @return la liste des transitions possibles 
	*/
	public Queue<Transition> next(String symbol) throws JFSMException  {
		throw new JFSMException("Méthode next non implémentée");
	}

	/** 
	* Exécute l'automate sur un mot (une liste de symboles)
	* @param l la liste de symboles
	* @return un booléen indiquant sur le mot est reconnu 
	* @exception JFSMException Exception levée si la méthode n'est pas implémentée
	*/
	public boolean run(List<String> l) throws JFSMException  {
		throw new JFSMException("Méthode run non implémentée");
	}

	/** 
	* Enregistre un automate sous le format XML "JFLAP 4".
	* @param file le nom du fichier
	*/
	public void save(String file) {
		try{
			File ff=new File(file); 
			ff.createNewFile();
			FileWriter ffw=new FileWriter(ff);
			ffw.write("<?xml version='1.0' encoding='UTF-8' standalone='no'?><!--Created with JFSM.--><structure>\n");  
			ffw.write("\t<type>fa</type>\n"); 
			ffw.write("\t<automaton>\n");
			for(Etat e : Q.values()) {
				ffw.write("\t\t<state id='"+e.no+"' name='"+e.name+"'>\n\t\t\t<x>0</x>\n\t\t\t<y>0</y>\n");
				if (isInitial(e.name)) ffw.write("\t\t\t<initial/>\n");
				if (isFinal(e.name)) ffw.write("\t\t\t<final/>\n");
				ffw.write("\t\t</state>\n");
			}
			for(Transition t : mu){
				ffw.write("\t\t<transition>\n");
				Etat from = getEtat(t.source);
				ffw.write("\t\t\t<from>"+from.no+"</from>\n");
				Etat to = getEtat(t.cible);
				ffw.write("\t\t\t<to>"+to.no+"</to>\n");
				if (!(t instanceof EpsilonTransition)) ffw.write("\t\t\t<read>"+t.symbol+"</read>\n");
				else ffw.write("\t\t\t<read/>\n");
				ffw.write("\t\t</transition>\n");
			}
			ffw.write("\t</automaton>\n");
			ffw.write("</structure>\n"); 
			ffw.close(); 
		} catch (Exception e) {}
	}

	/** 
	* Charge un automate construit avec JFLAP au format XML "JFLAP 4".
	* @param file le nom du fichier
	* @return un automate 
	*/
	static public Automate load(String file) {
		JFLAPHandler handler = new JFLAPHandler();
		try {
			XMLReader saxParser = XMLReaderFactory.createXMLReader();
			saxParser.setContentHandler(handler);
			saxParser.setErrorHandler(handler);
			saxParser.parse( file ); 
		} catch (Exception e) {
			System.out.println("Exception capturée : ");
			e.printStackTrace(System.out);
			return null;
		}
		try {
			if (AFD.testDeterminisme(handler.res) ) 
				return new AFD(handler.res) ;
				else return new AFN(handler.res) ;
		} catch (JFSMException e) {return null;}
	}

}

class JFLAPHandler extends DefaultHandler {
	String cdc ;
	public Set<Etat> Q;
	public Set<String> F, I;
	public Set<String> A;
	public Set<Transition> mu;
	private Etat e;
	private Transition t;
	private String from, to, read, state_name, state_id;
	private boolean final_state, initial_state;
	public Automate res ;

	public JFLAPHandler() {super();}

	public void characters(char[] caracteres, int debut, int longueur) {
		cdc = new String(caracteres,debut,longueur);
	}

	public void startDocument() {
		cdc="";
		A = new HashSet<String>();
		I = new HashSet<String>();
		F = new HashSet<String>();
		Q = new HashSet<Etat>();
		mu = new HashSet<Transition>();
		res = null;
	}

	public void endDocument() {
		try{
			res = new Automate(A,Q,I,F,mu);
		} catch (JFSMException e) {
				System.out.println("Erreur:"+e);
		}
	}

	public void startElement(String namespaceURI, String sName, String name, Attributes attrs) {
		if (name=="state") {
			state_name = attrs.getValue("name");
			state_id = attrs.getValue("id");
			final_state = false;
			initial_state = false;
		} else if (name=="initial") {
			initial_state = true;
		} else if (name=="final") {
			final_state = true;
		}
		cdc="";
	}

	public void endElement(String uri, String localName, String name) {
		if (name=="state") {
			e = new Etat(state_id);
			Q.add(e);
			if (initial_state) I.add(state_id);
			if (final_state) F.add(state_id);
		} else if (name=="transition") {
			try{
				if (read!="") {
					A.add(read);
					Transition t = new Transition(from,read,to);
					mu.add(t);
				} else {
					EpsilonTransition t = new EpsilonTransition(from,to);
					mu.add(t);
				}
			} catch (JFSMException e) {
				System.out.println("Erreur:"+e);
			}
		} else if (name=="type") {
		
		} else if (name=="from") {
			from = cdc;
		} else if (name=="to") {
			to = cdc;
		} else if (name=="read") {
			read = cdc;
		}
	}
}



