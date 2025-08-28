package org.example;//package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnswersScreen extends JPanel {
    private Poll poll;
    private Question question;
    private int index;
    private Image image;
    private JLabel label;
    private String rightUrl;
    private String leftUrl;
    private JFrame parentWindow;


    public AnswersScreen(int x,int y,int width,int height,Poll poll,JFrame parentWindow,int index) {
        this.poll = poll;
        this.index=index;
        this.parentWindow=parentWindow;
        this.image=new ImageIcon(getClass().getResource("/Images/background.png")).getImage();
        this.setBounds(x,y,width,height);
        this.setVisible(true);
        this.setLayout(null);
        labelBuilder();
        chartBuilder();
    }

    public void labelBuilder() {
        this.question=poll.getQuestions().get(this.index);
        String text = question.toString();
        Font font = new Font("Arial", Font.BOLD, 30);
        label = new JLabel(text);
        label.setFont(font);
        label.setForeground(Color.white);
        FontMetrics metrics = getFontMetrics(font);
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();
        int x = (getWidth() - textWidth) / 2;
        label.setBounds(x, 15, getWidth(), textHeight);
        add(label);
        urlBuilder();
        buttonBuilder();
    }

    public void paintComponent(Graphics g){
        g.drawImage(image,0,0,getWidth(),getHeight(),this);
    }

    private void chartBuilder() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> merged = new HashMap<>();
        for (Option option : question.getOptions()) {
            merged.merge(option.toString(), option.percentageOfQuestion(), Integer::sum);
        }

        // המרה לרשימה לצורך מיון
        ArrayList<Map.Entry<String, Integer>> mergedOptions = new ArrayList<>(merged.entrySet());
        mergedOptions.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        // הכנסת הנתונים ל-dataset
        for (Map.Entry<String, Integer> entry : mergedOptions) {
            dataset.addValue(entry.getValue(), "", entry.getKey());
        }
        JFreeChart chart = ChartFactory.createBarChart("", "options", "votes percentage", dataset);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(50, 50, 50, 200));
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        plot.getRangeAxis().setRange(0, 100);
        renderer.setMaximumBarWidth(0.15);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(100, 90, getWidth() - 200, getHeight() - 150);
        chartPanel.setOpaque(false);
        renderer.setSeriesPaint(0, Color.BLUE);
        add(chartPanel);
    }

    private void buttonBuilder(){
        ImageButton rightButton =new ImageButton(rightUrl);
        rightButton.setBounds(this.getWidth()-120,getHeight()/2-30,120,65);
        rightButton.addActionListener(e -> {
            rightButtonClicked();
        });
        this.add(rightButton);
        ImageButton leftButton =new ImageButton(leftUrl);
        leftButton.setBounds(0,getHeight()/2-30,120,65);
        leftButton.addActionListener(e -> {
            leftButtonClicked();
        });
        this.add(leftButton);
    }

    private void rightButtonClicked(){
        if (index>=poll.getQuestions().size()-1){
            return;
        }
        parentWindow.remove(this);
        parentWindow.add(new AnswersScreen(getX(),getY(),getWidth(),getHeight(),poll,parentWindow,index+1));
        parentWindow.revalidate();
        parentWindow.repaint();
    }

    private void leftButtonClicked(){
        if (index<=0){
            return;
        }
        parentWindow.remove(this);
        parentWindow.add(new AnswersScreen(getX(),getY(),getWidth(),getHeight(),poll,parentWindow,index-1));
        parentWindow.revalidate();
        parentWindow.repaint();
    }

    private void urlBuilder(){
        if (index<=0){
            leftUrl="/Images/grayLeft.png";
        }
        else {
            leftUrl="/Images/left (2).png";
        }
        if (index>=poll.getQuestions().size()-1){
            rightUrl="/images/grayRight.png";
        }
        else {
            rightUrl="/Images/right.png";
        }
    }
}

