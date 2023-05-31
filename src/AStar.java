import java.util.*;

public class AStar extends Thread{
  int[][] graph = new int[10][10];
  Point start = new Point(0, 0);
  Point dest = new Point(9, 9);

  Stack<Point> closed = new Stack<>();
  Queue<Point> opened = new PriorityQueue<>(Comparator.comparing(o->o.f));

  public void init() {
    graphType1();
  }

  private void graphType1() {
    for (int i = 0; i < 10; i++) {
      if (i < 7) {
        graph[i][3] = 1;
      }
      if (i > 3) {
        graph[i][7] = 1;
      }
      if (i == 4) {
        graph[i][5] = 1;
        graph[i][6] = 1;
      }
      if (i == 6) {
        graph[i][4] = 1;
      }
      if (i == 8) {
        graph[i][0] = 1;
        graph[i][1] = 1;
        graph[i][2] = 1;
      }
    }
  }

  private void graphType2() {
    for (int i = 4; i < 8; i++) {
      graph[7][i] = 1;
      graph[i][8] = 1;
    }
  }

  @Override
  public void run() {
    System.out.println("A* Started!");

    Point currentPoint = start;
    closed.add(start);
    opened = new PriorityQueue<>(Comparator.comparing(o->o.f));
    // Thêm các bước tiếp theo vào tập mở
    for (Point neighbor : neighbors(currentPoint)) {
      neighbor.parent = currentPoint; // Đặt điểm cha của bước tiếp theo để truy vết
      neighbor.g += currentPoint.g; // Tính chi phí đường đi
      neighbor.h = (int) Math.sqrt(
              Math.pow(dest.x - neighbor.x, 2) +
              Math.pow(dest.y - neighbor.y, 2)
      ); // Tính giá trị hàm heuristic bằng khoảng cách đường chim bay tới đích
      neighbor.f = neighbor.g + neighbor.h; // Tính hàm f
      opened.add(neighbor);

      // Chỗ này là dùng để vẽ, không liên quan đến thuật toán
      graph[neighbor.x][neighbor.y] = 3;
      sleep();
      //-------------------------------------------------------
    }

    while (!opened.isEmpty()) {
      // Kiểm tra nếu đã đi thì bỏ qua
      for (Point prevPoint : closed) {
        if (prevPoint.equal(opened.peek())) {
          opened.poll();
        }
      }
      // Dừng thuật toán nếu đến đích
      if (currentPoint.equal(dest)) {
        break;
      }
      // Thêm bước tiếp theo vào tập đóng
      currentPoint = opened.poll();
      closed.add(currentPoint);

      // Thêm các bước tiếp theo vào tập mở
      for (Point neighbor : neighbors(currentPoint)) {
        neighbor.parent = currentPoint;
        neighbor.g += currentPoint.g;
        neighbor.h = (int) Math.sqrt(
              Math.pow(dest.x - neighbor.x, 2) +
              Math.pow(dest.y - neighbor.y, 2)
        );
        neighbor.f = neighbor.g + neighbor.h;

        // Kiểm tra xem bước tiếp theo đã tìm thấy chưa
        // Nếu rồi thì thay bằng cách đi khác có độ ưu tiên tốt hơn
        for (Point prev : opened) {
          if (prev.equal(neighbor) && prev.f < neighbor.f) {
            opened.remove(prev);
            break;
          }
        }

        opened.add(neighbor);

        // Chỗ này là dùng để vẽ, không liên quan đến thuật toán
        graph[neighbor.x][neighbor.y] = 3;
        sleep();
        //-------------------------------------------------------
      }
    }

    closed.clear();

    // Truy vết lại đường đi
    while (currentPoint != null) {
      closed.add(currentPoint);

      // Chỗ này là dùng để vẽ, không liên quan đến thuật toán
      graph[currentPoint.x][currentPoint.y] = 2;
      sleep();
      //-------------------------------------------------------

      currentPoint = currentPoint.parent;
    }
    closed.pop();
  }

  public List<Point> neighbors(Point point) {
    List<Point> neighbors = new ArrayList<>();
    if (point.x > 0) {
      if (graph[point.x - 1][point.y] == 0) {
        neighbors.add(new Point(point.x - 1, point.y));
      }
    }
    if (point.x < 9) {
      if (graph[point.x + 1][point.y] == 0) {
        neighbors.add(new Point(point.x + 1, point.y));
      }
    }
    if (point.y > 0) {
      if (graph[point.x][point.y - 1] == 0) {
        neighbors.add(new Point(point.x, point.y - 1));
      }
    }
    if (point.y < 9) {
      if (graph[point.x][point.y + 1] == 0) {
        neighbors.add(new Point(point.x, point.y + 1));
      }
    }
    return neighbors;
  }

  public void sleep() {
    try {
      sleep(100);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void print() {
    for (int[] i : graph) {
      for (int e : i) {
        System.out.print(e + " ");
      }
      System.out.println();
    }
    System.out.println();
  }

  public void drawToGraph() {
    for (Point point : closed) {
      graph[point.x][point.y] = 2;
    }
  }

  static class Point {
    int x;
    int y;
    double f = 0;
    int g = 1;
    double h = 0;
    Point parent;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public boolean equal(Point other) {
      return this.x == other.x && this.y == other.y;
    }
  }
}
