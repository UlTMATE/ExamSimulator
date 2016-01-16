/**
 *
 * @author UlTMATE
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.sql.*;

public class QuestionPan extends JPanel{
    
    JToggleButton toggleBut[];
    JLabel quesLab, ansLab;
    String quesNum, pgNum, correctAns="", userAns="";
    JButton finishBut;
    
    public QuestionPan(String quesNum, String pgNum){
        this.quesNum = quesNum;
        this.pgNum = pgNum;
        createGUI();
    }
    
    public void createGUI(){
        setLayout(new BorderLayout());
        JPanel topPan = new JPanel(new BorderLayout());
        topPan.setBackground(Color.black);
        JLabel pgNumLab = new JLabel("Page:"+pgNum);
        pgNumLab.setForeground(Color.white);
        pgNumLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        topPan.add(pgNumLab, "West");
        
        JPanel botPan = new JPanel();
        JButton prevBut = new JButton("Prev.");
        if(pgNum.equals("1")){
            prevBut.setEnabled(false);
        }
        prevBut.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent axnEve){
                setAnswers();
                Home.cl.previous(Home.centerPan);
            }
        });
        JButton nextBut = new JButton("Next");
        nextBut.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent axnEve){
                setAnswers();
                Home.cl.next(Home.centerPan);
            }
        });
        finishBut = new JButton("Finish");
        finishBut.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent axnEve){
                setAnswers();
                int option = JOptionPane.showConfirmDialog(null, "Are You Sure, You Can't Make Changes After This", "SUBMIT", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(option==JOptionPane.YES_OPTION){
                    Home.showResult();
                }
            }
        });
        finishBut.setVisible(false);
        if(pgNum.equals("10")){
            nextBut.setEnabled(false);
            finishBut.setVisible(true);
        }
        botPan.add(prevBut);
        botPan.add(nextBut);
        botPan.add(finishBut);
        
        JPanel centPan = new JPanel();
        centPan.setLayout(new BoxLayout(centPan, BoxLayout.Y_AXIS));
        try {
            ResultSet rst=Database.getData(quesNum);
            rst.next();
            boolean isMultiple = rst.getString(8).equals("true");
            quesLab = new JLabel("Q "+pgNum+".)" +rst.getString(2));
            quesLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
            ansLab = new JLabel();
            centPan.add(Box.createVerticalStrut(20));
            centPan.add(quesLab);
            centPan.add(Box.createVerticalStrut(3));
            centPan.add(ansLab);
            centPan.add(Box.createVerticalStrut(10));
            
            if (isMultiple) {
                toggleBut = new JCheckBox[4];
                for (int i = 0; i < 4; i++) {
                    toggleBut[i] = new JCheckBox(rst.getString(i + 3));
                    toggleBut[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                    centPan.add(toggleBut[i]);
                    centPan.add(Box.createVerticalStrut(5));
                }
            } else {
                toggleBut = new JRadioButton[4];
                ButtonGroup bg = new ButtonGroup();
                for (int i = 0; i < 4; i++) {
                    toggleBut[i] = new JRadioButton(rst.getString(i + 3));
                    toggleBut[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                    bg.add(toggleBut[i]);
                    centPan.add(toggleBut[i]);
                    centPan.add(Box.createVerticalStrut(5));
                }
            }
            correctAns = rst.getString(7);
            
        } catch (SQLException sqlExc) {
            sqlExc.printStackTrace();
        }
        
        add(topPan, "North");
        add(centPan, "Center");
        add(botPan, "South");
    }
    
    private void setAnswers() {
        userAns="";
        for (int i = 0; i < toggleBut.length; i++) {
            if (toggleBut[i].isSelected()) {
                userAns += " " + correspondingOptionTo(i);
            }
        }
        userAns=userAns.trim();
    }
    
    private String correspondingOptionTo(int i){
        String str="";
        switch(i){
            case 0:
                str="A";
                break;
            case 1:
                str="B";
                break;
            case 2:
                str="C";
                break;
            case 3:
                str="D";
                break;
        }
        return str;
    }
    
    private int correspondingOptionTo(String str){
        int option=0;
        switch(str){
            case "A":
                option=0;
                break;
            case "B":
                option=1;
                break;
            case "C":
                option=2;
                break;
            case "D":
                option=3;
                break;
        }
        return option;
    }
    
    public int enableReviewModeAndGetScore(){
        ArrayList correctAnsList = new ArrayList();
        StringTokenizer stroken = new StringTokenizer(correctAns);
        while(stroken.hasMoreTokens()){
            correctAnsList.add(stroken.nextToken());
        }
        ArrayList userAnsList = new ArrayList();
        stroken = new StringTokenizer(userAns);
        while(stroken.hasMoreTokens()){
            userAnsList.add(stroken.nextToken());
        }
        for(int i=0; i<correctAnsList.size(); i++){
            toggleBut[correspondingOptionTo(correctAnsList.get(i)+"")].setForeground(new Color(40, 128, 43));
        }
        for(int i=0; i<userAnsList.size(); i++){
            if(!correctAnsList.contains(userAnsList.get(i))){
                toggleBut[correspondingOptionTo(userAnsList.get(i)+"")].setForeground(Color.red);
            } 
//                else {
//                toggleBut[correspondingOptionTo(userAnsList.get(i)+"")].setForeground(new Color(40, 128, 43));
//            }
        }
        for(int i=0; i<toggleBut.length; i++){
            toggleBut[i].setEnabled(false);
        }
        ansLab.setText("Ans: " + correctAns + ", UserAns:" + userAns);
        finishBut.setEnabled(false);
        int score=0;
        if(correctAns.equals(userAns)){
            score=10;
        }
        return score;
    }
}
