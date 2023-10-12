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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.davidbuzatto.yaas.gui.tm;

import br.com.davidbuzatto.yaas.model.tm.TM;
import br.com.davidbuzatto.yaas.model.tm.TMOperation;
import br.com.davidbuzatto.yaas.model.tm.TMTransition;
import br.com.davidbuzatto.yaas.util.CharacterConstants;
import br.com.davidbuzatto.yaas.util.Utils;
import java.awt.Frame;
import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * TMOperation edit dialog.
 * TODO update
 * 
 * @author Prof. Dr. David Buzatto
 */
public class TMEditOperationsDialog extends javax.swing.JDialog {

    private TMInternalFrame tmIFrame;
    private TMTransition transition;
    private TM tm;
    private DefaultListModel<TMOperation> model;
    private boolean transitionRemoved;
    
    /**
     * Creates new form TMEditTransitionOperationDialog
     */
    public TMEditOperationsDialog( Frame parent, TMInternalFrame tmIFrame, 
            TM tm, TMTransition transition, boolean modal ) {
        super( parent, modal );
        this.tmIFrame = tmIFrame;
        this.tm = tm;
        this.transition = transition;
        initComponents();
        customInit();
        setLocationRelativeTo( tmIFrame );
    }

    private void customInit() {
        
        Utils.registerDefaultAndCancelButton( getRootPane(), btnOK, btnCancel );
        
        model = new DefaultListModel<>();
        lstOp.setModel( model );
        
        lblHeader.setText( String.format( "Transition: %s %c %s", 
                transition.getOriginState(), 
                CharacterConstants.ARROW_RIGHT, 
                transition.getTargetState() ) );
        
        try {
            
            for ( TMOperation op : transition.getOperations() ) {
                model.addElement( (TMOperation) op.clone() );
            }
            
        } catch ( CloneNotSupportedException exc ) {
            // should never be reached
            exc.printStackTrace();
        }
        
        setIconImage( new ImageIcon( getClass().getResource( "/transition.png" ) ).getImage() );
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblHeader = new javax.swing.JLabel();
        panelOp = new javax.swing.JPanel();
        scrollOp = new javax.swing.JScrollPane();
        lstOp = new javax.swing.JList<>();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edit Transition Operation(s)");

        lblHeader.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        panelOp.setBorder(javax.swing.BorderFactory.createTitledBorder("Operation(s)"));

        lstOp.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        lstOp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstOpMouseClicked(evt);
            }
        });
        scrollOp.setViewportView(lstOp);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add.png"))); // NOI18N
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pencil.png"))); // NOI18N
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelOpLayout = new javax.swing.GroupLayout(panelOp);
        panelOp.setLayout(panelOpLayout);
        panelOpLayout.setHorizontalGroup(
            panelOpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOpLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelOpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOpLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete))
                    .addComponent(scrollOp))
                .addContainerGap())
        );
        panelOpLayout.setVerticalGroup(
            panelOpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOpLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollOp, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelOpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnEdit)
                    .addComponent(btnAdd))
                .addContainerGap())
        );

        btnOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/accept.png"))); // NOI18N
        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cancel.png"))); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelOp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelOp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        
        TMOperation op = Utils.showInputDialogNewTMOperation( 
                this, "Add Transition Operation", 
                tm.getStackStartingSymbol(), null );

        if ( op != null ) {
            model.addElement( op );
        }
        
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        
        int[] indexes = lstOp.getSelectedIndices();
        
        if ( indexes.length == 0 ) {
            Utils.showErrorMessage( this, "You must select one operation to edit!" );
        } else {
            Utils.showInputDialogNewTMOperation( 
                    this, "Edit Transition Operation", 
                    tm.getStackStartingSymbol(), model.get( indexes[0] ) );
        }
        
        lstOp.updateUI();
        
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        
        int[] indexes = lstOp.getSelectedIndices();
        
        if ( indexes.length == 0 ) {
            Utils.showErrorMessage( this, 
                    "You must select at least one operation to delete!" );
        } else if ( Utils.showConfirmationMessageYesNo(
                this, 
                "Do you really want to delete the selected operation(s)?" ) 
                == JOptionPane.YES_OPTION ) {
            for ( int i = indexes.length-1; i >= 0; i-- ) {
                model.remove( indexes[i] );
            }
        }
        
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        
        Set<TMOperation> newOperations = new HashSet<>();
        
        for ( int i = 0; i < model.size(); i++ ) {
            newOperations.add( model.elementAt( i ) );
        }
        
        if ( newOperations.isEmpty() ) {
            tm.removeTransition( transition );
            transitionRemoved = true;
        } else {
            transition.replaceAllOperations( newOperations );
        }
        
        tm.updateType();
        dispose();
        
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void lstOpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstOpMouseClicked
        if ( evt.getClickCount() >= 2 ) {
            btnEdit.doClick();
        }
    }//GEN-LAST:event_lstOpMouseClicked

    public boolean isTransitionRemoved() {
        return transitionRemoved;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JList<TMOperation> lstOp;
    private javax.swing.JPanel panelOp;
    private javax.swing.JScrollPane scrollOp;
    // End of variables declaration//GEN-END:variables
}
