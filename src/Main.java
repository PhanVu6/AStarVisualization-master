import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame();

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setTitle("A* Visualization");
      frame.setResizable(false);

      System.out.println("Create success!");
      App app = new App();
      app.requestFocusInWindow();


      frame.add(app);
      frame.pack();
      frame.requestFocusInWindow();
      // center on the screen
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
    });
  }
}