package Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;



import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends JDialog {

    private final JPanel contentPanel = new JPanel();
    CommandAction command = new CommandAction();
    JButton[] buttons;
    // set для хранения отгаданых кнопок
    Set<JButton> set = new LinkedHashSet<JButton>();
    // list для хранения открытых кнопок
    List<JButton> pair = new ArrayList<JButton>();
    // таймер
    Timer timer = new Timer();
    int size = 0;
    // массив чисел
    Integer arr[];



    /**
     * Create the dialog.
     */
    public Game(Integer arr[], int fields) {
        setModal(true);
        setResizable(false);
        this.arr = arr;
        size = fields * fields;
        setBounds(100, 100, 212, 261);
        contentPanel.setLayout(new GridLayout(fields, 10, fields, 10));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        int w = 0;
        switch (fields) {
            case 4:
                w=100;
                break;
            case 6:
                w=85;
                break;
            case 8:
                w=65;
                break;
            default:
                w=120;
                break;
        }
        Dimension btSize = new Dimension(w, w);
        // массив кнопок
        buttons = new JButton[size];
        Font font = new Font("Courier", Font.BOLD,20);
        // заполним игровое поле
        for (int i = 0; i < (fields * fields); i++) {
            JButton button = new JButton();
            // в actionCommand будем хранить индекс массива
            button.setActionCommand(String.format("%d", i));
            button.setPreferredSize(btSize);
            // добавим слушатель
            button.addActionListener(command);
            button.setText(String.format("%d", arr[i]));
            button.setEnabled(false);
            button.setFont(font);
            buttons[i] = button;
            pair.add(button);
            contentPanel.add(button);
        }
        {
            JPanel buttonPane = new JPanel();
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            {
                JButton btExit = new JButton("Выход");
                btExit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                buttonPane.add(btExit);
                getRootPane().setDefaultButton(btExit);
            }
        }
        //setLocationRelativeTo(null);
        pack();
        setLocationRelativeTo(null);
        // запустим таймер для запоминания позиций кнопок
        timer.schedule(new MyTimerTask(), fields * 500);
    }
    // слушатель нажатия на кнопку
    private class CommandAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {


            // если нажатых кнопок меньше 2х
            if (pair.size() < 2) {
                // получим индекс массива
                String id = e.getActionCommand();
                // получим кнопку которая была нажата
                JButton button = (JButton) e.getSource();
                // выведем число
                button.setText(String.format("%d", arr[Integer.valueOf(id)]));
                if (pair.contains(button)) return;
                // добавим кнопку список открытых кнопок
                pair.add(button);
                // если в списке 2 кнопки
                if (pair.size() == 2) {
                    // проверим равны ли они
                    int one = Integer.valueOf(pair.get(0).getActionCommand());
                    int two = Integer.valueOf(pair.get(1).getActionCommand());
                    // если равны
                    if (arr[one] == arr[two]) {
                        pair.get(0).setEnabled(false);
                        pair.get(1).setEnabled(false);
                        set.add(pair.get(0));
                        set.add(pair.get(1));
                        pair.clear();
                        if (set.size() == size) {
                            JOptionPane.showMessageDialog(null, "Вы выиграли!");
                            return;

                        }
                    } else {
                        // запстим таймен на скрытие надписей через 1 сек
                        if (timer != null)
                            timer.cancel();
                        timer = new Timer();
                        timer.schedule(new MyTimerTask(), 1000);
                    }
                }
            }
        }
    }
    // задача, которая будет "прятать" надписи на кнопках
    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            while (!pair.isEmpty()) {
                JButton button = pair.get(0);
                button.setText("");
                button.setEnabled(true);
                pair.remove(0);
            }

        }

    }
}

