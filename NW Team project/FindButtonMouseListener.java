import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class FindButtonMouseListener implements MouseListener {

	ImageIcon f_pre = new ImageIcon("find_pre.png");
	ImageIcon f_af = new ImageIcon("find_af.png");
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
		b.setIcon(MessengerUI.setImageSize(f_af, 40, 40));
		b.setCursor(c);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		JButton b = (JButton)e.getSource();
		b.setIcon(MessengerUI.setImageSize(f_pre, 40, 40));
	}
	
}
