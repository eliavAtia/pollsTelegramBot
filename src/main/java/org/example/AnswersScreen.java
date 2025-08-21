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
import java.util.List;

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

    public void labelBuilder() {
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
    }


    public void paintComponent(Graphics g){
        g.drawImage(image,0,0,getWidth(),getHeight(),this);
    }

    private void chartBuilder() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Option> options = new ArrayList<>(question.getOptions());
        options.sort((o1, o2) -> Double.compare(o2.percentageOfQuestion(), o1.percentageOfQuestion()));
        for (Option option: options) {
            dataset.addValue(option.percentageOfQuestion(),"",option.toString());
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
        chartPanel.setBounds(90, 90, getWidth() - 200, getHeight() - 150);
        chartPanel.setOpaque(false);
        renderer.setSeriesPaint(0, Color.BLUE);
        add(chartPanel);
    }

}

