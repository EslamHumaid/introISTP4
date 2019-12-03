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

package JFSM.Transducteur;

/**
 * TransitionMealy.java
 *
 *
 * Created: 2017-08-25
 *
 * @author Emmanuel Desmontils
 * @version 1.0
 */

import JFSM.Transition ;
import JFSM.JFSMException ;
import JFSM.Etat ;

public class TransitionMealy extends Transition {
	public String prod ;

	public TransitionMealy(String s, String symbolIn, String symbolOut, String c) throws JFSMException {
		super(s,symbolIn,c);
		prod = symbolOut ;
	}

	public TransitionMealy(Etat s, String symbolIn, String symbolOut, Etat c) throws JFSMException {
		this(s.name,symbolIn,symbolOut,c.name);
	}
}
