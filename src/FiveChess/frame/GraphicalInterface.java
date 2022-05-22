package FiveChess.frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author jobszhu
 * @date 2022/5/22 10:02
 * @project FiveChess
 * @Description  窗体基本结构UI
 */
public class GraphicalInterface extends JFrame implements MouseListener {
    //定义基本属性  创建需要的基本组件
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

    //保存之前下过所有的棋子的坐标
    int[][] chess = new int[15][15];  //0：代表这个点没有落子   1：代表下的黑子  2：代表下的白子

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

        try {
            //读取背景图片
            backgroundImage = ImageIO.read(new FileInputStream("src/images/table545.png"));
            //读取黑棋图片
            black = ImageIO.read(new FileInputStream("src/images/black-32.png"));
            //读取白棋图片
            white = ImageIO.read(new FileInputStream("src/images/white-32.png"));
            //读取选中棋点图片
            select = ImageIO.read(new FileInputStream("src/images/select-32.png"));
        }catch (Exception e){
            e.printStackTrace();
        }

        //添加鼠标监听
        addMouseListener(this);

        setVisible(true);  //设置窗体可见
    }


    public void paint(Graphics g) {
        //绘制背景图片
        g.drawImage(backgroundImage, 0, 80, this);
        //绘制游戏信息板块  输出标题信息
        g.setFont(new Font("宋体", Font.BOLD, 40));
        g.drawString("游戏信息：",10,65);
        g.setFont(new Font("宋体", Font.ITALIC, 20));
        //输出时间信息
        g.drawString("黑方时间：无限制",190,45);
        g.drawString("白方时间：无限制",380,45);

        //绘制旗子
//        g.drawImage(black,row*34,col*34,this);
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (chess[i][j] == 1) {
                    g.drawImage(black, i * 34, j * 34, this);
                } else if (chess[i][j] == 2) {
                    g.drawImage(white, i * 34, j * 34, this);
                }
            }
        }
    }



    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("x:"+e.getX()+"y:"+e.getY());
        x = e.getX();
        y = e.getY();
        //判断点击范围是否在棋盘上
        if(x>=34 && x<=510 && y>=116 && y<=590){
            //点击棋盘上的棋子
            row = (y-116)/34;  //每个棋点的间隙是34  所以除以34得到行数
            col = (x-34)/34;  //每个棋点的间隙是34  所以除以34得到列数
            repaint();
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

}
