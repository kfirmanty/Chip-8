package pl.kfirmanty.chip8;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

import pl.kfirmanty.chip8.configuration.Configuration;

public class SwingMain extends JFrame {
	private static final long serialVersionUID = 588277464385444817L;

	private ProcessingMain processingMain;
	private static final int WINDOW_SIZE_OFFSET = 100;

	public SwingMain() {
		this.setLayout(new BorderLayout());
		processingMain = new ProcessingMain();

		processingMain.frame = this;
		setResizable(true);
		setTitle("Chip8");
		setLocation(WINDOW_SIZE_OFFSET, WINDOW_SIZE_OFFSET);

		Configuration configuration = Configuration.getInstance();
		setSize(configuration.getWindowWidth() + WINDOW_SIZE_OFFSET, configuration.getWindowHeight() + WINDOW_SIZE_OFFSET);
		add(processingMain, BorderLayout.CENTER);
		initMenu();
		setVisible(true);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new SwingMain();
	}

	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Actions");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("Actions");
		menuBar.add(menu);

		menu.add(createMenuItem("Load program", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				processingMain.init();
			}
		}));

		menu.add(createMenuItem("Toggle synesthesia", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Configuration.getInstance().toggleSynesthesia();
			}
		}));

		menu.add(createMenuItem("Settings", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SwingSettings();
			}
		}));

		setJMenuBar(menuBar);
	}

	private JMenuItem createMenuItem(String title, ActionListener actionListener) {
		JMenuItem menuItem = new JMenuItem(title);
		menuItem.addActionListener(actionListener);
		return menuItem;
	}
}
