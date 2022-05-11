package io.trickle.basicgui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class BasicGUI {
   public boolean closed = false;

   public boolean isClosed() {
      return this.closed;
   }

   public BasicGUI() {
      JPanel var1 = new JPanel(new FlowLayout(3, 5, 2));
      Label var2 = new Label("Delay     ");
      TextField var3 = new TextField(10);
      JCheckBox var4 = new JCheckBox("OFF");
      JCheckBox var5 = new JCheckBox("Auto");
      DelayOverrideListener var6 = new DelayOverrideListener(var3, var4);
      var3.addKeyListener(var6);
      var4.addActionListener(var6);
      var5.addActionListener(new AIOverrideListener(var5));
      var1.add(var2);
      var1.add(var3);
      var1.add(var4);
      var1.add(var5);
      JPanel var7 = new JPanel(new FlowLayout(3, 5, 2));
      Label var8 = new Label("Link       ");
      TextField var9 = new TextField(40);
      LinkOverrideListener var10 = new LinkOverrideListener();
      var9.addKeyListener(var10);
      var7.add(var8);
      var7.add(var9);
      JPanel var11 = new JPanel(new FlowLayout(3, 5, 2));
      Label var12 = new Label("Keyword ");
      TextField var13 = new TextField(40);
      JButton var14 = new JButton("Change");
      KeywordOverrideListener var15 = new KeywordOverrideListener(var13);
      var14.addActionListener(var15);
      var11.add(var12);
      var11.add(var13);
      var11.add(var14);
      JPanel var16 = new JPanel(new FlowLayout(3, 5, 2));
      Label var17 = new Label("Password");
      TextField var18 = new TextField(40);
      JButton var19 = new JButton("Change");
      PasswordOverrideListener var20 = new PasswordOverrideListener(var18);
      var19.addActionListener(var20);
      var16.add(var17);
      var16.add(var18);
      var16.add(var19);
      Frame var21 = new Frame("Bot Manager");
      var21.setLayout(new GridLayout(0, 1));
      var21.add(var1);
      var21.add(var7);
      var21.add(var11);
      var21.add(var16);
      var21.setSize(500, 250);
      Dimension var22 = Toolkit.getDefaultToolkit().getScreenSize();
      int var23 = (int)((var22.getWidth() - (double)var21.getWidth()) / Double.longBitsToDouble(4611686018427387904L));
      int var24 = (int)((var22.getHeight() - (double)(var21.getHeight() * 2)) / Double.longBitsToDouble(4611686018427387904L));
      var21.setLocation(var23, var24);
      var21.addWindowListener(new BasicGUI$1(this, var21));
      var21.setVisible(true);
      var21.setResizable(true);
   }
}
