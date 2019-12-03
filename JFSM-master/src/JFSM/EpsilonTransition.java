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

package JFSM;

/**
 * EpsilonTransition.java
 *
 *
 * Created: 2017-08-25
 *
 * @author Emmanuel Desmontils
 * @version 1.0
 */

public class EpsilonTransition extends Transition {
	
	public EpsilonTransition(String s, String c) throws JFSMException {
		super(s,"###",c);
		this.symbol = "\u03b5";
	}

	public EpsilonTransition(Etat s, Etat c) throws JFSMException {
		this(s.name,c.name);
	}	

	public boolean candidate(String etat, String symbol) {
		return etat.equals(source) ;
	}

	public Object clone() {
		Object o = null;
		// try {
		o = super.clone();
		// } catch(CloneNotSupportedException cnse) {
		// 	cnse.printStackTrace(System.err);
		// }
		return o;
	}
}
