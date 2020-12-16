import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class EditButtonMouseListener implements MouseListener {

	ImageIcon e_pre = new ImageIcon("edit_pre.png");
	ImageIcon e_af = new ImageIcon("edit_af.png");
	Cursor c = new Cursor(Cursor.HAND_CURSOR);
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		JButton b = (JButton)e.getSource();
		b.setIcon(MessengerUI.setImageSize(e_af, 40, 40));
		b.setCursor(c);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		JButton b = (JButton)e.getSource();
		b.setIcon(MessengerUI.setImageSize(e_pre, 40, 40));
	}
	
}
