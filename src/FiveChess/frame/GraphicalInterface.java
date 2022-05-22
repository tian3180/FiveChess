package FiveChess.frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author jobszhu
 * @date 2022/5/22 10:02
 * @project FiveChess
 * @Description 窗体基本结构UI
 */
public class GraphicalInterface extends JFrame implements MouseListener {
    //定义基本属性  创建需要的基本组件
    JPanel jPanel = new JPanel();
    int width = Toolkit.getDefaultToolkit().getScreenSize().width; //获取屏幕的宽度
    int height = Toolkit.getDefaultToolkit().getScreenSize().height; //获取屏幕的高度
    //利用获取到的屏幕的高度和宽度来使窗体居中

    BufferedImage backgroundImage = null; //背景图片
    BufferedImage black = null; //黑棋
    BufferedImage white = null; //白棋
    BufferedImage select = null; //棋盘位置被选中

    int x = 0;  //鼠标点击中的x坐标
    int y = 0; //鼠标点击中的y坐标

    int row = 0; //棋盘行数
    int col = 0; //棋盘列数

    int cnt = 0; //记录棋子个数   也用来记录轮到白方还是黑方下了

    //保存之前下过所有的棋子的坐标
    int[][] chess = new int[15][15];  //0：代表这个点没有落子   1：代表下的黑子  2：代表下的白子

    //selectTarget保存选中的目标点  true代表鼠标正在这个位置 false代表鼠标没有在这个位置
    boolean[][] selectTarget = new boolean[15][15];

    //标识当前游戏是否可以继续
    boolean isGameOver = false;

    //按键
    Button blackBtn = new Button("黑棋"); //声明底部组件
    Button whiteBtn = new Button("白棋");

    Button startAllOverAgain = new Button("重新开始");
    //悔棋
    Button back = new Button("悔棋");

    //设置提示信息
    String message="黑方先行";

    //构造函数  设置窗体基本信息
    //建造图形界面
    public GraphicalInterface() {
        setTitle("五子棋");  //设置窗体标题
        setSize(545, 625);  //窗体大小应该和棋盘照片的大小一致  多设置的高用于填充游戏信息板块
        setLocation(width / 2 - getWidth() / 2, height / 2 - getHeight() / 2); //设置窗体居中
        //getWidth()、getHeight()获取窗体的宽度和高度
        setResizable(false);  //设置窗体不可改变大小   为了避免窗体大小出现显示错误
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //设置窗体关闭时的操作
        //默认关闭后程序结束

        //获取容器 并设置布局方式
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        jPanel.add(whiteBtn);
        jPanel.add(blackBtn);
        jPanel.add(startAllOverAgain);
        jPanel.add(back);
        //设置初始按钮背景颜色
        whiteBtn.setBackground(Color.white);
        blackBtn.setBackground(Color.black);
        startAllOverAgain.setBackground(Color.red);
        back.setBackground(Color.blue);

        //假如按键模块
        container.add(jPanel, BorderLayout.SOUTH);


        try {
            //读取背景图片
            backgroundImage = ImageIO.read(new FileInputStream("src/images/table545.png"));
            //读取黑棋图片
            black = ImageIO.read(new FileInputStream("src/images/black-32.png"));
            //读取白棋图片
            white = ImageIO.read(new FileInputStream("src/images/white-32.png"));
            //读取选中棋点图片
            select = ImageIO.read(new FileInputStream("src/images/select-32.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //添加鼠标监听
        addMouseListener(this);
        //添加鼠标移动监听
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                //判断点击范围是否在棋盘上
                if (x >= 34 && x <= 510 && y >= 116 && y <= 590) {
                    //把其他位置都设置为false
                    for (int i = 0; i < 15; i++) {
                        for (int j = 0; j < 15; j++) {
                            selectTarget[i][j] = false;
                        }
                    }
                    //点击棋盘上的棋子
                    col = (y - 115) / 32;  //每个棋点的间隙是34  所以除以34得到列数
                    row = (x - 33) / 32;  //每个棋点的间隙是34  所以除以34得到行数
                    //绘制鼠标选定点
                    selectTarget[row][col] = true;
                    //为避免绘图过于频繁卡顿，休息100毫秒
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    repaint();
                }
            }
        });

        setVisible(true);  //设置窗体可见
    }


