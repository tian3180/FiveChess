package FiveChess.frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.jar.JarEntry;

/**
 * @author jobszhu
 * @date 2022/5/22 10:02
 * @project FiveChess
 * @Description 窗体基本结构UI
 */
public class GraphicalInterface extends JFrame implements MouseListener ,Runnable {

    //定义基本属性  创建需要的基本组件
    JPanel jPanel = new JPanel();
    Container container = getContentPane();
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


    //设置倒计时的最大值
    int maxTime = 0;

    //倒计时的线程
    Thread thread = new Thread(this);



    //保存黑方与白方的剩余倒计时时间
    int blackTime = 0;
    int whiteTime = 0;

    String whiteMessage = "无限制";
    String blackMessage = "无限制";



    int backRow, backCol; //记录棋子的位置

    //保存之前下过所有的棋子的坐标
    int[][] chess = new int[15][15];  //0：代表这个点没有落子   1：代表下的黑子  2：代表下的白子

    //selectTarget保存选中的目标点  true代表鼠标正在这个位置 false代表鼠标没有在这个位置
    boolean[][] selectTarget = new boolean[15][15];

    //标识当前游戏是否可以继续
    boolean isGameOver = false;

    //按键
    JButton blackBtn = new JButton("黑棋"); //声明底部组件
    JButton whiteBtn = new JButton("白棋");

    JButton startAllOverAgain = new JButton("重新开始");
    //悔棋
    JButton back = new JButton("悔棋");
    //认输按钮
    JButton admitDefeat = new JButton("认输");
    //游戏设置按钮  用来设置步时
    JButton gameSetting = new JButton("设置");
    //游戏说明按键
    JButton gameInstruction = new JButton("说明");
    //设置提示信息
    String message="黑方先行";


