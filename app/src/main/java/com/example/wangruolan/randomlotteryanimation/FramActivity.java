package com.example.wangruolan.randomlotteryanimation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by wangruolan on 15-12-4.
 * 吃啥随机选餐动画，主要是平移
 * 可拓展点：gridview中item可自定义，显示自己需要的内容，可增加点击事件
 * 动画速度及显示位置可调整
 */

public class FramActivity extends FragmentActivity {
    public static final int ITEM_WIDTH = 100;    //gridview中item的宽高，可自行调整
    public static final int SPACING = 5;         //gridview中各item之间的间隔
    private static final int NORMAL_ITEM = 0;    //普通item的类型值
    private static final int START_ITEM = 1;     //中心开启动画位置的item类型值
    private static final int START_RANDOM_POSITION = 4;       //开启动画item的位置
    private static final int START_TIME = 100;               //动画最先的duration值
    private static final int TIME_INTERVAL = 50;             //动画在不断减速，duration的增量值
    private static final int RANDOM_TIMES = 3;               //最多可随机的次数
    private static final int DEFAULT_TRANSLATE_TIMES = 15;   //每次随机默认的动画次数，此处应设置成随机值，待扩展

    private GridView gridView;
    private ImageView imageView;
    private TextView textView;
    int mIndex;
    int count = 1;
    int randomCount = 0;
    android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int index = (int) msg.obj;
                    textView.setEnabled(false);
                    textView.setClickable(false);
                    startAnimation(index, imageView, START_TIME);
                    break;
                case 2:
                    textView.setEnabled(true);
                    textView.setClickable(true);
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fram);
        gridView = (GridView) findViewById(R.id.gridView);
        imageView = (ImageView) findViewById(R.id.iv);
        MyAdapter adapter = new MyAdapter(FramActivity.this);
        gridView.setAdapter(adapter);
    }

    class MyAdapter extends BaseAdapter {
        LayoutInflater inflater;

        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //九宫格中心位置position＝4是开启动画的item，与其它位置item不一样，单独设置View及其点击事件
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (getItemViewType(position) == NORMAL_ITEM) {
                convertView = inflater.inflate(R.layout.gridview_item, parent, false);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
                imageView.setBackgroundResource(R.drawable.grid_item);
            } else {
                convertView = inflater.inflate(R.layout.start_random_item, parent, false);
                textView = (TextView) convertView;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (randomCount < RANDOM_TIMES) {
                            if (imageView.getVisibility() == View.GONE) {
                                mIndex = getRadom();
                                int x = (int) getImageX(mIndex);
                                int y = (int) getImageY(mIndex);
                                imageView.setTranslationX(x);
                                imageView.setTranslationY(y);
                                imageView.setVisibility(View.VISIBLE);
                            }
                            randomCount++;
                            count = 1;
                            if (randomCount < RANDOM_TIMES) {
                                textView.setText("再试一次");
                            } else {
                                textView.setText("不要再犹豫了哦!");
                            }
                            Message message = mHandler.obtainMessage();
                            message.obj = mIndex;
                            message.what = 1;
                            mHandler.sendMessage(message);
                        } else {
                            textView.setText("不要再犹豫了哦！");
                        }
                    }
                });
            }
            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            return position == START_RANDOM_POSITION ? START_ITEM : NORMAL_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }
    }

    /**
     * 第一次随机时，动画默认的开始位置
     *
     * @return
     */
    public int getRadom() {
        Random random = new Random();
        int i = random.nextInt(8);
        return i;
    }

    /**
     * 计算相应位置的x轴偏移量，此处i并非adapater中position的位置，而是实现显示的位置,如下
     * 1   2   3
     * <p/>
     * 8       4
     * <p/>
     * 7   6   5
     *
     * @param i
     * @return
     */
    public float getImageX(int i) {
        float margin = (BaseConfig.width - BaseConfig.density * (ITEM_WIDTH * 3 + SPACING * 4)) / 2;
        switch (i) {
            case 1:
                return margin;
            case 2:
                return margin + BaseConfig.density * (ITEM_WIDTH + SPACING);
            case 3:
                return margin + BaseConfig.density * (ITEM_WIDTH * 2 + SPACING * 2);
            case 4:
                return margin + BaseConfig.density * (ITEM_WIDTH * 2 + SPACING * 2);
            case 5:
                return margin + BaseConfig.density * (ITEM_WIDTH * 2 + SPACING * 2);
            case 6:
                return margin + BaseConfig.density * (ITEM_WIDTH + SPACING);
            case 7:
                return margin;
            case 8:
                return margin;
            default:
                return margin;
        }
    }

    /**
     * 计算相应位置的y轴偏移量，此处i并非adapater中position的位置，而是实现显示的位置,如下
     * 1   2   3
     * <p/>
     * 8       4
     * <p/>
     * 7   6   5
     *
     * @param i
     * @return
     */
    public float getImageY(int i) {
        //此处有疑问，按计算应该是SPACING * 4，但此时会有10dp向下的偏移，暂时还未找到原因
        float margin = (BaseConfig.height - BaseConfig.density * (ITEM_WIDTH * 3 + SPACING * 8)) / 2;
        switch (i) {
            case 1:
                return margin;
            case 2:
                return margin;
            case 3:
                return margin;
            case 4:
                return margin + BaseConfig.density * (ITEM_WIDTH + SPACING);
            case 5:
                return margin + BaseConfig.density * (ITEM_WIDTH * 2 + SPACING * 2);
            case 6:
                return margin + BaseConfig.density * (ITEM_WIDTH * 2 + SPACING * 2);
            case 7:
                return margin + BaseConfig.density * (ITEM_WIDTH * 2 + SPACING * 2);
            case 8:
                return margin + BaseConfig.density * (ITEM_WIDTH + SPACING);
            default:
                return margin;
        }
    }

    //开始一次随机动画
    private void startAnimation(final int index, View view, long time) {
        float currentX = view.getTranslationX();
        float currentY = view.getTranslationY();
        ObjectAnimator objectAnimator;
        if (index == 1 || index == 2 || index == 5 || index == 6) {
            float x = getImageX(index + 1);
            objectAnimator = ObjectAnimator.ofFloat(view, "translationX", currentX, x);
        } else {
            float y = getImageY(index + 1);
            if (index == 8) {
                y = getImageY(1);
            }
            objectAnimator = ObjectAnimator.ofFloat(view, "translationY", currentY, y);
        }
        if (index != 8) {
            mIndex++;
        } else {
            mIndex = 1;
        }
        objectAnimator.setDuration(time);
        count++;
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (count < DEFAULT_TRANSLATE_TIMES) {
                    long time = START_TIME + (count - 1) * TIME_INTERVAL;
                    startAnimation(mIndex, imageView, time);
                } else {
                    Message message = mHandler.obtainMessage();
                    message.what = 2;
                    mHandler.sendMessage(message);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
    }


}
