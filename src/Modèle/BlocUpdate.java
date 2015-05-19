/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modèle;

import Controleur.Controleur;
import java.awt.Color;
import vue.BlocGraphique.BlocUpdateGraphique;

/**
 * Ce bloc est appelé en permanence tout au long du programme Arduino (représente
 * le loop).
 * @author tancfire
 */
public class BlocUpdate extends Bloc {
    
    public BlocUpdate(Controleur ctrl)
    {
        super(Color.green, ctrl); 
        blocGraph= new BlocUpdateGraphique(this);
       // ctrl.ajouterBlocGraphique(blocGraph);
    }
    
    
    @Override
    public void mettreAjourCode()
    {
        sonCodeDebut = "\nvoid loop(){\n";
        sonCodeFin = "}\n";
    }
}