    //构造函数  设置窗体基本信息
    //建造图形界面
    public GraphicalInterface() {
        //设置窗体基本信息
        setTitle("五子棋");  //设置窗体标题
        setSize(545, 700);  //窗体大小应该和棋盘照片的大小一致  多设置的高用于填充游戏信息板块
        setLocation(width / 2 - getWidth() / 2, height / 2 - getHeight() / 2); //设置窗体居中
        //getWidth()、getHeight()获取窗体的宽度和高度
        setResizable(false);  //设置窗体不可改变大小   为了避免窗体大小出现显示错误
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //设置窗体关闭时的操作
        //默认关闭后程序结束

        //获取容器 并设置布局方式

        container.setLayout(new BorderLayout());
        jPanel.setLayout(new GridLayout(2,5));
        //加入按钮
        jPanel.add(whiteBtn);
        jPanel.add(blackBtn);
        jPanel.add(startAllOverAgain);
        jPanel.add(back);
        jPanel.add(admitDefeat);
        jPanel.add(gameSetting);
        jPanel.add(gameInstruction);


        //设置初始按钮背景颜色
//        whiteBtn.setBackground(Color.red);
//        whiteBtn.setBackground(Color.ORANGE);
//        blackBtn.setBackground(Color.black);
//        startAllOverAgain.setBackground(Color.red);
//        back.setBackground(Color.blue);

        //假如按键模块
        container.add(jPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(null,"祝你游戏愉快");


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
                    //为避免绘图过于频繁卡顿，休息10毫秒
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    repaint();
                }
            }
        });

        //点击重新开始游戏按键 重新开始游戏
        startAllOverAgain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //重新开始游戏
                startAllOverAgain();
                JOptionPane.showMessageDialog(null, "游戏已重新开始");
            }

            private void startAllOverAgain() {
                //将chess数组中的所有元素设置为0
                for (int i = 0; i < 15; i++) {
                    for (int j = 0; j < 15; j++) {
                        chess[i][j] = 0;
                    }
                }
                repaint();
                //取消游戏结束的状态
                isGameOver= false;
                //设置初始化的提示语
                message = "黑方先行";
                //设置黑方先行
                cnt = 0;
                //初始化剩余时间
                blackTime = maxTime;
                whiteTime = maxTime;
            }
        });

        //游戏说明按键点击事件
        gameInstruction.addActionListener(new ActionListener() {
                                              @Override
                                              public void actionPerformed(ActionEvent e) {
                                                  JOptionPane.showMessageDialog(null, "游戏说明：\n" +
                                                          "1.点击棋盘上的棋子即可下棋，每次下棋后，系统会自动判断是否有五子连珠，如果有，则游戏结束，并显示输赢信息。\n" +
                                                          "2.默认设置为不限步时，黑方先手，自动切换黑白子下棋。\n" +
                                                          "3.点击游戏设置可以设置步时，游戏中某方步时耗尽则输掉比赛。\n" +
                                                          "4.更多游戏介绍和设置请打开工程文件代码目录里的Readme.md 或 Readme.pdf。\n"
                                                          );
                                              }
                                          });

        //悔棋按键点击事件
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //判断是否有棋子可悔棋
                if (cnt!=0) {
                    //悔棋
                    if (cnt%2!=0) {
                        //黑棋悔棋
                        chess[backRow][backCol] = 0;
                        //提示语
                        JOptionPane.showMessageDialog(null, "黑方悔棋成功");
                        message = "请黑方落子";
                    } else if (cnt%2==0) {
                        //白棋悔棋
                        chess[backRow][backCol] = 0;
                        //提示语
                        JOptionPane.showMessageDialog(null, "白方悔棋成功");
                        message = "请白方落子";
                    }

                    //悔棋后提示语
                    repaint();
                    cnt--;
                }
            }});

        //启动线程
        thread.start();

        //挂起线程

        //游戏设置按钮点击事件
        gameSetting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("请输入游戏的最大时间（分钟）输入0表示没有时间限制：");
                try {
                    maxTime = Integer.parseInt(input) * 60;  //转化为秒
                    if(maxTime<0)
                        JOptionPane.showMessageDialog(null, "输入的时间不能小于0");
                    if(maxTime == 0){
                        JOptionPane.showMessageDialog(null, "游戏时间限制已经取消");
                        //重新开始游戏

                        //将chess数组中的所有元素设置为0
                        for (int i = 0; i < 15; i++) {
                            for (int j = 0; j < 15; j++) {
                                chess[i][j] = 0;
                            }
                        }

                        //取消游戏结束的状态
                        isGameOver= false;
                        //设置初始化的提示语
                        message = "黑方先行";
                        //设置黑方先行
                        cnt = 0;
                        //初始化剩余时间
                        blackTime = maxTime;
                        whiteTime = maxTime;

                        blackMessage = "无限制";
                        whiteMessage = "无限制";

                        repaint();


                    }

                    if(maxTime>0){
                        JOptionPane.showMessageDialog(null, "设置完成");
                        //重新开始游戏

                        //将chess数组中的所有元素设置为0
                        for (int i = 0; i < 15; i++) {
                            for (int j = 0; j < 15; j++) {
                                chess[i][j] = 0;
                            }
                        }

                        //取消游戏结束的状态
                        isGameOver= false;
                        //设置初始化的提示语
                        message = "黑方先行";
                        //设置黑方先行
                        cnt = 0;
                        //初始化剩余时间
                        blackTime = maxTime;
                        whiteTime = maxTime;
                        blackMessage = maxTime/3600+":"+(maxTime - maxTime/3600)/60+":"+(maxTime-maxTime/60*60);  //格式化输出时间
                        whiteMessage = maxTime/3600+":"+(maxTime - maxTime/3600)/60+":"+(maxTime-maxTime/60*60);

                        repaint();
                    }

                }catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "请输入正确的数字");
                }

            }
        });
        setVisible(true);  //设置窗体可见
    }


    public void paint(Graphics g) {
        //双缓冲技术 减少绘图过程中的卡顿
        BufferedImage bufferedImage1 = new BufferedImage(545, 625, BufferedImage.TYPE_INT_RGB);
        Graphics g2 = bufferedImage1.createGraphics();  //将所有东西以照片的方式写入内存

        //绘制背景图片
        g2.drawImage(backgroundImage, 0, 80, this);
        //绘制游戏信息板块  输出标题信息
        g2.setFont(new Font("宋体", Font.BOLD, 40));
        g2.drawString("游戏信息：", 10, 65);
        g2.setFont(new Font("宋体", Font.ITALIC, 20));
        //输出时间信息
        g2.drawString("黑方时间："+blackMessage, 190, 45);
        g2.drawString("白方时间："+ whiteMessage, 380, 45);

        //提示该谁下棋
        g2.setFont(new Font("宋体", Font.BOLD, 20));
        g2.drawString("目前轮到："+message, 200, 75);

        //绘制旗子
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (chess[i][j] == 1) {
                    g2.drawImage(black, i * 34 + 20, j * 34 + 100, this);
                } else if (chess[i][j] == 2) {
                    g2.drawImage(white, i * 34 + 20, j * 34 + 100, this);
                }
            }
        }

        //绘制选中棋点
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (selectTarget[i][j]) {
                    g2.drawImage(select, i * 34 + 20, j * 34 + 100, this);
                }
            }
        }
        g.drawImage(bufferedImage1,0,0,this);
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
            backRow= row;
            backCol= col;
            //判断位置是否以及有棋子  如果有棋子则不能下
            if (chess[row][col] != 0) {
                JOptionPane.showMessageDialog(this, "当前位置已经有棋了，请重新落棋");
                return;
            }
            //记录目标棋盘位置 并且给予赋值
            if (cnt % 2 == 0){
                chess[row][col] = 1; //黑棋
                message="请白方落子";
            }

            else{
                chess[row][col] = 2; //白棋
                message="请黑方落子";
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

    @Override
    public void run() {
        //判断是否有时间的限制
//        int maxTime=10;
//        if(maxTime>0){
            while(true){
                if(cnt% 2 ==0)
                    blackTime--;
                else
                    whiteTime--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(maxTime>0){
                    blackMessage = blackTime/3600+":"+(blackTime - blackTime/3600)/60+":"+(blackTime-blackTime/60*60);
                    whiteMessage = whiteTime/3600+":"+(whiteTime - whiteTime/3600)/60+":"+(whiteTime-whiteTime/60*60);
                    repaint();
                    if(blackTime ==0){
                        JOptionPane.showMessageDialog(this, "黑棋时间到，白棋胜利");
                        isGameOver=true;
                        break;
                    }
                    if(whiteTime ==0){
                        JOptionPane.showMessageDialog(this, "白棋时间到，黑棋胜利");
                        isGameOver=true;
                        break;
                    }
                }
                //test
//                System.out.println("黑棋剩余时间："+blackTime+"白棋剩余时间："+whiteTime);
            }
//        }

    }
}
