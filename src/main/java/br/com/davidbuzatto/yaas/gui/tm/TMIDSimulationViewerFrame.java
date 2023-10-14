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
import br.com.davidbuzatto.yaas.model.tm.TMID;
import br.com.davidbuzatto.yaas.util.Utils;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.JScrollBar;

/**
 * A viewer of a ID tree for the simulation proccess.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class TMIDSimulationViewerFrame extends javax.swing.JFrame {

    private int currentSimulationStep;
    private TMInternalFrame tmIFrame;
    private TM tm;
    List<TMSimulationStep> simulationSteps;
    
    /**
     * Creates new form TMIDSimulationViewerFrame
     */
    public TMIDSimulationViewerFrame( 
            TMInternalFrame tmIFrame, 
            TM tm,
            List<TMSimulationStep> simulationSteps ) {
        
        this.tmIFrame = tmIFrame;
        this.tm = tm;
        this.simulationSteps = simulationSteps;
        
        initComponents();
        setLocationRelativeTo( tmIFrame );
        setAlwaysOnTop( true );
        
        drawPanel.setTm( tm );
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
        drawPanel = new br.com.davidbuzatto.yaas.gui.tm.TMIDViewerDrawPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Simulation ID Viewer");
        setIconImage(new javax.swing.ImageIcon( getClass().getResource( "/arrow_right.png" ) ).getImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        drawPanel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                drawPanelMouseWheelMoved(evt);
            }
        });

        javax.swing.GroupLayout drawPanelLayout = new javax.swing.GroupLayout(drawPanel);
        drawPanel.setLayout(drawPanelLayout);
        drawPanelLayout.setHorizontalGroup(
            drawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 338, Short.MAX_VALUE)
        );
        drawPanelLayout.setVerticalGroup(
            drawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        scrollDrawPanel.setViewportView(drawPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollDrawPanel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollDrawPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Utils.showInformationMessage( this, "This window will close automatically when you stop the simulation." );
    }//GEN-LAST:event_formWindowClosing

    private void drawPanelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_drawPanelMouseWheelMoved
        
        JScrollBar sb;

        if ( evt.isShiftDown() ) {
            sb = scrollDrawPanel.getHorizontalScrollBar();
        } else {
            sb = scrollDrawPanel.getVerticalScrollBar();
        }

        if ( evt.getWheelRotation() > 0 ) {
            sb.setValue( sb.getValue() + sb.getBlockIncrement() );
        } else {
            sb.setValue( sb.getValue() - sb.getBlockIncrement() );
        }
        
    }//GEN-LAST:event_drawPanelMouseWheelMoved
    
    public void setCurrentSimulationStep( int currentSimulationStep ) {
        
        this.currentSimulationStep = currentSimulationStep;
        
        for ( TMSimulationStep step : simulationSteps ) {
            step.getId().setActiveInSimulation( false );
        }
        
        for ( int i = 0; i <= currentSimulationStep; i++ ) {
            simulationSteps.get( i ).getId().setActiveInSimulation( true );
        }
        
        if ( this.currentSimulationStep < simulationSteps.size() ) {
            TMID id = simulationSteps.get( this.currentSimulationStep ).getId();
            int tick = drawPanel.getHeight() / simulationSteps.size();
            scrollDrawPanel.getVerticalScrollBar().setValue( tick * ( this.currentSimulationStep - 2 ) );
        }
        
        drawPanel.repaint();
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private br.com.davidbuzatto.yaas.gui.tm.TMIDViewerDrawPanel drawPanel;
    private javax.swing.JScrollPane scrollDrawPanel;
    // End of variables declaration//GEN-END:variables
}
