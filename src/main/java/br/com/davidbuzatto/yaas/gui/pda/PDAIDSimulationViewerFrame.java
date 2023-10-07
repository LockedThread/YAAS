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
package br.com.davidbuzatto.yaas.gui.pda;

import br.com.davidbuzatto.yaas.model.pda.PDA;
import br.com.davidbuzatto.yaas.model.pda.PDAID;
import br.com.davidbuzatto.yaas.util.Utils;
import java.util.List;

/**
 * A viewer of a ID tree for the simulation proccess.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class PDAIDSimulationViewerFrame extends javax.swing.JFrame {

    private int currentSimulationStep;
    private PDAInternalFrame pdaIFrame;
    private PDA pda;
    List<PDASimulationStep> simulationSteps;
    
    /**
     * Creates new form PDAIDSimulationViewerFrame
     */
    public PDAIDSimulationViewerFrame( 
            PDAInternalFrame pdaIFrame, 
            PDA pda,
            List<PDASimulationStep> simulationSteps ) {
        
        this.pdaIFrame = pdaIFrame;
        this.pda = pda;
        this.simulationSteps = simulationSteps;
        
        initComponents();
        setLocationRelativeTo( pdaIFrame );
        setAlwaysOnTop( true );
        
        drawPanel.setPda( pda );
        drawPanel.arrangeAndProccessIdsForSimulation( simulationSteps );
        drawPanel.repaint();
        drawPanel.revalidate();
        
        setCurrentSimulationStep( currentSimulationStep );
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollDrawPanel = new javax.swing.JScrollPane();
        drawPanel = new br.com.davidbuzatto.yaas.gui.pda.PDAIDViewerDrawPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Simulation ID Viewer");
        setIconImage(new javax.swing.ImageIcon( getClass().getResource( "/arrow_right.png" ) ).getImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout drawPanelLayout = new javax.swing.GroupLayout(drawPanel);
        drawPanel.setLayout(drawPanelLayout);
        drawPanelLayout.setHorizontalGroup(
            drawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 798, Short.MAX_VALUE)
        );
        drawPanelLayout.setVerticalGroup(
            drawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 598, Short.MAX_VALUE)
        );

        scrollDrawPanel.setViewportView(drawPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollDrawPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollDrawPanel, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Utils.showInformationMessage( this, "This window will close automatically when you stop the simulation." );
    }//GEN-LAST:event_formWindowClosing
    
    public void setCurrentSimulationStep( int currentSimulationStep ) {
        this.currentSimulationStep = currentSimulationStep;
        for ( PDASimulationStep step : simulationSteps ) {
            step.getId().setActiveInSimulation( false );
        }
        for ( int i = 0; i <= currentSimulationStep; i++ ) {
            simulationSteps.get( i ).getId().setActiveInSimulation( true );
        }
        drawPanel.repaint();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private br.com.davidbuzatto.yaas.gui.pda.PDAIDViewerDrawPanel drawPanel;
    private javax.swing.JScrollPane scrollDrawPanel;
    // End of variables declaration//GEN-END:variables
}
