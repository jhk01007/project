import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class AddButtonMouseListener implements MouseListener {

	ImageIcon p_pre = new ImageIcon("plus_pre.png");
	ImageIcon p_af = new ImageIcon("plus_af.png");
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
		b.setIcon(MessengerUI.setImageSize(p_af, 40, 40));
		b.setCursor(c);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		JButton b = (JButton)e.getSource();
		b.setIcon(MessengerUI.setImageSize(p_pre, 40, 40));
	}
	
}
