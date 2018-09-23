package old;

import org.apache.commons.math3.complex.Complex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainWinow {

    private JFrame frame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        Complex a = new Complex(1.0, 1.0);
        Complex b = new Complex(1.0, 1.0);

        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("a.Add(b) = " + a.add(b));
        a = a.add(b);
        System.out.println("a = " + a);

        System.out.println("a = " + a.sqrt().abs());
        System.out.println("a+b = " + a.add(b));

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainWinow window = new MainWinow();
                    window.frame.setTitle("Lab_1");
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
    public MainWinow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(0, 0, 800, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btnLoad = new JButton("Начать");
        btnLoad.setBounds(495, 1, 117, 20);
       /*
        JPanel panel = new JPanel();
        panel.setBounds(0, 21, 450, 620);
        frame.getContentPane().add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(450, 21, 450, 620);
        frame.getContentPane().add(panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(900, 21, 440, 620);
        frame.getContentPane().add(panel_2);
        panel_2.setLayout(new BorderLayout(0, 0));
*/

        JPanel panel = new JPanel();
        panel.setBounds(20, 21, 700, 200);
        frame.getContentPane().add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(20, 241, 700, 200);
        frame.getContentPane().add(panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(20, 451, 700, 200);
        frame.getContentPane().add(panel_2);
        panel_2.setLayout(new BorderLayout(0, 0));
        //panel_2.set(Color.yellow);

        JComboBox comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"Кардио", "Рео", "Вело", "Пила", "Треугольный", "Прямоугольный"}));
        comboBox.setBounds(1, 1, 152, 20);
        frame.getContentPane().add(comboBox);

        JComboBox comboBox_1 = new JComboBox();
        comboBox_1.setModel(new DefaultComboBoxModel(new String[]{"\u0410\u0431\u0441\u043E\u043B\u044E\u0442\u043D\u043E\u0435 \u0437\u043D\u0430\u0447\u0435\u043D\u0438\u0435", "\u0414\u0435\u0439\u0441\u0442\u0432\u0438\u0442\u0435\u043B\u044C\u043D\u0430\u044F \u0447\u0430\u0441\u0442\u044C", "\u041C\u043D\u0438\u043C\u0430\u044F \u0447\u0430\u0441\u0442\u044C"}));
        comboBox_1.setBounds(164, 1, 152, 20);
        frame.getContentPane().add(comboBox_1);

        JComboBox comboBox_2 = new JComboBox();
        comboBox_2.setModel(new DefaultComboBoxModel(new String[]{"\u041E\u0414\u041F\u0424 (\u0444\u0438\u043B\u044C\u0442\u0440\u043E\u0432\u0430\u043D\u043D\u043E\u0435)", "\u0411\u041F\u0424", "\u0411\u041F\u0424n"}));
        comboBox_2.setBounds(330, 1, 152, 20);
        frame.getContentPane().add(comboBox_2);

        btnLoad.addActionListener(new LocalActionListener(comboBox, comboBox_1, comboBox_2, panel, panel_1, panel_2));
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(btnLoad);

        //генерируем
        Helper.siganlSaver(PrimitivesGenerator.getSaw(10., 5., 2., 128), "Пила.txt");
        Helper.siganlSaver(PrimitivesGenerator.getAngle(10., 5., 2., 128), "Треугольный.txt");
        Helper.siganlSaver(PrimitivesGenerator.getLevels(10., 5., 2., 128), "Прямоугольный.txt");
    }

    private class LocalActionListener implements ActionListener {

        JPanel[] panel;
        ArrayList<Complex> signals;
        JComboBox diogrammaComboBox;
        JComboBox paramComboBox;
        JComboBox functionComboBox;
        String diogramma;
        String function;
        String xLable = "Частота";
        ArrayList<Complex> DPF;
        ArrayList<Complex> fSignal;

        LocalActionListener(JComboBox diogrammaComboBox, JComboBox paramComboBox, JComboBox functionComboBox, JPanel... panel) {
            this.panel = panel;
            this.diogrammaComboBox = diogrammaComboBox;
            this.functionComboBox = functionComboBox;
            this.paramComboBox = paramComboBox;
            this.paramComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (signals != null) {
                        updateGraphics(paramComboBox.getSelectedIndex());
                    }
                }
            });

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            diogramma = (String) diogrammaComboBox.getSelectedItem();
            paramComboBox.setSelectedIndex(0);

            function = (String) functionComboBox.getSelectedItem();

            signals = Helper.doubleToComplex(Helper.siganlLoader(diogramma + ".txt"));
            DPF = FourierTransform.DPF(signals);

            switch (functionComboBox.getSelectedIndex()) {
                case 0:
                    fSignal = FourierTransform.ODPF(FourierTransform.filter(DPF, diogrammaComboBox.getSelectedIndex()));
                    break;

                case 1:
                    fSignal = FourierTransform.BPF(signals, true);
                    break;

                case 2:
                    fSignal = FourierTransform.BPFn(signals);
                    break;

                default:
                    break;
            }

            Helper.drawSignal(panel[0], "Оригинал", "Время", "Амплитуда", Helper.getSeries(Helper.complexToDouble(signals, 'r'), diogramma, FourierTransform.FREQUENCY));
            //old.Helper.drawSignal(panel[1], "АЧХ", xLable, "Амплитуда", old.Helper.getSeries(old.Helper.complexToDouble(DPF, 'a'), diogramma, DPF.size() / old.FourierTransform.FREQUENCY));
            Helper.drawSignal(panel[1], "ДПФ", xLable, "Амплитуда", Helper.getSeries(Helper.complexToDouble(DPF, 'i'), diogramma, DPF.size() / FourierTransform.FREQUENCY));
            Helper.drawSignal(panel[2], function, xLable, "Амплитуда", Helper.getSeries(Helper.complexToDouble(fSignal, 'a'), diogramma, fSignal.size() / FourierTransform.FREQUENCY));

        }

        public void updateGraphics(int param) {
            switch (param) {
                case 0:
                    // old.Helper.drawSignal(panel[1], "АЧХ", xLable, "Амплитуда", old.Helper.getSeries(old.Helper.complexToDouble(DPF, 'a'), diogramma, old.FourierTransform.FREQUENCY));
                    Helper.drawSignal(panel[1], "ДПФ", xLable, "Амплитуда", Helper.getSeries(Helper.complexToDouble(DPF, 'a'), diogramma, FourierTransform.FREQUENCY));
                    Helper.drawSignal(panel[2], function, xLable, "Амплитуда", Helper.getSeries(Helper.complexToDouble(fSignal, 'a'), diogramma, FourierTransform.FREQUENCY));
                    break;

                case 1:
                    //old.Helper.drawSignal(panel[1], "АЧХ", xLable, "Амплитуда", old.Helper.getSeries(old.Helper.complexToDouble(DPF, 'r'), diogramma, old.FourierTransform.FREQUENCY));
                    Helper.drawSignal(panel[1], "ДПФ", xLable, "Амплитуда", Helper.getSeries(Helper.complexToDouble(DPF, 'a'), diogramma, FourierTransform.FREQUENCY));
                    Helper.drawSignal(panel[2], function, xLable, "Амплитуда", Helper.getSeries(Helper.complexToDouble(fSignal, 'r'), diogramma, FourierTransform.FREQUENCY));
                    break;

                case 2:
                    //old.Helper.drawSignal(panel[1], "АЧХ", xLable, "Амплитуда", old.Helper.getSeries(old.Helper.complexToDouble(DPF, 'i'), diogramma, old.FourierTransform.FREQUENCY));
                    Helper.drawSignal(panel[1], "ДПФ", xLable, "Амплитуда", Helper.getSeries(Helper.complexToDouble(DPF, 'a'), diogramma, FourierTransform.FREQUENCY));
                    Helper.drawSignal(panel[2], function, xLable, "Амплитуда", Helper.getSeries(Helper.complexToDouble(fSignal, 'i'), diogramma, FourierTransform.FREQUENCY));
                    break;

                default:
                    break;
            }

        }

    }
}
