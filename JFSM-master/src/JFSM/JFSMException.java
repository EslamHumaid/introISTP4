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
 * FSMException.java
 *
 *
 * Created: 2017-08-25
 *
 * @author Emmanuel Desmontils
 * @version 1.0
 */


public class JFSMException extends Exception {
	/** 
	* Crée une nouvelle instance de JFSMException 
	*/  
	public JFSMException() {}  
	/** 
	* Crée une nouvelle instance de JFSMException 
	* @param message Le message détaillant exception 
	*/  
	public JFSMException(String message) {  
		super(message); 
	}  
	/** 
	* Crée une nouvelle instance de JFSMException 
	* @param cause L'exception à l'origine de cette exception 
	*/  
	public JFSMException(Throwable cause) {  
		super(cause); 
	}  
	/** 
	* Crée une nouvelle instance de JFSMException 
	* @param message Le message détaillant exception 
	* @param cause L'exception à l'origine de cette exception 
	*/  
	public JFSMException(String message, Throwable cause) {  
		super(message, cause); 
	}
}
