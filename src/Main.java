// Импортируем все необходимые библиотеки:
// Библиотеки для отрисовки GUI.
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
// Библиотеки для обработки событий, например хук нажатий клавишь с клавиатуры.
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
// Для управления изображением и его загрузки.
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
// Для обработки исключений.
import java.io.IOException;
// Для открытия и обработки ссылок.
import java.net.URL;

/**
 * @author      Kozhanov N.K. (justpd) <jp_dev @ mail.ru>
 * @version     1.0
 */


// Основной класс программы.
public class Main extends JPanel implements ActionListener {

    // Задаём наше окно для графической отрисовки.
    private static JFrame jFrame;
    private static int width = 600;
    private static int height = 600;

    public static int choice = 0; // Выбранный пункт меню.
    private static int option = 0; // Текущий (активный) пункт меню.
    private static int speed = 100; // Скорость отрисовки GUI.
    private static boolean convertToKelvin = true;

    // Параметры, необходимые для управления конвертером температур.

    private static String mode = "Kelvin";
    private static String ConverterInput = "";
    private static String ConverterInputPlaceholder = "Type Celsius temp.";
    private static String ConverterResult = "";
    private static String ConverterResultPlaceholder = "Result";

    private static BufferedImage javaIcon = null;

    // Строковые константы
    private static String converterInfo = "This tool converts Celsius temperature to Kelvin or Fahrenheit.\nYou can switch converter mode using L and R arrow keys.\nUse Enter key to see the result and Esc to exit.";

    // Инициализация интерактивного индикатора для меню.
    private Indicator indicator = new Indicator(0, 475, 90 , 75, 2);

    /**
     * Основной метод программы, отрисовывает GUI, загружает ресурсы.
     *
     * @param args массив принимаемых агрументов
     */
    public static void main(String[] args)
    {
        SetFrame("Kozhanov N.  Lab_№1");
        try
        {
            javaIcon = ImageIO.read( new File("java.png" ));
        }
        catch ( IOException exc )
        {
            System.out.println("Wrong picture.");
        }
    }

    /**
     * Настройка окна Jframe и его создание.
     *
     * @param title заголовок окна Jframe.
     */
    private static void SetFrame(String title)
    {
        jFrame = new JFrame(title);
        jFrame.setSize(width,height);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);

        jFrame.add(new Main());

