/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.trickle.basicgui.AIOverrideListener
 *  io.trickle.basicgui.BasicGUI$1
 *  io.trickle.basicgui.DelayOverrideListener
 *  io.trickle.basicgui.KeywordOverrideListener
 *  io.trickle.basicgui.LinkOverrideListener
 *  io.trickle.basicgui.PasswordOverrideListener
 */
package io.trickle.basicgui;

import io.trickle.basicgui.AIOverrideListener;
import io.trickle.basicgui.BasicGUI;
import io.trickle.basicgui.DelayOverrideListener;
import io.trickle.basicgui.KeywordOverrideListener;
import io.trickle.basicgui.LinkOverrideListener;
import io.trickle.basicgui.PasswordOverrideListener;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class BasicGUI {
    public boolean closed = false;

    public boolean isClosed() {
        return this.closed;
    }

    public BasicGUI() {
        JPanel jPanel = new JPanel(new FlowLayout(3, 5, 2));
        Label label = new Label("Delay     ");
        TextField textField = new TextField(10);
        JCheckBox jCheckBox = new JCheckBox("OFF");
        JCheckBox jCheckBox2 = new JCheckBox("Auto");
        DelayOverrideListener delayOverrideListener = new DelayOverrideListener(textField, jCheckBox);
        textField.addKeyListener((KeyListener)delayOverrideListener);
        jCheckBox.addActionListener((ActionListener)delayOverrideListener);
        jCheckBox2.addActionListener((ActionListener)new AIOverrideListener(jCheckBox2));
        jPanel.add(label);
        jPanel.add(textField);
        jPanel.add(jCheckBox);
        jPanel.add(jCheckBox2);
        JPanel jPanel2 = new JPanel(new FlowLayout(3, 5, 2));
        Label label2 = new Label("Link       ");
        TextField textField2 = new TextField(40);
        LinkOverrideListener linkOverrideListener = new LinkOverrideListener();
        textField2.addKeyListener((KeyListener)linkOverrideListener);
        jPanel2.add(label2);
        jPanel2.add(textField2);
        JPanel jPanel3 = new JPanel(new FlowLayout(3, 5, 2));
        Label label3 = new Label("Keyword ");
        TextField textField3 = new TextField(40);
        JButton jButton = new JButton("Change");
        KeywordOverrideListener keywordOverrideListener = new KeywordOverrideListener(textField3);
        jButton.addActionListener((ActionListener)keywordOverrideListener);
        jPanel3.add(label3);
        jPanel3.add(textField3);
        jPanel3.add(jButton);
        JPanel jPanel4 = new JPanel(new FlowLayout(3, 5, 2));
        Label label4 = new Label("Password");
        TextField textField4 = new TextField(40);
        JButton jButton2 = new JButton("Change");
        PasswordOverrideListener passwordOverrideListener = new PasswordOverrideListener(textField4);
        jButton2.addActionListener((ActionListener)passwordOverrideListener);
        jPanel4.add(label4);
        jPanel4.add(textField4);
        jPanel4.add(jButton2);
        Frame frame = new Frame("Bot Manager");
        frame.setLayout(new GridLayout(0, 1));
        frame.add(jPanel);
        frame.add(jPanel2);
        frame.add(jPanel3);
        frame.add(jPanel4);
        frame.setSize(500, 250);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int n = (int)((dimension.getWidth() - (double)frame.getWidth()) / Double.longBitsToDouble(0x4000000000000000L));
        int n2 = (int)((dimension.getHeight() - (double)(frame.getHeight() * 2)) / Double.longBitsToDouble(0x4000000000000000L));
        frame.setLocation(n, n2);
        frame.addWindowListener((WindowListener)new 1(this, frame));
        frame.setVisible(true);
        frame.setResizable(true);
    }
}
