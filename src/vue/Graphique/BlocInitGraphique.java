/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vue.Graphique;

import javax.swing.ImageIcon;

/**
 *
 * @author tancfire
 */
public class BlocInitGraphique extends BlocGraphique{

    public BlocInitGraphique() {
        super(new ImageIcon("src/images/BlocInit.png"));
    }

    @Override
    protected String getTexte1() {
        return "Initialisation";
    }

    @Override
    protected String getTexte2() {
        return "";
    }
    
}
