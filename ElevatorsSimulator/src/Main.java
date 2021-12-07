
import Interfaces.ElevatorStrategy;

import Logic.DummStrategy;
import Logic.KindStrategy;
import Models.*;
import Views.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    private static final Font defaultFont14 = new Font("Impact",Font.BOLD,14);
    private static final Font defaultFont20 = new Font("Impact",Font.BOLD,20);

    private static final Color textColor = new Color(249, 211, 66);
    private static final Color textColor2 = new Color(255, 255, 255);

    private static final Color backgroundColor = new Color(41, 40, 38);

    private static void Initialize(int floorsNum, int elevatorsNum, int elevatorStrategy,
                                   int elevatorMaxWeight) throws InterruptedException {
        int xMargin = 200, yMargin = 50, floorHeight = 100, elevatorWidth=50,
                passengerWidth = 25, passengerMargin = 10;

        MainWindow mainWindow = MainWindow.getInstance();
        mainWindow.Initialize(floorsNum, elevatorsNum, xMargin,
                yMargin, floorHeight, elevatorWidth, passengerWidth, passengerMargin);

        List<Floor> floors = new CopyOnWriteArrayList<>();
        for (int i = 0; i < mainWindow.getFloorsNum(); ++i){
            Floor f = new Floor();
            f.setY(mainWindow.getWorldHeight() - (i + 1) * mainWindow.getFloorHeight()
                    - i * mainWindow.get_yMargin());
            floors.add(f);
        }

        List<Elevator> elevators = new ArrayList<>();
        BlockingQueue<Passenger> passengersQueue = new LinkedBlockingQueue<>();
        String strategyStr = "";
        for (int i = 0; i < mainWindow.getElevatorsNum(); ++i){
            Elevator e = new Elevator(elevatorMaxWeight, floors.get(0));
            e.setX((mainWindow.get_xMargin() + mainWindow.getElevatorWidth()) * (i + 1));
            e.setY(mainWindow.getWorldHeight() - mainWindow.getFloorHeight());
            ElevatorStrategy strategy;
            if(elevatorStrategy == 0)
                strategy = new DummStrategy(e, new LinkedBlockingQueue<>());
            else
                strategy = new KindStrategy(e, new LinkedBlockingQueue<>());

            e.setStrategy(strategy);
            e.setDoorWidth(1.0 * elevatorWidth / 2);
            elevators.add(e);
        }

        Building building = new Building(elevators, floors, passengersQueue);
        mainWindow.setBuilding(building);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame mainFrame = new JFrame("Elevator simulator");

        mainWindow.setBounds(0, 0 , (int) mainWindow.getWorldWidth(),
                (int) mainWindow.getWorldHeight());
        JPanel mainPanel = new JPanel(null);
        mainPanel.setPreferredSize(new Dimension((int) mainWindow.getWorldWidth(),
                (int) mainWindow.getWorldHeight()));
        mainPanel.add(mainWindow);

        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setAlwaysOnTop(true);
        mainFrame.setResizable(false);
        mainFrame.setSize((int) mainWindow.getWorldWidth(),(int) mainWindow.getWorldHeight() + 40);
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
        mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2,
                dim.height/2-mainFrame.getSize().height/2);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Thread invalidatingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mainPanel.repaint();
                }
            }
        });
        invalidatingThread.start();
        MainWindow.getInstance().getBuilding().runAllThreads();
    }

    public static void main(String[] args) {
        InterfaceInitialization();
    }

    private static void InterfaceInitialization(){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        JFrame startFrame = new JFrame("Settings");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(backgroundColor);

        // to center
        mainPanel.add(Box.createVerticalGlue());

        // spacing
        mainPanel.add(Box.createVerticalStrut(30));

        JLabel title = new JLabel("Enter settings to get started");
        title.setFont(defaultFont20);
        title.setForeground(textColor2);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(title);
        String[] items = {
                "Dumm Strategy",
                "Kind Strategy"
        };

        // spacing
        mainPanel.add(Box.createVerticalStrut(30));

        JPanel jComboBoxPanel = new JPanel();
        jComboBoxPanel.setBackground(backgroundColor);
        jComboBoxPanel.setMaximumSize(new Dimension(400, 100));

        JComboBox<? extends String> jComboBox = new JComboBox<>(items);
        jComboBox.setForeground(textColor2);
        jComboBox.setMinimumSize(new Dimension(30, 30));
        jComboBox.setFont(defaultFont20);
        jComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        jComboBoxPanel.add(jComboBox);

        mainPanel.add(jComboBoxPanel);

        // spacing
        mainPanel.add(Box.createVerticalStrut(30));

        JPanel elevatorsPanel = new JPanel();
        elevatorsPanel.setBackground(backgroundColor);
        elevatorsPanel.setMaximumSize(new Dimension(400, 100));

        JLabel elevators = new JLabel("Elevators: ");
        elevators.setFont(defaultFont20);
        elevators.setPreferredSize(new Dimension(100, 30));
        elevators.setForeground(textColor);
        elevatorsPanel.add(elevators);

        SpinnerModel sm = new SpinnerNumberModel(1, 1, 4, 1);
        JSpinner elevatorsCount = new JSpinner(sm);
        elevatorsCount.setPreferredSize(new Dimension(40, 30));
        elevatorsCount.setFont(defaultFont14);
        elevatorsPanel.add(elevatorsCount);

        mainPanel.add(elevatorsPanel);

        // spacing
        mainPanel.add(Box.createVerticalStrut(5));

        JPanel floorsPanel = new JPanel();
        floorsPanel.setBackground(backgroundColor);
        floorsPanel.setMaximumSize(new Dimension(400, 100));

        JLabel floors = new JLabel("Floors: ");
        floors.setFont(defaultFont20);
        floors.setPreferredSize(new Dimension(100, 30));
        floors.setForeground(textColor);
        floorsPanel.add(floors);

        SpinnerModel smFloors = new SpinnerNumberModel(2, 2, 6, 1);
        JSpinner floorsCount = new JSpinner(smFloors);
        floorsCount.setPreferredSize(new Dimension(40, 30));
        floorsCount.setFont(defaultFont14);
        floorsPanel.add(floorsCount);

        mainPanel.add(floorsPanel);

        // spacing
        mainPanel.add(Box.createVerticalStrut(5));

        JPanel weightPanel = new JPanel();
        weightPanel.setBackground(backgroundColor);
        weightPanel.setMaximumSize(new Dimension(400, 100));

        JLabel weight = new JLabel("Capacity: ");
        weight.setFont(defaultFont20);
        weight.setForeground(textColor);
        weightPanel.add(weight);


        SpinnerModel sw = new SpinnerNumberModel(200, 150, 500, 50);
        JSpinner weightCount = new JSpinner(sw);
        weightCount.setFont(defaultFont14);
        weightPanel.add(weightCount);

        mainPanel.add(weightPanel);

        // spacing
        mainPanel.add(Box.createVerticalStrut(30));

        JPanel goPanel = new JPanel();
        goPanel.setBackground(backgroundColor);
        goPanel.setMaximumSize(new Dimension(400, 100));

        JButton goButton = new CustomButton("GO", new Color(252, 231, 125), new Color(249, 97, 103));
        goButton.setPreferredSize(new Dimension(200, 50));
        goButton.setFocusPainted(false);
        goButton.addActionListener((e) -> {
            try {
                Initialize((Integer)floorsCount.getValue(), (Integer)elevatorsCount.getValue(),
                        jComboBox.getSelectedIndex(), (Integer)weightCount.getValue());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            startFrame.dispose();
        });
        goPanel.add(goButton);
        mainPanel.add(goPanel);

        // to center
        mainPanel.add(Box.createVerticalGlue());

        startFrame.add(mainPanel);
        startFrame.setUndecorated(true);
        startFrame.setSize(400,400);
        startFrame.setVisible(true);
        startFrame.setLocation(dim.width/2-startFrame.getSize().width/2,
                dim.height/2-startFrame.getSize().height/2);
    }
}
