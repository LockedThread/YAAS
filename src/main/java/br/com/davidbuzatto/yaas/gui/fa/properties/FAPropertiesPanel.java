/*
 * Copyright (C) 2023 Prof. Dr. David Buzatto
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.davidbuzatto.yaas.gui.fa.properties;

import br.com.davidbuzatto.yaas.gui.fa.FA;
import br.com.davidbuzatto.yaas.gui.fa.FAInternalFrame;
import br.com.davidbuzatto.yaas.gui.fa.FAType;
import br.com.davidbuzatto.yaas.gui.fa.FAFormalDefinitionDialog;
import javax.swing.JOptionPane;

/**
 * Finite Automaton properties edit/visualization panel.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class FAPropertiesPanel extends javax.swing.JPanel {

    private FAInternalFrame faIFrame;
    
    private FA fa;
    
    /**
     * Creates new form FAPropertiesPanel
     */
    public FAPropertiesPanel( FAInternalFrame faIFrame ) {
        
        this.faIFrame = faIFrame;
        
        initComponents();
        customInit();
        
    }
    
    private void customInit() {
    }

    public void setFa( FA fa ) {
        this.fa = fa;
    }

    public void readProperties() {
        if ( fa.getType() == FAType.EMPTY ) {
            txtFAType.setText( "" );
            txtFAType.setToolTipText( "" );
        } else {
            txtFAType.setText( fa.getType().getAcronym() );
            txtFAType.setToolTipText( fa.getType().getDescription() );
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblProperties = new javax.swing.JPanel();
        lblFAType = new javax.swing.JLabel();
        txtFAType = new javax.swing.JTextField();
        btnFormalDefinition = new javax.swing.JButton();

        lblFAType.setText("FA Type:");

        txtFAType.setEnabled(false);
        txtFAType.setFocusable(false);

        javax.swing.GroupLayout lblPropertiesLayout = new javax.swing.GroupLayout(lblProperties);
        lblProperties.setLayout(lblPropertiesLayout);
        lblPropertiesLayout.setHorizontalGroup(
            lblPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblPropertiesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFAType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFAType, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                .addContainerGap())
        );
        lblPropertiesLayout.setVerticalGroup(
            lblPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblPropertiesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lblPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFAType)
                    .addComponent(txtFAType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnFormalDefinition.setText("Formal Definition");
        btnFormalDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFormalDefinitionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProperties, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnFormalDefinition)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblProperties, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFormalDefinition)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnFormalDefinitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFormalDefinitionActionPerformed
        
        if ( fa.getStates().isEmpty() ) {
            JOptionPane.showMessageDialog( 
                    faIFrame, 
                    "First add at least one state!", 
                    "Warning", 
                    JOptionPane.WARNING_MESSAGE );
        } else if ( fa.getInitialState() == null ) {
            JOptionPane.showMessageDialog( 
                    faIFrame, 
                    "Set the initial state!", 
                    "Warning", JOptionPane.WARNING_MESSAGE );
        } else {
            FAFormalDefinitionDialog d = new FAFormalDefinitionDialog( null, true, fa );
            d.setVisible( true );
        }
        
    }//GEN-LAST:event_btnFormalDefinitionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFormalDefinition;
    private javax.swing.JLabel lblFAType;
    private javax.swing.JPanel lblProperties;
    private javax.swing.JTextField txtFAType;
    // End of variables declaration//GEN-END:variables
}
