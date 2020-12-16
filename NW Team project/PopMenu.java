import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

// 친구 우클릭 했을 때 나오는 팝업메뉴 
public class PopMenu{

    JPopupMenu pm = new JPopupMenu();
    JMenuItem pm_item1 = new JMenuItem("상세정보");
    JMenuItem pm_item2 = new JMenuItem("1대1 채팅하기");
   public PopMenu() {
	   pm.add(pm_item1);
       pm.addSeparator(); // 구분선
       pm.add(pm_item2);
   }
}
