import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.*;
import java.time.Duration;
import java.util.TimerTask;
import java.awt.event.*;
import java.awt.*;


/**
 *  AudioPlayer_2
 */
public class AudioPlayer_final extends JPanel implements ActionListener
{
    //JFrame
    private JFrame player = new JFrame(" Unremarkable wav-Audio Player");

    //int
    private int hold = 0;
    private int filecount = 0;
    private int listcount = 0;

    //float
    private float [] fileduration = new float [256];

    //String
    private String [] file_audio = new String [256];
    private String gifimagepath = "C:/Users/user/Desktop/程式設計/大一下/程式設計4/專題/音樂播放器/gif/鸚鵡.gif";
    
    //JLabel
    private JLabel bottomtext = new JLabel("B   e      c   h   i   l   l", JLabel.CENTER);
    private JLabel marquee = new JLabel("", JLabel.CENTER);
    private JLabel imageLabel1 = new JLabel(); 
    private JLabel imageLabel2 = new JLabel(); 

    //ImagerIcon
    private ImageIcon play = new ImageIcon(new ImageIcon("icon/Second/play.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
    private ImageIcon pause = new ImageIcon(new ImageIcon("icon/Second/pause.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
    private ImageIcon previous = new ImageIcon(new ImageIcon("icon/Second/previous.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
    private ImageIcon next = new ImageIcon(new ImageIcon("icon/Second/next.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
    private ImageIcon gifimage = new ImageIcon(new ImageIcon(gifimagepath).getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT));
    
    //JPanel
    private JPanel marqueepn = new JPanel(new BorderLayout(0,0));
    private JPanel operaterpn = new JPanel(new GridLayout(1,3,2,2));
    private JPanel mixpn = new JPanel(new GridLayout(2,1,2,2));

    //JButton
    private JButton playBt = new JButton(play);
    private JButton previousBt = new JButton(previous);
    private JButton nextBt = new JButton(next);

    //JComboBox
    private JComboBox<String> list = new JComboBox<String>();
    
    //File
    private File sound;
    private File localfolder = new File("C:/Users/user/Desktop/程式設計/大一下/程式設計4/專題/音樂播放器/MusicList_wav");
    
    //AudioInputStream
    private AudioInputStream ais;

    //Clip
    private Clip clip;

    //Boolean
    private Boolean boolean1 = false;
    private Boolean boolean2 = false;
    
    AudioPlayer_final()
    {
        //讀取資料夾裡的音源檔(wav)
        findAllFilesInFolder(localfolder);

        //設定list跟顯示器預設的文字
        list.setSelectedIndex(0);
        marquee.setText(file_audio[0]);

        //operater設定
        /* 1.設定play按鈕 */
        playBt.addActionListener(this);
        playBt.setBackground(new Color(75,92,116));
        /* 2.設定previous按鈕 */
        previousBt.addActionListener(this);
        previousBt.setBackground(new Color(75,92,116));
        /* 3.設定next按鈕 */
        nextBt.addActionListener(this);
        nextBt.setBackground(new Color(75,92,116));
        /* 4.設定好後，加到控制列中 */
        operaterpn.add(previousBt);
        operaterpn.add(playBt);
        operaterpn.add(nextBt);

        //marqueepn設定
        /* 1.設定文字顯示器 */
        marquee.setFont(new Font("標楷體", Font.BOLD, 18));
        marquee.setOpaque(true);
        marquee.setBackground(new Color(75,92,116));
        marquee.setForeground(new Color(188, 194, 212));
        /* 2.設定gif圖檔(左) */
        imageLabel1.setOpaque(true);
        imageLabel1.setBackground(new Color(75,92,116));
        imageLabel1.setIcon(gifimage);
        /* 3.設定gif圖檔(右) */
        imageLabel2.setOpaque(true);
        imageLabel2.setBackground(new Color(75,92,116));
        imageLabel2.setIcon(gifimage);
        /* 4.設定好後，加到顯示器中 */
        marqueepn.add(imageLabel1, BorderLayout.WEST);
        marqueepn.add(marquee);
        marqueepn.add(imageLabel2, BorderLayout.EAST);
        
        //mixpn設定
        mixpn.add(marqueepn);
        mixpn.add(operaterpn);

        //底部文字
        bottomtext.setFont(new Font("Cooper Black", Font.ITALIC, 12));
        bottomtext.setForeground(new Color(75,92,116));

        //設定combobox
        list.setBackground(new Color(188, 194, 212));
        list.setForeground(new Color(224, 255, 255));
        list.setFont(new Font("標楷體", Font.BOLD, 12));
        list.addActionListener(this);

        //播放器add元件
        player.add(bottomtext, BorderLayout.PAGE_END);
        player.add(mixpn);
        player.add(list, BorderLayout.PAGE_START);

        //播放器設定
        player.setSize(600,255);
        player.setLocationRelativeTo(null);
        player.setResizable(false);
        player.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        player.setVisible(true);
    }

    //類:按鈕跟combobox的傾聽器運作
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        /* 1.觸發播放按鈕 */
        if(ae.getSource() == playBt)
        {
            try
            {
                if(boolean1 == false)
                {   
                    for(listcount = 0; listcount < 256; listcount++)
                    {
                        if(list.getSelectedIndex() == listcount)
                        {
                            sound = new File(localfolder.toString() + "/" + file_audio[list.getSelectedIndex()]);
                            ais = AudioSystem.getAudioInputStream(sound);
                            clip = AudioSystem.getClip();
                            clip.open(ais);
                            clip.start();
                            boolean1 = true;
                            playBt.setIcon(pause);
                        }
                    }
                }
                else
                {
                    clip.stop();
                    boolean1 = false;
                    playBt.setIcon(play);
                }
            }catch(Exception e) {JOptionPane.showMessageDialog(null, "You haven't added audio yet");}
        }

        /* 2-1.播放時，觸發上一首按鈕 */
        if(ae.getSource() == previousBt && boolean1 == true)
        {
            boolean2 = true;
            clip.stop();
            hold = list.getSelectedIndex();
            if(hold > 0 )
            {
                hold--;
                list.setSelectedIndex(hold);
                marquee.setText((String)list.getSelectedItem());
                try {
                    sound = new File(localfolder.toString() + "/" + file_audio[list.getSelectedIndex()]);
                    ais = AudioSystem.getAudioInputStream(sound);
                    clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.start();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "This is the First song in this musicplayer.");
                }
            }
            else
            {
                boolean1 = false;
                playBt.setIcon(play);
            }
            boolean2 = false;
        }

        /* 2-2.停止時，觸發上一首按鈕 */
        if(ae.getSource() == previousBt && boolean1 == false)
        {
            boolean2 = true;
            hold = list.getSelectedIndex();
            if(hold > 0 )
            {
                hold--;
                list.setSelectedIndex(hold);
                marquee.setText(list.getSelectedItem().toString());
                try {

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "This is the First song in this musicplayer.");
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "This is the First song in this musicplayer.");
            }
            boolean2 = false;
        }

        /* 3-1.播放時，觸發下一首按鈕 */
        if(ae.getSource() == nextBt && boolean1 == true)
        {
            boolean2 = true;
            clip.stop();
            hold = list.getSelectedIndex();
            if(hold < filecount-1)
            {
                hold++;
                list.setSelectedIndex(hold);
                marquee.setText((String)list.getSelectedItem());
                try {
                    sound = new File(localfolder.toString() + "/" + file_audio[list.getSelectedIndex()]);
                    ais = AudioSystem.getAudioInputStream(sound);
                    clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.start();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "This is the last song in this musicplayer.");
                }
            }
            else
            {
                boolean1 = false;
                playBt.setIcon(play);
            }
            boolean2 = false;
        }

        /* 3-2.停止時，觸發下一首按鈕 */
        if(ae.getSource() == nextBt && boolean1 == false)
        {
            boolean2 = true;
            hold = list.getSelectedIndex();
            if(hold < filecount-1)
            {
                hold++;
                list.setSelectedIndex(hold);
                marquee.setText(list.getSelectedItem().toString());
                try {
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "This is the last song in this musicplayer.");
                }
            }
            else
            {
                boolean1 = false;
                playBt.setIcon(play);
                JOptionPane.showMessageDialog(null, "This is the last song in this musicplayer.");
            }
            boolean2 = false;
        }

        /* 4.combobox觸發 */
        if(ae.getSource() == list)
        {
            String s = (String)list.getSelectedItem();
            if (boolean1 == false)
            {
                marquee.setText(s);
            }
            else if (boolean2 == false)
            {
                clip.stop();
                marquee.setText(s);
                hold = list.getSelectedIndex();
                list.setSelectedIndex(hold);
                try {
                    sound = new File(localfolder.toString() + "/" + file_audio[list.getSelectedIndex()]);
                    ais = AudioSystem.getAudioInputStream(sound);
                    clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.start();
                } catch (Exception e) {
    
                }
            }
        }
    }

    //類:把wav檔加到combobox
    public void findAllFilesInFolder(File localfolder) {
		for (File file : localfolder.listFiles()) {
			if (!file.isDirectory()) {
                file_audio[filecount] = file.getName();
                fileduration[filecount] = getDuration(localfolder + "/" + file_audio[filecount]);
                list.addItem(file_audio[filecount]);
                marquee.setText(file_audio[filecount]);
                list.setSelectedIndex(filecount);
                filecount++;
			} else {
				findAllFilesInFolder(file);
			}
		}
	}

    //類:得到音訊長度
    public static Float getDuration(String filePath){
        try{
            File destFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(destFile);
            AudioFormat format = audioInputStream.getFormat();
            long audioFileLength = destFile.length();
            int frameSize = format.getFrameSize();
            float frameRate = format.getFrameRate();
            float durationInSeconds = (audioFileLength / (frameSize * frameRate));
            return durationInSeconds;
        }catch (Exception e){
            e.printStackTrace();
            return 0f;
        }
    }

    public void waitTimer(int wait)
    {
        Timer t1 = new Timer(wait*1000, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println(listcount);
                listcount++;
                list.setSelectedIndex(listcount);
                marquee.setText(list.getSelectedItem().toString());
            }
        });
        t1.setRepeats(false); //只會執行一次action
        t1.start();
    }
}