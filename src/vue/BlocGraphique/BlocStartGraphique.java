/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vue.BlocGraphique;

import Modèle.BlocStart;
import javax.swing.ImageIcon;

/**
 * C'est le bloc graphique qui représentera le blocStart.
 * @author tancfire
 */
public class BlocStartGraphique extends BlocGraphique {

    public BlocStartGraphique() {
        super(new ImageIcon("src/images/BlocStart.png"));
    }
    
    @Override
    protected void mettreAjourTexte() {
        this.setTexte("Start", "");
    }
}
