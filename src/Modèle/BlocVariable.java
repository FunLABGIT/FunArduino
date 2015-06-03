/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modèle;

import Controleur.Controleur;
import java.awt.Color;
import vue.BlocGraphique.BlocGraphique;

/**
 *
 * @author tancfire
 */
public class BlocVariable extends Bloc{
    private Variable var;

    public BlocVariable(TypeBloc typeBloc, Color couleur, BlocGraphique blocGraph, Variable var, Controleur ctrl) {
        super(typeBloc, couleur, blocGraph, ctrl);
        this.var = var;
    }

    public BlocVariable(int id, TypeBloc typeBloc, Color couleur, BlocGraphique blocGraph, Variable var, Controleur ctrl) {
        super(id, typeBloc, couleur, blocGraph, ctrl);
        this.var = var;
    }
    
    
    @Override
    public void mettreAjourCode() {
       if(ctrl.getVariableById(var.getId())==null)
       {
           delete();
       }
    }
    
    
    public Variable getVariable() {
        return var;
    }
    
}