    public void paint(Graphics g) {
        //绘制背景图片
        g.drawImage(backgroundImage, 0, 80, this);
        //绘制游戏信息板块  输出标题信息
        g.setFont(new Font("宋体", Font.BOLD, 40));
        g.drawString("游戏信息：", 10, 65);
        g.setFont(new Font("宋体", Font.ITALIC, 20));
        //输出时间信息
        g.drawString("黑方时间：无限制", 190, 45);
        g.drawString("白方时间：无限制", 380, 45);

        //提示该谁下棋
        g.setFont(new Font("宋体", Font.BOLD, 20));
        g.drawString(message, 200, 75);

        //绘制旗子
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {//dwa
                if (chess[i][j] == 1) {
                    g.drawImage(black, i * 34 + 20, j * 34 + 100, this);
                } else if (chess[i][j] == 2) {
                    g.drawImage(white, i * 34 + 20, j * 34 + 100, this);
                }
            }
        }

        //绘制选中棋点
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (selectTarget[i][j]) {
                    g.drawImage(select, i * 34 + 20, j * 34 + 100, this);
                }
            }
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(isGameOver)  //如果游戏结束不在允许下棋
            return;
//        System.out.println("x:" + e.getX() + "y:" + e.getY());
        x = e.getX();
        y = e.getY();
        //判断点击范围是否在棋盘上
        if (x >= 34 && x <= 510 && y >= 116 && y <= 590) {
            //点击棋盘上的棋子
            col = (y - 115) / 32;  //每个棋点的间隙是34  所以除以34得到列数
            row = (x - 33) / 32;  //每个棋点的间隙是34  所以除以34得到行数
            //判断位置是否以及有棋子  如果有棋子则不能下
            if (chess[row][col] != 0) {
                JOptionPane.showMessageDialog(this, "当前位置已经有棋了，请重新落棋");
                return;
            }
            //记录目标棋盘位置 并且给予赋值
            if (cnt % 2 == 0){
                chess[row][col] = 1; //黑棋
                message="轮到白方";
            }

            else{
                chess[row][col] = 2; //白棋
                message="轮到黑方";
            }

            cnt++;
            this.repaint();
            //判断游戏是否结束
            if (checkWin(row, col)) {
                if(chess[row][col] == 1){
                    JOptionPane.showMessageDialog(this, "黑棋胜利");
                    isGameOver=true;
                }
                else if (chess[row][col] == 2){
                    JOptionPane.showMessageDialog(this, "白棋胜利");
                    isGameOver=true;
                }

            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    //判断赢棋的方法
    private boolean checkWin(int row, int col) {
        //判断横向  纵坐标相同 即y相同
        int color = chess[row][col];
        int count = 1;  //记录相同棋子的个数
        int i = 1;
        while (color == chess[row+i][col]) {
            count++;
            i++;
            if(count==5) {
                return true;
            }
        }
        i = 1;
        count=1;
        while (color == chess[row-i][col]) {
            count++;
            i++;
            if(count==5) {
                return true;
            }
        }
        //判断纵向  横坐标相同 即x相同
        i=1;
        count=1;
        while (color == chess[row][col+i]) {
            count++;
            i++;
            if(count==5) {
                return true;
            }
        }
        i=1;
        count=1;
        while (color == chess[row][col-i]) {
            count++;
            i++;
            if(count==5) {
                return true;
            }
        }
        //斜的方向
        i=1;
        count=1;
        while (color == chess[row+i][col+i]) {
            count++;
            i++;
            if(count==5) {
                return true;
            }
        }
        i=1;
        count=1;
        while (color == chess[row-i][col-i]) {
            count++;
            i++;
            if(count==5) {
                return true;
            }
        }
        i=1;
        count=1;
        while (color == chess[row-i][col+i]) {
            count++;
            i++;
            if(count==5) {
                return true;
            }
        }
        i=1;
        count=1;
        while (color == chess[row+i][col-i]) {
            count++;
            i++;
            if(count==5) {
                return true;
            }
        }


        //找不到五子连珠
        return false;
    }
}
