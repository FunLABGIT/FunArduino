/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vue.Graphique;

import Modèle.Composant;
import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.BUTTON1;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author tancfire
 */
public class ComposantGraphique extends JLabel {
    protected Composant comp;
    protected SimulateurGraphique simu;
    private JLabel labelCroix;
    private JLabel labelLigne;
    private JLabel labelLigneVert;
   
    public ComposantGraphique(ImageIcon image, SimulateurGraphique simulateur)
    {
        super(image);
        this.simu = simulateur;
        setSize(20,50);
        setLocation((int) (400+(Math.random()*20)), (int) (10+(Math.random()*5)));
        
        this.labelLigne = new JLabel(new ImageIcon("src/images/ligneJaune.png"));
        this.labelLigneVert = new JLabel(new ImageIcon("src/images/ligneJauneVertical.png"));
        this.labelCroix = new JLabel(new ImageIcon("src/images/croixComp.png"));
        labelCroix.setSize(20, 19);
        labelCroix.setVisible(false);
        
        
        /*========================================
        -----------GESTION SOURIS LIGNE-----------
        ========================================*/
        
        labelLigneVert.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
               
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                 comp.changerSlot(e.getX()+simu.getX()-5, e.getY()+simu.getY());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        
        /*========================================
        -----------GESTION SOURIS CROIX-----------
        ========================================*/
        
        labelCroix.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                    comp.delete();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
               labelCroix.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelCroix.setVisible(false);
            }
        });
        
        /*=======================================
        ---------GESTION SOURIS COMPOSANT--------
        ========================================*/
         this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton()==BUTTON1)
                     labelCroix.setVisible(false);
            }

            @Override
            public void mouseReleased(MouseEvent e) { //clic relâché
                 if(e.getButton()==BUTTON1){ //si c'est un clic gauche
                     setLocation(e.getX()+getX()-(getWidth()/2),e.getY()+getY()-(getHeight()/2));
                     mettreAJour();
                 }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labelCroix.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelCroix.setVisible(false);
            }
        });
         
        //mettreAJour();
    }
    
    public void setComposant(Composant comp)
    {
        this.comp = comp;
        mettreAJour();
    }
    
    public void mettreAJour()
    {
        if(comp!=null){ //Pour être sûr
        int longueur = comp.getPin().getX()+simu.getX()-this.getX()-20;
        int hauteur = comp.getPin().getY()+simu.getY()-this.getY();
        
        if(longueur>0){
            labelLigne.setLocation(this.getX()+15, this.getY()+45);
            labelLigne.setSize(longueur, 5);
        }else{
            labelLigne.setLocation(this.getX()+15+longueur, this.getY()+45);
            labelLigne.setSize(longueur*-1, 5);
        }
        
        if(hauteur>0){
            labelLigneVert.setLocation(this.getX()+longueur+15, this.getY()+45);
            labelLigneVert.setSize(5, hauteur);
        }else{
            labelLigneVert.setLocation(this.getX()+longueur+15, this.getY()+50+hauteur);
            labelLigneVert.setSize(5, hauteur*-1); 
        }
        
        labelCroix.setLocation(this.getX()+this.getWidth()-5, this.getY());
        
        comp.mettreAJourBlocsGraphiques();
        }
    }
    
     public void attacher(JPanel panel)
    {
        panel.add(this);
        panel.add(labelLigne);
        panel.add(labelLigneVert);
        panel.add(labelCroix);
    }
     
    public void detacher(JPanel panel)
    {
        panel.remove(this);
        panel.remove(labelLigne);
        panel.remove(labelLigneVert);
        panel.remove(labelCroix);
    }
    
    
}
