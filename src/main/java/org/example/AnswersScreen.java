package org.example;//package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class AnswersScreen extends JPanel {
    private Question question;
    private Image image;
    private JLabel label;


    public AnswersScreen(int x,int y,int width,int height,Question question) {
        this.question = question;
        this.image=new ImageIcon(getClass().getResource("/Images/background.png")).getImage();
        this.setBounds(x,y,width,height);
        this.setVisible(true);
        this.setLayout(null);
        labelBuilder();
        chartBuilder();
    }

    public void labelBuilder(){
        label = new JLabel("Question: " + question);
        label.setBounds(getWidth()/2-175,25,350,100);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.white);
        add(label);
    }

    public void paintComponent(Graphics g){
        g.drawImage(image,0,0,getWidth(),getHeight(),this);
    }

    private void chartBuilder() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Option option: question.getOptions()) {
            dataset.addValue(option.percentageOfQuestion(),"",option.toString());
        }
        JFreeChart chart = ChartFactory.createBarChart("", "options", "votes percentage", dataset);
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        plot.getRangeAxis().setRange(0, 100);
        renderer.setMaximumBarWidth(0.2);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(75, 100, getWidth() - 200, getHeight() - 150);
        chartPanel.setOpaque(false);
        renderer.setSeriesPaint(0, Color.BLUE);
        add(chartPanel);
    }

}

