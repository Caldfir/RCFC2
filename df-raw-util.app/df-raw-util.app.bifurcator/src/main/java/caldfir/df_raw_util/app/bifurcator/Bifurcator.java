package caldfir.df_raw_util.app.bifurcator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caldfir.df_raw_util.core.config.RelationshipConfig;
import caldfir.df_raw_util.core.parse.TreeBuilder;
import caldfir.df_raw_util.core.primitives.Tag;
import caldfir.df_raw_util.core.relationship.RelationshipMap;

public class Bifurcator extends javax.swing.JFrame {

  private static final Logger LOG = LoggerFactory.getLogger(Bifurcator.class);

  final static String SRC_FOLDER = "in";
  final static String DST_FOLDER = "out";

  private static final long serialVersionUID = 1L;

  /** Creates new form Bifurcator */
  public Bifurcator() {
    initComponents();
  }

  public void bifurcate(Tag lookfor) {

    FileWriter writer = null;
    PrintWriter out;
    TreeBuilder t;
    RelationshipConfig c = new RelationshipConfig();
    RelationshipMap relFileMap = c.buildRelationshipMap();

    String inName = null;
    String shortName;
    String extension;

    String output1 = "";
    String output2 = "";

    for (int i = 0; i < fileList.length; i++) {
      try {
        inName = fileList[i].getName();
        shortName = inName.substring(0, inName.length() - 4);
        extension = inName.substring(inName.length() - 3, inName.length());

        // read and parse
        // display.set("reading " + inName, i);
        t = new TreeBuilder(SRC_FOLDER + "/" + inName, relFileMap);

        if (extension.equals("txt")) {
          Tag root = t.getRoot();
          if (root != null) {

            Tag root1 = new Tag();
            root1.copyTags(root);
            Tag root2 = new Tag();
            root2.copyTags(root);
            output1 = shortName + "\n\n";
            output2 = shortName + "\n\n";

            // display.set("bifurcating " + shortName + ".txt", i);

            for (int j = 0; j < root.getChildCount(); j++) {
              if (root.getChildAt(j).hasChild(lookfor))
                root1.addChild(root.getChildAt(j));
              else
                root2.addChild(root.getChildAt(j));
            }

            // display.set("writing " + shortName + ".txt", i);
            if (root1.getChildCount() > 0) {

              for (int j = 0; j < root1.getChildCount(); j++)
                output1 =
                    output1 + "\t" + root1.getChildAt(j).getArgument(1) + "\n";

              output1 = output1 + "\n" + root1.toRawString();

              // write1
              File f = new File(DST_FOLDER + "/Y/" + shortName + ".txt");
              // f.mkdirs();
              writer = new FileWriter(f);
              out = new PrintWriter(writer);
              out.print(output1);

              out.close();
              writer.close();

            }

            // display.set("writing " + shortName + ".txt", i);
            if (root2.getChildCount() > 0) {

              for (int j = 0; j < root2.getChildCount(); j++)
                output2 =
                    output2 + "\t" + root2.getChildAt(j).getArgument(1) + "\n";

              output2 = output2 + "\n" + root2.toRawString();

              // write2
              File f = new File(DST_FOLDER + "/N/" + shortName + ".txt");
              // f.mkdirs();
              writer = new FileWriter(f);
              out = new PrintWriter(writer);
              out.print(output2);

              out.close();
              writer.close();
            }
          } else {
            LOG.error("invalid or empty file: " + shortName + ".txt");
          }
        }
      } catch (IOException e0) {
        e0.printStackTrace();
      }
    }
    // display.setVisible(false);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */

  // <editor-fold defaultstate="collapsed" desc="Generated Code">
  private void initComponents() {

    folder = new File(SRC_FOLDER);
    fileList = folder.listFiles();

    jLabel1 = new javax.swing.JLabel();
    jTextField1 = new javax.swing.JTextField();
    jButton1 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jLabel1.setText("please enter the tag to filter on:");

    jTextField1.setText("[BIOME:ANY_LAND]");
    jTextField1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jTextField1ActionPerformed(evt);
      }
    });

    jButton1.setText("search");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout =
        new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        layout
                            .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.TRAILING,
                                false)
                            .addComponent(
                                jTextField1,
                                javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(
                                jLabel1,
                                javax.swing.GroupLayout.Alignment.LEADING,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE))
                    .addContainerGap())
            .addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addComponent(
                        jButton1,
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        155,
                        Short.MAX_VALUE)
                    .addContainerGap()));
    layout.setVerticalGroup(
        layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addPreferredGap(
                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(
                        jTextField1,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(
                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton1)
                    .addContainerGap(
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)));

    pack();
  }// </editor-fold>

  private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
  }

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
    Tag lookfor = Tag.rawTag(jTextField1.getText());
    bifurcate(lookfor);
    this.setVisible(false);
    System.exit(0);
  }

  /**
   * @param args
   *          the command line arguments
   */
  public static void main(String args[]) {

    /* Set the Nimbus look and feel */
    // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
    // (optional) ">
    /*
     * If Nimbus (introduced in Java SE 6) is not available, stay with the
     * default look and feel. For details see
     * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
          .getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      LOG.error(ex.toString());
    } catch (InstantiationException ex) {
      LOG.error(ex.toString());
    } catch (IllegalAccessException ex) {
      LOG.error(ex.toString());
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      LOG.error(ex.toString());
    }
    // </editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {

      public void run() {
        new Bifurcator().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JTextField jTextField1;

  private File folder;
  private File[] fileList;
  // End of variables declaration
}