        jFrame.setVisible(true);
    }

    /**
     * Конструктор основновного класса.
     *
     * Передает свой контекст для отрисовки в Jframe.
     * Здесь же задается скорость отрисовки и обработчик событий клавиатуры.
     */
    private Main()
    {
        Timer timer = new Timer(1000 / speed, this);
        timer.start();
        addKeyListener(new KeyBoardInput());
        setFocusable(true);
    }

    /**
     * Отрисовывает компоненты.
     *
     * Полную документацию можно найти в классе {@link Component}.
     *
     * @param g графический контекст для отрисовки.
     */
    public void paint(Graphics g)
    {
        switch (option)
        {
            case 1:
                g.setColor(Color.BLACK);
                g.fillRect(0,0, width, height);

                g.setColor(Color.WHITE);
                g.fillRect(150, 175, 300, 50);
                g.fillRect(150, 250, 300, 50);
                g.fillRect(150, 325, 300, 50);

                g.setColor(Color.BLACK);
                g.fillRect(155, 180, 290, 40);
                g.fillRect(155, 255, 290, 40);
                g.fillRect(155, 330, 290, 40);
                int lineIndex = 0;
                for (String line : converterInfo.split("\n"))
                {
                    lineIndex++;
                    centerString(g, new Rectangle(150, 50 + lineIndex * 20, 300, 50), line, new Font("TimesRoman", Font.PLAIN, 18), Color.WHITE);
                }


                if (ConverterInput.length() > 0)
                    centerString(g, new Rectangle(150, 175, 300, 50), ConverterInput, new Font("TimesRoman", Font.PLAIN, 20), Color.WHITE);
                else
                    centerString(g, new Rectangle(150, 175, 300, 50), ConverterInputPlaceholder, new Font("TimesRoman", Font.PLAIN, 20), Color.GRAY);

                centerString(g, new Rectangle(150, 250, 300, 50), mode, new Font("TimesRoman", Font.PLAIN, 20), Color.WHITE);
                if (ConverterResult.length() > 0)
                    centerString(g, new Rectangle(150, 325, 300, 50), ConverterResult, new Font("TimesRoman", Font.PLAIN, 20), Color.WHITE);
                else
                    centerString(g, new Rectangle(150, 325, 300, 50), ConverterResultPlaceholder, new Font("TimesRoman", Font.PLAIN, 20), Color.GRAY);
                break;
            default:
                g.setColor(Color.BLACK);
                g.fillRect(0,0, width, height);

                g.setColor(Color.RED);
                g.fillOval(indicator.x, indicator.y, 15, 15);

                g.setColor(Color.WHITE);
                g.fillRect(150, 75, 300, 50);
                g.fillRect(150, 150, 300, 50);

                g.setColor(Color.BLACK);
                g.fillRect(155, 80, 290, 40);
                g.fillRect(155, 155, 290, 40);

                centerString(g, new Rectangle(150, 75, 300, 50),"Converter", new Font("TimesRoman", Font.PLAIN, 20), Color.WHITE);
                centerString(g, new Rectangle(150, 150, 300, 50),"Task list", new Font("TimesRoman", Font.PLAIN, 20), Color.WHITE);

                g.drawImage(javaIcon, 170, 300, this);
        }


    }

    /**
     * Централизацирует строку относительно родительского контейнера.
     *
     * @param g объект графического контекста.
     * @param r родительский контейнер.
     * @param s строка для отрисовки.
     * @param font объект, описывающий свойства шрифта.
     * @param color цвет для отрисовки.
     */
    private void centerString(Graphics g, Rectangle r, String s,
                             Font font, Color color) {
        FontRenderContext frc =
                new FontRenderContext(null, true, true);

        Rectangle2D r2D = font.getStringBounds(s, frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());
        int rX = (int) Math.round(r2D.getX());
        int rY = (int) Math.round(r2D.getY());

        int a = (r.width / 2) - (rWidth / 2) - rX;
        int b = (r.height / 2) - (rHeight / 2) - rY;

        g.setFont(font);
        g.setColor(color);
        g.drawString(s, r.x + a, r.y + b);
    }

    /**
     * Открывает страницу в браузере по умолчанию.
     *
     * @param urlString URL ссылка на страницу.
     */
    private static void openWebPage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Обновляет и переотрисовывает интерфейс.
     *
     * @param e семантическое событие, произошедшее в данном контексте.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        indicator.UpdateIndicator();
        repaint();
    }

    /**
     * Переключает режим работы конвертера.
     */
    private void changeMode()
    {
        convertToKelvin =! convertToKelvin;
        if (convertToKelvin) mode = "Kelvin";
        else mode = "Fahrenheit";
    }

    /**
     * Класс обработки пользовательского ввода с клавиатуры.
     */
    public class KeyBoardInput extends KeyAdapter
    {
        /**
         * Вызывается когда нажимается та или иная клавиша.
         * Описание {@link KeyEvent} можно найти в документации к классу.
         */
        public void keyPressed (KeyEvent event)
        {
            int key = event.getKeyCode();

            if (key == KeyEvent.VK_UP)
            {
                choice --;
                if (choice < 0) choice = 0;
            }

            if (key == KeyEvent.VK_DOWN)
            {
                choice ++;
                if (choice > 1) choice = 1;
            }

            if (key == KeyEvent.VK_ENTER)
            {
                switch (option)
                {
                    case 0:
                        if (choice == 1) openWebPage("https://sdo.tusur.ru/mod/assign/view.php?id=3457");
                        else option = choice + 1;
                        break;
                    case 1:
                        if (ConverterInput.length() == 0) return;
                        float celsius = Float.parseFloat(ConverterInput);
                        float fahrenheit = celsius * 1.8f + 32;
                        float kelvin = celsius + 273.15f;

                        ConverterInput = "";

                        if (convertToKelvin) ConverterResult = celsius + " °C = " + kelvin + " °K";
                        else ConverterResult = celsius + " °C = " + fahrenheit + " °F";

                        break;
                }
            }

            if (option == 1)
            {
                switch (key)
                {
                    case KeyEvent.VK_LEFT:
                        changeMode();
                        break;
                    case KeyEvent.VK_RIGHT:
                        changeMode();
                        break;
                    case KeyEvent.VK_1:
                        ConverterInput += "1";
                        break;
                    case KeyEvent.VK_2:
                        ConverterInput += "2";
                        break;
                    case KeyEvent.VK_3:
                        ConverterInput += "3";
                        break;
                    case KeyEvent.VK_4:
                        ConverterInput += "4";
                        break;
                    case KeyEvent.VK_5:
                        ConverterInput += "5";
                        break;
                    case KeyEvent.VK_6:
                        ConverterInput += "6";
                        break;
                    case KeyEvent.VK_7:
                        ConverterInput += "7";
                        break;
                    case KeyEvent.VK_8:
                        ConverterInput += "8";
                        break;
                    case KeyEvent.VK_9:
                        ConverterInput += "9";
                        break;
                    case KeyEvent.VK_0:
                        ConverterInput += "0";
                        break;
                    case KeyEvent.VK_BACK_SPACE:
                        if (ConverterInput.length() > 0)
                            ConverterInput = ConverterInput.substring(0, ConverterInput.length() - 1);
                        break;
                    case KeyEvent.VK_ESCAPE:
                        ConverterInput = "";
                        option = 0;
                        break;
                }
            }
        }
    }
}
