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
 * JFSM.java
 *
 *
 * Created: 2017-08-25
 *
 * @author Emmanuel Desmontils
 * @version 1.0
 */


/*
 * Methodes : { transpose(), etoile(), standardiser(), normalise(), estStandard(),
 *  estNormalise(), emonder(), estUtile(), estAccessible(), estCoAccessible() }
 *  
 *  Implimentees par HUMAID Eslam, BAGHAWITAH Ahmed  Groupe 384J
 *  
 */

import java.util.Set;
import java.util.HashSet;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import java.util.Iterator;

import JFSM.*;
import JFSM.Transducteur.*;

public class JFSM {
    public static void main(String argv []) throws JFSMException {
    	/*********** automate 1 *************/ 
      Set<String> A = new HashSet<String>();      
      A.add("a");A.add("b");A.add("c");

      Set<Etat> Q = new HashSet<Etat>();
      Q.add(new Etat("1"));Q.add(new Etat("2"));
      Q.add(new Etat("3"));Q.add(new Etat("4"));Q.add(new Etat("5"));
      

      Set<Transition> mu = new HashSet<Transition>();
      mu.add(new Transition("1","a","2"));
      mu.add(new Transition("1","b","4"));
      mu.add(new Transition("2","b","3"));
      mu.add(new Transition("2","c","4"));
      mu.add(new Transition("3","a","2"));
      mu.add(new Transition("3","b","4"));
      mu.add(new Transition("4","a","5"));
      mu.add(new Transition("5","c","5"));
      

      Set<String> F = new HashSet<String>();
      F.add("5");
      F.add("4");
      F.add("1");
      Automate afn = new AFD(A, Q, "1", F, mu);
      /************ fin automate 1 *************/ 
      
      //test automate 1 
      List<String> l = new ArrayList<String>();
      l.add("a");l.add("b");l.add("a");l.add("c");

      
    /************ automate 2 *************/ 
      
      
      Set<String> A2 = new HashSet<String>();      
      A2.add("ZO");A2.add("MEU");A2.add("GA");A2.add("BU");

      Set<Etat> Q2 = new HashSet<Etat>();
      Q2.add(new Etat("1"));Q2.add(new Etat("5"));Q2.add(new Etat("9"));
      Q2.add(new Etat("2"));Q2.add(new Etat("6"));
      Q2.add(new Etat("3"));Q2.add(new Etat("7"));
      Q2.add(new Etat("4"));Q2.add(new Etat("8"));

      Set<Transition> mu2 = new HashSet<Transition>();
      mu2.add(new Transition("1","ZO","1"));
      mu2.add(new Transition("1","GA","4"));
      mu2.add(new Transition("1","BU","5"));
      mu2.add(new Transition("2","MEU","1"));
      mu2.add(new Transition("2","BU","5"));
      mu2.add(new Transition("2","ZO","6"));
      mu2.add(new Transition("3","GA","3"));
      mu2.add(new Transition("3","MEU","2"));
      mu2.add(new Transition("3","ZO","6"));
      mu2.add(new Transition("4","ZO","5"));
      mu2.add(new Transition("4","BU","8"));
      mu2.add(new Transition("4","GA","7"));
      mu2.add(new Transition("5","BU","8"));
      mu2.add(new Transition("5","ZO","9"));
      mu2.add(new Transition("5","GA","6"));
      mu2.add(new Transition("6","GA","6"));
      mu2.add(new Transition("6","ZO","9"));
      mu2.add(new Transition("7","MEU","7"));
      mu2.add(new Transition("8","MEU","7"));
      mu2.add(new Transition("9","MEU","8"));
      mu2.add(new Transition("9","BU","9"));
   


      Set<String> F2 = new HashSet<String>();
      F2.add("8");
      F2.add("7");
      F2.add("9");
     
      
      
      Set<String> I2 = new HashSet<String>();
      I2.add("1");
      I2.add("2");
      I2.add("3");
      
      Automate afn_des = new AFN(A2, Q2, I2, F2, mu2); //AFN car l'automate n'est pas deteministe 
      
      
      
      
     /************fin automate 2*************/ 
      
      /************automate 3 cas 1 transpose*************/ 
      Set<String> A3 = new HashSet<String>();      
      A3.add("a");A3.add("b");A3.add("c");
      
      
      Set<Etat> Q3 = new HashSet<Etat>();
      Q3.add(new Etat("1"));Q3.add(new Etat("2"));Q3.add(new Etat("3"));

      Set<Transition> mu3 = new HashSet<Transition>();
      mu3.add(new Transition("1","b","1"));
      mu3.add(new Transition("1","a","2"));
      mu3.add(new Transition("2","c","2"));
      mu3.add(new Transition("2","b","3"));
      
      Set<String> F3 = new HashSet<String>();
      F3.add("3");
      
      Set<String> I3 = new HashSet<String>();
      I3.add("1");
      
      Automate auto3 = new AFN(A3, Q3, I3, F3, mu3);
      
      /************fin automate 3*************/
      
      /************automate 4 cas 2 mise a l'etoile*************/
      Set<String> A4 = new HashSet<String>();      
      A4.add("a");A4.add("b");A4.add("c");
      
      
      Set<Etat> Q4 = new HashSet<Etat>();
      Q4.add(new Etat("1"));Q4.add(new Etat("2"));Q4.add(new Etat("3"));

      Set<Transition> mu4 = new HashSet<Transition>();
      mu4.add(new Transition("1","a","2"));
      mu4.add(new Transition("2","b","2"));
      mu4.add(new Transition("2","c","3"));
      mu4.add(new Transition("1","b","3"));
    
      
      Set<String> F4 = new HashSet<String>();
      F4.add("3");
     
      
      
      Set<String> I4 = new HashSet<String>();
      I4.add("1");
      
      Automate auto4 = new AFN(A4, Q4, I4, F4, mu4);
      
      /************fin automate 4*************/
      
      //test automate 2 
      List<String> l2 = new ArrayList<String>();    //MEU MEU BU ZO BU MEU
      l2.add("MEU");l2.add("MEU");l2.add("BU");l2.add("ZO");l2.add("BU");l2.add("MEU");
      
      List<String> l3 = new ArrayList<String>();    //GA BU ZO MEU
      l3.add("GA");l3.add("BU");l3.add("ZO");l3.add("MEU");

      List<String> l4 = new ArrayList<String>();    //ZO ZO GA ZO GA GA ZO
      l4.add("ZO");l4.add("ZO");l4.add("GA");l4.add("ZO");l4.add("GA");l4.add("GA");l4.add("ZO");

    
      List<String> l5 = new ArrayList<String>();    //BU GA ZO MEU
      l5.add("BU");l5.add("GA");l5.add("ZO");l5.add("MEU");
      
      
      System.out.println("TEST AUTOMATE 1");
      System.out.println(afn);
      System.out.println(afn.run(l));
      
      
      System.out.println("TEST AUTOMATE 2");
      System.out.println(afn_des);      
      System.out.println(afn_des.run(l2));
      System.out.println(afn_des.run(l3));
      System.out.println(afn_des.run(l4));
      System.out.println(afn_des.run(l5));
      
      
      System.out.println("-------------------TEST STANDARISER AUTOMATE 2--------------------");
      System.out.println(afn_des);
      System.out.println("est standard? " + afn_des.estStandard());
      System.out.println(afn_des.standardiser());
      System.out.println("est standard?" + afn_des.standardiser().estStandard());
      
      
      
      System.out.println("-------------------TEST NORMELISER AUTOMATE 2--------------------");
      System.out.println(afn_des);
      System.out.println("est normale? " + afn_des.estNormalise());
      System.out.println(afn_des.normaliser());
      System.out.println("est normale?" + afn_des.normaliser().estNormalise());
      /*
      System.out.println(afn.emonder());

      System.out.println(afn);
      System.out.println("Epsilon Libre ? "+afn.epsilonLibre());

      afn.save("test.jff");

      Automate afn2 = Automate.load("essai.jff");
      System.out.println(afn2.getClass().getName());
      System.out.println(afn2);
      
      
      
      System.out.println("test de la methode transpose() par un example ( cas 1 des jeux d'essaies)");
      System.out.println("L'automate:");
      System.out.println(auto3);
      System.out.println("La transpose: :");
      System.out.println(auto3.transpose());
      
    
      System.out.println("test de la methode etoile() par un example ( cas 2 des jeux d'essaies)");
      System.out.println(auto4);
      System.out.println(" l'autmoate reconnaissant le langage L* :");
      System.out.println(auto4.etoile());
      */
   }
}
