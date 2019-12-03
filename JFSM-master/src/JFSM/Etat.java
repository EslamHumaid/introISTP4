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

package JFSM ;

/**
 * Etat.java
 *
 *
 * Created: 2017-08-25
 *
 * @author Emmanuel Desmontils
 * @version 1.0
 */

public class Etat  implements Cloneable {
	static public int nb = 0;

	public int no;
	public String name;

	public Etat(String n) {
		this.name = n;
		Etat.nb++;
		this.no = Etat.nb;
	}

	public String toString() {
		return this.name;//+'('+this.no+')';
	}

	public void rename(String newName) {
		this.name = newName ;
	}
	
	public Object clone() {
		Etat o = null;
		try {
			o = (Etat)super.clone();
			Etat.nb++;
			o.no = Etat.nb;
		} catch(CloneNotSupportedException cnse) {
			cnse.printStackTrace(System.err);
		}
		return o;
	}
}

