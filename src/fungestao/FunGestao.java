package fungestao;

import br.com.login.view.LoginView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FunGestao {

    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
        
    }
    
}
