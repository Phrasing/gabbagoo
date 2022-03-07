/*
 * Decompiled with CFR 0.151.
 */
package io.trickle.task.sites.yeezy.util;

import io.trickle.task.sites.yeezy.util.CodeScreen$1;
import io.trickle.util.concurrent.ContextCompletableFuture;
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

public class CodeScreen
extends JPanel
implements ActionListener {
    public JFrame controllingFrame;
    public CompletableFuture<String> result;
    public static String SKIP = "skip";
    public static String OK = "ok";
    public JTextField inputField;

    public CodeScreen(JFrame jFrame, String string, CompletableFuture completableFuture) {
        this.result = completableFuture;
        this.controllingFrame = jFrame;
        this.inputField = new JTextField(10);
        this.inputField.setActionCommand("ok");
        this.inputField.addActionListener(this);
        JLabel jLabel = new JLabel("Enter the code for number " + string + ": ");
        jLabel.setLabelFor(this.inputField);
        JComponent jComponent = this.createButtonPanel();
        JPanel jPanel = new JPanel(new FlowLayout(4));
        jPanel.add(jLabel);
        jPanel.add(this.inputField);
        this.add(jPanel);
        this.add(jComponent);
    }

    public static CompletableFuture request(int n, String string) {
        ContextCompletableFuture contextCompletableFuture = new ContextCompletableFuture();
        SwingUtilities.invokeLater(() -> CodeScreen.lambda$request$0(n, string, contextCompletableFuture));
        return contextCompletableFuture;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String string = actionEvent.getActionCommand();
        if (!"ok".equals(string)) {
            if (!"skip".equals(string)) return;
            this.result.complete("~SKIP~SKIP~");
            this.controllingFrame.dispatchEvent(new WindowEvent(this.controllingFrame, 201));
            return;
        }
        String string2 = this.inputField.getText();
        if (string2 != null && !string2.isBlank()) {
            this.result.complete(string2);
            this.controllingFrame.dispatchEvent(new WindowEvent(this.controllingFrame, 201));
        }
        this.inputField.selectAll();
        this.resetFocus();
    }

    public void close() {
        this.controllingFrame.dispatchEvent(new WindowEvent(this.controllingFrame, 201));
    }

    public static CodeScreen createAndShow(int n, String string, CompletableFuture completableFuture) {
        JFrame jFrame = new JFrame(String.format("TASK-%d || Phone: %s", n, string));
        jFrame.setDefaultCloseOperation(2);
        CodeScreen codeScreen = new CodeScreen(jFrame, string, completableFuture);
        codeScreen.setOpaque(true);
        jFrame.setContentPane(codeScreen);
        jFrame.addWindowListener(new CodeScreen$1(codeScreen));
        jFrame.pack();
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        return codeScreen;
    }

    public JComponent createButtonPanel() {
        JPanel jPanel = new JPanel(new GridLayout(0, 1));
        JButton jButton = new JButton("OK");
        JButton jButton2 = new JButton("Skip");
        jButton.setActionCommand("ok");
        jButton2.setActionCommand("skip");
        jButton.addActionListener(this);
        jButton2.addActionListener(this);
        jPanel.add(jButton);
        jPanel.add(jButton2);
        return jPanel;
    }

    public static void lambda$request$0(int n, String string, CompletableFuture completableFuture) {
        CodeScreen.createAndShow(n, string, completableFuture);
    }

    public void resetFocus() {
        this.inputField.requestFocusInWindow();
    }
}

