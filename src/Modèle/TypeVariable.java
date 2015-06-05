/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modèle;

/**
 * C'est le type de la variable.
 * @author tancfire
 */
public enum TypeVariable {
    nombreEntier("int"), nombreAVirgule("float"), texte("String");
    
    private String type;
    
    TypeVariable(String type)
    {
        this.type = type;
    }
    
    public String getType()
    {
        return type;
    }
}
