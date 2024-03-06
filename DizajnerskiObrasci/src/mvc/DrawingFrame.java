package mvc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import exception.RadiusException;
import net.miginfocom.swing.MigLayout;
import shapes.Shape;
import javax.swing.JButton;
import javax.swing.JColorChooser;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.TextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class DrawingFrame extends JFrame {
	
	private DrawingView view = new DrawingView();
	private DrawingController controller;
	
	
	
	private JToggleButton tglbtnModify;
	private JToggleButton tglbtnSelect;
	private JToggleButton tglbtnDelete;
	private JPanel contentPane;
	private int state = 0;
	private JButton btnInsideColor;
	private JButton btnOutsideColor;
	private JButton btnUndo;
	private JButton btnRedo;
	private JTextArea textAreaLog;
	private JButton btnLoadNext;
	private JMenuItem mntOpenLog;
	private JMenu mnFile;
	private JMenuItem mntSaveLog;
	private JMenuItem mntSavePainting;
	private JMenuItem mntOpenPainting;
	private JButton btnFront;
	private JButton btnToFront;
	private JButton btnToBack;
	private JButton btnBack;
	/**
	 * Create the frame.
	 */
	
	/*Icnos*/
	
	public DrawingFrame() {
		view.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.mouseClicked(e);
			}
		});
		setTitle("Dimitrije Corlija IT24-2018");
		view.setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
	    mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntSaveLog = new JMenuItem("Save log");
		mntSaveLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.saveLog();
			}
		});
		mnFile.add(mntSaveLog);
		
		
		mntOpenLog = new JMenuItem("Open log");
		mntOpenLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					controller.openLog();
				} catch (IOException ioe)
				{
					ioe.printStackTrace();
				} 
			}
		});
		mnFile.add(mntOpenLog);
		
		mntSavePainting = new JMenuItem("Save painting");
		mntSavePainting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					controller.savePainting();
				} catch (IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
		});
		mnFile.add(mntSavePainting);
		
		mntOpenPainting = new JMenuItem("Open painting");
		mntOpenPainting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					controller.openPainting();
				}  catch (ClassNotFoundException | IOException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		mnFile.add(mntOpenPainting);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		ButtonGroup group = new ButtonGroup();

		JPanel pnlNorth = new JPanel();
		contentPane.add(pnlNorth, BorderLayout.NORTH);
				pnlNorth.setLayout(new BorderLayout(0, 0));
		
				JLabel lblNaslov = new JLabel("Drawing");
				lblNaslov.setFont(new Font("Courier New", Font.BOLD, 20));
				lblNaslov.setHorizontalAlignment(SwingConstants.CENTER);
				pnlNorth.add(lblNaslov);
//-----------------------------WEST-----------------------------------------------------------

		JPanel pnlWest = new JPanel();
		pnlWest.setForeground(Color.RED);
		contentPane.add(pnlWest, BorderLayout.WEST);
		GridBagLayout gbl_pnlWest = new GridBagLayout();
		gbl_pnlWest.columnWidths = new int[]{126, 0};
		gbl_pnlWest.rowHeights = new int[]{23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 0};
		gbl_pnlWest.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_pnlWest.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		pnlWest.setLayout(gbl_pnlWest);
		
				JRadioButton rdbRectangle = new JRadioButton("Rectangle");
				group.add(rdbRectangle);
				rdbRectangle.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						state = 4;
					}
				});
				
						JRadioButton rdbPoint = new JRadioButton("Point");
						group.add(rdbPoint);
						rdbPoint.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								state = 1;
							}
						});
						GridBagConstraints gbc_rdbPoint = new GridBagConstraints();
						gbc_rdbPoint.anchor = GridBagConstraints.WEST;
						gbc_rdbPoint.insets = new Insets(0, 0, 5, 0);
						gbc_rdbPoint.gridx = 0;
						gbc_rdbPoint.gridy = 0;
						pnlWest.add(rdbPoint, gbc_rdbPoint);
				
						JRadioButton rdbLine = new JRadioButton("Line");
						group.add(rdbLine);
						rdbLine.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								state = 2;
							}
						});
						
						GridBagConstraints gbc_rdbLine = new GridBagConstraints();
						gbc_rdbLine.anchor = GridBagConstraints.WEST;
						gbc_rdbLine.insets = new Insets(0, 0, 5, 0);
						gbc_rdbLine.gridx = 0;
						gbc_rdbLine.gridy = 1;
						pnlWest.add(rdbLine, gbc_rdbLine);
				GridBagConstraints gbc_rdbRectangle = new GridBagConstraints();
				gbc_rdbRectangle.anchor = GridBagConstraints.WEST;
				gbc_rdbRectangle.insets = new Insets(0, 0, 5, 0);
				gbc_rdbRectangle.gridx = 0;
				gbc_rdbRectangle.gridy = 2;
				pnlWest.add(rdbRectangle, gbc_rdbRectangle);
		
		JRadioButton rdbHexagon = new JRadioButton("Hexagon");
		rdbHexagon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				state=7;
			}
		});
		
				JRadioButton rdbCircle = new JRadioButton("Circle");
				rdbCircle.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						state = 3;
					}
				});
				group.add(rdbCircle);
				GridBagConstraints gbc_rdbCircle = new GridBagConstraints();
				gbc_rdbCircle.anchor = GridBagConstraints.WEST;
				gbc_rdbCircle.insets = new Insets(0, 0, 5, 0);
				gbc_rdbCircle.gridx = 0;
				gbc_rdbCircle.gridy = 3;
				pnlWest.add(rdbCircle, gbc_rdbCircle);
		
				JRadioButton rdbDonut = new JRadioButton("Donut");
				group.add(rdbDonut);
				rdbDonut.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						state = 5;
					}
				});
				
					GridBagConstraints gbc_rdbDonut = new GridBagConstraints();
					gbc_rdbDonut.anchor = GridBagConstraints.WEST;
					gbc_rdbDonut.insets = new Insets(0, 0, 5, 0);
					gbc_rdbDonut.gridx = 0;
					gbc_rdbDonut.gridy = 4;
					pnlWest.add(rdbDonut, gbc_rdbDonut);
		group.add(rdbHexagon);
		GridBagConstraints gbc_rdbHexagon = new GridBagConstraints();
		gbc_rdbHexagon.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbHexagon.insets = new Insets(0, 0, 5, 0);
		gbc_rdbHexagon.gridx = 0;
		gbc_rdbHexagon.gridy = 5;
		pnlWest.add(rdbHexagon, gbc_rdbHexagon);
		

	   tglbtnSelect = new JToggleButton("Select");
	   GridBagConstraints gbc_tglbtnSelect = new GridBagConstraints();
	   gbc_tglbtnSelect.fill = GridBagConstraints.HORIZONTAL;
	   gbc_tglbtnSelect.insets = new Insets(0, 0, 5, 0);
	   gbc_tglbtnSelect.gridx = 0;
	   gbc_tglbtnSelect.gridy = 6;
	   pnlWest.add(tglbtnSelect, gbc_tglbtnSelect);
	   group.add(tglbtnSelect);
		
				btnUndo = new JButton("Undo");
				btnUndo.setEnabled(false);
				btnUndo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						controller.undoShape();
						btnRedo.setEnabled(true);
						btnLoadNext.setEnabled(false);
						if((controller.getCmdList().getCurrent())==0) {
					btnUndo.setEnabled(false);
					btnLoadNext.setEnabled(false);
					view.repaint();
				}
						
					}
				});
				
						tglbtnDelete = new JToggleButton("Delete");
						tglbtnDelete.setEnabled(false);
						tglbtnDelete.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								controller.delete();
								
							}
						});
						
						tglbtnModify = new JToggleButton("Modify");
						tglbtnModify.setEnabled(false);
						tglbtnModify.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
										controller.modifyShape();
								}
							});
						
							GridBagConstraints gbc_tglbtnModify = new GridBagConstraints();
							gbc_tglbtnModify.anchor = GridBagConstraints.NORTHWEST;
							gbc_tglbtnModify.insets = new Insets(0, 0, 5, 0);
							gbc_tglbtnModify.gridx = 0;
							gbc_tglbtnModify.gridy = 7;
							pnlWest.add(tglbtnModify, gbc_tglbtnModify);
							group.add(tglbtnModify);
						GridBagConstraints gbc_tglbtnDelete = new GridBagConstraints();
						gbc_tglbtnDelete.anchor = GridBagConstraints.NORTH;
						gbc_tglbtnDelete.fill = GridBagConstraints.HORIZONTAL;
						gbc_tglbtnDelete.insets = new Insets(0, 0, 5, 0);
						gbc_tglbtnDelete.gridx = 0;
						gbc_tglbtnDelete.gridy = 8;
						pnlWest.add(tglbtnDelete, gbc_tglbtnDelete);
						group.add(tglbtnDelete);
				GridBagConstraints gbc_btnUndo = new GridBagConstraints();
				gbc_btnUndo.anchor = GridBagConstraints.NORTHWEST;
				gbc_btnUndo.insets = new Insets(0, 0, 5, 0);
				gbc_btnUndo.gridx = 0;
				gbc_btnUndo.gridy = 9;
				pnlWest.add(btnUndo, gbc_btnUndo);
		
				btnRedo = new JButton("Redo");
				btnRedo.setEnabled(false);
				btnRedo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						controller.redoShape();
						btnUndo.setEnabled(true);
						if(controller.getCmdList().getCurrent()-1==controller.getCmdList().getList().size()-1)
						{
							btnRedo.setEnabled(false);
							if(controller.isloadLogEnd()) {
								btnLoadNext.setEnabled(false);
							} else {
								btnLoadNext.setEnabled(true);
							}
						}
					}
				});
				GridBagConstraints gbc_btnRedo = new GridBagConstraints();
				gbc_btnRedo.anchor = GridBagConstraints.NORTHWEST;
				gbc_btnRedo.gridx = 0;
				gbc_btnRedo.gridy = 10;
				pnlWest.add(btnRedo, gbc_btnRedo);
		

				


		JPanel pnlCentar = new JPanel();
		contentPane.add(view, BorderLayout.CENTER);

		view.setSize(new Dimension(20,40));
		view.setPreferredSize(new Dimension(200,400));
		contentPane.add(view);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.EAST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		  
		  btnBack = new JButton("Back");
		  btnBack.setEnabled(false);
		  btnBack.addActionListener(new ActionListener() {
		  	public void actionPerformed(ActionEvent e) {
		  		controller.Back();
		  	
		  	}
		  });
		 
		  btnFront = new JButton("Front");
		  btnFront.setEnabled(false);
		  btnFront.addActionListener(new ActionListener() {
		  	public void actionPerformed(ActionEvent e) {
		  		controller.Front();
		  	}
		  });
		  
		   btnOutsideColor = new JButton("Outside color");
		   btnOutsideColor.setBackground(Color.BLACK);
		   btnOutsideColor.setForeground(Color.WHITE);
		   btnOutsideColor.addActionListener(new ActionListener() {
		   	public void actionPerformed(ActionEvent e) {
		   		btnOutsideColor.setBackground(JColorChooser.showDialog(null, "Chose outside color", Color.RED));
		   		if(btnOutsideColor.getBackground().equals(Color.BLACK)) {
		   			btnOutsideColor.setForeground(Color.WHITE);
		   		}
		   	}
		   });
		   
		   btnInsideColor = new JButton("Inside Color");
		   btnInsideColor.setBackground(Color.WHITE);
		   btnInsideColor.setForeground(Color.BLACK);
		   btnInsideColor.addActionListener(new ActionListener() {
		   	public void actionPerformed(ActionEvent e) {
		   	btnInsideColor.setBackground(JColorChooser.showDialog(null, "Chose inside color", Color.RED));
		   	if(btnInsideColor.getBackground().equals(Color.BLACK)) {
		   		btnInsideColor.setForeground(Color.WHITE);
		   		}
		   	}
		   });
		   GridBagConstraints gbc_btnInsideColor = new GridBagConstraints();
		   gbc_btnInsideColor.insets = new Insets(0, 0, 5, 0);
		   gbc_btnInsideColor.gridx = 0;
		   gbc_btnInsideColor.gridy = 1;
		   panel.add(btnInsideColor, gbc_btnInsideColor);
		   GridBagConstraints gbc_btnOutsideColor = new GridBagConstraints();
		   gbc_btnOutsideColor.insets = new Insets(0, 0, 5, 0);
		   gbc_btnOutsideColor.gridx = 0;
		   gbc_btnOutsideColor.gridy = 2;
		   panel.add(btnOutsideColor, gbc_btnOutsideColor);
		   
		  
		 
		  btnToBack = new JButton("To back");
		  btnToBack.setEnabled(false);
		  btnToBack.addActionListener(new ActionListener() {
		  	public void actionPerformed(ActionEvent e) {
		  		controller.ToBack();
		  	}
		  });
		  
		  btnToFront = new JButton("To front");
		  btnToFront.setEnabled(false);
		  btnToFront.addActionListener(new ActionListener() {
		  	public void actionPerformed(ActionEvent e) {
		  		controller.ToFront();
		  	}
		  });
		  GridBagConstraints gbc_btnToFront = new GridBagConstraints();
		  gbc_btnToFront.insets = new Insets(0, 0, 5, 0);
		  gbc_btnToFront.gridx = 0;
		  gbc_btnToFront.gridy = 5;
		  panel.add(btnToFront, gbc_btnToFront);
		  GridBagConstraints gbc_btnToBack = new GridBagConstraints();
		  gbc_btnToBack.insets = new Insets(0, 0, 5, 0);
		  gbc_btnToBack.gridx = 0;
		  gbc_btnToBack.gridy = 6;
		  panel.add(btnToBack, gbc_btnToBack);
		  GridBagConstraints gbc_btnFront = new GridBagConstraints();
		  gbc_btnFront.insets = new Insets(0, 0, 5, 0);
		  gbc_btnFront.gridx = 0;
		  gbc_btnFront.gridy = 7;
		  panel.add(btnFront, gbc_btnFront);
		  
		  
		  
		  GridBagConstraints gbc_btnBack = new GridBagConstraints();
		  gbc_btnBack.insets = new Insets(0, 0, 5, 0);
		  gbc_btnBack.gridx = 0;
		  gbc_btnBack.gridy = 8;
		  panel.add(btnBack, gbc_btnBack);
		  
		   btnLoadNext = new JButton("Load next");
		   btnLoadNext.setEnabled(false);
		   btnLoadNext.addActionListener(new ActionListener() {
		   	public void actionPerformed(ActionEvent e) {
		   		try {
					controller.loadNext();
				} catch (RadiusException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		   	}
		   });
		   GridBagConstraints gbc_btnLoadNext = new GridBagConstraints();
		   gbc_btnLoadNext.insets = new Insets(0, 0, 5, 0);
		   gbc_btnLoadNext.gridx = 0;
		   gbc_btnLoadNext.gridy = 10;
		   panel.add(btnLoadNext, gbc_btnLoadNext);
		
		JPanel pnlLog = new JPanel();
		pnlLog.setPreferredSize(new Dimension(300,100));
		contentPane.add(pnlLog, BorderLayout.SOUTH);
		pnlLog.setLayout(new BorderLayout(0,0));
		
		JScrollPane scrollPane = new JScrollPane();
		pnlLog.add(scrollPane, BorderLayout.CENTER);
		
		textAreaLog = new JTextArea();
		textAreaLog.setEditable(false);
		scrollPane.setViewportView(textAreaLog);
		
	
		
	}

	public DrawingView getView() {
		return view;
	}

	public void setController(DrawingController controller) {
		this.controller = controller;
	}

	public int getState() {
		return state;
	}

	public JToggleButton getTglbtnModify() {
		return tglbtnModify;
	}

	public void setTglbtnModify(JToggleButton tglbtnModify) {
		this.tglbtnModify = tglbtnModify;
	}

	public JToggleButton getTglbtnSelect() {
		return tglbtnSelect;
	}

	public void setTglbtnSelect(JToggleButton tglbtnSelect) {
		this.tglbtnSelect = tglbtnSelect;
	}

	public JToggleButton getTglbtnDelete() {
		return tglbtnDelete;
	}

	public void setTglbtnDelete(JToggleButton tglbtnDelete) {
		this.tglbtnDelete = tglbtnDelete;
	}

	public JButton getBtnInsideColor() {
		return btnInsideColor;
	}

	public void setBtnInsideColor(JButton btnInsideColor) {
		this.btnInsideColor = btnInsideColor;
	}

	public JButton getBtnUndo() {
		return btnUndo;
	}

	public void setBtnUndo(JButton btnUndo) {
		this.btnUndo = btnUndo;
	}

	public JButton getBtnRedo() {
		return btnRedo;
	}

	public void setBtnRedo(JButton btnRedo) {
		this.btnRedo = btnRedo;
	}

	public JButton getBtnOutsideColor() {
		return btnOutsideColor;
	}

	public void setBtnOutsideColor(JButton btnOutsideColor) {
		this.btnOutsideColor = btnOutsideColor;
	}

	public JScrollPane getScrollPane() {
		return getScrollPane();
	}

	

	public JTextArea getTextAreaLog() {
		return textAreaLog;
	}

	public void setTextAreaLog(JTextArea textAreaLog) {
		this.textAreaLog = textAreaLog;
	}

	public JButton getBtnLoadNext() {
		return btnLoadNext;
	}

	public void setBtnLoadNext(JButton btnLoadNext) {
		this.btnLoadNext = btnLoadNext;
	}

	public JButton getBtnFront() {
		return btnFront;
	}

	public void setBtnFront(JButton btnFront) {
		this.btnFront = btnFront;
	}

	public JButton getBtnToFront() {
		return btnToFront;
	}

	public void setBtnToFront(JButton btnToFront) {
		this.btnToFront = btnToFront;
	}

	public JButton getBtnToBack() {
		return btnToBack;
	}

	public void setBtnToBack(JButton btnToBack) {
		this.btnToBack = btnToBack;
	}

	public JButton getBtnBack() {
		return btnBack;
	}

	public void setBtnBack(JButton btnBack) {
		this.btnBack = btnBack;
	}

	
	
	
}

