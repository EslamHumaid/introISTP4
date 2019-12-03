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
 * AFD.java
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
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

import java.util.Map;
import java.util.HashMap;

import java.util.Iterator;

public class AFD extends Automate {
	private String i; // l'état initial

	public AFD(Set<String> A, Set<Etat> Q, String i, Set<String> F, Set<Transition> mu) throws JFSMException {
		super(A,Q,new HashSet<String>(),F,mu);
		assert this.Q.containsKey(i) : "i n'est pas un état" ;
		setInitial(i);
		this.i = i;
		assert testDeterminisme(this) : "L'automate doit être déterministe";
	}

	public AFD(Automate a)  throws JFSMException {
		this(a.A, new HashSet(a.Q.values()), (String)a.I.iterator().next() , a.F, a.mu);
	}

	public void init() {
		super.init();
		current = i;
	}

	/** 
	* Détermine si un automate est déterministe)
	* @param T L'automate à tester
	* @return un booléen indiquant sur l'automate est déterministe 
	*/
	static public boolean testDeterminisme(Automate T) {
		if (T.I.size() !=1) {
			return false;
		} else {
			// un seul état initial !
			boolean ok = true;
			for(Transition t : T.mu) ok = ok && ! (t instanceof EpsilonTransition);
			if (ok) {
				// pas d'epsilon-transition !
				int nb;
				for(Etat e : T.Q.values()) {// pour chaque état e...
					for (String a : T.A) {// pour chaque symbole a...
						nb = 0;
						for(Transition t : T.mu){ // compter le nombre de transitions qui partent de e avec a
							if ((t.source==e.name) && (t.symbol == a)) nb += 1;
						}
						ok = ok && (nb < 2) ;
					}
				}
				// Si ok : chaque état a au max 1 transition vers un autre état avec un symbol donné.
				return ok;
			} else return false;
		}
	}

	/** 
	* Permet de transformer l'automate en un automate déterministe minimal  
	* @return un automate équivalent minimal
	*/
	public AFD minimiser() {
		System.out.println("minimiser() : méthode non implémentée");
		return this;
	}

	public Queue<Transition> next(String symbol) {
		assert A.contains(symbol) : "next() : le symbole doit être un symbole de l'alphabet." ;
		Iterator<Transition> it = mu.iterator();
		boolean ok = false;
		Transition t = null ;
		while(it.hasNext() && (!ok)){
			t = it.next();
			ok = t.candidate(current,symbol);
		}
		Queue<Transition> l = new LinkedList<Transition>();
		if (ok) {
			l.add(t);
		} 
		return l;
	}

	public boolean run(List<String> l) {
        init();
        boolean ok = true;
        for (String symbol : l) {
            // System.out.println(symbol);

            Queue<Transition> lt = next(symbol);
            if (lt.isEmpty()) {
                ok = false;
                break;
            } else {
            	Transition t = lt.poll();
	            current = t.appliquer();
				histo.push(t);
            }
        }
        return ok && isFinal(current);
    }
}
