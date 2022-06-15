package MySort;
import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
public class ViewSort extends JFrame
{
	private JButton btn1=new JButton("开始排序"); //开始按钮
	private JButton btn2=new JButton("回顾"); //用于回放上一次排序过程
	private JButton btn3=new JButton("打开历史数据"); //打开文件夹
	private JTextField text=new JTextField(15); //用于输入一组数组
	private JTextField run=new JTextField(25); //显示程序运行过程
	private JTextField run2=new JTextField(25);
	private JTextArea run3=new JTextArea(25,30); //显示文件内容
	private JLabel lb=new JLabel();
	private JPanel jp1=new JPanel();
	private JPanel jp2=new JPanel(); 
	private JComboBox<String> box=new JComboBox<String>(); //下拉列表框,选择排序方式
	static int[] nums=new int[5];
	FileDialog open=new FileDialog(this,"open",FileDialog.LOAD); //初始化打开对话框
	
	public ViewSort() //构造方法
	{
		this.setTitle("MySort"); //设置标题
		jp1.add(text); //添加组件
		jp1.add(box); 
		jp1.add(btn1);  
		jp1.add(btn2);
		jp1.add(btn3);
		//编辑box的选项
		box.addItem("请选择:");
		box.addItem("冒泡排序");
		box.addItem("选择排序");
		box.addItem("插入排序");
		
		jp1.setLayout(new FlowLayout());
		jp2.add(run);	
		jp2.add(run2);
		jp2.add(lb);
		jp2.add(run3);
		run3.setText("历史数据:");
		
		jp2.setLayout(new GridLayout(5,1,10,10));
		this.add(jp1,BorderLayout.NORTH);
		this.add(jp2,BorderLayout.EAST);
		btn1.addActionListener(new action()); //按钮注册监听事件
		btn2.addActionListener(new action());
		btn3.addActionListener(new action());
		this.setVisible(true);
		
	}
	class action implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			String s=e.getActionCommand(); //获得按钮上的标签
			String[] strs;
			String str=reView(); //用于回顾数据
			
