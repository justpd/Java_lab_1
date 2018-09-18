public class Indicator
{
    public int position;
    public int x;
    public int y;
    public int buttonsCount;
    public int[] y_anchors;

    /**
     * Инизиализирует индикатор пунктов меню.
     *
     * @param position      текущий пункт меню.
     * @param x             позиция x.
     * @param y             позиция y.
     * @param step          растояние между пунктами меню.
     * @param buttonsCount  количество пунктов в меню.
     */
    public Indicator(int position, int x, int y, int step, int buttonsCount)
    {
        this.x = x;
        this.y = y;
        this.position = position;
        this.buttonsCount = buttonsCount;
        y_anchors = new int[buttonsCount];
        for (int i = 0; i < buttonsCount; i++)
        {
            y_anchors[i] = y + i*step;
        }
    }

    /**
     * Обновляет позицию и состояние индикатора.
     */
    public void UpdateIndicator()
    {
        if (Main.choice == position) return;

        if (Main.choice > position)
        {
            if (y < y_anchors[y_anchors.length-1])
                y += 5;
        }
        else
        {
            if (y > y_anchors[0])
                y -= 5;
        }

        for (int i = 0; i < buttonsCount; i++)
        {
            if (y == y_anchors[i]) position = i;
        }
    }
}
