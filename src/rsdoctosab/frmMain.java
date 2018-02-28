package rsdoctosab;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import rsdoctosab.Converter.Converter_RsDocToSab;
import rsdoctosab.FileDrop.FileDrop;
import rsdoctosab.UI.UITools.Position2D;
import rsdoctosab.UI.UITools.UITools;

/**
 *
 * @author Nikos Siatras
 */
public class frmMain extends javax.swing.JFrame
{

    private FileDrop fFileDrop;
    private final Converter_RsDocToSab fConverter = new Converter_RsDocToSab();

    public frmMain()
    {
        initComponents();

        Position2D pos = UITools.getPositionForFormToOpenInMiddleOfScreen(super.getSize().width, super.getSize().height);
        super.setLocation((int) pos.getX(), (int) pos.getY());

        Initialize();
    }

    private void Initialize()
    {
        fFileDrop = new FileDrop(jPanelFileDrop, (java.io.File[] files) ->
        {
            for (java.io.File f : files)
            {
                // Convert file to .sab
                fConverter.Convert(f);

                // Get rsDoc dir path
                String rsDocDirPath = f.getParent();
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanelFileDrop = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(".rsdoc to .sab Converter");

        jPanelFileDrop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setText("Drop .rsdoc here !");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rsdoctosab/UI/Images/DSM_ICON_32to64-5.png"))); // NOI18N
        jLabel3.setToolTipText("");

        javax.swing.GroupLayout jPanelFileDropLayout = new javax.swing.GroupLayout(jPanelFileDrop);
        jPanelFileDrop.setLayout(jPanelFileDropLayout);
        jPanelFileDropLayout.setHorizontalGroup(
            jPanelFileDropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFileDropLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102))
            .addGroup(jPanelFileDropLayout.createSequentialGroup()
                .addGap(159, 159, 159)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelFileDropLayout.setVerticalGroup(
            jPanelFileDropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFileDropLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        jLabel1.setText("<html> \t<p>Drag and Drop the <b>.rsdoc</b> files made by DesignSpark Mechanical on the above area and </p> \t<p><b>.sab</b> files will be generated in the same folder as the .rsdoc</p> </html>");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelFileDrop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelFileDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[])
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
        {
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() ->
        {
            new frmMain().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanelFileDrop;
    // End of variables declaration//GEN-END:variables
}
