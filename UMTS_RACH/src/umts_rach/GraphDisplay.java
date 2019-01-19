/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umts_rach;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class GraphDisplay extends JPanel {
String chartTitle,YTitle,XTitle;
Dimension defaultDimension;
   public GraphDisplay(String chartTitle,String YTitle,String XTitle ) {
      this.chartTitle = chartTitle;
      this.YTitle = YTitle;
      this.XTitle = XTitle;
      defaultDimension = new Dimension( 450, 367 );
   }
   public GraphDisplay(String chartTitle,String YTitle,String XTitle,Dimension d ) {
      this.chartTitle = chartTitle;
      this.YTitle = YTitle;
      this.XTitle = XTitle;
      defaultDimension = d;
   }
   public ChartPanel getContent(DefaultCategoryDataset dataSet){
        JFreeChart lineChart = ChartFactory.createLineChart(
         chartTitle,
         XTitle,YTitle,
         dataSet,
         PlotOrientation.VERTICAL,
         true,true,false);
         
      ChartPanel chartPanel = new ChartPanel( lineChart );
      chartPanel.setPreferredSize( defaultDimension );
      return chartPanel;
   }

   public static DefaultCategoryDataset createDataset( ) {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
      dataset.addValue( 15 , "schools" , "1970" );
      dataset.addValue( 30 , "schools" , "1980" );
      dataset.addValue( 60 , "schools" ,  "1990" );
      dataset.addValue( 120 , "schools" , "2000" );
      dataset.addValue( 240 , "schools" , "2010" );
      dataset.addValue( 300 , "schools" , "2014" );
      return dataset;
   }
   
   public static void main( String[ ] args ) {
      GraphDisplay chart = new GraphDisplay(
         "Numer of Schools vs years","NSchool","Years");
      JFrame jframe = new JFrame();
      jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
      jframe.setContentPane(chart.getContent(createDataset()));
      jframe.pack();
      jframe.setVisible(true);
      RefineryUtilities.centerFrameOnScreen( jframe );
   }
}
