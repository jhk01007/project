import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

// ģ�� ��Ŭ�� ���� �� ������ �˾��޴� 
public class PopMenu{

    JPopupMenu pm = new JPopupMenu();
    JMenuItem pm_item1 = new JMenuItem("������");
    JMenuItem pm_item2 = new JMenuItem("1��1 ä���ϱ�");
   public PopMenu() {
	   pm.add(pm_item1);
       pm.addSeparator(); // ���м�
       pm.add(pm_item2);
   }
}