			if(s=="开始排序") //开始新一次排序
			{
				strs=text.getText().split(" "); //按空格分割获得输入的字符串
				for(int i=0;i<strs.length;i++) //将字符串转换为整数,储存到整数数组中
				{
					nums[i]=Integer.parseInt(strs[i].trim());
				}
				repaint(); //重新绘制
				
				//将数组显示在右边文本框
				String out="";
				for(int i=0;i<strs.length;i++)
				{
					if(i==0)
						out=out+strs[i];
					else
						out=out+" "+strs[i];
				}
				run.setText("当前数组序列:"+out);
				
				//数据写入文件
				try {
					FileWriter w1=new FileWriter("D:\\test.txt"); //保存上一次运行数据
					w1.write(out);	
					w1.close(); //关闭字符流
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					FileWriter w2=new FileWriter("D:\\intest.txt",true); //保存所有数据
					w2.write(out+"\r\n");	
					w2.close(); //关闭字符流
				} catch (Exception e1) {
					e1.printStackTrace();
				}			
				
				StartToSort(); //开始排序				
			}	
			else if(s=="回顾") //获得上一次数据
			{
				str=reView(); //获取数据
				lb.setText("上一次输入的数据为:"+str); //输出到文本框和标签中
				text.setText(str);
			}
			else if(s=="打开历史数据") //打开文件夹获取历史数据
			{
				open.setVisible(true);
				try {
					String path = open.getDirectory()+ open.getFile();
				    FileInputStream is;
					is = new FileInputStream(path);
					byte[] buffer=new byte[is.available()]; //根据大小设置缓冲
				    is.read(buffer);
				    is.close();
				    String wr=new String(buffer,"UTF-8");
				    run3.setText(wr);
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
			}
		}
	}
	
	public void StartToSort() //开始排序
	{
		SortThread so=new SortThread(nums);
		so.start();
	}
	class SortThread extends Thread
	{
		String item=(String) box.getSelectedItem(); //获得选中的box中的选项
		private int[] nums;
		public SortThread(int[] nums) //构造方法
		{
			this.nums=nums;
		}
		public void run() //复写run方法
		{
			if(item=="冒泡排序") //如果是冒泡排序
			{
				for(int i=1;i<nums.length;i++) //冒泡排序
				{
					for(int j=0;j<nums.length-i;j++)
					{
						run2.setText("nums["+j+"] "+"正在和nums["+(j+1)+"] 比较");//显示交换过程
						try {
							sleep(1000); //暂停一秒,便于观察
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						if(nums[j]>nums[j+1]) //如果前者比后者大,交换
						{
							lb.setText("nums["+j+"] 比 nums["+(j+1)+"] 大,交换次序");
							int t;
							t=nums[j]; 
							nums[j]=nums[j+1];
							nums[j+1]=t;
							repaint();
							try {
								Thread.sleep(2000); //暂停两秒
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						String outs="";
						for(int k=0;k<nums.length;k++) //连接数组并输出
							outs=outs+" "+Integer.toString(nums[k]);
						run.setText("当前数组序列:"+outs);
					}
				}
			}
			else if(item=="选择排序") //如果是选择排序
			{
				for(int i=0;i<nums.length-1;i++)
				{
					int minIndex=i; //此时最小元素的下标
					for(int j=i+1;j<nums.length;j++)
					{
						run2.setText("nums["+j+"] "+"正在和nums["+minIndex+"] 比较");//显示交换过程
						try {
							sleep(1000); //暂停一秒,便于观察
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						if(nums[minIndex]>nums[j]) //交换
						{
							lb.setText("nums["+j+"] 比 nums["+minIndex+"] 大,交换次序");
							int t;
							t=nums[minIndex];
							nums[minIndex]=nums[j];
							nums[j]=t;
							repaint(); //重新绘制
							try {
								Thread.sleep(2000); //暂停两秒
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						String outs="";
						for(int k=0;k<nums.length;k++) //连接数组并输出
							outs=outs+" "+Integer.toString(nums[k]);
						run.setText("当前数组序列:"+outs);
					}	
				}				
			}
			else if(item=="插入排序")  //如果是插入排序
			{
				for(int i=1;i<nums.length;i++)
				{
					//如果前者比后者大,进行交换,否则执行下一次循环
					for(int j=i-1;j>=0 && nums[j]>nums[j+1];j--) 
					{
						run2.setText("nums["+j+"] "+"正在和nums["+(j+1)+"] 比较");//显示交换过程
						try {
							sleep(1000); //暂停一秒,便于观察
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						lb.setText("nums["+j+"] 比 nums["+(j+1)+"] 大,交换次序");
						int t;
						t=nums[j];
						nums[j]=nums[j+1];
						nums[j+1]=t;	
						
						repaint(); //重新绘制
						try {
							Thread.sleep(2000); //暂停两秒
						} catch (Exception e) {
							e.printStackTrace();
						}
						String outs="";
						for(int k=0;k<nums.length;k++) //连接数组并输出
							outs=outs+" "+Integer.toString(nums[k]);
						run.setText("当前数组序列:"+outs);
					}
				}				
			}		
			int flag=1;
			for(int k=1;k<nums.length;k++) //检测是否排序完成
			{
				for(int x=0;x<nums.length-k;x++)
				{
					if(nums[x]>nums[x+1])
						flag=0;
				}
			}
			if(flag==1)
				lb.setText("排序完成!"); //排序完成,结束
		}
	}
	
	public static String reView() //用于回放上一次排序数据 
	{
		String outP="";
		try {
			FileReader r=new FileReader("D:\\test.txt"); //打开字符流	
			int i;
			char[] ch=new char[1000];
			while((i=r.read(ch))!=-1)
			{
				
			}
			outP=new String(ch);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return outP;		
	}
	
	public void paints(Graphics g)
	{
		//(left,y,width,height)
		//左边缘和右边缘=左边缘+width; 上边缘和下边缘=上边缘+height
		for(int i=0;i<5;i++)
		{
			int n=nums[i];
			g.setColor(Color.pink);
			g.fillRect(40+i*40,380-n,40,20+n); //绘制填充矩形
			g.setColor(Color.red);
			g.drawRect(40+i*40,380-n,40,20+n); //绘制矩形边框
		}
	}
	public void paint(Graphics g)  //绘制
	{
		super.paint(g);
		paints(g);
	}
	
	public static void main(String[] args) 
	{
		ViewSort sort=new ViewSort();
		//width,height
		sort.setSize(600,400);
		sort.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
