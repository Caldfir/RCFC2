package caldfir.df_raw_util.ui;

import java.awt.GridLayout;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileProgressFrame extends JFrame{
  
  private static final Logger LOG = LoggerFactory.getLogger(FileProgressFrame.class);

	private static final long serialVersionUID = 1L;
	//window dimension things
	private static final int FRAME_WIDTH = 256;
	private static final int FRAME_HEIGHT = 128;
	
	private JPanel mainPanel;
	private JProgressBar progressBar;
	private JLabel currentTask;
	
	public FileProgressFrame(String title, int total){
		super(title);
		
		mainPanel = new JPanel(new GridLayout(2,1));
		add(mainPanel);
		
		progressBar = new JProgressBar(0,total);
		mainPanel.add(progressBar);
		
		currentTask = new JLabel("starting...");
		mainPanel.add(currentTask);
		
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public void set(String text, int progress){
	  LOG.debug(text);
		progressBar.setValue(progress);
		currentTask.setText(text);
	}

}
