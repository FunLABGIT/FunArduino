/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vue;

import vue.BlocGraphique.BlocGraphique;
import Controleur.Controleur;
import Modèle.Bloc;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import vue.BlocGraphique.StockCouleurTexte;


/**
 * 
 * Prochaines améliorations: faire une liste des blocs graphiques et créer des
 * méthodes pour pouvoir la gérer (Pour le positionnement en Y).
 * @author tancfire
 */
public class KitArduinoFrame extends javax.swing.JFrame {
    private Controleur ctrl;
    private ArrayList<BlocGraphique> sesBlocsGraphs;
    
    /**
     * Creates new form KitArduinoFrame
     */
    public KitArduinoFrame() {
            initComponents();
            
            nouveauFichier.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                 ctrl.creerProjet(nouveauFichier.getSelectedFile().getPath().substring(0, nouveauFichier.getSelectedFile().getPath().length()-nouveauFichier.getSelectedFile().getName().length()), nouveauFichier.getSelectedFile().getName());
                 actualiserTitre();
                 ctrl.remettreAZero();
                }
            });
            
            enregistrerFichier.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                 ctrl.changerSauvegardeProjet(enregistrerFichier.getSelectedFile().getPath().substring(0, enregistrerFichier.getSelectedFile().getPath().length()-enregistrerFichier.getSelectedFile().getName().length()), enregistrerFichier.getSelectedFile().getName());
                 actualiserTitre();
                }
            });
            
            ouvrirFichier.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                 ctrl.charger(ouvrirFichier.getSelectedFile().getPath().substring(0, ouvrirFichier.getSelectedFile().getPath().length()-ouvrirFichier.getSelectedFile().getName().length()), ouvrirFichier.getSelectedFile().getName());
                 actualiserTitre();
                }
            });
            
            
            sesBlocsGraphs = new ArrayList<BlocGraphique>();
            ctrl = new Controleur(this);
            ctrl.mettreAjourCode();
            
            
            actualiserTitre();
    }
    
    
    public void mettreAJourBlocsGraphiques(ArrayList<BlocGraphique> blocsGraphs)
    {
        //effacer tout les anciens blocs graphiques
        for(int i=0; i<sesBlocsGraphs.size();i++)
        {
            supprimerBlocGraphique(sesBlocsGraphs.get(i));
        }
        sesBlocsGraphs = blocsGraphs;
        
        for(int i=0; i<sesBlocsGraphs.size();i++)
        {
            sesBlocsGraphs.get(i).setPosition(i);
            ajouterBlocGraphique(sesBlocsGraphs.get(i));
        }
    }
    
    
    
    public void actualiserTitre()
    {
      this.setTitle("FunArduino ("+ctrl.getNomProjet()+")");
    }
    
    // A mettre dans le controleur
    public void setCode(String code)
    {
     ArrayList<StockCouleurTexte> stock = new ArrayList<StockCouleurTexte>();
     String codeFinal="";
     boolean bool = true;
        
     //Avec cet algorithme, on cherche à trouver toutes les balises [color rXXX bXXX gXXX] et [/color]
     String txtP = "\\[color r\\d{1,3} b\\d{1,3} g\\d{1,3}\\](.|$|\\n)*?\\[/color\\]";
     Pattern p = Pattern .compile(txtP);
    String entree = code;
     Matcher m = p.matcher(entree);
     
        while (m.find()){ //Lorsque l'on tombe sur une des balises
        if(bool==true)
        {
            codeFinal+= entree.substring(0,m.start()); // on ajoute tout ce qui se trouve avant le premier [color]
            bool = false;
        }
      
      String expression = entree.substring(m.start(), m.end()); //On récupère l'expression [color ...] ... [/color]
                //Cette fois, on regarde DANS la balise pour récupérer le code couleur
                //Il y a surement possibilité de l'optimiser
                 String patternBaliseE = "\\[color r\\d{1,3} b\\d{1,3} g\\d{1,3}\\]";
                 Pattern p2 = Pattern .compile(patternBaliseE);
                 Matcher m2 = p2.matcher(expression);
                 if(m2.find()) //Si on tombe sur la balise (normalement, il ne devrait pas y avoir de problèmes ...)
                 {
                     String baliseE = expression.substring(m2.start(), m2.end()); //On récupère que la balise du début
                     String txtCode= expression.substring(m2.end(),expression.length()-8);//On récupère l'expression entre les deux balises
                     codeFinal+=txtCode; //On ajoute l'expression contenu entre les deux balises dans le code final.
                     
                      int c1 =  decoderCouleur(baliseE, "r"); //on récupère la valeur du rouge pour le code couleur
                      int c2 =  decoderCouleur(baliseE, "b"); //on récupère la valeur du bleu pour le code couleur
                      int c3 =  decoderCouleur(baliseE, "g"); //on récupère la valeur du vert pour le code couleur
                     
                     stock.add(new StockCouleurTexte(new Color(c1,c3,c2), codeFinal.length()-txtCode.length(), txtCode.length()));//On ajoute un nouveau StockCouleurTexte (qui stocke où se trouve les couleurs) dans le stock.
                 }
        }
          editCode.setText(codeFinal); //On ajoute le code final au Panel.
          
          for(int i=0; i<stock.size(); i++) //On parcourt le stock (où sont sotcké les couleurs avec leurs positions).
          {
              StockCouleurTexte stockI = stock.get(i);
              setJTextPaneFont(editCode, stockI.getCouleur(), stockI.getDebut(), stockI.getLongueur()); //On définit la couleur du stockI pour l'appliquer au panel.
          }
    }
    
    /*============================================================================================
    --------------------------------Pour la couleur dans le texte -------------------------------
    ============================================================================================*/
    
    
    private int decoderCouleur(String balise, String cCouleur)
    {
       int couleur=0; //entre 0 et 255
       
       String patternC1 = cCouleur+"\\d{1,3}";
       Pattern p3 = Pattern.compile(patternC1);
       Matcher m3 = p3.matcher(balise);
       if(m3.find())
       couleur = Integer.parseInt(balise.substring(m3.start()+1,m3.end()));
        
        return couleur;
    }
    
    
    private void setJTextPaneFont(JTextPane jtp, Color c, int from, int length) {
            StyleContext sc = StyleContext.getDefaultStyleContext();

            AttributeSet attrs = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

            StyledDocument doc = jtp.getStyledDocument();
            doc.setCharacterAttributes(from, length, attrs, true);
        }
    
    /*============================================================================================
    -------------------------------Fin pour la couleur dans le texte------------------------------
    ============================================================================================*/
    

    private void ajouterBlocGraphique(BlocGraphique blocGraph)
    {
        blocGraph.attacher(panelGraphique);
    }
    
    private void supprimerBlocGraphique(BlocGraphique blocGraph)
    {
        blocGraph.detacher(panelGraphique);
       panelGraphique.repaint();
    }
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        choixArduinoGroupe = new javax.swing.ButtonGroup();
        nouveauFichier = new javax.swing.JFileChooser();
        enregistrerFichier = new javax.swing.JFileChooser();
        ouvrirFichier = new javax.swing.JFileChooser();
        scrollPanelGraphique = new javax.swing.JScrollPane();
        panelGraphique = new javax.swing.JPanel();
        labelImgArduino = new javax.swing.JLabel();
        scrollEditCode = new javax.swing.JScrollPane();
        editCode = new javax.swing.JTextPane();
        btnTeleverser = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        scrollListeObjets = new javax.swing.JScrollPane();
        listeBlocs = new javax.swing.JList();
        menuBarre = new javax.swing.JMenuBar();
        menuFichier = new javax.swing.JMenu();
        itemNouveau = new javax.swing.JMenuItem();
        itemOuvrir = new javax.swing.JMenuItem();
        itemSauvegarder = new javax.swing.JMenuItem();
        itemSauvegarderSous = new javax.swing.JMenuItem();
        itemQuitter = new javax.swing.JMenuItem();
        menuEdition = new javax.swing.JMenu();
        menuOutils = new javax.swing.JMenu();
        menuChoixArduino = new javax.swing.JMenu();
        itemLeonardo = new javax.swing.JRadioButtonMenuItem();
        itemUno = new javax.swing.JRadioButtonMenuItem();

        nouveauFichier.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);

        enregistrerFichier.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);

        ouvrirFichier.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FunArduino Project");
        setMinimumSize(new java.awt.Dimension(1024, 576));

        panelGraphique.setBackground(new java.awt.Color(255, 255, 255));

        labelImgArduino.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LeonardoNoHeadersFront_2_450px.jpg"))); // NOI18N

        javax.swing.GroupLayout panelGraphiqueLayout = new javax.swing.GroupLayout(panelGraphique);
        panelGraphique.setLayout(panelGraphiqueLayout);
        panelGraphiqueLayout.setHorizontalGroup(
            panelGraphiqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGraphiqueLayout.createSequentialGroup()
                .addGap(242, 242, 242)
                .addComponent(labelImgArduino, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(522, Short.MAX_VALUE))
        );
        panelGraphiqueLayout.setVerticalGroup(
            panelGraphiqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGraphiqueLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(labelImgArduino)
                .addContainerGap(419, Short.MAX_VALUE))
        );

        scrollPanelGraphique.setViewportView(panelGraphique);

        editCode.setEditable(false);
        scrollEditCode.setViewportView(editCode);

        btnTeleverser.setText("téléverser");
        btnTeleverser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTeleverserActionPerformed(evt);
            }
        });

        listeBlocs.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "BlocStart", "BlocCustom" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrollListeObjets.setViewportView(listeBlocs);

        jTabbedPane1.addTab("Blocs", scrollListeObjets);

        menuFichier.setText("Fichier");

        itemNouveau.setText("Nouveau");
        itemNouveau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNouveauActionPerformed(evt);
            }
        });
        menuFichier.add(itemNouveau);

        itemOuvrir.setText("Ouvrir");
        itemOuvrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemOuvrirActionPerformed(evt);
            }
        });
        menuFichier.add(itemOuvrir);

        itemSauvegarder.setText("Enregistrer");
        itemSauvegarder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemSauvegarderActionPerformed(evt);
            }
        });
        menuFichier.add(itemSauvegarder);

        itemSauvegarderSous.setText("Enregistrer-sous");
        itemSauvegarderSous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemSauvegarderSousActionPerformed(evt);
            }
        });
        menuFichier.add(itemSauvegarderSous);

        itemQuitter.setText("Quitter");
        itemQuitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemQuitterActionPerformed(evt);
            }
        });
        menuFichier.add(itemQuitter);

        menuBarre.add(menuFichier);

        menuEdition.setText("Edition");
        menuBarre.add(menuEdition);

        menuOutils.setText("Outils");

        menuChoixArduino.setText("Arduino");

        choixArduinoGroupe.add(itemLeonardo);
        itemLeonardo.setSelected(true);
        itemLeonardo.setText("Leonardo");
        itemLeonardo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemLeonardoActionPerformed(evt);
            }
        });
        menuChoixArduino.add(itemLeonardo);

        choixArduinoGroupe.add(itemUno);
        itemUno.setText("Uno");
        menuChoixArduino.add(itemUno);

        menuOutils.add(menuChoixArduino);

        menuBarre.add(menuOutils);

        setJMenuBar(menuBarre);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPanelGraphique, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollEditCode, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTeleverser, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scrollEditCode)
                    .addComponent(scrollPanelGraphique, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1))
                .addGap(18, 18, 18)
                .addComponent(btnTeleverser)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemQuitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemQuitterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemQuitterActionPerformed

    private void btnTeleverserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTeleverserActionPerformed
      ctrl.compilerEtTeleverser(editCode.getText(), getSelectedButtonText(choixArduinoGroupe));
            
    }//GEN-LAST:event_btnTeleverserActionPerformed

    private void itemLeonardoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemLeonardoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemLeonardoActionPerformed

    private void itemSauvegarderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemSauvegarderActionPerformed
        // TODO add your handling code here:
        ctrl.sauvegarder();
    }//GEN-LAST:event_itemSauvegarderActionPerformed

    private void itemOuvrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemOuvrirActionPerformed
        // TODO add your handling code here:
       ouvrirFichier.setCurrentDirectory(new File("./saves"));
       ouvrirFichier.showOpenDialog(this);
    }//GEN-LAST:event_itemOuvrirActionPerformed

    private void itemSauvegarderSousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemSauvegarderSousActionPerformed
       enregistrerFichier.setCurrentDirectory(new File("./saves")); 
       enregistrerFichier.showSaveDialog(this);
    }//GEN-LAST:event_itemSauvegarderSousActionPerformed

    private void itemNouveauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNouveauActionPerformed
        // TODO add your handling code here:
       nouveauFichier.setCurrentDirectory(new File("./saves")); 
       nouveauFichier.showSaveDialog(this);
    }//GEN-LAST:event_itemNouveauActionPerformed

    
     private String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(KitArduinoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KitArduinoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KitArduinoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KitArduinoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KitArduinoFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTeleverser;
    private javax.swing.ButtonGroup choixArduinoGroupe;
    private javax.swing.JTextPane editCode;
    private javax.swing.JFileChooser enregistrerFichier;
    private javax.swing.JRadioButtonMenuItem itemLeonardo;
    private javax.swing.JMenuItem itemNouveau;
    private javax.swing.JMenuItem itemOuvrir;
    private javax.swing.JMenuItem itemQuitter;
    private javax.swing.JMenuItem itemSauvegarder;
    private javax.swing.JMenuItem itemSauvegarderSous;
    private javax.swing.JRadioButtonMenuItem itemUno;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelImgArduino;
    private javax.swing.JList listeBlocs;
    private javax.swing.JMenuBar menuBarre;
    private javax.swing.JMenu menuChoixArduino;
    private javax.swing.JMenu menuEdition;
    private javax.swing.JMenu menuFichier;
    private javax.swing.JMenu menuOutils;
    private javax.swing.JFileChooser nouveauFichier;
    private javax.swing.JFileChooser ouvrirFichier;
    private javax.swing.JPanel panelGraphique;
    private javax.swing.JScrollPane scrollEditCode;
    private javax.swing.JScrollPane scrollListeObjets;
    private javax.swing.JScrollPane scrollPanelGraphique;
    // End of variables declaration//GEN-END:variables
}
