public class SeqViewer {
	public static void main(String[] args) {
		javax.swing.ToolTipManager.sharedInstance().setInitialDelay(10);
		javax.swing.ToolTipManager.sharedInstance().setDismissDelay(10000000);
		javax.swing.ToolTipManager.sharedInstance().setReshowDelay(10);
		gui.startMenu startMenuFrame = new gui.startMenu();
		    startMenuFrame.setVisible(true);
		    startMenuFrame.setDefaultCloseOperation(gui.startMenu.EXIT_ON_CLOSE);
	}

}