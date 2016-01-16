/**
 *
 * @author UlTMATE
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Home {
    
    static JFrame homeFrame;
    static JPanel centerPan, titlePan;
    static JLabel pageCountLab;
//    JButton prev, next, finish;
    static CardLayout cl;
    int pageCount=1;
    static JMenuBar jmb;
    static ArrayList quesNums;
    
    public Home(){
        new Database();
        createGUI();
    }
    
    public void createGUI(){
        homeFrame = new JFrame("Exam Simulator");
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try{
            for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
                if("Nimbus".equals(info.getName())){
                    UIManager.setLookAndFeel(info.getClassName());
                }
            }
        } catch (Exception exc1){
            try{
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch(Exception exc2){
                homeFrame.dispose();
                new Home();
            }
        }
        
//        titlePan = new JPanel(new BorderLayout());
        jmb = new JMenuBar();
        JMenu start = new JMenu("Start");
        JMenuItem newMI = new JMenuItem("New Test");
        newMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newMI.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent axnEve){
                homeFrame.setVisible(true);
                homeFrame.dispose();
                new Home();
            }
        });
        start.add(newMI);
        jmb.add(start);
        homeFrame.setJMenuBar(jmb);
        centerPan = new JPanel();
        centerPan.setBorder(BorderFactory.createMatteBorder(10,10,10,10, Color.black));
        cl = new CardLayout();
        centerPan.setLayout(cl);
        JPanel startPan = new JPanel(new GridBagLayout());
        startPan.setBackground(Color.black);
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel msgLab = new JLabel("You Can't Score 100%");
        msgLab.setForeground(Color.orange);
        msgLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        JButton startBut = new JButton("Let's See");
        startBut.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent axnEve){
                setUpNewTest();
            }
        });
        startPan.add(msgLab, gbc);
        gbc.gridy=1;
        startPan.add(Box.createRigidArea(new Dimension(2,10)),gbc);
        gbc.gridy=10;
        startPan.add(startBut,gbc);
        centerPan.add(startPan, "start");
        
//        homeFrame.add(titlePan, "North");
        homeFrame.add(centerPan,"Center");
        
        homeFrame.setSize(400,360);
        homeFrame.setResizable(true);
        homeFrame.setLocationRelativeTo(null);
        homeFrame.setVisible(true);
    }
    
    public static void setUpNewTest(){
        jmb.setVisible(false);
        quesNums = new ArrayList(10);
        Random rand = new Random();
        while(quesNums.size()!=10){
            int num = rand.nextInt(101);
            if(!quesNums.contains(num+"") & num!=0){
                quesNums.add(num+"");
            }
        }
        for(int i=0; i<quesNums.size(); i++){
            centerPan.add(new QuestionPan(quesNums.get(i)+"", (i+1)+""), (i+1)+"");
        }
        cl.show(centerPan, "1");
        centerPan.setVisible(true);
            
//            comp = comp.getComponentAt(50,50);
//            System.out.println(comp.getClass());
//        System.out.println(comp.getClass());
//            comp = comp.getComponentAt(50,50);
//            System.out.println(comp.getClass());
//            comp = comp.getComponentAt(50,50);
//            System.out.println(comp.getClass());
    }
    
    public static void showResult(){
        JDialog d = new JDialog(homeFrame, "RESULT", true);
        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        int score = calculateScore();
        JPanel topPan = new JPanel();
        topPan.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JLabel msgLab = new JLabel("You Scored " +score+ "%");
        msgLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        topPan.add(msgLab);
        JPanel centPan = new JPanel(new GridBagLayout());
        centPan.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JProgressBar jpb = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        jpb.setValue(score);
        centPan.add(jpb);
        JPanel botPan = new JPanel();
        botPan.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JButton reviewBut = new JButton("Review");
        reviewBut.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent axnEve){
                centerPan.setVisible(true);
                cl.show(centerPan, "1");
                d.setVisible(false);
                d.dispose();
                jmb.setVisible(true);
            }
        });
        JButton retestBut = new JButton("Retest");
        retestBut.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent axnEve){
                homeFrame.setVisible(false);
                homeFrame.dispose();
                new Home();
                d.setVisible(false);
                d.dispose();
            }
        });
        JButton exitBut = new JButton("Exit");
        exitBut.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent axnEve){
                d.setVisible(false);
                d.dispose();
                homeFrame.setVisible(false);
                homeFrame.dispose();
            }
        });
        botPan.add(reviewBut);
        botPan.add(retestBut);
        botPan.add(exitBut);
        d.add(topPan, "North");
        d.add(centPan, "Center");
        d.add(botPan, "South");
        d.pack();
        d.setLocationRelativeTo(homeFrame);
        d.setVisible(true);
    }
    
    public static int calculateScore(){
        int score=0;
        centerPan.setVisible(true);
        for(int i=0; i<10; i++){
            QuestionPan qp = (QuestionPan) centerPan.getComponent(i + 1);
            score+=qp.enableReviewModeAndGetScore();
//            qp.finishBut.setEnabled(false);
//            for (int k = 0; k < qp.toggleBut.length; k++) {
//                qp.toggleBut[k].setEnabled(false);
//            }
//            qp.ansLab.setText("Ans: " + qp.correctAns + ", UserAns:" + qp.userAns);
//            if (!qp.correctAns.equals(qp.userAns)) {
//                qp.quesLab.setForeground(Color.red);
//            } else {
//                qp.quesLab.setForeground(new Color(40, 128, 43));
//                score += 10;
//            }
        }
        
        return score;
    }
    
    public static void main(String args[]){
        new Home();
    }
}
