package Client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Remote.GenerateService;

public class MainFrame extends JFrame {
    static final String SERVICE_NAME = "processing";

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainFrame() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 230);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Пазл");
        lblNewLabel.setBounds(140, 13, 163, 22);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        contentPane.add(lblNewLabel);

        JComboBox<Select> chField = new JComboBox();
        chField.setBounds(185, 76, 73, 22);
        contentPane.add(chField);
        chField.setModel(new DefaultComboBoxModel(new Select[] { new Select(2,"2x2"),new Select(4,"4x4"),
                new Select(6,"6x6"),new Select(8, "8x8") }));

        JButton btStart = new JButton("Начать");
        btStart.setBounds(168, 126, 108, 35);
        contentPane.add(btStart);

        JLabel lblNewLabel_1 = new JLabel("Выберите размер поля");
        lblNewLabel_1.setBounds(154, 47, 156, 16);
        contentPane.add(lblNewLabel_1);

        JButton btInfo = new JButton("i");
        btInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    InfoDialog dialog = new InfoDialog();
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btInfo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
        btInfo.setBounds(295, 131, 57, 25);
        contentPane.add(btInfo);
        btStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Select select = (Select) chField.getSelectedItem();
                int fields = select.getSize();
                try {
                    GenerateService generator = (GenerateService) Naming.lookup("//localhost/" + SERVICE_NAME);
                    Integer[] arr = generator.gameFields(fields);
                    Game dialog = new Game(arr, fields);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (MalformedURLException | RemoteException | NotBoundException e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null,"Ошибка! Не удалось установить соединение с сервером.");
                    e1.printStackTrace();
                }

            }
        });
        setLocationRelativeTo(null);

    }

    public Integer[] generate(int field) {
        Random random = new Random();
        Set<Integer> set = new HashSet();
        int limit = (field * field) / 2;
        while (set.size() != limit) {
            set.add(random.nextInt(99) + 1);
        }
        List<Integer> inList = new ArrayList<>();
        inList.addAll(set);
        inList.addAll(set);
        Integer[] res = new Integer[inList.size()];
        Collections.shuffle(inList);
        inList.toArray(res);
        return res;
    }

    private class Select {
        int size;
        String info;
        public Select(int size, String info) {
            this.size = size;
            this.info = info;
        }
        public String getInfo() {
            return info;
        }
        public int getSize() {
            return size;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return info;
        }

    }
}