import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class App extends JPanel implements ActionListener {

  protected Timer timer;
  private int margin = 50;
  private int dimension = 550;
  private int tileSize = (dimension - 2 * margin) / 10;
  private int[][] graph;
  private AStar aStar;

  private int currentSize = 0;

  public App() {
    init();
    setPreferredSize(new Dimension(dimension, dimension));
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (!aStar.isAlive()) {
          run();
        }
      }
    });
  }

  public void init() {
    timer = new Timer(0, this);
    aStar = new AStar();
    aStar.init();
    graph = aStar.graph;
  }

  public void run() {
    init();
    timer.start();
    System.out.println("Running!");
    aStar.start();
    currentSize = aStar.closed.size();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (currentSize != aStar.closed.size()) {
      repaint();
    }
    if (!aStar.isAlive()) {
      timer.stop();
      System.out.println("Finished!");
    }
  }

  /**
   * Draw the board
   * @param g Graphics2D
   */
  private void draw(Graphics2D g) {
    int x, y;
    for (int id = 0; id < 100; id++) {
      x = (id % 10) * tileSize + (dimension - tileSize * 10) / 2;
      y = margin + (id / 10) * tileSize;

      // for other tiles
      switch (graph[id / 10][id % 10]) {
        case 1 -> g.setColor(Color.darkGray);
        case 2 -> g.setColor(Color.cyan);
        case 3 -> g.setColor(Color.green);
        default -> g.setColor(Color.white);
      }
      g.fillRoundRect(x, y, tileSize, tileSize, 0, 0);
      g.setColor(Color.BLACK);
      g.drawRoundRect(x, y, tileSize, tileSize, 0, 0);
    }
    // Draw start point
    x = aStar.start.y * tileSize + (dimension - tileSize * 10) / 2;
    y = margin + aStar.start.x * tileSize;
    g.setColor(Color.yellow);
    g.fillRoundRect(x, y, tileSize, tileSize, 0, 0);
    g.setColor(Color.black);
    g.drawRoundRect(x, y, tileSize, tileSize, 0, 0);

    // Draw end point
    x = aStar.dest.y * tileSize + (dimension - tileSize * 10) / 2;
    y = margin + aStar.dest.x * tileSize;
    g.setColor(Color.red);
    g.fillRoundRect(x, y, tileSize, tileSize, 0, 0);
    g.setColor(Color.black);
    g.drawRoundRect(x, y, tileSize, tileSize, 0, 0);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    draw(g2D);
  }
}
