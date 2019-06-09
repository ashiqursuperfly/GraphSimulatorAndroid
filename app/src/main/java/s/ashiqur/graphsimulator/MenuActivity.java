package s.ashiqur.graphsimulator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import s.ashiqur.graphsimulator.views.Size;

public class MenuActivity extends AppCompatActivity {

    public static final boolean DEBUG = true;

    RelativeLayout relativeLayoutMenu;
    TextView tvCoordinates;
    Button buttonBfsDFs;
    Button buttonExit;
    Button buttonHowTo;
    private Animation animZoomin;
    private Animation animBlink;
    private Animation animBounce;
    private Animation animRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initializeSizeVariables();
        initializeXmlVariables();

        addTouchListeners();
        addOnClickListeners();




    }
    private void addOnClickListeners()
    {
        buttonBfsDFs.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                tvCoordinates.setText("BFS");
                v.startAnimation(animRotate);

                Intent mIntent = new Intent(MenuActivity.this,BfsDfsActivity.class);
                startActivity(mIntent);

                //v.clearAnimation();


            }
        });
        buttonHowTo.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MenuActivity.this,HowToUseActivity.class);
                startActivity(mIntent);

            }
        });
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void initializeSizeVariables()
    {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Size.HEIGHT.setValue( displayMetrics.heightPixels);
        Size.WIDTH.setValue(displayMetrics.widthPixels);
        int size = getApplicationContext().getResources().getDimensionPixelSize(R.dimen.textSize);
        Size.NODETEXTSIZE.setValue(size);
        size= getApplicationContext().getResources().getDimensionPixelSize(R.dimen.xoffset);
        Size.XOFFSET.setValue(size);
        size= getApplicationContext().getResources().getDimensionPixelSize(R.dimen.yoffset);

        Size.YOFFSET.setValue(size);
        Size.XLIMIT.setValue(Size.WIDTH.value-2*Size.XOFFSET.value);
        Size.YLIMIT.setValue(Size.HEIGHT.value-9*Size.YOFFSET.value);///TODO:check 10.5 for multiple devices

        //Size.NODESIZE.setValue(Math.sqrt(Size.XLIMIT.value* Size.YLIMIT.value/125));//TODO :check 125 for multiple devices
        size= getApplicationContext().getResources().getDimensionPixelSize(R.dimen.nodeSize);
        Size.NODESIZE.setValue(size);

    }
    private void addTouchListeners()
    {
        relativeLayoutMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float x=event.getX();
                float y=event.getY();
                if(MenuActivity.DEBUG)tvCoordinates.setText("Coordinates("+x+","+y+")");
                return false;
            }
        });

    }
    private void initializeXmlVariables()
    {
        relativeLayoutMenu=findViewById(R.id.relativeLayoutMenu);
        tvCoordinates=findViewById(R.id.tvCoordinates);
        buttonBfsDFs =findViewById(R.id.buttonBFS);
        buttonHowTo=findViewById(R.id.buttonHowTo);
        buttonExit=findViewById(R.id.buttonExit);
        animZoomin=(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoomin));
        animBlink=(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink_anim));
        animBounce=(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce));
        animRotate=(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate));


    }
}
