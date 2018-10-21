package old;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Lab_4 {

    private JFrame frame;

    JSpinner JSfrom;
    JSpinner JSto;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Helper.siganlSaver(PrimitivesGenerator.getSaw(10., 5., 2., 128), "Пила.txt");
                    Helper.siganlSaver(PrimitivesGenerator.getAngle(10., 5., 2., 128), "Треугольный.txt");
                    Helper.siganlSaver(PrimitivesGenerator.getLevels(10., 5., 2., 128), "Прямоугольный.txt");
                    Lab_4 window = new Lab_4();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Lab_4() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 991, 589);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btnLoad = new JButton("Начать");
        btnLoad.setBounds(430, 0, 117, 20);
        JPanel panel = new JPanel();
        panel.setBounds(9, 34, 464, 245);

        frame.getContentPane().add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(9, 290, 464, 245);
        frame.getContentPane().add(panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(483, 34, 464, 245);
        frame.getContentPane().add(panel_2);
        panel_2.setLayout(new BorderLayout(0, 0));

        JPanel panel_3 = new JPanel();
        panel_3.setBounds(483, 290, 464, 245);
        frame.getContentPane().add(panel_3);
        panel_3.setLayout(new BorderLayout(0, 0));

        JComboBox comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"Кардио", "Рео", "Вело", "Пила", "Треугольный", "Прямоугольный"}));
        comboBox.setBounds(1, 1, 122, 20);
        frame.getContentPane().add(comboBox);

        JComboBox comboBox_2 = new JComboBox();
        comboBox_2.setModel(new DefaultComboBoxModel(new String[]{"\u0423\u043E\u043B\u0448", "\u0410\u0434\u0430\u043C\u0430\u0440"}));
        comboBox_2.setBounds(124, 1, 144, 20);
        frame.getContentPane().add(comboBox_2);

        btnLoad.addActionListener(new LocalActionListener(comboBox, comboBox_2, panel, panel_1, panel_2, panel_3));
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(btnLoad);

        JLabel label = new JLabel("\u041E\u0442");
        label.setBounds(270, 4, 19, 14);
        frame.getContentPane().add(label);

        JSfrom = new JSpinner();
        JSfrom.setBounds(290, 1, 59, 20);
        frame.getContentPane().add(JSfrom);

        JLabel label_1 = new JLabel("\u0414\u043E");
        label_1.setBounds(350, 4, 19, 14);
        frame.getContentPane().add(label_1);

        JSto = new JSpinner();
        JSto.setModel(new SpinnerNumberModel(new Integer(7), null, null, new Integer(1)));
        JSto.setBounds(370, 1, 59, 20);
        frame.getContentPane().add(JSto);

    }

    private class LocalActionListener implements ActionListener {

        JPanel[] panel;

        JComboBox diogrammaComboBox;
        JComboBox functionComboBox;
        String diogramma;
        String function;
        String xLable = "Частота";

        ArrayList<Double> signals;
        ArrayList<Double> fSignal;
        ArrayList<Double> rSignal;
        ArrayList<Double> aprox;

        LocalActionListener(JComboBox diogrammaComboBox, JComboBox functionComboBox, JPanel... panel) {
            this.panel = panel;
            this.diogrammaComboBox = diogrammaComboBox;
            this.functionComboBox = functionComboBox;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            int from = (int) JSfrom.getValue(), to = (int) JSto.getValue();

            diogramma = (String) diogrammaComboBox.getSelectedItem();

            function = (String) functionComboBox.getSelectedItem();

            signals = Helper.siganlLoader(diogramma + ".txt");

            switch (functionComboBox.getSelectedIndex()) {
                case 0:
                    fSignal = UolshAdamarHelper.getUolshTransform(signals, false);
                    aprox = UolshAdamarHelper.getAprox(fSignal, from, to);
                    if (diogrammaComboBox.getSelectedIndex() > 2) {
                        rSignal = UolshAdamarHelper.getUolshTransform(aprox, true);
                    } else {
                        rSignal = UolshAdamarHelper.getUolshTransform(FourierTransform.filter(fSignal, diogrammaComboBox.getSelectedIndex()), true);
                    }
                    break;

                case 1:
                    fSignal = UolshAdamarHelper.getAdamarTransform(signals, false);
                    aprox = UolshAdamarHelper.getAprox(fSignal, from, to);
                    if (diogrammaComboBox.getSelectedIndex() > 2) {
                        rSignal = UolshAdamarHelper.getAdamarTransform(aprox, true);
                    } else {
                        rSignal = UolshAdamarHelper.getAdamarTransform(FourierTransform.filter(fSignal, diogrammaComboBox.getSelectedIndex()), true);
                    }
                    //rSignal = UolshAdamarHelper.getAdamarTransform(aprox, true);
                    break;

                default:
                    break;
            }

            Helper.drawSignal(panel[0], "Оригинал", null, null, Helper.getSeries(signals, "", FourierTransform.FREQUENCY));
            Helper.drawSignal(panel[1], "Амплитуда", null, null, Helper.getSeries(UolshAdamarHelper.getAmplitude(fSignal), "", 1));
            Helper.drawSignal(panel[2], "Обратное", null, null, Helper.getSeries(rSignal, "", FourierTransform.FREQUENCY));
            Helper.drawSignal(panel[3], "Фаза", null, null, Helper.getSeries(UolshAdamarHelper.getPhase(fSignal), "", 1));

        }

    }
}
