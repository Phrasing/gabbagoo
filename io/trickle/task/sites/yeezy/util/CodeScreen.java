package io.trickle.task.sites.yeezy.util;

import io.trickle.util.concurrent.ContextCompletableFuture;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.concurrent.CompletableFuture;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class CodeScreen extends JPanel implements ActionListener {
   public static String OK = "ok";
   public CompletableFuture result;
   public JTextField inputField;
   public JFrame controllingFrame;
   public static String SKIP = "skip";

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      if ("ok".equals(var2)) {
         String var3 = this.inputField.getText();
         if (var3 != null && !var3.isBlank()) {
            this.result.complete(var3);
            this.controllingFrame.dispatchEvent(new WindowEvent(this.controllingFrame, 201));
         }

         this.inputField.selectAll();
         this.resetFocus();
      } else if ("skip".equals(var2)) {
         this.result.complete("~SKIP~SKIP~");
         this.controllingFrame.dispatchEvent(new WindowEvent(this.controllingFrame, 201));
      }

   }

   public static void lambda$request$0(int var0, String var1, CompletableFuture var2) {
      createAndShow(var0, var1, var2);
   }

   public void resetFocus() {
      this.inputField.requestFocusInWindow();
   }

   public CodeScreen(JFrame var1, String var2, CompletableFuture var3) {
      this.result = var3;
      this.controllingFrame = var1;
      this.inputField = new JTextField(10);
      this.inputField.setActionCommand("ok");
      this.inputField.addActionListener(this);
      JLabel var4 = new JLabel("Enter the code for number " + var2 + ": ");
      var4.setLabelFor(this.inputField);
      JComponent var5 = this.createButtonPanel();
      JPanel var6 = new JPanel(new FlowLayout(4));
      var6.add(var4);
      var6.add(this.inputField);
      this.add(var6);
      this.add(var5);
   }

   public void close() {
      this.controllingFrame.dispatchEvent(new WindowEvent(this.controllingFrame, 201));
   }

   public static CodeScreen createAndShow(int var0, String var1, CompletableFuture var2) {
      JFrame var3 = new JFrame(String.format("TASK-%d || Phone: %s", var0, var1));
      var3.setDefaultCloseOperation(2);
      CodeScreen var4 = new CodeScreen(var3, var1, var2);
      var4.setOpaque(true);
      var3.setContentPane(var4);
      var3.addWindowListener(new CodeScreen$1(var4));
      var3.pack();
      var3.setResizable(false);
      var3.setLocationRelativeTo((Component)null);
      var3.setVisible(true);
      return var4;
   }

   public JComponent createButtonPanel() {
      JPanel var1 = new JPanel(new GridLayout(0, 1));
      JButton var2 = new JButton("OK");
      JButton var3 = new JButton("Skip");
      var2.setActionCommand("ok");
      var3.setActionCommand("skip");
      var2.addActionListener(this);
      var3.addActionListener(this);
      var1.add(var2);
      var1.add(var3);
      return var1;
   }

   public static CompletableFuture request(int var0, String var1) {
      ContextCompletableFuture var2 = new ContextCompletableFuture();
      SwingUtilities.invokeLater(CodeScreen::lambda$request$0);
      return var2;
   }
}
