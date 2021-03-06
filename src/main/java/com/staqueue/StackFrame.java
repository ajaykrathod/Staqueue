package com.staqueue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

/**
 *
 * @author DELL
 */
public class StackFrame extends JPanel{
    
    boolean isRandom;
    boolean isDynamic;
    Stack choiceStack;
    StackDisplay stackDisplay;
    JPanel stackDisplayPanel;
    StackMenu stackMenu;
    JPanel stackMenuPanel;
    JPanel stackPanel;
    JLabel stackMessage;
    JPanel stackMessagePanel;
    
    StackFrame(int numberOfStacks,boolean isDynamicStack){
        
        super.setBackground(Color.BLACK);
        
        choiceStack = new Stack(3);
        isRandom = false;
        isDynamic = isDynamicStack;
        
        stackPanel = new JPanel(new BorderLayout());
        
        //Stack title and styling
        JLabel stackTitle = new JLabel("<HTML><U>DATA STRUCTURES : DEMONSTRATION OF THE STACK</U></HTML>");
        stackTitle.setFont(new Font("Baskerville Old Face", Font.PLAIN, 28));
        stackTitle.setForeground(Color.yellow);
        stackTitle.setPreferredSize(new Dimension(800, 80));
        
        JPanel stackTitlePanel = new JPanel();
        stackTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        stackTitlePanel.setBackground(Color.darkGray);
        stackTitlePanel.add(stackTitle);
        
        //Stack message panel and styling
        stackMessage = new JLabel(">>>Stack setup has been made as requested. Perform operations from the menu");
        stackMessage.setForeground(Color.ORANGE);
        stackMessage.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        
        stackMessagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        stackMessagePanel.setBackground(Color.BLACK);
        stackMessagePanel.add(stackMessage);
        
        //Stack Menu and styling
        stackMenu = new StackMenu(numberOfStacks);
        stackMenu.setBackground(Color.DARK_GRAY);
        
        stackMenuPanel = new JPanel();
        stackMenuPanel.setBorder(BorderFactory.createEtchedBorder(Color.DARK_GRAY, Color.LIGHT_GRAY));
        stackMenuPanel.setBackground(Color.darkGray);
        stackMenu.setPreferredSize(new Dimension(1200, 100));
        stackMenuPanel.add(stackMenu);
        stackMenuPanel.add(Box.createRigidArea(new Dimension(0, 100)));
        
        Box topDisplay = Box.createVerticalBox();
        topDisplay.add(stackTitlePanel);
        topDisplay.add(stackMessagePanel);
        topDisplay.add(stackMenuPanel);
        
        stackPanel.add(topDisplay, BorderLayout.NORTH);
        
        stackDisplayPanel = new JPanel();
        
            stackDisplay = new StackDisplay(isDynamic);
            stackDisplayPanel = new JPanel();
            stackDisplayPanel.add(stackDisplay);
            stackDisplayPanel.setBorder(BorderFactory.createEtchedBorder(Color.DARK_GRAY, Color.LIGHT_GRAY));

        Box stackDisplayBox = Box.createHorizontalBox();
            stackDisplayBox.add(stackDisplayPanel);
        
        //stackDisplayBox.setPreferredSize(new Dimension(500,500));
        
        stackPanel.add(stackDisplayBox, BorderLayout.CENTER);
        
        super.add(stackPanel);

        //Interface to set the text in the Message Panel
            stackDisplay.textSetter = new TextInterface() {
                public void setText(String str) {
                    stackMessage.setText(">>>" + str);
                }
            };

        addStackActionListeners(); //Function to add the actionlisteners to the buttons
    }
    
     private void addStackActionListeners() {

        //Push Button on click actionlistener
        stackMenu.pushButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String str = stackMenu.text.getText();
                stackMenu.text.setValue(null);
                int selectedStack = 0;
                if (isNum(str)) {
                    str = str.replace(",", "");
                    int num = Integer.parseInt(str);
                    stackDisplay.update_push(num, choiceStack,selectedStack,isDynamic);
                } else {
                    stackMessage.setText(">>>Invalid number. Enter a valid number to be pushed.");
                }
                stackPanel.updateUI();
            }
        });

        //Pop Button on click actionlistener
        stackMenu.popButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selectedStack = 0;
                stackDisplay.update_pop(choiceStack,selectedStack,isDynamic);
                System.out.println(choiceStack.nElts);
                stackPanel.updateUI();
            }
        });

        //Undo Button on click actionlistener
        stackMenu.undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selectedStack = 0;
                if(choiceStack.nElts == 0){
                    stackMessage.setText(">>>Cannot undo any more operations on Stack " + 
                            Integer.toString(selectedStack+1) +
                            ".Perform some other operation and try again.");
                    return;
                }
                int choice = choiceStack.pop();
                System.out.println(choiceStack.nElts);
                if (choice == 0) {
                    //Recent operation was push
                    stackDisplay.undoPush(selectedStack,isDynamic);
                } else {
                    //Recent operation was pop
                    stackDisplay.undoPop(selectedStack,isDynamic);
                }
                stackPanel.updateUI();
            }
        });

        //Random operations Button on click actionlistener
        // stackMenu.randomButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent ae) {
        //         if (!isRandom) {
        //             isRandom = true;
        //             stackMenu.randomButton.setText("Stop");
        //             stackMenu.text.setEnabled(false);
        //             stackMenu.pushButton.setEnabled(false);
        //             stackMenu.popButton.setEnabled(false);
        //             stackMenu.undoButton.setEnabled(false);
        //             stackMenu.resetButton.setEnabled(false);
        //             int selectedStack = 1;
        //             startStackRandom(selectedStack);
        //         } else {
        //             isRandom = false;
        //             stackMenu.randomButton.setText("Random");
        //             stackMenu.text.setEnabled(true);
        //             stackMenu.pushButton.setEnabled(true);
        //             stackMenu.popButton.setEnabled(true);
        //             stackMenu.undoButton.setEnabled(true);
        //             stackMenu.resetButton.setEnabled(true);
        //         }
        //     }
        // });

        //Reset Button on click actionlistener
        stackMenu.resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selectedStack = 1;
                stackDisplay.reset(selectedStack,isDynamic);
                choiceStack.top = -1;
                choiceStack.nElts = 0;
                stackPanel.updateUI();
            }
        });
    }

    //Function to implement automatic random operations in Stack
    private void startStackRandom(int selectedStack) {
        SwingWorker<Void, Void> stk_random = new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception { //Repeatedly performs random operations on a background thread
                Thread.sleep(100);
                stackDisplay.random(choiceStack,selectedStack,isDynamic);
                stackPanel.updateUI();
                Thread.sleep(750);
                return null;
            }

            protected void done() {
                if (isRandom) {
                    startStackRandom(selectedStack);
                }
            }
        };
        stk_random.execute();
    }
    
    //Function to check if the input string is a valid number or not
    private static boolean isNum(String str) {
        str = str.replace(",", "");
        try {
            Integer x = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
