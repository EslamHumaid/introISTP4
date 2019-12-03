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
 * AFN.java
 *
 *
 * Created: 2018-09-18
 *
 * @author Emmanuel Desmontils
 * @version 1.0
 */

package JFSM;

import java.util.Set;
import java.util.HashSet;

import java.util.List;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Iterator;

import java.util.Stack;
import java.util.Deque;

public class AFN extends Automate {

	public AFN(Set<String> A, Set<Etat> Q,  Set<String> I,  Set<String> F, Set<Transition> mu) throws JFSMException {
		super(A,Q,I,F,mu);
	}

	public AFN(Automate a)  throws JFSMException {
		this(a.A, new HashSet(a.Q.values()),a.I, a.F, a.mu);
	}

	/** 
	* Permet de transformer l'automate en un automate déterministe  
	* @return un automate déterministe équivalent
	*/
	public AFD determiniser() { 
		System.out.println("determiniser() : méthode non implémentée");
		return null;
	}

	public Queue<Transition> next(String symbol) {
		assert A.contains(symbol) : "next() : le symbole doit être un symbole de l'alphabet." ;
		Queue<Transition> l = new LinkedList<Transition>();
		for(Transition t : mu) {
			if (t.candidate(current,symbol)) {
				l.add(t) ;
			}
		}
		return l;
	}

	// ATTENTION : Fonctionne pas si l'automate possède un cycle d'espilon-transitions !
	private boolean runAFN(String currentState, Deque<String> evts) {
		boolean ok = false;
		System.out.println("State:"+evts+"/"+currentState);
		current = currentState ;
		if (evts.isEmpty()) {
			ok = isFinal(currentState);
			if (ok) System.out.println("OK");
			else System.out.println("Echec (état non final)");
			return ok;
		} else {
			String symbol = evts.pollFirst();
			Queue<Transition> lt = next(symbol);
			if (lt.size()==0) System.out.println("Echec (pas de transition)");
			else {
				for (Transition t : lt) {
					if (t instanceof EpsilonTransition) {
						evts.addFirst(symbol);
					}
					System.out.println("Transition choisie:"+t+" avec "+evts);
					String next = t.appliquer();
					histo.push(t);
					ok = runAFN(next,evts);
					if (ok) break;
					else {
						System.out.println("Mauvais choix:"+t);
						current = currentState ;
						t = histo.pop();
						if (t instanceof EpsilonTransition) symbol = evts.pollFirst();
					}
				} 
			}
			if (!ok) evts.addFirst(symbol);
			return ok;
		}
	}

	public boolean run(List<String> l) {
        init();
        boolean ok = false;
        for(String i : I) {
        	if (runAFN(i,new LinkedList<String>(l))) {
        		ok = true;
        		break;
        	}
        }
        return ok ;
    }

}
