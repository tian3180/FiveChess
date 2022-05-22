package FiveChess.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

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



    //构造函数  设置窗体基本信息
    //建造图形界面


    public GraphicalInterface() {
        setTitle("五子棋");  //设置窗体标题
        setSize(545, 545);  //窗体大小应该和棋盘照片的大小一致
        setLocation(width / 2 - getWidth() / 2, height / 2 - getHeight() / 2); //设置窗体居中
        //getWidth()、getHeight()获取窗体的宽度和高度
        setResizable(false);  //设置窗体不可改变大小   为了避免窗体大小出现显示错误
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //设置窗体关闭时的操作
        //默认关闭后程序结束
        setVisible(true);  //设置窗体可见
    }



    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

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
